package it.uniroma3.internship.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma3.internship.domain.BabelScore;
import it.uniroma3.internship.io.IO;
import it.uniroma3.internship.util.builder.GraphBuilder;

/**
 * 
 * @author Silvio Severino
 *
 */
public class Controller
{
	private Lemmatizer lemm;
	private Finder finder;
	private GraphBuilder build;
	
	private List<Map<BabelSynsetID, BabelScore>> graphMapList;
	private List<BabelSynsetID> synIDs;
	
	
	public Controller()
	{
		this.lemm = new Lemmatizer();
		this.finder = new Finder();
		this.build = new GraphBuilder();
		
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
	 * 
	 */
	public void initGraph()
	{
		Map<String, Graph<String, DefaultEdge>> graph = this.build.getWordnetBabelnetGraphs();
		System.out.println(graph.keySet().size());
	}

}
