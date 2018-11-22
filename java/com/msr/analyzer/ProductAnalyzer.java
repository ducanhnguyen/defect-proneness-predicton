package com.msr.analyzer;

import java.io.File;
import java.util.HashMap;
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
public class ProductAnalyzer {
	private File productFile = null;
	private Map<Integer, MsrIssue> msrIssues = new HashMap<>();
	private String softwareName = null;

	public static void main(String[] args) {
		ProductAnalyzer productAnalyzer = new ProductAnalyzer();
		productAnalyzer.setProductFile(new File(".\\data\\msr2013-bug_dataset\\data\\v02\\eclipse\\product.json"));
		productAnalyzer.setSoftwareName("JDT");
		productAnalyzer.parse();

		for (Integer issueId : productAnalyzer.getMsrIssues().keySet())
			System.out.println(productAnalyzer.getMsrIssues().get(issueId));

		System.out.println(productAnalyzer.getMsrIssues().size());
	}

	public ProductAnalyzer() {
	}

	public void parse() {
		if (productFile != null && productFile.exists() /**/
				&& softwareName != null) {

			JSONParser parser = new JSONParser();
			String productContent = Utils.readFileContent(productFile);

			try {
				JSONObject productObject = (JSONObject) parser.parse(productContent);
				JSONObject msrIssuesObject = (JSONObject) productObject.get("product");

				for (Object issueId : msrIssuesObject.keySet()) {
					Object issue = msrIssuesObject.get(issueId);

					if (issue instanceof JSONArray) {
						JSONArray castedIssue = (JSONArray) issue;

						for (Object element : castedIssue) {
							if (element instanceof JSONObject) {
								JSONObject castedElement = (JSONObject) element;
								if (castedElement.get("what").equals(softwareName)) {
									MsrIssue msrIssue = new MsrIssue();

									msrIssue.setOpeningTime((long) castedElement.get("when"));

									int issueID = Integer.parseInt(issueId.toString());
									msrIssue.setIssueID(issueID);

									msrIssue.setSoftware(softwareName);

									msrIssues.put(issueID, msrIssue);
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

	public void setProductFile(File productFile) {
		this.productFile = productFile;
	}

	public File getProductFile() {
		return productFile;
	}

	public Map<Integer, MsrIssue> getMsrIssues() {
		return msrIssues;
	}

	public void setMsrIssues(Map<Integer, MsrIssue> issues) {
		this.msrIssues = issues;
	}

	public void setSoftwareName(String softwareName) {
		this.softwareName = softwareName;
	}

	public String getSoftwareName() {
		return softwareName;
	}
}
