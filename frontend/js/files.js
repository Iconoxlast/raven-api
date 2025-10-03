import { searchedCharacter, showFlash } from "./dom.js";

export function getAppearancesListTxt() {
  const blob = new Blob([getAppearancesListContent()], {
    type: "text/plain;charset=utf-8",
  });
  const url = URL.createObjectURL(blob);
  const a = document.createElement("a");
  a.href = url;
  a.download = `${searchedCharacter} Appearances.txt`;
  document.body.appendChild(a);
  a.click();
  a.remove();
  setTimeout(() => URL.revokeObjectURL(url), 0);
  showFlash("Success!");
}

function getAppearancesListContent() {
  var content = `Raven API — ${searchedCharacter} appearances in chronological order\r\n`;
  var list = document.getElementById("appearances");
  for (const entry of list.querySelectorAll("li")) {
    if (entry.className === "month") {
      content += `\r\n\r\n${entry.textContent}`;
    } else if (entry.className === "issue") {
      content += `\r\n\t${entry.querySelector(".title").textContent}`;
    }
  }
  return content;
}
