package com.defectpronenesspredicton.object;

public abstract class AbstractTermRoot implements ITermRoot {

	private int tp = 0;
	private int fp = 0;
	private int tn = 0;
	private int fn = 0;

	public AbstractTermRoot() {
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
