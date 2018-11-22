package com.msr.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AffectedComponentLevel extends HashMap<String, ArrayList<MsrIssue>> {

	public List<MsrIssue> getAllIssues() {
		List<MsrIssue> issues = new ArrayList<>();
		for (String affectedComponent : this.keySet()) {
			issues.addAll(this.get(affectedComponent));
		}
		return issues;
	}

	/**
	 * 
	 * @param month [1..12]
	 * @return
	 */
	public List<MsrIssue> getAllIssuesByAuthor(String affectedComponent) {
		return this.get(affectedComponent);
	}

	/**
	 * Create document by merging all issue descriptions
	 * 
	 * @return
	 */
	public String getDocument2Gram(String affectedComponent) {
		String document = new String();
		for (MsrIssue issue : this.get(affectedComponent))
			document += /* issue.getAffectedComponent() + " " + */ issue.getDescription2Gram() + " ";

		document = document.replaceAll("\\s+", " ");
		return document;
	}

	/**
	 * Create document by merging all issue descriptions
	 * 
	 * @return
	 */
	public String getDocument1Gram(String affectedComponent) {
		String document = new String();
		for (MsrIssue issue : this.get(affectedComponent))
			document += /* issue.getAffectedComponent() + " " + */ issue.getDescription1Gram() + " ";

		document = document.replaceAll("\\s+", " ");
		return document;
	}

	public String getDocument(String affectedComponent) {
		String document = new String();
		for (MsrIssue issue : this.get(affectedComponent))
			document += issue.getDocument() + "\n";

		return document;
	}
}
