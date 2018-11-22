package com.msr.analyzer;

import java.io.File;
import java.util.Map;

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
public class ReportAnalyzer {
	private File reportFile = null;
	private Map<Integer, MsrIssue> msrIssues = null;

	public static void main(String[] args) {
		// STEP: Get all issues belonged to an application, e.g, JDT
		ProductAnalyzer productAnalyzer = new ProductAnalyzer();
		productAnalyzer.setProductFile(new File(".\\data\\msr2013-bug_dataset\\data\\v02\\eclipse\\product.json"));
		productAnalyzer.setSoftwareName("JDT");
		productAnalyzer.parse();

		// STEP: Get author
		ReportAnalyzer reportAnalyzer = new ReportAnalyzer();
		reportAnalyzer.setMsrIssues(productAnalyzer.getMsrIssues());
		reportAnalyzer.setReportFile(new File(".\\data\\msr2013-bug_dataset\\data\\v02\\eclipse\\reports.json"));
		reportAnalyzer.parse();

		for (Integer issueId : reportAnalyzer.getMsrIssues().keySet())
			System.out.println(reportAnalyzer.getMsrIssues().get(issueId));

		System.out.println(reportAnalyzer.getMsrIssues().size());
	}

	public ReportAnalyzer() {
	}

	public void parse() {
		if (reportFile != null && reportFile.exists()/**/
				&& msrIssues != null && msrIssues.size() >= 1) {

			JSONParser parser = new JSONParser();
			String reportContent = Utils.readFileContent(reportFile);

			try {
				JSONObject version = (JSONObject) parser.parse(reportContent);
				JSONObject versionObject = (JSONObject) version.get("reports");

				for (Object issueId : versionObject.keySet()) {

					MsrIssue msrIssue = msrIssues.get(Integer.parseInt(issueId + ""));

					// If the issue belongs to the software we need to analyze
					if (msrIssue != null) {
						Object issue = versionObject.get(issueId);

						if (issue instanceof JSONObject) {
							JSONObject castedIssue = (JSONObject) issue;

							if (msrIssue.getOpeningTime() == (long) castedIssue.get("opening")) {
								// In the case the issue is assigned
								if (castedIssue.get("reporter") != null)
									msrIssue.setReporterID(Integer.parseInt(castedIssue.get("reporter").toString()));
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

	public void setReportFile(File reportFile) {
		this.reportFile = reportFile;
	}

	public File getReportFile() {
		return reportFile;
	}

	public Map<Integer, MsrIssue> getMsrIssues() {
		return msrIssues;
	}

	public void setMsrIssues(Map<Integer, MsrIssue> issues) {
		this.msrIssues = issues;
	}
}
