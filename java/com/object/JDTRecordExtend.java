package com.object;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a record in JDT data set
 * 
 * @author adn0019
 *
 */
public class JDTRecordExtend extends JDTRecord {

	private List<String> termRootColumns = new ArrayList<String>();

	public static void main(String[] args) {
		JDTRecordExtend record = new JDTRecordExtend();
		record.setFileName("internal::codeassist::select::SelectionOnQualifiedNameReference");
		System.out.println(record.getTermRoots(CLASS_IDENTIFIER));
	}

	@Override
	public String toString() {
		String output = fileName + ", loc=" + loc + ", noc=" + noc + ", bf=" + bf + ", bug=" + bug + ", ";
		for (String frequency : getTermRootColumns())
			output += frequency + ", ";
		return output;
	}

	public List<String> getTermRootColumns() {
		return termRootColumns;
	}

	public void setTermRootColumns(List<String> termRootColumns) {
		this.termRootColumns = termRootColumns;
	}

}
