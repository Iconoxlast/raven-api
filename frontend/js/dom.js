import { doSearch } from "./html.js";
import { navigate } from "./router.js";
import { formatYearMonth, getFandomPageUrl } from "./util.js";

export var publisherSelect;
export var characterInput;

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
  const wrap = document.createElement("div");

  const header = document.createElement("div");
  header.className = "header-disamb";
  const h2 = document.createElement("h2");
  h2.textContent = `Disambiguation — ${ character }`;
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

  const list = document.createElement("ul");
  list.className = "list";

  versions.forEach((name) => {
    const li = document.createElement("li");
    const left = document.createElement("div");
    // left.textContent = name;
    left.append(
        getHyperlink({
          text: name,
          url: getFandomPageUrl({ publisher, page: name }),
        })
      );
    const btn = document.createElement("button");
    btn.className = "btn-list"
    // btn.className = "linklike";
    btn.textContent = "Search";
    btn.addEventListener("click", () =>
      doSearch({ publisher, character: name })
    );
    li.append(left, btn);
    list.append(li);
  });

  wrap.append(header, list);
  return wrap;
}

export function viewAppearances({ publisher, character, appearancesData }) {
  const wrap = document.createElement("div");
  const header = document.createElement("div");
  header.className = "row";
  const h = document.createElement("h2");
  // h.textContent = `Appearances — ${character} (${publisher})`;
  h.innerHTML = `Appearances — <a href="${getFandomPageUrl({
    publisher,
    page: character,
  })}" target="_blank" rel="noopener noreferrer">${character}</a> (${publisher})`;
  const back = document.createElement("button");
  back.className = "linklike";
  back.textContent = "← New search";
  back.addEventListener("click", () => navigate("/"));
  header.append(h, back);

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

  for (const [publicationMonth, issues] of appearancesData) {
    var li = document.createElement("li");
    var h1 = document.createElement("h1");
    h1.textContent =
      publicationMonth === "1900-01"
        ? "Undefined"
        : formatYearMonth(publicationMonth);
    li.append(h1);
    list.append(li);
    issues.forEach((issue) => {
      //   console.log(` - (${issue.id}) ${issue.title}`);
      li = document.createElement("li");
      var title = document.createElement("div");
      // title.textContent = issue.title ?? "(no title)";
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
  return list;
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
  const wrap = document.createElement("div");
  wrap.innerHTML = `
        <h2 class="error">Error</h2>
        <p class="error">${message}</p>
        <hr />
      `;
  wrap.append(viewHome());
  return wrap;
}
