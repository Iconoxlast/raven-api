package com.santos.ravenapi.model.dto.output;

import java.util.List;

public record DisambiguationOutput(List<String> characterVersions) implements OutputDTO {

}
