package com.object;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import example.Stemmer;

/**
 * Represent a record in JDT files
 * 
 * @author adn0019
 *
 */
public class JDTRecord {
	protected String fileName;

	// the number of line of codes
	protected int loc;

	// number of changes
	protected int noc;

	// The number of bugs before release
	protected int bf;

	// The number of bugs after release
	protected int bug;

	protected List<String> terms = null;

	protected List<String> termRoots = null;

	public static void main(String[] args) {
		JDTRecord record = new JDTRecord();
		record.setFileName("internal::codeassist::select::SelectionOnQualifiedNameReference");
		System.out.println(record.getTermRoots(CLASS_IDENTIFIER));
		System.out.println(record.getTerms(CLASS_IDENTIFIER));
		System.out.println(record.getTerms(PACKAGE_IDENTIFIER));
		System.out.println(record.getTermRoots(PACKAGE_IDENTIFIER));
	}

	public List<String> getTerms(int mode) {
		terms = new ArrayList<String>();

		// Pattern 1
		Pattern pattern = Pattern.compile("([a-zA-Z][a-z]*)");
		Matcher matcher = null;

		if (mode == PACKAGE_IDENTIFIER) {
			String packageName = getFileName().substring(0, getFileName().lastIndexOf("::"));
			matcher = pattern.matcher(packageName);

		} else if (mode == CLASS_IDENTIFIER) {
			String className = getFileName().substring(getFileName().lastIndexOf("::"));
			matcher = pattern.matcher(className);

		} else if (mode == FULL_IDENTIFIER) {
			matcher = pattern.matcher(getFileName());
		}

		if (matcher != null)
			while (matcher.find()) {
				if (matcher.group(0).toLowerCase().length() > 1)
					terms.add(matcher.group(0).toLowerCase());
			}
		return terms;
	}

	public JDTRecord() {
	}

	/**
	 * Get the root of terms in a text.
	 * 
	 * For example: the text "reference " -> root of term: "refer"
	 * 
	 * @param text
	 * @param mode
	 * @return
	 */
	public List<String> getTermRoots(int mode) {
		termRoots = new ArrayList<>();

		List<String> terms = getTerms(mode);

		Stemmer stemmer = new Stemmer();
		for (String term : terms)
			termRoots.add(stemmer.stem(term));
		return termRoots;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getLoc() {
		return loc;
	}

	public void setLoc(int loc) {
		this.loc = loc;
	}

	public int getNoc() {
		return noc;
	}

	public void setNoc(int noc) {
		this.noc = noc;
	}

	public int getBf() {
		return bf;
	}

	public void setBf(int bf) {
		this.bf = bf;
	}

	public int getBug() {
		return bug;
	}

	public void setBug(int bug) {
		this.bug = bug;
	}

	@Override
	public String toString() {
		return fileName + ", loc=" + loc + ", noc=" + noc + ", bf=" + bf + ", bug=" + bug;
	}

	public static final int PACKAGE_IDENTIFIER = 0;
	public static final int CLASS_IDENTIFIER = 1;
	public static final int FULL_IDENTIFIER = 2;

	public static final String PACKAGE_NAME_SEPERATOR = "::";
	
	public static final String[] HEADERS = new String[] {"file", "loc", "noc", "bf", "bug"};
}
