package com.santos.ravenapi.model.dto.search.disambiguation;

import java.util.Map;

public record DisambiguationQuery(Map<String, Page> pages) {

}
