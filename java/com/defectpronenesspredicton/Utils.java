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

public class Utils {
	public static void main(String[] args) {
		System.out.println(getLowercaseIdentifiers("internal::codeassist::select::SelectionOnQualifiedNameReference",
				PACKAGE_IDENTIFIER));
		System.out.println(
				getLowercaseIdentifiers("internal::core::dom::rewrite::ASTRewriteFormatter", PACKAGE_IDENTIFIER));
	}

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

	public static List<String> getLowercaseIdentifiers(String text, int mode) {
		List<String> output = new ArrayList<String>();

		// Pattern 1
		Pattern pattern = Pattern.compile("([a-zA-Z][a-z]*)");
		Matcher matcher = null;

		if (mode == PACKAGE_IDENTIFIER) {
			String packageName = text.substring(0, text.lastIndexOf("::"));
			matcher = pattern.matcher(packageName);

		} else if (mode == CLASS_IDENTIFIER) {
			String className = text.substring(text.lastIndexOf("::"));
			matcher = pattern.matcher(className);

		} else if (mode == FULL_IDENTIFIER) {
			matcher = pattern.matcher(text);
		}

		if (matcher != null)
			while (matcher.find()) {
				if (matcher.group(0).toLowerCase().length() > 1)
					output.add(matcher.group(0).toLowerCase());
			}
		return output;
	}

	public static final int PACKAGE_IDENTIFIER = 0;
	public static final int CLASS_IDENTIFIER = 1;
	public static final int FULL_IDENTIFIER = 1;
}
