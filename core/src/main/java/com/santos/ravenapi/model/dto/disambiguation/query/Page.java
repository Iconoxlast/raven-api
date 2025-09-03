package com.santos.ravenapi.model.dto.disambiguation.query;

import java.util.List;

public record Page(String title, List<Revision> revisions) {

}
