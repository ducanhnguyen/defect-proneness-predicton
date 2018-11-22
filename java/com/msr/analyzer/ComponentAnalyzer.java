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
public class ComponentAnalyzer {
	private File componentFile = null;
	private Map<Integer, MsrIssue> msrIssues = null;

	public static void main(String[] args) {
		// STEP: Get all issues belonged to an application, e.g, JDT
		ProductAnalyzer productAnalyzer = new ProductAnalyzer();
		productAnalyzer.setProductFile(new File(".\\data\\msr2013-bug_dataset\\data\\v02\\eclipse\\product.json"));
		productAnalyzer.setSoftwareName("JDT");
		productAnalyzer.parse();

		// STEP: Get affected component
		ComponentAnalyzer componentAnalyzer = new ComponentAnalyzer();
		componentAnalyzer.setMsrIssues(productAnalyzer.getMsrIssues());
		componentAnalyzer
				.setComponentFile(new File(".\\data\\msr2013-bug_dataset\\data\\v02\\eclipse\\component.json"));
		componentAnalyzer.parse();

		for (Integer issueId : componentAnalyzer.getMsrIssues().keySet())
			System.out.println(componentAnalyzer.getMsrIssues().get(issueId));

		System.out.println(componentAnalyzer.getMsrIssues().size());
	}

	public ComponentAnalyzer() {
	}

	public void parse() {
		if (componentFile != null && componentFile.exists()/**/
				&& msrIssues != null && msrIssues.size() >= 1) {

			JSONParser parser = new JSONParser();
			String shortDescription = Utils.readFileContent(componentFile);

			try {
				JSONObject version = (JSONObject) parser.parse(shortDescription);
				JSONObject versionObject = (JSONObject) version.get("component");

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
										msrIssue.setAffectedComponent(castedElement.get("what").toString());
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

	public void setComponentFile(File componentFile) {
		this.componentFile = componentFile;
	}

	public File getComponentFile() {
		return componentFile;
	}

	public Map<Integer, MsrIssue> getMsrIssues() {
		return msrIssues;
	}

	public void setMsrIssues(Map<Integer, MsrIssue> issues) {
		this.msrIssues = issues;
	}
}
