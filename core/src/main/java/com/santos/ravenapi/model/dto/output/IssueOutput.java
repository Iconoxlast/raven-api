package com.santos.ravenapi.model.dto.output;

import java.time.LocalDate;
import java.time.YearMonth;

public record IssueOutput(YearMonth date, String title, Long id) implements OutputDTO {

	public LocalDate getLocalDate() {
		return date().atDay(1);
	}
}
