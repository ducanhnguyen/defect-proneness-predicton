package com.msr.categorizer;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.msr.analyzer.AssignedAuthorAnalyzer;
import com.msr.analyzer.ComponentAnalyzer;
import com.msr.analyzer.ProductAnalyzer;
import com.msr.analyzer.ReportAnalyzer;
import com.msr.analyzer.ResolutionAnalyzer;
import com.msr.analyzer.ShortDescAnalyzer;
import com.msr.analyzer.VersionAnalyzer;
import com.msr.object.MonthLevel;
import com.msr.object.MsrIssue;
import com.msr.object.VersiontoYearLevel;
import com.msr.object.YearToMonthLevel;
import com.utils.Utils;

/**
 * Categorize issues in terms of version, year, month, etc.<br/>
 * 
 * <Type 1><br/>
 * Version ---- Year ---- Month (January) <br/>
 * ................. ---- Month (February) <br/>
 * ................. ---- Month (...)<br/>
 * <Type 2><br/>
 * Version ---- Reporter<br/>
 * ....... ---- Reporter<br/>
 * 
 * @author adn0019
 *
 */
public class MsrIssueCategorizerType1 extends AbstractCategorizer {

	private VersiontoYearLevel versionLevel = new VersiontoYearLevel();

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
		MsrIssueCategorizerType1 categorizer = new MsrIssueCategorizerType1();
		categorizer.setMsrIssues(reportAnalyzer.getMsrIssues());
		categorizer.filterRedundantMsrIssues();
		categorizer.parse();

		// Export to file
		for (String version : categorizer.getVersionLevel().keySet()) {
			System.out.println("Export version " + version);
			Utils.exportToFile(new File("C:\\Users\\adn0019\\Desktop\\jdt1gram\\" + version + ".txt"),
					categorizer.getVersionLevel().getDocument1Gram(version));
		}

		for (Integer issueId : categorizer.getMsrIssues().keySet())
			System.out.println(categorizer.getMsrIssues().get(issueId));
	}

	public MsrIssueCategorizerType1() {
	}

	public void parse() {
		if (msrIssues != null && msrIssues.size() >= 1) {
			for (Integer issueId : msrIssues.keySet()) {
				MsrIssue msrIssue = msrIssues.get(issueId);
				String version = msrIssue.getVersion();

				if (!versionLevel.containsKey(version))
					versionLevel.put(version, new YearToMonthLevel());

				// Get time
				Date date = msrIssue.getOpeningDate();
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(date);
				int year = calendar.get(Calendar.YEAR);

				// categorize by year and month
				YearToMonthLevel yearLevel = versionLevel.get(version);

				if (!yearLevel.containsKey(year))
					yearLevel.put(year, new MonthLevel());

				MonthLevel monthLevel = yearLevel.get(year);
				int month = calendar.get(Calendar.MONTH);

				if (!monthLevel.containsKey(month))
					monthLevel.put(month, new ArrayList<>());

				monthLevel.get(month).add(msrIssue);
			}

		}
	}

	public VersiontoYearLevel getVersionLevel() {
		return versionLevel;
	}

	public void setVersionLevel(VersiontoYearLevel versionLevel) {
		this.versionLevel = versionLevel;
	}

}
