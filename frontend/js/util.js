import { API_BASE } from "./html.js";

export const $app = document.getElementById("app");

export function setView(el) {
  document.getElementById("app").replaceChildren(el);
  window.scrollTo({ top: 0, behavior: "smooth" });
}

export function formatYearMonth(ym) {
  if (!ym || !/^\d{4}-\d{2}$/.test(ym)) return ym || "";
  const [y, m] = ym.split("-");
  const date = new Date(Date.UTC(y, m, 1));
  const monthName = date.toLocaleDateString("en-US", { month: "long" });
  return `${monthName}, ${y}`;
  // return `${m}/${y}`;
}

export function getFandomPageUrl({ publisher, page }) {
  let url;
  switch (publisher) {
    case "DC":
      url = `https://dc.fandom.com/wiki/${page.replaceAll(" ", "_")}`;
      break;
    case "MARVEL":
      url = `https://marvel.fandom.com/wiki/${page.replaceAll(" ", "_")}`;
      break;
    default:
      throw new Error(`Unidentified publisher: ${publisher}`);
  }
  return url;
}

export function buildUrl(publisher, character) {
  const sp = new URLSearchParams();
  sp.set("publisher", publisher.toUpperCase());
  sp.set("character", character);
  return `${API_BASE}?${sp.toString()}`;
}

export function getRandomSearchingMessage() {
  const messages = [
    "She's extending her mind into the ether, questing for its secrets...",
    "She's peering beyond the veil, sifting through unseen currents for truth...",
    "Her mind is searching the psychic realms for answers...",
    "Her Soul-Self is venturing into the astral realm, seeking hidden knowledge...",
  ];
  return (
    "Please wait... " + messages[Math.floor(Math.random() * messages.length)]
  );
}

