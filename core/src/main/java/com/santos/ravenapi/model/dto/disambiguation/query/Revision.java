package com.santos.ravenapi.model.dto.disambiguation.query;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Revision(@JsonProperty("*") String content) {

}
