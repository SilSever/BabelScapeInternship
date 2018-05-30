package it.uniroma3.internship.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma3.internship.domain.BabelScore;
import it.uniroma3.internship.domain.BabelScoreTry;

/**
 * 
 * @author silvio
 *
 */
public class Builder
{
	private Finder finder;
	private GraphScore graph;

	public Builder()
	{
		this.finder = new Finder();
		this.graph = GraphScore.getInstance();
	}

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

	public void buildRecursiveGraph(String rootConcept)
	{
		BabelSynsetID root = this.finder.findByID(rootConcept);
		if(root == null) return;

		Graph<BabelScoreTry, DefaultEdge> singleGraph = new DirectedAcyclicGraph<>(DefaultEdge.class);

		recursiveGraph(root, root, 0, 2, singleGraph);
		System.gc();

	}

	private void recursiveGraph(BabelSynsetID node, BabelSynsetID oldNode, int deep, int deepMax, Graph<BabelScoreTry, DefaultEdge> singleGraph)
	{
		if(!this.graph.contains(node))
		{
			BabelScoreTry bst = new BabelScoreTry(node, deep);
			this.graph.addMapNode(node, singleGraph);
			this.graph.addVertex(node, bst);
		}
		
	}

	public List<Map<BabelSynsetID, BabelScore>> getWordnetBabelnetGraphs()
	{
		List<Map<BabelSynsetID, BabelScore>> ID2ScoreList = new LinkedList<>();
		List<BabelSynsetID> wordnetSyns = this.finder.getAllWordnetSynset();
		System.out.println("Sono in builder: "+ wordnetSyns.size());
		//wordnetSyns.forEach(syn -> ID2ScoreList.add(this.buildRecursiveMap(syn.toString())));

		return ID2ScoreList;
	}

}
