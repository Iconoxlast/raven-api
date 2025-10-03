import { doSearch } from "./http.js";
import { navigate } from "./router.js";
import { formatYearMonth, getFandomPageUrl } from "./util.js";
import { getAppearancesListTxt } from "./files.js";

export var publisherSelect;
export var characterInput;
export var searchedCharacter;

function updatePlaceholder() {
  const map = {
    DC: "Ex.: Raven (Prime Earth)",
    MARVEL: "Ex.: Wanda Maximoff (Earth-616)",
  };
  characterInput.placeholder =
    map[publisherSelect.value] || "Enter a character name";
}

export function viewHome({
  initialPublisher = "DC",
  initialCharacter = "",
  waitScreen = false,
  errorScreen = false,
} = {}) {
  const wrap = document.createElement("div");
  wrap.innerHTML = `
        <form id="searchForm" novalidate>
          <div>
            <label for="publisher">Publisher</label>
            <select id="publisher" name="publisher" required>
              <option value="DC">DC</option>
              <option value="MARVEL">Marvel</option>
            </select>
          </div>
          <div>
            <label for="character">Character</label>
            <input id="character" name="character" type="text" inputmode="latin-name"
                   placeholder="Ex.: Raven (Prime Earth)" required />
          </div>
          <div>
            <button id="btnSearch" type="submit">Search</button>
          </div>
        </form>
        <div id="status" class="pill" aria-live="polite"></div>
      `;
  publisherSelect = wrap.querySelector("#publisher");
  characterInput = wrap.querySelector("#character");
  publisherSelect.value = initialPublisher;
  characterInput.value = initialCharacter;
  if (!errorScreen) {
    if (waitScreen) {
      changeStatusImage("process");
    } else {
      changeStatusImage("ini");
    }
  }

  wrap.querySelector("#searchForm").addEventListener("submit", async (ev) => {
    ev.preventDefault();
    const publisher = publisherSelect.value.trim();
    const character = characterInput.value.trim();
    if (!publisher || !character) {
      wrap.querySelector("#status").textContent =
        "Select a publisher and enter a character name.";
      return;
    }
    await doSearch({ publisher, character });
  });

  publisherSelect.addEventListener("change", updatePlaceholder);
  updatePlaceholder();

  return wrap;
}

export function viewDisambiguation({ publisher, character, versions }) {
  changeStatusImage("disamb");
  const wrap = document.createElement("div");
  const header = document.createElement("div");
  header.className = "header-disamb";
  const h2 = document.createElement("h2");
  h2.textContent = `Disambiguation — ${character}`;
  const p = document.createElement("p");
  p.textContent = `Alternate versions and characters related to ${character}`;
  const back = document.createElement("button");
  back.className = "linklike";
  back.textContent = "← New search";
  back.addEventListener("click", () => navigate("/"));
  const headerTop = document.createElement("div");
  headerTop.className = "row";
  headerTop.append(h2, back);
  const headerBottom = document.createElement("div");
  headerBottom.append(p);
  header.append(headerTop, headerBottom);

  wrap.append(header, getDisambiguationList({ publisher, versions }));
  return wrap;
}

function getDisambiguationList({ publisher, versions }) {
  const list = document.createElement("ul");
  list.className = "list";
  versions.forEach((name) => {
    const li = document.createElement("li");
    const left = document.createElement("div");
    left.append(
      getHyperlink({
        text: name,
        url: getFandomPageUrl({ publisher, page: name }),
      })
    );
    const btn = document.createElement("button");
    btn.className = "btn-list";
    btn.textContent = "Search";
    btn.addEventListener("click", () =>
      doSearch({ publisher, character: name })
    );
    li.append(left, btn);
    list.append(li);
  });
  const wrap = document.createElement("div");
  wrap.className = "results";
  wrap.append(list);
  return wrap;
}

export function viewAppearances({ publisher, character, appearancesData }) {
  searchedCharacter = character;
  changeStatusImage(character === "Raven (New Earth)" ? "new" : "success");
  const wrap = document.createElement("div");
  const header = document.createElement("div");
  header.className = "row";
  const h = document.createElement("h2");
  h.innerHTML = `Appearances — <a href="${getFandomPageUrl({
    publisher,
    page: character,
  })}" target="_blank" rel="noopener noreferrer">${character}</a> (${publisher})`;

  const flash = document.createElement("div");
  flash.id = "flash";
  header.append(h, getBackButton(), getDownloadButton(), flash);

  if (!appearancesData?.length) {
    wrap.append(header, getNoAppearanceFoundMessage);
    return wrap;
  }

  const list = document.createElement("ul");
  list.className = "list";

  wrap.append(header, getAppearancesList({ publisher, appearancesData }));
  return wrap;
}

function getNoAppearanceFoundMessage() {
  const p = document.createElement("p");
  p.className = "muted";
  p.textContent = "No appearance found.";
  return p;
}

function getAppearancesList({ publisher, appearancesData }) {
  const list = document.createElement("ul");
  list.className = "list";
  list.id = "appearances";

  for (const [publicationMonth, issues] of appearancesData) {
    var li = document.createElement("li");
    var h1 = document.createElement("h1");
    li.className = "month";
    h1.textContent =
      publicationMonth === "1900-01"
        ? "Undefined"
        : formatYearMonth(publicationMonth);
    li.append(h1);
    list.append(li);
    issues.forEach((issue) => {
      li = document.createElement("li");
      li.className = "issue";
      var title = document.createElement("div");
      title.className = "title";
      title.append(
        getHyperlink({
          text: issue.title,
          url: getFandomPageUrl({ publisher, page: issue.title }),
        })
      );
      var when = document.createElement("div");
      when.className = "pill";
      when.textContent = formatYearMonth(issue.date) || "";
      li.append(title, when);
      list.append(li);
    });
  }
  const wrap = document.createElement("div");
  wrap.className = "results";
  wrap.append(list);

  return wrap;
}

function getBackButton() {
  const back = document.createElement("button");
  back.className = "linklike";
  back.textContent = "← New search";
  back.addEventListener("click", () => navigate("/"));
  return back;
}

function getDownloadButton() {
  const download = document.createElement("button");
  download.id = "btn-download";
  download.textContent = "💾 Download";
  download.addEventListener("click", () => getAppearancesListTxt());
  return download;
}

function getHyperlink({ text, url }) {
  var a = document.createElement("a");
  a.target = "_blank";
  a.rel = "noopener noreferrer";
  a.href = url;
  a.className = "list-link";
  a.textContent = text;
  return a;
}

export function viewError({
  message = "Something went wrong. Please try again.",
} = {}) {
  changeStatusImage("error");
  const wrap = document.createElement("div");
  wrap.innerHTML = `
        <h2 class="error">Error</h2>
        <p class="error">${message}</p>
        <hr />
      `;
  wrap.append(viewHome({ errorScreen: true }));
  return wrap;
}

export function changeStatusImage(status) {
  var ravenImg = document.querySelector("#img-raven");
  ravenImg.src = `./images/raven-${status}.webp`;
}

export function showFlash(text) {
  const flash = document.getElementById("flash");
  const message = document.createElement("p");
  message.textContent = text;
  flash.appendChild(message);
  flash.classList.remove("flash--animate");
  void flash.offsetWidth;
  flash.classList.add("flash--animate");

  flash.addEventListener(
    "animationend",
    () => {
      flash.classList.remove("flash--animate");
      flash.removeChild(message);
      flash.textContent = "";
    },
    { once: true }
  );
}
