package com.defectpronenesspredicton;

/**
 * Represent a record in JDT files
 * 
 * @author adn0019
 *
 */
public class JDTRecord {
	private String fileName;

	// the number of line of codes
	private int loc;

	// number of changes
	private int noc;

	// The number of bugs before release
	private int bf;

	// The number of bugs after release
	private int bug;

	public JDTRecord() {
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

}
