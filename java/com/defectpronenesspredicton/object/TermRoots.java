package com.defectpronenesspredicton.object;

import java.util.ArrayList;
import java.util.List;

public class TermRoots extends AbstractTermRoot implements ITermRoot {
	private List<ITermRoot> termRootElements = new ArrayList<>();

	public void ranking(List<JDTRecord> records, int rankingMode) {
		for (ITermRoot termRoot : termRootElements)

			for (JDTRecord record : records)

				if (termRoot instanceof SingleTermRoot) {
					SingleTermRoot cast = (SingleTermRoot) termRoot;

					if (record.getTermRoots(rankingMode).contains(cast.getRootName())) {

						if (record.getBug() > 0)
							termRoot.setTp(termRoot.getTp() + 1);
						else {
							termRoot.setFp(termRoot.getFp() + 1);
						}

					} else {
						if (record.getBug() > 0)
							termRoot.setFn(termRoot.getFn() + 1);
						else {
							termRoot.setTn(termRoot.getTn() + 1);
						}
					}
				}

				else if (termRoot instanceof TermRoots) {
					boolean matchOne = false;

					// If the record contains one term
					for (ITermRoot item : ((TermRoots) termRoot).getTermRootElements())

						if (item instanceof SingleTermRoot) {

							SingleTermRoot cast = (SingleTermRoot) item;

							if (record.getTermRoots(rankingMode).contains(cast.getRootName())) {
								matchOne = true;

								if (record.getBug() > 0)
									termRoot.setTp(termRoot.getTp() + 1);
								else {
									termRoot.setFp(termRoot.getFp() + 1);
								}
								break;
							}
						}

					// If the record does not contain any terms
					if (!matchOne) {
						if (record.getBug() > 0)
							termRoot.setFn(termRoot.getFn() + 1);
						else {
							termRoot.setTn(termRoot.getTn() + 1);
						}
					}
				}
	}

	public void sortByPrecision() {
		for (int i = 0; i < termRootElements.size() - 1; i++)
			for (int j = i + 1; j < termRootElements.size(); j++) {
				ITermRoot A = termRootElements.get(i);
				ITermRoot B = termRootElements.get(j);

				if (A.getPrecision() < B.getPrecision()) {
					termRootElements.remove(i);
					termRootElements.add(i, B);

					termRootElements.remove(j);
					termRootElements.add(j, A);
				}
			}
	}

	public void sortByRecall() {
		for (int i = 0; i < termRootElements.size() - 1; i++)
			for (int j = i + 1; j < termRootElements.size(); j++) {
				ITermRoot A = termRootElements.get(i);
				ITermRoot B = termRootElements.get(j);

				if (A.getRecall() < B.getRecall()) {
					termRootElements.remove(i);
					termRootElements.add(i, B);

					termRootElements.remove(j);
					termRootElements.add(j, A);
				}
			}
	}

	public void sortByFscore() {
		for (int i = 0; i < termRootElements.size() - 1; i++)
			for (int j = i + 1; j < termRootElements.size(); j++) {
				ITermRoot A = termRootElements.get(i);
				ITermRoot B = termRootElements.get(j);

				if (A.getFscore() < B.getFscore()) {
					termRootElements.remove(i);
					termRootElements.add(i, B);

					termRootElements.remove(j);
					termRootElements.add(j, A);
				}
			}
	}

	@Override
	public String getTermsInStr() {
		String terms = "";
		for (ITermRoot term : termRootElements)
			if (term instanceof SingleTermRoot)
				terms += ((SingleTermRoot) term).getTermsInStr() + "; ";
		return terms;

	}

	@Override
	public String getRootNameInStr() {
		String terms = "";
		for (ITermRoot term : termRootElements)
			if (term instanceof SingleTermRoot)
				terms += ((SingleTermRoot) term).getRootName() + "; ";
		return terms;
	}

	public List<ITermRoot> getTermRootElements() {
		return termRootElements;
	}
}
