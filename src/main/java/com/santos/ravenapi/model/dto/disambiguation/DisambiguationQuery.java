package com.santos.ravenapi.model.dto.disambiguation;

import java.util.Map;

public record DisambiguationQuery(Map<String, Page> pages) {

}
