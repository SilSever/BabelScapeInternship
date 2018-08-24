package it.uniroma3.internship.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap; 

/**
 * 
 * @author Silvio Severino
 *
 */
public class Lemmatizer { 

	protected StanfordCoreNLP pipeline; 

	public Lemmatizer() { 
		Properties props; 
		props = new Properties(); 
		props.put("annotators", "tokenize, ssplit, pos, lemma"); 

		this.pipeline = new StanfordCoreNLP(props); 
	} 

	/**
	 * Method to perform lemmatization on a given text 
	 * @param documentText the text to lemmatize 
	 * @return a List with all the words' lemmas 
	 */ 
	public List<String> lemmatize(String documentText) 
	{ 
		List<String> lemmas = new LinkedList<>(); 

		Annotation document = new Annotation(documentText); 
		this.pipeline.annotate(document); 

		List<CoreMap> sentences = document.get(SentencesAnnotation.class); 
		for(CoreMap sentence: sentences) 
			for (CoreLabel token: sentence.get(TokensAnnotation.class))  
				lemmas.add(token.get(LemmaAnnotation.class)); 

		return this.validate(lemmas); 
	}

	private List<String> validate(List<String> lemmas)
	{
		List<String> app = new LinkedList<>();
		lemmas.forEach(elem -> 
		{
			if(elem != null && !elem.equals("") && !Pattern.matches("\\p{Punct}", elem) && !elem.equals("''") && !elem.equals("``"))
				app.add(elem);
		});
		return app;
	}
	
	public Set<String> toSet(List<String> list)
	{
		Set<String> setLemm = new HashSet<>(list);
		return setLemm;
	}
}