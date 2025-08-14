package com.santos.ravenapi.model.dto.issues;

import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public record Category(String category) {

	/**
	 * Tests whether the category title indicates an issue's publication date. The
	 * pattern needs to be either "Month, NNNN" or "NNNN, Month". This YearMonth
	 * object will be used to sort out the list. It's possible for the issue not to
	 * have a publication month on its article, so at least the publication year
	 * should be taken into consideration.
	 * 
	 * @return true if the category is considered valid, false if not
	 */
	public boolean isCategoryIssueDate() {
		try {
			if (category.length() < 4) {
				return false;
			}
			try {
				YearMonth.parse(category, DateTimeFormatter.ofPattern("uuuu,_MMMM", Locale.ENGLISH));
			} catch (Exception e) {
				try {
					YearMonth.parse(category, DateTimeFormatter.ofPattern("MMMM,_uuuu", Locale.ENGLISH));
				} catch (Exception e2) {
					Year.parse(category);
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
