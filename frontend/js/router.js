import { viewHome, viewError } from "./dom.js";
import { setView } from "./util.js";
import { doSearch } from "./html.js";

export function navigate(path) {
  // paths: "/", "/search?publisher=...&character=..."
  location.hash = "#" + path.replace(/^#/, "");
}

function parseHash() {
  const h = location.hash.slice(1) || "/";
  const [pathname, query = ""] = h.split("?");
  const params = new URLSearchParams(query);
  return { pathname, params };
}

window.addEventListener("hashchange", renderFromHash);

export async function renderFromHash() {
  const { pathname, params } = parseHash();
  if (pathname === "/") {
    setView(viewHome());
    return;
  }
  if (pathname === "/search") {
    const publisher = params.get("publisher") || "DC";
    const character = params.get("character") || "";
    setView(
      viewHome({
        initialPublisher: publisher,
        initialCharacter: character,
      })
    );
    if (character) await doSearch({ publisher, character });
    return;
  }
  setView(viewError({ message: "Unknown path." }));
}
