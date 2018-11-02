package com.defectpronenesspredicton;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opencsv.CSVReader;

import example.nlp.Stemmer;

public class Utils {

	/**
	 * Read records from a csv file
	 * 
	 * @param csvFile
	 * @param hasHeaders
	 * @return
	 */
	public static List<String[]> readRecordsFromCsv(File csvFile) {
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

}
