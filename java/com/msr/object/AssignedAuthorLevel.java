package com.msr.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AssignedAuthorLevel extends HashMap<Integer, ArrayList<MsrIssue>> {

	public List<MsrIssue> getAllIssues() {
		List<MsrIssue> issues = new ArrayList<>();
		for (Integer assignedAuthor : this.keySet()) {
			issues.addAll(this.get(assignedAuthor));
		}
		return issues;
	}

	/**
	 * 
	 * @param month [1..12]
	 * @return
	 */
	public List<MsrIssue> getAllIssuesByAuthor(int reporterID) {
		return this.get(reporterID);
	}

	/**
	 * Create document by merging all issue descriptions
	 * 
	 * @return
	 */
	public String getDocument2Gram(int assignedAuthor) {
		String document = new String();
		for (MsrIssue issue : this.get(assignedAuthor))
			document += /* issue.getAffectedComponent() + " " + */ issue.getDescription2Gram() + " ";

		document = document.replaceAll("\\s+", " ");
		return document;
	}

	/**
	 * Create document by merging all issue descriptions
	 * 
	 * @return
	 */
	public String getDocument1Gram(int assignedAuthor) {
		String document = new String();
		for (MsrIssue issue : this.get(assignedAuthor))
			document += /* issue.getAffectedComponent() + " " + */ issue.getDescription1Gram() + " ";

		document = document.replaceAll("\\s+", " ");
		return document;
	}

	public String getDocument(int assignedAuthor) {
		String document = new String();
		for (MsrIssue issue : this.get(assignedAuthor))
			document += issue.getDocument() + "\n";

		return document;
	}
}
