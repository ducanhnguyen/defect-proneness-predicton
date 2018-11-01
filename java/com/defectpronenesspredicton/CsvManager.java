package com.defectpronenesspredicton;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class CsvManager {
	private CSVWriter csvWriter = null;

	public static void main(String[] args) throws IOException {
		CsvManager csvWritter = new CsvManager();

		List<String[]> records = new ArrayList<String[]>();
		records.add(new String[] { "1", "1111" });
		records.add(new String[] { "1", "1" });

		String[] headers = new String[] { "header 1", "header 2" };

		File csvFile = new File("./test.csv");

		csvWritter.appendRecordsToCsv(csvFile, headers, records);
		csvWritter.closeCSVWriter();
	}

	public CsvManager() {
	}

	/**
	 * Export a set of records to csv file. Each record is stored in a line in csv.
	 * <br/>
	 * Delete the csv file if it exists before.
	 * 
	 * @param csvFile
	 * @param headers
	 * @param records
	 */
	public void appendRecordsToCsv(File csvFile, String[] headers, List<String[]> records) {
		// Read the existing records
		List<String[]> existingRecords = csvFile.exists() ? readRecordsFromCsv(csvFile) : new ArrayList<String[]>();

		// Write data to file
		try {
			Writer writer = Files.newBufferedWriter(Paths.get(csvFile.getPath()));
			csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER,
					CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

			if (existingRecords.size() > 0) {
				// Write the old records to csv file
				csvWriter.writeAll(existingRecords);

				// Append new records to the csv file
				for (String[] record : records)
					if (!isInStringList(existingRecords, record))
						csvWriter.writeNext(record);
			} else {
				if (headers != null && headers.length > 0)
					csvWriter.writeNext(headers);
				csvWriter.writeAll(records);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void appendARecordToCsv(File csvFile, String[] headers, String[] record) {
		// Read the existing records
		List<String[]> existingRecords = csvFile.exists() ? readRecordsFromCsv(csvFile) : new ArrayList<String[]>();

		// Write data to file
		try {
			Writer writer = Files.newBufferedWriter(Paths.get(csvFile.getPath()));
			csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER,
					CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

			if (existingRecords.size() > 0) {
				// Write the old records to csv file
				csvWriter.writeAll(existingRecords);

				// Append new records to the csv file
				if (!isInStringList(existingRecords, record))
					csvWriter.writeNext(record);
			} else {
				if (headers != null && headers.length > 0)
					csvWriter.writeNext(headers);
				csvWriter.writeNext(record);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean isInStringList(List<String[]> existingRecords, String[] newRecord) {
		boolean isInStringList = false;

		for (String[] existingRecord : existingRecords) {
			if (newRecord.length != existingRecord.length)
				continue;
			else {
				int numOfDuplicatedString = 0;
				for (int i = 0; i < newRecord.length; i++)
					if (newRecord[i].equals(existingRecord[i]))
						numOfDuplicatedString++;
					else
						break;

				if (numOfDuplicatedString == newRecord.length) {
					isInStringList = true;
					break;
				} else
					continue;
			}
		}
		return isInStringList;
	}

	/**
	 * Read records from a csv file
	 * 
	 * @param csvFile
	 * @param hasHeaders
	 * @return
	 */
	public List<String[]> readRecordsFromCsv(File csvFile) {
		List<String[]> records = new ArrayList<String[]>();
		if (csvFile.exists()) {
			try {
				Reader reader = Files.newBufferedReader(Paths.get(csvFile.getPath()));
				CSVReader csvReader = new CSVReader(reader);
				records = csvReader.readAll();
				csvReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return records;
	}

	public void closeCSVWriter() {
		if (csvWriter != null)
			try {
				csvWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}
