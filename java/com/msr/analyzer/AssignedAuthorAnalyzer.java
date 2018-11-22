package com.msr.analyzer;

import java.io.File;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.msr.object.MsrIssue;
import com.utils.Utils;

/**
 * Product file:
 * https://github.com/ansymo/msr2013-bug_dataset/tree/master/data/v02/eclipse
 * 
 * @author adn0019
 *
 */
public class AssignedAuthorAnalyzer {
	private File authorFile = null;
	private Map<Integer, MsrIssue> msrIssues = null;

	public static void main(String[] args) {
		// STEP: Get all issues belonged to an application, e.g, JDT
		ProductAnalyzer productAnalyzer = new ProductAnalyzer();
		productAnalyzer.setProductFile(new File(".\\data\\msr2013-bug_dataset\\data\\v02\\eclipse\\product.json"));
		productAnalyzer.setSoftwareName("JDT");
		productAnalyzer.parse();

		// STEP: Get author
		AssignedAuthorAnalyzer authorAnalyzer = new AssignedAuthorAnalyzer();
		authorAnalyzer.setMsrIssues(productAnalyzer.getMsrIssues());
		authorAnalyzer.setAuthorFile(new File(".\\data\\msr2013-bug_dataset\\data\\v02\\eclipse\\assigned_to.json"));
		authorAnalyzer.parse();

		for (Integer issueId : authorAnalyzer.getMsrIssues().keySet())
			System.out.println(authorAnalyzer.getMsrIssues().get(issueId));

		System.out.println(authorAnalyzer.getMsrIssues().size());
	}

	public AssignedAuthorAnalyzer() {
	}

	public void parse() {
		if (authorFile != null && authorFile.exists()/**/
				&& msrIssues != null && msrIssues.size() >= 1) {

			JSONParser parser = new JSONParser();
			String shortDescription = Utils.readFileContent(authorFile);

			try {
				JSONObject version = (JSONObject) parser.parse(shortDescription);
				JSONObject versionObject = (JSONObject) version.get("assigned_to");

				for (Object issueId : versionObject.keySet()) {
					MsrIssue msrIssue = msrIssues.get(Integer.parseInt(issueId + ""));

					// If the issue belongs to the software we need to analyze
					if (msrIssue != null) {
						Object issue = versionObject.get(issueId);

						if (issue instanceof JSONArray) {
							JSONArray castedIssue = (JSONArray) issue;

							for (Object element : castedIssue) {
								if (element instanceof JSONObject) {
									JSONObject castedElement = (JSONObject) element;

									if (msrIssue.getOpeningTime() == (long) castedElement.get("when")) {
										// In the case the issue is assigned
										if (castedElement.get("what") != null)
											msrIssue.setAssignedAuthorID(castedElement.get("what").toString().hashCode());
										break;
									}
								}
							}
						}
					}
				}
			} catch (ParseException pe) {
				System.out.println("position: " + pe.getPosition());
				System.out.println(pe);
			}
		}
	}

	public void setAuthorFile(File componentFile) {
		this.authorFile = componentFile;
	}

	public File getAuthorFile() {
		return authorFile;
	}

	public Map<Integer, MsrIssue> getMsrIssues() {
		return msrIssues;
	}

	public void setMsrIssues(Map<Integer, MsrIssue> issues) {
		this.msrIssues = issues;
	}
}
