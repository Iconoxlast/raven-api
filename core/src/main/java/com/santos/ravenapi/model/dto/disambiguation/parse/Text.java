package com.santos.ravenapi.model.dto.disambiguation.parse;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Text(@JsonProperty("*") String content) {

}
