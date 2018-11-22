package com.msr.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class YearToMonthLevel extends HashMap<Integer, MonthLevel> {

	public List<MsrIssue> getAllIssues() {
		List<MsrIssue> issues = new ArrayList<>();

		for (Integer year : this.keySet())
			issues.addAll(this.get(year).getAllIssues());

		return issues;
	}

	/**
	 * 
	 * @param year
	 * @return
	 */
	public List<MsrIssue> getAllIssuesByYear(int year) {
		return this.get(year).getAllIssues();
	}

	/**
	 * Create document by merging all issue descriptions
	 * 
	 * @return
	 */
	public String getDocument2Gram(int year) {
		String document = new String();

		for (MsrIssue issue : getAllIssuesByYear(year))
			document += /* issue.getAffectedComponent() + " " + */ issue.getDescription2Gram() + " ";

		document = document.replaceAll("\\s+", " ");
		return document;
	}

	/**
	 * Create document by merging all issue descriptions
	 * 
	 * @return
	 */
	public String getDocument1Gram(int year) {
		String document = new String();

		for (MsrIssue issue : getAllIssuesByYear(year))
			document += /* issue.getAffectedComponent() + " " + */issue.getDescription1Gram() + " ";

		document = document.replaceAll("\\s+", " ");
		return document;
	}
}
