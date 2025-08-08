package com.santos.ravenapi.model.dto.search.output;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AppearancesOutput(
		@JsonProperty("publicationmonths") Map<YearMonth, List<IssueOutput>> publicationMonths)
		implements OutputDTO {

}
