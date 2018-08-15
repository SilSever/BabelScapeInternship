package it.uniroma3.internship.util.builder;

import java.util.Iterator;

import java.util.LinkedList;
import java.util.List;

import it.uniroma1.lcl.babelnet.BabelSense;
import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma1.lcl.babelnet.data.BabelGloss;
import it.uniroma1.lcl.babelnet.data.BabelSenseSource;
import it.uniroma1.lcl.jlt.util.Language;
import it.uniroma3.internship.io.handler.WordnetNodeHandler;
import it.uniroma3.internship.util.Finder;
import it.uniroma3.internship.util.Lemmatizer;

/**
 * 
 * @author Silvio Severino
 *
 */
public class GlossAndSensesBuilder
{
	private Finder finder;
	private Lemmatizer lemm;
	
	public GlossAndSensesBuilder()
	{
		this.finder = new Finder();
		this.lemm = new Lemmatizer();
	}
	
	public void wordnetNodeToGloss()
	{
		List<String> wordnetNode = finder.getAllWordnetSynset();
		List<String> sensesAndGlosses = new LinkedList<>();
		
		wordnetNode.stream().forEach(elem ->
		{		
			BabelSynsetID id = finder.findByID(elem);
			List<BabelSense> senses = id.toSynset().getSenses(Language.EN, BabelSenseSource.WN);
			Iterator<BabelSense> itSenses = senses.iterator();
			while(itSenses.hasNext())
				sensesAndGlosses.addAll(lemm.lemmatize(itSenses.next().getSimpleLemma()));

			List<BabelGloss> glosses = id.toSynset().getGlosses(Language.EN, BabelSenseSource.WN);
			Iterator<BabelGloss> itGlosses = glosses.iterator();
			while(itGlosses.hasNext())
				sensesAndGlosses.addAll(lemm.lemmatize(itGlosses.next().toString()));
		});
		
		WordnetNodeHandler handler = new WordnetNodeHandler();
		handler.write(sensesAndGlosses);
	}

}
