import { navigate, renderFromHash } from "./router.js";
// start
renderFromHash();
if (!location.hash) {
  navigate("/");
}

