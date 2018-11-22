package com.msr.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MonthLevel extends HashMap<Integer, ArrayList<MsrIssue>> {

	public List<MsrIssue> getAllIssues() {
		List<MsrIssue> issues = new ArrayList<>();
		for (Integer month : this.keySet()) {
			issues.addAll(this.get(month));
		}
		return issues;
	}

	/**
	 * 
	 * @param month [1..12]
	 * @return
	 */
	public List<MsrIssue> getAllIssuesByMonth(int month) {
		return this.get(month);
	}

	/**
	 * Create document by merging all issue descriptions
	 * 
	 * @return
	 */
	public String getDocument2Gram(int month) {
		String document = new String();
		for (MsrIssue issue : this.get(month))
			document += /* issue.getAffectedComponent() + " " + */ issue.getDescription2Gram() + " ";

		document = document.replaceAll("\\s+", " ");
		return document;
	}

	/**
	 * Create document by merging all issue descriptions
	 * 
	 * @return
	 */
	public String getDocument1Gram(int month) {
		String document = new String();
		for (MsrIssue issue : this.get(month))
			document += /* issue.getAffectedComponent() + " " + */ issue.getDescription1Gram() + " ";

		document = document.replaceAll("\\s+", " ");
		return document;
	}

}
