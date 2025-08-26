package com.santos.ravenapi.search.service.parser;

import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FandomDataParser {

	/**
	 * Parses the informed category name in order to convert it to a YearMonth
	 * object that should be used as the publication date, based on specific
	 * patterns.
	 * 
	 * @param category
	 * @return String converted to YearMonth, or a "1900-01" YearMonth in case the
	 *         String value is invalid for parsing
	 */
	public synchronized static YearMonth getYearMonth(String category) {
		try {
			return YearMonth.parse(category, DateTimeFormatter.ofPattern("uuuu, MMMM", Locale.ENGLISH));
		} catch (Exception e) {
			try {
				return YearMonth.parse(category, DateTimeFormatter.ofPattern("MMMM, uuuu", Locale.ENGLISH));
			} catch (Exception e2) {
				try {
					return YearMonth.of(Year.parse(category).getValue(), 1);
				} catch (Exception e3) {
					return YearMonth.of(1900, 1);
				}
			}
		}
	}

}
