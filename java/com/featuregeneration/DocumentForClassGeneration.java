package com.featuregeneration;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.object.JDTRecord;
import com.utils.CsvManager;
import com.utils.Utils;

public class DocumentForClassGeneration {
	private File csvInputFile = null;

	private File csvOutputFile = null;

	public static void main(String[] args) {
		DocumentForClassGeneration groupClasses = new DocumentForClassGeneration();
		groupClasses.setCsvInputFile(new File("./data/jdt.csv"));
		groupClasses.setCsvOutputFile(new File("./data/jdt_document.csv"));
		groupClasses.export();
	}

	public DocumentForClassGeneration() {
	}

	private void export() {
		if (csvInputFile != null && csvInputFile.exists()) {

			// Load data
			List<JDTRecord> jdtRecords = new ArrayList<JDTRecord>();
			List<String[]> rows = Utils.readRecordsFromCsv(csvInputFile);
			rows.remove(0);// remove header

			for (String[] row : rows) {
				JDTRecord record = new JDTRecord();
				record.setFileName(row[0]);
				record.setLoc(Integer.parseInt(row[1]));
				record.setNoc(Integer.parseInt(row[2]));
				record.setBf(Integer.parseInt(row[3]));
				record.setBug(Integer.parseInt(row[4]));
				jdtRecords.add(record);
			}

			// Export
			if (csvOutputFile.exists())
				csvOutputFile.delete();

			for (JDTRecord record : jdtRecords) {
				String document = "";
				for (String word : record.getTerms(JDTRecord.FULL_IDENTIFIER))
					document += word + " ";

				String[] data = new String[] { record.getFileName(), record.getLoc() + "", record.getNoc() + "",
						record.getBf() + "", record.getBug() + "", document };

				// Merge all
				CsvManager csvExporter = new CsvManager();
				csvExporter.appendARecordToCsv(csvOutputFile, JDTRecord.HEADERS, data);
				csvExporter.closeCSVWriter();
			}
		}
	}

	public void setCsvInputFile(File csvInputFile) {
		this.csvInputFile = csvInputFile;
	}

	public File getCsvInputFile() {
		return csvInputFile;
	}

	public void setCsvOutputFile(File csvOutputFile) {
		this.csvOutputFile = csvOutputFile;
	}

	public File getCsvOutputFile() {
		return csvOutputFile;
	}
}
