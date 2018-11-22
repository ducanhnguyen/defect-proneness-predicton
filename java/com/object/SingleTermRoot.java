package com.object;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a single term root
 * 
 * @author adn0019
 *
 */
public class SingleTermRoot extends AbstractTermRoot {
	private String rootName;

	// All the names corresponding to the root name
	private List<String> terms = new ArrayList<>();

	public SingleTermRoot() {
	}

	public void setRootName(String term) {
		this.rootName = term;
	}

	public String getRootName() {
		return rootName;
	}

	@Override
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof SingleTermRoot))
			return false;
		else
			return rootName.equals(((SingleTermRoot) arg0).getRootName());
	}

	@Override
	public String toString() {
		return "(" + rootName + ", Fscore: " + getFscore() + ")";
	}

	public double getFscore() {
		return 1.0f * 2 / (1 / getPrecision() + 1 / getRecall());
	}

	public void addNewTerm(String newTerm) {
		if (!terms.contains(newTerm))
			terms.add(newTerm);
	}

	public List<String> getTerms() {
		return terms;
	}

	public void setTerms(List<String> terms) {
		this.terms = terms;
	}

	public String getTermsInStr() {
		return terms.toString().replace(",", ";");
	}

	public String getRootNameInStr() {
		return rootName;
	}
}
