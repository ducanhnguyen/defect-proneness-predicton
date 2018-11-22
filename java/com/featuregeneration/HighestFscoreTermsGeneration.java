package com.featuregeneration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.object.ITermRoot;
import com.object.JDTRecord;
import com.object.JDTRecordExtend;
import com.object.SingleTermRoot;
import com.object.TermRoots;
import com.utils.CsvManager;
import com.utils.Utils;

import example.Stemmer;

/**
 * Add new features as the highest fscore to the JDT.
 * 
 * @author adn0019
 *
 */
public class HighestFscoreTermsGeneration {

	private File csvInputFile;
	private File csvOutputFile;

	public static void main(String[] args) {
		HighestFscoreTermsGeneration exporter = new HighestFscoreTermsGeneration();
		exporter.setCsvInputFile(new File("./data/jdt.csv"));
		exporter.setCsvOutputFile(new File("./data/jdt_highest_fscore.csv"));
		exporter.export();
	}

	public void export() {
		if (csvInputFile != null && csvInputFile.exists()) {
			// Load data
			List<JDTRecordExtend> records = new ArrayList<JDTRecordExtend>();
			List<String[]> rows = Utils.readRecordsFromCsv(csvInputFile);
			rows.remove(0);// remove header

			for (String[] row : rows) {
				JDTRecordExtend record = new JDTRecordExtend();
				record.setFileName(row[0]);
				record.setLoc(Integer.parseInt(row[1]));
				record.setNoc(Integer.parseInt(row[2]));
				record.setBf(Integer.parseInt(row[3]));
				record.setBug(Integer.parseInt(row[4]));
				records.add(record);
			}
			System.out.println("Size of records = " + records.size());

			// Extract terms
			TermRoots termRoots = new TermRoots();
			for (JDTRecordExtend record : records)

				for (String term : record.getTerms(JDTRecord.FULL_IDENTIFIER)) {

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
			System.out.println("Size of term roots = " + termRoots.getTermRootElements().size());

			/**
			 * Add features. A feature is a term having the highest f-score
			 */
			// Way 1
			List<String> newFeatures = new ArrayList<String>();
			newFeatures.add("pattern");
			newFeatures.add("refer");
			newFeatures.add("type");
			newFeatures.add("bind");
			newFeatures.add("code");
			newFeatures.add("locat");
			newFeatures.add("declar");
			newFeatures.add("method");
			newFeatures.add("java");
			newFeatures.add("parser");
			newFeatures.add("complet");
			newFeatures.add("match");
			newFeatures.add("compil");
			newFeatures.add("field");
			newFeatures.add("intern");
			newFeatures.add("scope");
			newFeatures.add("snippet");
			newFeatures.add("search");
			newFeatures.add("packag");
			newFeatures.add("name");
			newFeatures.add("context");

			// Only for testing
			String tmp = "";
			for (String newFeature : newFeatures)
				tmp += newFeature + "+";
			System.out.println("R formula: " + tmp);
			System.out.println("Size of new features = " + newFeatures.size());

			// Create new JDT
			for (JDTRecordExtend record : records) {
				for (String newColumn : newFeatures) {
					if (record.getTermRoots(JDTRecord.FULL_IDENTIFIER).contains(newColumn))
						record.getTermRootColumns().add("1");
					else
						record.getTermRootColumns().add("0");
				}
			}

			// Export
			if (csvOutputFile.exists())
				csvOutputFile.delete();

			for (JDTRecordExtend record : records) {
				CsvManager csvExporter = new CsvManager();

				// Create header
				String[] headers = new String[5 + newFeatures.size()];
				headers[0] = "file";
				headers[1] = "loc";
				headers[2] = "noc";
				headers[3] = "bf";
				headers[4] = "bug";

				int count = 5;
				for (String newColumn : newFeatures)
					headers[count++] = newColumn;

				// Create data
				String[] data = new String[5 + newFeatures.size()];
				int startRootTermId = 5;
				data[0] = record.getFileName();
				data[1] = record.getLoc() + "";
				data[2] = record.getNoc() + "";
				data[3] = record.getBf() + "";
				data[4] = record.getBug() + "";

				for (String newColumn : record.getTermRootColumns())
					data[startRootTermId++] = newColumn + "";

				// Merge all
				csvExporter.appendARecordToCsv(csvOutputFile, headers, data);
				csvExporter.closeCSVWriter();
			}
		}
	}

	public void setCsvInputFile(File inputJdtCsvFile) {
		this.csvInputFile = inputJdtCsvFile;
	}

	public File getCsvInputFile() {
		return csvInputFile;
	}

	public void setCsvOutputFile(File onputJdtCsvFile) {
		this.csvOutputFile = onputJdtCsvFile;
	}

	public File getCsvOutputFile() {
		return csvOutputFile;
	}
}
