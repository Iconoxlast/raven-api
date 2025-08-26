package com.santos.ravenapi.model.dto.issues;

import java.util.List;

public record Page(Long pageid, String title, List<Category> categories) {

}
