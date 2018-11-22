package com.msr.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VersiontoYearLevel extends HashMap<String, YearToMonthLevel> {

	public List<MsrIssue> getAllIssues() {
		List<MsrIssue> issues = new ArrayList<>();

		for (String version : this.keySet())
			issues.addAll(this.get(version).getAllIssues());
		return issues;
	}

	/**
	 * 
	 * @param year
	 * @return
	 */
	public List<MsrIssue> getAllIssuesByVersion(String version) {
		return this.get(version).getAllIssues();
	}

	/**
	 * Create document by merging all issue descriptions
	 * 
	 * @return
	 */
	public String getDocument2Gram(String version) {
		String document = new String();

		for (MsrIssue issue : getAllIssuesByVersion(version))
			document += /* issue.getAffectedComponent() + " " + */issue.getDescription2Gram() + " ";

		document = document.replaceAll("\\s+", " ");
		return document;
	}

	/**
	 * Create document by merging all issue descriptions
	 * 
	 * @return
	 */
	public String getDocument1Gram(String version) {
		String document = new String();

		for (MsrIssue issue : getAllIssuesByVersion(version))
			document += /* issue.getAffectedComponent() + " " + */issue.getDescription1Gram() + " ";

		document = document.replaceAll("\\s+", " ");
		return document;
	}
}
