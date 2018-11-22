package com.msr.object;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

/**
 * Example: "3171":[{"when":1002747040,"what":"After a child component has been
 * updated once, it can be updated to an incompatible level!
 * (1GENNZE)","who":33}]"
 * 
 * (Issue 3171)
 * 
 * @author adn0019
 *
 */
public class MsrIssue {
	private int issueID = -1;

	private Date openingDate = null;
	private long openingTime = -1;

	private String software = null;
	// The version of software
	private String version = null;
	private String description = null;
	// The component is affected by this bug, i.e., GUI, Core
	private String affectedComponent = null;

	private boolean isDuplicated = false;
	private boolean isInvalid = false;
	private int assignedAuthorID = -1;
	private int reporterID = -1;

	@Override
	public String toString() {
		return "id: " + issueID + ", reporter: " + reporterID + ", author: " + assignedAuthorID + ", components: "
				+ getAffectedComponent() + ", duplicated: " + isDuplicated + ", invalid: " + isInvalid + ", version: "
				+ version + ", opening date: " + openingDate + ", description: " + description;
	}

	private List<String> getNecessaryWords(String document) {
		List<String> words = new ArrayList<>();

		// Step 0: Only get all words
		String normalizedDocument = "";
		Pattern pattern = Pattern.compile("[a-zA-Z]+");
		Matcher matcher = pattern.matcher(document);
		while (matcher.find()) {

			// Remove stop words
			boolean isStopWord = false;
			for (String stopWord : stopWords)
				if (stopWord.equals(matcher.group())) {
					isStopWord = true;
					break;
				}

			if (!isStopWord)
				normalizedDocument += matcher.group() + " ";

		}

		// Step 1: lemmatization + stop words removal
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
		Annotation nlpDocument = pipeline.process(normalizedDocument);

		for (CoreMap sentence : nlpDocument.get(SentencesAnnotation.class))
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				words.add(token.lemma());
			}
		return words;
	}

	public String getDocument() {
		return /* getAffectedComponent() + " " + */ getDescription();
	}

	/**
	 * lemmatization + stop words removal
	 * 
	 * @return
	 */
	public String getDescription1Gram() {
		String twoGram = new String();
		List<String> words = getNecessaryWords(getDescription());

		if (words.size() >= 2) {
			for (int i = 0; i < words.size() - 1; i++)
				twoGram += words.get(i) + " ";

			twoGram += words.get(words.size() - 1);

		} else if (words.size() == 1) {
			twoGram += words.get(0);
		}
		return twoGram;
	}

	public String getDescription2Gram() {
		String twoGram = new String();
		List<String> words = getNecessaryWords(getDescription());

		if (words.size() >= 2) {
			for (int i = 0; i < words.size() - 2; i++)
				twoGram += words.get(i) + "_" + words.get(i + 1) + " ";

			twoGram += words.get(words.size() - 2) + "_" + words.get(words.size() - 1);

		} else if (words.size() == 1)
			twoGram += words.get(0) + " ";
		return twoGram;
	}

	public long getOpeningTime() {
		return openingTime;
	}

	public void setOpeningTime(long openingTime) {
		this.openingTime = openingTime;

		openingDate = new Date();
		openingDate.setTime(1000 * openingTime);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description.toLowerCase();
	}

	public String getAffectedComponent() {
		return affectedComponent;
	}

	public void setAffectedComponent(String affectedComponent) {
		this.affectedComponent = affectedComponent.toLowerCase();
	}

	public void setIssueID(int issueID) {
		this.issueID = issueID;
	}

	public int getIssueID() {
		return issueID;
	}

	public void setSoftware(String software) {
		this.software = software;
	}

	public String getSoftware() {
		return software;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setDuplicated(boolean isDuplicated) {
		this.isDuplicated = isDuplicated;
	}

	public boolean isDuplicated() {
		return isDuplicated;
	}

	public boolean isInvalid() {
		return isInvalid;
	}

	public void setInvalid(boolean isInvalid) {
		this.isInvalid = isInvalid;
	}

	public Date getOpeningDate() {
		return openingDate;
	}

	public void setAssignedAuthorID(int assignedAuthorID) {
		this.assignedAuthorID = assignedAuthorID;
	}

	public int getAssignedAuthorID() {
		return assignedAuthorID;
	}

	public void setReporterID(int reporterID) {
		this.reporterID = reporterID;
	}

	public int getReporterID() {
		return reporterID;
	}

	// Link: https://algs4.cs.princeton.edu/35applications/stopwords.txt
	final String[] stopWords = new String[] { "a", "an", "the", "a's", "able", "about", "above", "according",
			"accordingly", "across", "actually", "after", "afterwards", "again", "against", "ain't", "all", "allow",
			"allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst",
			"an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere",
			"apart", "appear", "appreciate", "appropriate", "are", "aren't", "around", "as", "aside", "ask", "asking",
			"associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes",
			"becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides",
			"best", "better", "between", "beyond", "both", "brief", "but", "by", "c'mon", "c's", "came", "can", "can't",
			"cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come",
			"comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains",
			"corresponding", "could", "couldn't", "course", "currently", "definitely", "described", "despite", "did",
			"didn't", "different", "do", "does", "doesn't", "doing", "don't", "done", "down", "downwards", "during",
			"each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et",
			"etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly",
			"example", "except", "far", "few", "fifth", "first", "five", "followed", "following", "follows", "for",
			"former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given",
			"gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadn't", "happens", "hardly",
			"has", "hasn't", "have", "haven't", "having", "he", "he's", "hello", "help", "hence", "her", "here",
			"here's", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his",
			"hither", "hopefully", "how", "howbeit", "however", "i'd", "i'll", "i'm", "i've", "ie", "if", "ignored",
			"immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar",
			"instead", "into", "inward", "is", "isn't", "it", "it'd", "it'll", "it's", "its", "itself", "just", "keep",
			"keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less",
			"lest", "let", "let's", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly",
			"many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly",
			"much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs",
			"neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor",
			"normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok",
			"okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought",
			"our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per",
			"perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite",
			"qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively",
			"respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see",
			"seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious",
			"seriously", "seven", "several", "shall", "she", "should", "shouldn't", "since", "six", "so", "some",
			"somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon",
			"sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "t's", "take",
			"taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "that's", "thats", "the",
			"their", "theirs", "them", "themselves", "then", "thence", "there", "there's", "thereafter", "thereby",
			"therefore", "therein", "theres", "thereupon", "these", "they", "they'd", "they'll", "they're", "they've",
			"think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout",
			"thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try",
			"trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up",
			"upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz",
			"vs", "want", "wants", "was", "wasn't", "way", "we", "we'd", "we'll", "we're", "we've", "welcome", "well",
			"went", "were", "weren't", "what", "what's", "whatever", "when", "whence", "whenever", "where", "where's",
			"whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while",
			"whither", "who", "who's", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with",
			"within", "without", "won't", "wonder", "would", "would", "wouldn't", "yes", "yet", "you", "you'd",
			"you'll", "you're", "you've", "your", "yours", "yourself", "yourselves", "zero", "doesn_t" };
}
