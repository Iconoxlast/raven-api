package com.santos.ravenapi.model.dto.appearances;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FandomAppearancesDTO(@JsonProperty("continue") Continue cont, Query query) {

}
