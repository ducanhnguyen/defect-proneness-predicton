package com.featuregeneration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.object.JDTRecord;
import com.utils.CsvManager;
import com.utils.Utils;

/**
 * Add features as package names to JDT
 * 
 * @author adn0019
 *
 */
public class PackageNameGeneration {
	private File csvInputFile = null;
	private File csvOutputFile = null;

	public static void main(String[] args) {
		PackageNameGeneration groupClasses = new PackageNameGeneration();
		groupClasses.setCsvInputFile(new File("./data/jdt.csv"));
		groupClasses.setCsvOutputFile(new File("./data/jdt_package_Noc_Loc_Bf.csv"));
		groupClasses.group();
	}

	public PackageNameGeneration() {

	}

	private void group() {
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

			// Group classes
			Map<String, List<JDTRecord>> groups = new HashMap<>();

			for (JDTRecord jdtRecord : jdtRecords) {

				// Get all combination of packages
				String[] packages = jdtRecord.getFileName().split("::");

				List<String> packagesCombination = new ArrayList<String>();
				String packageName = packages[0];
				packagesCombination.add(packageName);

				for (int i = 1; i < packages.length - 1; i++) {
					packageName += "::" + packages[i];
					packagesCombination.add(packageName);
				}

				// Save
				for (String packageTmp : packagesCombination)
					if (!groups.containsKey(packageTmp)) {
						List<JDTRecord> records = new ArrayList<>();
						records.add(jdtRecord);
						groups.put(packageTmp, records);
					} else {
						groups.get(packageTmp).add(jdtRecord);
					}
			}
//			for (String key : groups.keySet())
//				System.out.println(key);
			System.out.println("The size of packages = " + groups.size());

			// Filter package name
			List<String> removedPackageNames = new ArrayList<>();

			for (String packageName : groups.keySet()) {
				List<JDTRecord> records = groups.get(packageName);
				int totalNoc = 0;
				int totalLoc = 0;
				int totalBf = 0;

				for (JDTRecord record : records) {
					totalNoc += record.getNoc();
					totalLoc += record.getLoc();
					totalBf += record.getBf();
				}

				if (totalNoc <= 70 || totalLoc <= 600 || totalBf <= 8)
					removedPackageNames.add(packageName);
			}
			for (int i = removedPackageNames.size() - 1; i >= 0; i--)
				groups.remove(removedPackageNames.get(i));
			System.out.println("Removed packages  = " + removedPackageNames.toString());
			System.out.println("The size of normalized packages = " + groups.size());
			/**
			 * Export
			 */
			if (csvOutputFile.exists())
				csvOutputFile.delete();

			// Create new header
			String[] newHeader = new String[JDTRecord.HEADERS.length + groups.keySet().size()];
			int startPackageFeature = JDTRecord.HEADERS.length;
			for (int i = 0; i < startPackageFeature; i++)
				newHeader[i] = JDTRecord.HEADERS[i];
			for (String packageName : groups.keySet())
				newHeader[startPackageFeature++] = packageName;

			// Write to CSV
			for (JDTRecord value : jdtRecords) {

				String[] data = new String[JDTRecord.HEADERS.length + groups.keySet().size()];
				data[0] = value.getFileName();
				data[1] = value.getLoc() + "";
				data[2] = value.getNoc() + "";
				data[3] = value.getBf() + "";
				data[4] = value.getBug() + "";
				startPackageFeature = JDTRecord.HEADERS.length;

				for (String packageName : groups.keySet()) {
					if (value.getFileName().startsWith(packageName))
						data[startPackageFeature++] = "1";
					else
						data[startPackageFeature++] = "0";
				}
				// Merge all
				CsvManager csvExporter = new CsvManager();
				csvExporter.appendARecordToCsv(csvOutputFile, newHeader, data);
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
