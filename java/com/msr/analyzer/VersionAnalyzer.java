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
public class VersionAnalyzer {
	private File versionFile = null;
	private Map<Integer, MsrIssue> msrIssues = null;

	public static void main(String[] args) {
		// STEP: Get all issues belonged to an application, e.g, JDT
		ProductAnalyzer productAnalyzer = new ProductAnalyzer();
		productAnalyzer.setProductFile(new File(".\\data\\msr2013-bug_dataset\\data\\v02\\eclipse\\product.json"));
		productAnalyzer.setSoftwareName("JDT");
		productAnalyzer.parse();

		// STEP: Analyze the short description file to get the content of issues
		ShortDescAnalyzer shortDescAnalyzer = new ShortDescAnalyzer();
		shortDescAnalyzer.setMsrIssues(productAnalyzer.getMsrIssues());
		shortDescAnalyzer
				.setShortDescFile(new File(".\\data\\msr2013-bug_dataset\\data\\v02\\eclipse\\short_desc.json"));
		shortDescAnalyzer.parse();

		// STEP: get version
		VersionAnalyzer versionAnalyzer = new VersionAnalyzer();
		versionAnalyzer.setMsrIssues(shortDescAnalyzer.getMsrIssues());
		versionAnalyzer.setVersionFile(new File(".\\data\\msr2013-bug_dataset\\data\\v02\\eclipse\\version.json"));
		versionAnalyzer.parse();

		for (Integer issueId : versionAnalyzer.getMsrIssues().keySet())
			System.out.println(versionAnalyzer.getMsrIssues().get(issueId));

		System.out.println(versionAnalyzer.getMsrIssues().size());
	}

	public VersionAnalyzer() {
	}

	public void parse() {
		if (versionFile != null && versionFile.exists()/**/
				&& msrIssues != null && msrIssues.size() >= 1) {

			JSONParser parser = new JSONParser();
			String shortDescription = Utils.readFileContent(versionFile);

			try {
				JSONObject version = (JSONObject) parser.parse(shortDescription);
				JSONObject versionObject = (JSONObject) version.get("version");

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
										msrIssue.setVersion(castedElement.get("what").toString());
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

	public void setVersionFile(File productFile) {
		this.versionFile = productFile;
	}

	public File getVersionFile() {
		return versionFile;
	}

	public Map<Integer, MsrIssue> getMsrIssues() {
		return msrIssues;
	}

	public void setMsrIssues(Map<Integer, MsrIssue> issues) {
		this.msrIssues = issues;
	}
}
