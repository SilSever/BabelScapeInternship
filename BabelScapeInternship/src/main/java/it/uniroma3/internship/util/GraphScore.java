package it.uniroma3.internship.util;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma3.internship.domain.BabelScore;
import it.uniroma3.internship.domain.BabelScoreTry;

public class GraphScore
{
	private static GraphScore single_instance = null;
	
	private Map< BabelSynsetID , Graph<BabelScoreTry, DefaultEdge> > graph;
	
	private GraphScore()
	{
		this.graph = new HashMap<>();
	}
	
	public static GraphScore getInstance()
	{
		if(single_instance == null)
			single_instance = new GraphScore();
		
		return single_instance;
	}
	
	public boolean contains(BabelSynsetID syn) 
	{
		if(this.graph.containsKey(syn)) return true;
		
		for(BabelSynsetID synID : this.graph.keySet())
			if(this.graph.get(synID).containsVertex(new BabelScoreTry(syn, 0)))
				return true;
		
		return false;
	}

	public void addMapNode(BabelSynsetID BsID, Graph<BabelScoreTry, DefaultEdge> graph)
	{
		this.graph.put(BsID, graph);
	}
	
	public void addVertex(BabelSynsetID bsID, BabelScoreTry babelScoreTry)
	{
		this.graph.get(bsID).addVertex(babelScoreTry);
	}
	
	public void addEdge(BabelSynsetID bsID, BabelScoreTry from, BabelScoreTry to) 
	{
		this.graph.get(bsID).addEdge(from, to);
	}
	
	

}
