package com.santos.ravenapi.model.dto.issues;

import com.santos.ravenapi.search.service.parser.FandomDateParser;

public record Category(String title) {

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
			String category = getCategory();
			if (category.length() < 4) {
				return false;
			}
			FandomDateParser.getYearMonth(category, false);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public String getCategory() {
		return title.replace("Category:", "");
	}

}
