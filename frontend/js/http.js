import {
  viewHome,
  viewAppearances,
  viewDisambiguation,
  viewError,
} from "./dom.js";
import { navigate } from "./router.js";
import { setView, buildUrl, getRandomSearchingMessage } from "./util.js";

export const API_BASE = "http://localhost:8080/search";

function withTimeout(ms, signal) {
  const ctrl = new AbortController();
  const onAbort = () => ctrl.abort();
  if (signal) signal.addEventListener("abort", onAbort, { once: true });
  const t = setTimeout(() => ctrl.abort(), ms);
  return {
    signal: ctrl.signal,
    cleanup: () => {
      clearTimeout(t);
      signal?.removeEventListener("abort", onAbort);
    },
  };
}

export async function doSearch({ publisher, character }) {
  setView(
    viewHome({
      initialPublisher: publisher,
      initialCharacter: character,
      waitScreen: true,
    })
  );
  const btn = document.querySelector("#btnSearch");
  const status = document.querySelector("#status");
  if (btn) btn.disabled = true;
  if (status) status.textContent = getRandomSearchingMessage();

  const ac = new AbortController();
  const { signal, cleanup } = withTimeout(300000, ac.signal); // 5m
  let res, data;
  try {
    const url = buildUrl(publisher, character);
    res = await fetch(url, { method: "GET", signal });
    data = await res.json();
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    var responseType = res.headers.get("Type");

    switch (responseType) {
      case "disambiguation":
        setView(
          viewDisambiguation({
            publisher,
            character,
            versions: data.characterVersions,
          })
        );
        navigate(
          `/search?publisher=${encodeURIComponent(
            publisher
          )}&character=${encodeURIComponent(character)}`
        );
        break;
      case "appearances":
        var appearancesData = Object.entries(data.publicationmonths);
        setView(
          viewAppearances({
            publisher,
            character,
            appearancesData,
          })
        );
        navigate(
          `/search?publisher=${encodeURIComponent(
            publisher
          )}&character=${encodeURIComponent(character)}`
        );
        break;
      default:
        // fallback
        setView(viewError({ message: "Unexpected API response." }));
    }
  } catch (err) {
    console.log(err);
    const msg =
      data?.message ??
      (err?.name === "AbortError"
        ? "Response time exceeded."
        : "Failed request.");
    setView(viewError({ message: msg }));
  } finally {
    cleanup();
    if (btn) btn.disabled = false;
    if (status) status.textContent = "";
  }
}
