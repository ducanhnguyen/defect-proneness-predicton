package com.featuregeneration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.object.ITermRoot;
import com.object.JDTRecord;
import com.object.SingleTermRoot;
import com.object.TermRoots;
import com.utils.CsvManager;
import com.utils.Utils;

import example.Stemmer;

/**
 * 
 * Apply "Association rule learning" on the JDT dataset.
 * 
 * 
 * Association rule learning: <br/>
 * <ul>
 * <li>I = {(the root of term)*, "buggy", "non-buggy"} <br/>
 * </li>
 * <li>A rule is defined as: X => Y, where X, Y belong to I</li>
 * </ul>
 * 
 * <b>A term</b> is an element in the name of file name. For example, consider
 * the file name:
 * "internal::codeassist::select::SelectionOnQualifiedNameReference", where
 * "internal::codeassist::select" is the name of package,
 * "SelectionOnQualifiedNameReference" is the name of class. The name of class
 * has 5 terms: "selection", "on", "qualified", "name", "reference".
 * 
 * <br/>
 * <br/>
 * <b>The root of a term</b> is the root word in English. For example, the root
 * of the term "selection" is "select".
 * 
 */
public class JDTTermRootsAnalysis {
	private File jdtCsvFile = null;
	private int rankingMode = JDTRecord.CLASS_IDENTIFIER; // by default
	private int numofColumnInTheDataSet = 0;

	public static void main(String[] args) {
		// Load data
		JDTTermRootsAnalysis ranking = new JDTTermRootsAnalysis();
		ranking.setJdtCsvFile(new File("./data/jdt.csv"));
		ranking.setRankingMode(JDTRecord.CLASS_IDENTIFIER);
		ranking.setNumofColumnInTheDataSet(5); // i.e., filename, noc, loc, bf, bug

		// Analyze
		TermRoots termRoots = ranking.analyzeTermsInBugDefectProneness();

		// Export the result
		File csvFile = new File("./termsRanking.csv");
		if (csvFile.exists())
			csvFile.delete();
		for (ITermRoot termRoot : termRoots.getTermRootElements()) {
			CsvManager csvExporter = new CsvManager();

			csvExporter.appendARecordToCsv(csvFile,
					new String[] { "root of term", "terms", "accuracy", "precision", "recall", "Fscore", "tp", "fp",
							"tn", "fn" },
					new String[] { termRoot.getRootNameInStr(), termRoot.getTermsInStr(), termRoot.getAccuracy() + "",
							termRoot.getPrecision() + "", termRoot.getRecall() + "", termRoot.getFscore() + "",
							termRoot.getTp() + "", termRoot.getFp() + "", termRoot.getTn() + "",
							termRoot.getFn() + "" });
			csvExporter.closeCSVWriter();
		}
	}

	public JDTTermRootsAnalysis() {

	}

	public TermRoots analyzeTermsInBugDefectProneness() {
		TermRoots termRoots = new TermRoots();

		if (jdtCsvFile != null && jdtCsvFile.exists()) {
			// Load data
			List<JDTRecord> records = new ArrayList<JDTRecord>();
			List<String[]> rows = Utils.readRecordsFromCsv(jdtCsvFile);
			rows.remove(0);// remove header

			for (String[] row : rows)
				if (row.length == getNumofColumnInTheDataSet()) {
					JDTRecord record = new JDTRecord();
					record.setFileName(row[0]);
					record.setLoc(Integer.parseInt(row[1]));
					record.setNoc(Integer.parseInt(row[2]));
					record.setBf(Integer.parseInt(row[3]));
					record.setBug(Integer.parseInt(row[4]));
					records.add(record);
				}

			// Extract terms
			for (JDTRecord record : records)

				for (String term : record.getTerms(rankingMode)) {

					boolean duplicated = false;
					String termRoot = new Stemmer().stem(term);

					for (ITermRoot termRootItem : termRoots.getTermRootElements())

						if (termRootItem instanceof SingleTermRoot) {
							if (((SingleTermRoot) termRootItem).getRootName().equals(termRoot)) {
								duplicated = true;
								((SingleTermRoot) termRootItem).addNewTerm(term);
								break;
							}
						}

					if (!duplicated) {
						SingleTermRoot newTermRoot = new SingleTermRoot();
						newTermRoot.setRootName(termRoot);
						newTermRoot.addNewTerm(term);
						termRoots.getTermRootElements().add(newTermRoot);
					}
				}

			// Ranking terms
			termRoots.ranking(records, rankingMode);
			termRoots.sortByRecall();

			// Improve the model
			// Add some terms
			int TOP_K = 7;
			System.out.println("Start adding the combination of 3 terms in TOP_K. TOP_K = " + TOP_K);
			for (int i = 0; i <= TOP_K - 2; i++)
				for (int j = i + 1; j <= TOP_K - 1; j++)
					for (int k = j + 1; k <= TOP_K; k++) {

						ITermRoot termRootA = termRoots.getTermRootElements().get(i);
						ITermRoot termRootB = termRoots.getTermRootElements().get(j);
						ITermRoot termRootC = termRoots.getTermRootElements().get(k);

						TermRoots combinedTermRoots = new TermRoots();
						combinedTermRoots.getTermRootElements().add(termRootA);
						combinedTermRoots.getTermRootElements().add(termRootB);
						combinedTermRoots.getTermRootElements().add(termRootC);

						termRoots.getTermRootElements().add(combinedTermRoots);
					}

			// Add a new term by merging TOP_K terms
			System.out.println("Add a combination of TOP_K terms");
			TermRoots combinedTermRoots = new TermRoots();

			for (int i = 0; i <= TOP_K; i++)
				combinedTermRoots.getTermRootElements().add(termRoots.getTermRootElements().get(i));

			termRoots.getTermRootElements().add(combinedTermRoots);

			// Ranking terms again
			System.out.println("Start ranking again");
			termRoots.ranking(records, rankingMode);

			System.out.println("Start arranging");
			termRoots.sortByRecall();

		}

		return termRoots;

	}

	public void setJdtCsvFile(File csvFile) {
		this.jdtCsvFile = csvFile;
	}

	public File getJdtCsvFile() {
		return jdtCsvFile;
	}

	public void setRankingMode(int mode) {
		this.rankingMode = mode;
	}

	public int getRankingMode() {
		return rankingMode;
	}

	public void setNumofColumnInTheDataSet(int numofColumnInTheDataSet) {
		this.numofColumnInTheDataSet = numofColumnInTheDataSet;
	}

	public int getNumofColumnInTheDataSet() {
		return numofColumnInTheDataSet;
	}

	public static final int PACKAGE_IDENTIFIER = 0;
	public static final int CLASS_IDENTIFIER = 1;
	public static final int FULL_IDENTIFIER = 2;
}
