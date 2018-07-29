package it.uniroma3.internship.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma3.internship.domain.BabelScore;
import it.uniroma3.internship.ui.IO;

/**
 * 
 * @author silvio
 *
 */
public class Controller
{
	private Lemmatizer lemm;
	private Finder finder;
	private Builder build;
	
	private List<Map<BabelSynsetID, BabelScore>> graphMapList;
	private List<BabelSynsetID> synIDs;
	
	
	public Controller()
	{
		this.lemm = new Lemmatizer();
		this.finder = new Finder();
		this.build = new Builder();
		
		this.graphMapList = new LinkedList<>();
		this.synIDs = new LinkedList<>();
		
	}
	
	/**
	 * Crea grafi ricorsivamente partendo da una stringa in input
	 */
	public void init()
	{
		String toFind = IO.getToFind();
		
		List<String> toFindLemmatize = lemm.lemmatize(toFind);
		
		System.out.println(toFindLemmatize);
		System.out.println("Synset:");
		long in = System.currentTimeMillis(); 
		
		toFindLemmatize.parallelStream().forEach(elem -> this.synIDs.addAll(finder.findByName(elem)));
		this.synIDs.parallelStream().forEach(elem -> this.graphMapList.add(build.buildRecursiveMap(elem.toString())));
		
		long fin = System.currentTimeMillis();
		
		fin -= in;
		System.out.println("#Grafi: " + this.graphMapList.size());
		System.out.println("Tempo totale[ms]: " + fin);
		//MapWriter writer = new MapWriter(this.graphMapList);
		//writer.Write2();
	}

	/**
	 * Sarà il metodo per disambiguare una stringa utilizzando i grafi di wordnet
	 * 
	 * 
	 * Probabilmente dovrà ritornare (o delegare ad un altro metodo) la classifica dei grafi secondo il punteggio
	 * 
	 * ***********DA IMPLEMENTARE************
	 */
	public void disambiguation()
	{
		return;
	}

	/**
	 * Crea grafi ricorsivamente utilizzando il file wordnetList.txt
	 * 
	 * Probabilmente dovrà ritornare l'hashset di grafi.
	 * Inoltre sarebbe meglio salvare il tutto su un file in quanto i grafi saranno sempre uguali 
	 * per questo non dovrebbe servire ricostruire tutto l'hash set ogni volta
	 * 
	 * ***********DA IMPLEMENTARE************
	 */
	public void initGraph()
	{
		System.out.println(this.build.getWordnetBabelnetGraphs().size());
	}

}
