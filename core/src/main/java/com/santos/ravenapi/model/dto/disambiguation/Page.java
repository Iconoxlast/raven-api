package com.santos.ravenapi.model.dto.disambiguation;

import java.util.List;

public record Page(String title, List<Revision> revisions) {

}
