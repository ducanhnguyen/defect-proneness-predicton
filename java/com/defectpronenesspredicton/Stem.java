package com.defectpronenesspredicton;

public class Stem {
	private String lowercaseIdentifier;
	private int tp = 0;
	private int fp = 0;
	private int tn = 0;
	private int fn = 0;

	public Stem(String lowercaseIdentifier) {
		this.lowercaseIdentifier = lowercaseIdentifier;
	}

	public void setLowercaseIdentifier(String identifier) {
		this.lowercaseIdentifier = identifier;
	}

	public String getLowercaseIdentifier() {
		return lowercaseIdentifier;
	}

	@Override
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof Stem))
			return false;
		else
			return lowercaseIdentifier.equals(((Stem) arg0).getLowercaseIdentifier());
	}

	@Override
	public String toString() {
		return "(" + lowercaseIdentifier + ", Fscore: " + getFscore() + ")";
	}

	public int getTp() {
		return tp;
	}

	public void setTp(int tp) {
		this.tp = tp;
	}

	public int getFp() {
		return fp;
	}

	public void setFp(int fp) {
		this.fp = fp;
	}

	public int getTn() {
		return tn;
	}

	public void setTn(int tn) {
		this.tn = tn;
	}

	public int getFn() {
		return fn;
	}

	public void setFn(int fn) {
		this.fn = fn;
	}

	public double getAccuracy() {
		return 1.0f * (tp + tn) / (tp + fp + tn + fn);
	}

	public double getPrecision() {
		return 1.0f * tp / (tp + fp);
	}

	public double getRecall() {
		return 1.0f * tp / (tp + fn);
	}

	public double getFscore() {
		return 1.0f * 2 / (1 / getPrecision() + 1 / getRecall());
	}
}
