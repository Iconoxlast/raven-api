package com.santos.ravenapi.model.dto.issues;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FandomIssueDetailsDTO(@JsonProperty("continue") Continue cont, Query query) {

}
