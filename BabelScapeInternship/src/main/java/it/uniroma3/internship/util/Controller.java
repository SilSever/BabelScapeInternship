package it.uniroma3.internship.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

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

	public void disambiguation()
	{
		return;
	}

	public void initGraph()
	{
//		this.build.takeTheWordnetBabelnetGraphs();
//		FileHandler ha = new FileHandler();
//		List<String> s = ha.readList();
//		
//		for(int i = 0; i < 10; i ++)
//			System.out.println(s.get(i));
		
		String toFind = IO.getToFind();
		List<String> lemmList = this.lemm.lemmatize(toFind);
		Set<String> set = this.lemm.toSet(lemmList);
		
		Graph<String,DefaultEdge> grafo = new SimpleGraph<>(DefaultEdge.class);
		
		System.out.println(lemmList);
		System.out.println(set);
	}

}
