package com.msr.categorizer;

import java.util.HashMap;
import java.util.Map;

import com.msr.object.MsrIssue;

public class AbstractCategorizer {
	protected Map<Integer, MsrIssue> msrIssues = null;

	/**
	 * For JDT, remove 3599/50857 redundant issues
	 * 
	 * @return
	 */
	public Map<Integer, MsrIssue> filterRedundantMsrIssues() {
		if (msrIssues != null) {
			Map<Integer, MsrIssue> filteredIssues = new HashMap<>();
			for (Integer issueId : msrIssues.keySet())
				if (msrIssues.get(issueId).getReporterID() == -1
						/* no reporter */ || msrIssues.get(issueId).getAssignedAuthorID() == -1 /* no assigned author */
						|| msrIssues.get(issueId).getVersion() == null /* no version */
						|| msrIssues.get(issueId).getDescription() == null /* no description */) {
					System.out.println(msrIssues.get(issueId).getIssueID());
					// nothing to do
				} else {
					filteredIssues.put(issueId, msrIssues.get(issueId));
				}

			msrIssues = filteredIssues;
		}
		return msrIssues;
	}

	public void setMsrIssues(Map<Integer, MsrIssue> msrIssues) {
		this.msrIssues = msrIssues;
	}

	public Map<Integer, MsrIssue> getMsrIssues() {
		return msrIssues;
	}
}
