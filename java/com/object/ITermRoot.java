package com.object;

/**
 * Interface of a term
 * 
 * @author adn0019
 *
 */
public interface ITermRoot {
	double getAccuracy();

	double getPrecision();

	double getRecall();

	double getFscore();

	int getTp();

	void setTp(int tp);

	int getFp();

	void setFp(int fp);

	int getTn();

	void setTn(int tn);

	int getFn();

	void setFn(int fn);

	String getTermsInStr();

	String getRootNameInStr();
}
