package com.defectpronenesspredicton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Calculate the accuracy, precision, recall, and fscore of each term
 *
 */
public class StemsRanking {
	private File csvFile = null;
	private int rankingMode = Utils.CLASS_IDENTIFIER; // by default

	public static void main(String[] args) {
		StemsRanking ranking = new StemsRanking();
		ranking.setCsvFile(new File("./data/jdt.csv"));
		ranking.setRankingMode(Utils.CLASS_IDENTIFIER);
		List<Stem> identifiers = ranking.rank();
		System.out.println(identifiers);

		File csvFile = new File("./stemsRanking.csv");
		if (csvFile.exists())
			csvFile.delete();
		for (Stem identifier : identifiers) {
			CsvManager csvExporter = new CsvManager();
			csvExporter.appendARecordToCsv(csvFile,
					new String[] { "identifier", "accuracy", "precision", "recall", "Fscore", "tp", "fp", "tn", "fn" },
					new String[] { identifier.getLowercaseIdentifier(), identifier.getAccuracy() + "",
							identifier.getPrecision() + "", identifier.getRecall() + "", identifier.getFscore() + "",
							identifier.getTp() + "", identifier.getFp() + "", identifier.getTn() + "",
							identifier.getFn() + "" });
			csvExporter.closeCSVWriter();
		}

	}

	public StemsRanking() {

	}

	public List<Stem> rank() {
		List<Stem> identifiers = new ArrayList<Stem>();

		if (csvFile != null && csvFile.exists()) {
			// Load data
			List<JDTRecord> records = new ArrayList<JDTRecord>();
			List<String[]> rows = Utils.readRecordsFromCsv(csvFile);
			rows.remove(0);// remove header

			for (String[] row : rows)
				if (row.length == NUM_OF_COLUMN_IN_DATASET) {
					JDTRecord record = new JDTRecord();
					record.setFileName(row[0]);
					record.setLoc(Integer.parseInt(row[1]));
					record.setNoc(Integer.parseInt(row[2]));
					record.setBf(Integer.parseInt(row[3]));
					record.setBug(Integer.parseInt(row[4]));
					records.add(record);
				}

			// Extract identifiers
			for (JDTRecord record : records)
				for (String identifier : Utils.getLowercaseIdentifiers(record.getFileName(), rankingMode)) {
					boolean duplicated = false;
					for (Stem iden : identifiers)
						if (iden.getLowercaseIdentifier().equals(identifier)) {
							duplicated = true;
							break;
						}

					if (!duplicated)
						identifiers.add(new Stem(identifier));
				}

			for (Stem identifier : identifiers)

				for (JDTRecord record : records) {

					if (Utils.getLowercaseIdentifiers(record.getFileName(), rankingMode)
							.contains(identifier.getLowercaseIdentifier())) {

						if (record.getBug() > 0)
							identifier.setTp(identifier.getTp() + 1);
						else {
							identifier.setFp(identifier.getFp() + 1);
						}

					} else {
						if (record.getBug() > 0)
							identifier.setFn(identifier.getFn() + 1);
						else {
							identifier.setTn(identifier.getTn() + 1);
						}
					}
				}
		}

		return identifiers;
	}

	public void setCsvFile(File csvFile) {
		this.csvFile = csvFile;
	}

	public File getCsvFile() {
		return csvFile;
	}

	public void setRankingMode(int mode) {
		this.rankingMode = mode;
	}

	public int getRankingMode() {
		return rankingMode;
	}

	public static final int NUM_OF_COLUMN_IN_DATASET = 5;
}
