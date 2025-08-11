package com.santos.ravenapi.model.dto.search.disambiguation;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Revision(@JsonProperty("*") String content) {

}
