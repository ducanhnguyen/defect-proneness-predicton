package com.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;

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

	public static String readFileContent(File file) {
		String content = new String();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			while ((str = in.readLine()) != null)
				content += str;
			in.close();
		} catch (IOException e) {
		}
		return content;
	}

	public static boolean exportToFile(File file, String content) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(content);
			writer.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
