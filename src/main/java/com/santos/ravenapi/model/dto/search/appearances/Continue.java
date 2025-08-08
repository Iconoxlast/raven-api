package com.santos.ravenapi.model.dto.search.appearances;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Continue(String cmcontinue, @JsonProperty("continue") String cont) {

}
