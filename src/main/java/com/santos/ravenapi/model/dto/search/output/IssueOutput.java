package com.santos.ravenapi.model.dto.search.output;

import java.time.YearMonth;

public record IssueOutput(YearMonth date, String title, Integer id) implements OutputDTO {

}
