package com.msr.categorizer;

import java.io.File;

import com.msr.analyzer.AssignedAuthorAnalyzer;
import com.msr.analyzer.ComponentAnalyzer;
import com.msr.analyzer.ProductAnalyzer;
import com.msr.analyzer.ReportAnalyzer;
import com.msr.analyzer.ResolutionAnalyzer;
import com.msr.analyzer.ShortDescAnalyzer;
import com.msr.analyzer.VersionAnalyzer;
import com.msr.object.AffectedComponentLevel;
import com.utils.Utils;

/**
 * Run your categorizer here
 * 
 * @author adn0019
 *
 */
public class Main {
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

		// STEP: get the state of issue
		ResolutionAnalyzer resolutionAnalyzer = new ResolutionAnalyzer();
		resolutionAnalyzer.setMsrIssues(versionAnalyzer.getMsrIssues());
		resolutionAnalyzer
				.setVersionFile(new File(".\\data\\msr2013-bug_dataset\\data\\v02\\eclipse\\resolution.json"));
		resolutionAnalyzer.parse();

		// STEP: Get affected component
		ComponentAnalyzer componentAnalyzer = new ComponentAnalyzer();
		componentAnalyzer.setMsrIssues(productAnalyzer.getMsrIssues());
		componentAnalyzer
				.setComponentFile(new File(".\\data\\msr2013-bug_dataset\\data\\v02\\eclipse\\component.json"));
		componentAnalyzer.parse();

		// STEP: Get author
		AssignedAuthorAnalyzer authorAnalyzer = new AssignedAuthorAnalyzer();
		authorAnalyzer.setMsrIssues(componentAnalyzer.getMsrIssues());
		authorAnalyzer.setAuthorFile(new File(".\\data\\msr2013-bug_dataset\\data\\v02\\eclipse\\assigned_to.json"));
		authorAnalyzer.parse();

		// STEP: Get reporter
		ReportAnalyzer reportAnalyzer = new ReportAnalyzer();
		reportAnalyzer.setMsrIssues(authorAnalyzer.getMsrIssues());
		reportAnalyzer.setReportFile(new File(".\\data\\msr2013-bug_dataset\\data\\v02\\eclipse\\reports.json"));
		reportAnalyzer.parse();

		// STEP: Categorize
		MsrIssueCategorizerType4 categorizer = new MsrIssueCategorizerType4();
		categorizer.setMsrIssues(reportAnalyzer.getMsrIssues());
		categorizer.filterRedundantMsrIssues();
		categorizer.parse();

		// Export to file
		File outputFolder = new File("C:\\Users\\adn0019\\Desktop\\affected_component_nlp_1gram");
		if (outputFolder.exists())
			outputFolder.delete();
		outputFolder.mkdirs();

		for (String version : categorizer.getVersionLevel().keySet()) {
			System.out.println("Export version " + version);

			// create version folder
			File versionFile = new File(outputFolder.getAbsolutePath() + "\\" + version);
			if (versionFile.exists())
				versionFile.delete();
			versionFile.mkdirs();

			// Export
			AffectedComponentLevel affectedComponentLevels = categorizer.getVersionLevel().get(version);
			for (String affectedComponent : affectedComponentLevels.keySet())
				Utils.exportToFile(new File(versionFile.getAbsolutePath() + "\\" + affectedComponent + ".txt"),
						affectedComponentLevels.getDocument1Gram(affectedComponent));
		}
	}
}
