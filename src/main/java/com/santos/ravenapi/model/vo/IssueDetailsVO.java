package com.santos.ravenapi.model.vo;

import java.time.YearMonth;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class IssueDetailsVO {

	@NonNull
	private String title;
	@NonNull
	private Integer id;
	private YearMonth date;
}
