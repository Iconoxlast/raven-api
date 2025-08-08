package com.santos.ravenapi.model.dto.search.appearances;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FandomAppearancesDTO(@JsonProperty("continue") Continue cont, Query query) {

}
