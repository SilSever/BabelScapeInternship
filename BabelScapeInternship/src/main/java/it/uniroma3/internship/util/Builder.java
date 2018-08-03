package it.uniroma3.internship.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma3.internship.domain.BabelScore;
import it.uniroma3.internship.ui.FileHandler;

/**
 * This class it's used for build the map of graphs relative at the wordnet nodes in BabelNet
 * 
 * @author Silvio Severino
 *
 */
public class Builder
{
	private Finder finder;
	private Graph<String, DefaultEdge> graph;
	private Map<BabelSynsetID, Graph<String, DefaultEdge>> mapOfWordnetSynsets;

	/**
	 * Constructor
	 */
	public Builder()
	{
		this.finder = new Finder();
		this.mapOfWordnetSynsets = new HashMap<>();
		this.graph = new SimpleDirectedGraph<>(DefaultEdge.class);
	}


	/**
	 * Un vecchio metodo che costruiva una mappa (rappresentante un grafo) partendo da un nodo
	 * @param rootConcept
	 * @return
	 */
	public Map<BabelSynsetID, BabelScore> buildMap(String rootConcept)
	{
		Map<BabelSynsetID, BabelScore> ID2Score = new HashMap<>();
		BabelSynsetID root = this.finder.findByID(rootConcept);
		if(root == null) return null;

		List<BabelSynsetID> firstChilds = this.finder.getEdge(root);
		if(firstChilds != null)
		{
			ID2Score.put(root, new BabelScore(root, root, firstChilds.size(),0));
			firstChilds.parallelStream().forEach(first -> 
			{
				List<BabelSynsetID> secondChilds = this.finder.getEdge(first);
				if(secondChilds != null)
				{
					ID2Score.put(first, new BabelScore(first, root, secondChilds.size(), 1));
					secondChilds.parallelStream().forEach(second ->
					{
						//int size = second.getOutgoingEdges().size();
						ID2Score.put(second, new BabelScore(second, first, 10, 2));
					});
				}
			});
		}
		System.out.println("algoritmo terminato");
		return ID2Score;
	}	

	public Map<BabelSynsetID, BabelScore> buildRecursiveMap(String rootConcept)
	{
		Map<BabelSynsetID, BabelScore> ID2Score = new HashMap<>();
		BabelSynsetID root = this.finder.findByID(rootConcept);

		if(root == null) return null;

		recursiveMap(root, root, 0, 2, ID2Score);
		System.gc();
		System.out.println("Ricorsione terminata ID2Score.size: " + ID2Score.size());
		return ID2Score;
	}

	private void recursiveMap(BabelSynsetID node, BabelSynsetID oldNode, int deep, int deepMax, Map<BabelSynsetID, BabelScore> ID2Score)
	{
		List<BabelSynsetID> childs = deep == deepMax ? new ArrayList<>() : this.finder.getEdge(node);
		ID2Score.put(node, new BabelScore(node, oldNode, childs.size(), deep));
		if(deep != deepMax)
		{
			for(int i = 0; i < childs.size(); i++)
			{
				BabelSynsetID syn = childs.get(i);
				if(syn != null && !ID2Score.containsKey(syn))
					recursiveMap(syn, node, deep + 1, deepMax, ID2Score);
			}
		}
		//		deep = 0;
	}

	/**
	 * 
	 */
	public Map<BabelSynsetID, Graph<String, DefaultEdge>> getWordnetBabelnetGraphs()
	{
		List<String> wordnetSyns = this.finder.getAllWordnetSynset();
		System.out.println(wordnetSyns.size());
		System.out.println("Synsets are had load...\nI'm building the graphs\nThis operation can be during many minutes...");

		wordnetSyns.stream().forEach(elem -> 
		{
			BabelSynsetID syn = new BabelSynsetID(elem);
			this.graph = new SimpleDirectedGraph<>(DefaultEdge.class);

			recursiveWordnetGraph(syn, 0);

			this.mapOfWordnetSynsets.put(syn, this.graph);
			System.out.println("Grafo: " + syn + " terminato.");
		});

		return this.mapOfWordnetSynsets;
	}

	public Graph<String, DefaultEdge> buildWordnetGraph()
	{
		FileHandler fileH = new FileHandler();
		
		if(fileH.checkGraphFileIsEmpty())
		{
			this.finder.getAllWordnetSynset().stream().forEach(elem -> recursiveWordnetGraph(this.finder.findByID(elem), 0));
			fileH.writeGraph(this.graph);
		}
		else
			this.graph = fileH.readGraph();
		
		return this.graph;
	}

	/*
	 * 
	 */
	private void recursiveWordnetGraph(BabelSynsetID oldNode, int deep)
	{
		if(!this.graph.containsVertex(oldNode.toString()))
			this.graph.addVertex(oldNode.toString());

		List<BabelSynsetID> syns = this.finder.getWordnetEdge(oldNode);
		//		System.out.println("\t\t" + syns.size());

		for(BabelSynsetID child : syns)
		{			
			if(!this.graph.containsVertex(child.toString()))
				this.graph.addVertex(child.toString());

			if(!child.equals(oldNode))
				this.graph.addEdge(oldNode.toString(), child.toString());

			if(deep < 1)
				recursiveWordnetGraph(child, deep+1);
		}
	}

}
