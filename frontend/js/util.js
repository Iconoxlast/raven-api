import { API_BASE } from "./config.js";

export const $app = document.getElementById("app");

export function setView(el) {
  document.getElementById("app").replaceChildren(el);
  // rolar topo em navegações
  window.scrollTo({ top: 0, behavior: "smooth" });
}

export function formatYearMonth(ym) {
  // espera "YYYY-MM"
  if (!ym || !/^\d{4}-\d{2}$/.test(ym)) return ym || "";
  const [y, m] = ym.split("-");
  return `${m}/${y}`;
}

export function getFandomPageUrl({ publisher, page }) {
  let url;
  switch (publisher) {
    case "DC":
      url = `https://dc.fandom.com/wiki/${page.replaceAll(" ", "_")}`;
      break;
    case "Marvel":
      url = `https://marvel.fandom.com/wiki/${ccharacter.replaceAll(" ", "_")}`;
      break;
    default:
      throw new Error(`Unidentified publisher: ${publisher}`);
  }
  return url;
}

export function buildUrl(publisher, character) {
  const sp = new URLSearchParams();
  sp.set("publisher", publisher);
  sp.set("character", character);
  return `${API_BASE}?${sp.toString()}`;
}
