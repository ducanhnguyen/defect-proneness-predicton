package example.nlp;

import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class StanfordNLPTester {

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
		String text = "I love you. He loves her.";
		Annotation document = pipeline.process(text);

		// Get all sentences in the paragraph
		for (CoreMap sentence : document.get(SentencesAnnotation.class)) {
			System.out.println("Sentence :" + sentence.toString());
		}
		System.out.println();

		// Get all tokens
		for (CoreMap sentence : document.get(SentencesAnnotation.class))
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				System.out.println("Token :" + token.value());
			}
		System.out.println();

		// Get all pos
		for (CoreMap sentence : document.get(SentencesAnnotation.class))
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				System.out.println("Lemma: " + token.lemma());

			}
		// Get all pos
		for (CoreMap sentence : document.get(SentencesAnnotation.class))
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				System.out.println("POS: " + token.tag());

			}
	}
}