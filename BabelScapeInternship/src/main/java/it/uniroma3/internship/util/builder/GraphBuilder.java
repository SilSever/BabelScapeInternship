package it.uniroma3.internship.util.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import com.google.common.util.concurrent.AtomicDouble;
import com.medallia.word2vec.Searcher.UnknownWordException;
import com.medallia.word2vec.Word2VecModel;

import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma3.internship.domain.BabelScore;
import it.uniroma3.internship.io.handler.GraphHandler;
import it.uniroma3.internship.util.Finder;

/**
 * This class it's used for build the map of graphs relative at the wordnet nodes in BabelNet
 * 
 * @author Silvio Severino
 *
 */
public class GraphBuilder
{
	private Finder finder;
	private Word2VecModel model;
	private Graph<String, DefaultEdge> graph;
	private Map<String, Graph<String, DefaultEdge>> mapOfWordnetSynsets;

	/**
	 * Constructor
	 */
	public GraphBuilder()
	{
		this.finder = new Finder();

		Word2VecBuilder builder = new Word2VecBuilder();
		this.model = builder.buildWord2VecModel();

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
	public synchronized Map<String, Graph<String, DefaultEdge>> getWordnetBabelnetGraphs()
	{
		GraphHandler handler = new GraphHandler();
		if(handler.checkIfEmpty())
		{
			AtomicDouble counter = new AtomicDouble(0);
			List<String> wordnetNode = this.finder.getAllWordnetSynset();
			int size = wordnetNode.size();

			wordnetNode.stream().forEach(elem ->{
				
				BabelSynsetID syn = this.finder.findByID(elem);
				this.graph = new SimpleDirectedGraph<>(DefaultEdge.class);
	
				recursiveWordnetGraph(syn, 0);
	
				this.mapOfWordnetSynsets.put(syn.toString(), this.graph);
				 
				System.out.print(("Processing---------"+(counter.addAndGet(1)/size)*100) + "%\r");
				System.out.flush();
			});
			handler.write(this.mapOfWordnetSynsets);
			return this.mapOfWordnetSynsets;
		}
		else
			return handler.read();
	}

	/*
	 * 
	 */
	private void recursiveWordnetGraph(BabelSynsetID root, int deep)
	{
		try{
			prova(root, root, deep);
		}catch (UnknownWordException e) {
		}
	}


	@SuppressWarnings("unused")
	private void recursiveWordnetGraphApp(BabelSynsetID root, BabelSynsetID oldNode, int deep) throws UnknownWordException
	{
		List<BabelSynsetID> syns = this.finder.getWordnetEdge(oldNode);

		for(BabelSynsetID child : syns)
		{
			if(this.model.forSearch()
			.cosineDistance(this.finder.getFirstSenseOf(root), this.finder.getFirstSenseOf(child)) >= 0.5)
			{
				if(!this.graph.containsVertex(child.toString()))
					this.graph.addVertex(child.toString());

				if(!child.equals(oldNode))
					this.graph.addEdge(oldNode.toString(), child.toString());
				
				if(deep < 1)
					recursiveWordnetGraphApp(root, child, deep+1);
			}
		}
	}
	
	private void prova(BabelSynsetID root, BabelSynsetID beginNode, int deep) throws UnknownWordException
	{
		if(!this.graph.containsVertex(root.toString()))
			this.graph.addVertex(root.toString());
		
		List<BabelSynsetID> childs = this.finder.getWordnetEdge(root);
		for(BabelSynsetID childID : childs)
		{
			if(this.graph.containsVertex(childID.toString()))
			{
				if(!root.toString().equals(childID.toString()))
					this.graph.addEdge(root.toString(), childID.toString());
			}
			else
			{
				if(this.model.forSearch()
				.cosineDistance(this.finder.getFirstSenseOf(beginNode), this.finder.getFirstSenseOf(childID)) >= 0.5)
				{
					this.graph.addVertex(childID.toString());
					this.graph.addEdge(root.toString(), childID.toString());
				}
			}
			if(deep < 1)
				prova(childID, beginNode, deep+1);
		}
	}
	
//	public Graph<String, DefaultEdge> buildWordnetGraph()
//	{
//		GraphHandler fileH = new GraphHandler();
//
//		if(fileH.checkIfEmpty())
//		{
//			this.finder.getAllWordnetSynset().stream().forEach(elem -> recursiveWordnetGraph(this.finder.findByID(elem), 0));
//			fileH.write(this.graph);
//		}
//		else
//			this.graph = fileH.read();
//
//		return this.graph;
//	}

}
