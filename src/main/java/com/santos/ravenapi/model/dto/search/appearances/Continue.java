package com.santos.ravenapi.model.dto.search.appearances;

import com.fasterxml.jackson.annotation.JsonAlias;

public record Continue(String cmcontinue, @JsonAlias("continue") String cont) {

}
