package it.uniroma3.internship.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma3.internship.domain.BabelScore;
import it.uniroma3.internship.domain.BabelScoreTry;
import it.uniroma3.internship.domain.Node;

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

	/**
	 * Metodo identico a quello di sopra ma con svolgimento ricorsivo
	 * @param rootConcept
	 * @return
	 */
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
	 * Crea un grafo in maniera ricorsiva partendo da un nodo root
	 * 
	 * Sarà il metodo da utilizzare per initGraph
	 * @param rootConcept
	 */
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

	/**
	 * 
	 * @return Per adesso ritorna una lista contenente tutti i synsets di wordnet. 
	 * Idealmente è nata per cercare tutti i nodi di wordnet e costruire i rispettivi grafi
	 */
	public Map<BabelSynsetID, Graph<Node, DefaultEdge>> getWordnetBabelnetGraphs()
	{
		List<String> wordnetSyns = this.finder.getAllWordnetSynsetProva();
		System.out.println("Synset da file caricati");
		return buildMapOfWordnetGraph(wordnetSyns, new HashMap<>(), 0);
	}


	private Map<BabelSynsetID, Graph<Node, DefaultEdge>> buildMapOfWordnetGraph(
			List<String> wordnetSyns, Map<BabelSynsetID, Graph<Node, DefaultEdge>> mapOfWordnetSynsets, int i)
	{
		if(wordnetSyns == null) return null;
		if(i == wordnetSyns.size()) return mapOfWordnetSynsets;
		
		try {
		BabelSynsetID syn = this.finder.findByID(wordnetSyns.get(i));
		if(!mapOfWordnetSynsets.containsKey(syn))
		{
			Node currentNode = new Node(syn);
			Graph<Node, DefaultEdge> graph = new DirectedAcyclicGraph<>(DefaultEdge.class);
			graph.addVertex(currentNode);
			
			mapOfWordnetSynsets.put(syn, graph);
			
			//runOnSons(this.finder.getWordnetEdge(syn),mapOfWordnetSynsets, currentNode, 0, 1);
			runOnSonsFirstLevel(this.finder.getWordnetEdge(syn), mapOfWordnetSynsets, currentNode, 0);//Una delle due
			
			DepthFirstIterator<Node, DefaultEdge> dfi = new DepthFirstIterator<>(graph); //Probabilmente da modificare
			dfi.next().changeVisited();
		}
		else
		{
			DepthFirstIterator<Node, DefaultEdge> dfi = new DepthFirstIterator<>(mapOfWordnetSynsets.get(syn));
			Node currentNode = dfi.next();
			
			if(!currentNode.isVisited())
			{
				//runOnSons(this.finder.getWordnetEdge(syn),mapOfWordnetSynsets, currentNode, 0, 1);
				runOnSonsFirstLevel(this.finder.getWordnetEdge(syn), mapOfWordnetSynsets, currentNode, 0);//Una delle due
				currentNode.changeVisited();
			}
		}
		return buildMapOfWordnetGraph(wordnetSyns, mapOfWordnetSynsets, i+1);
		}
		catch(StackOverflowError e)
		{
			System.out.println(mapOfWordnetSynsets.size());
			return null;
		}
	}


	private void runOnSons(List<BabelSynsetID> edge, Map<BabelSynsetID, Graph<Node, DefaultEdge>> mapOfWordnetSynsets, Node root, int i, int depth)
	{
		if(edge == null || i == edge.size()) return;
		
		BabelSynsetID syn = edge.get(i);
		if(!mapOfWordnetSynsets.containsKey(syn))
		{
			Node currentNode = new Node(syn);
			Graph<Node, DefaultEdge> graph = new DirectedAcyclicGraph<>(DefaultEdge.class);
			graph.addVertex(currentNode);
			
			mapOfWordnetSynsets.put(syn, graph);
			mapOfWordnetSynsets.get(root.getId()).addEdge(root, currentNode);
			
			if(depth < 2)
				runOnSons(this.finder.getWordnetEdge(syn), mapOfWordnetSynsets, currentNode, 0, depth+1);
			
			runOnSons(edge, mapOfWordnetSynsets, root, i+1, depth);
		}
	}
	
	private void runOnSonsFirstLevel(List<BabelSynsetID> edge, Map<BabelSynsetID, Graph<Node, DefaultEdge>> mapOfWordnetSynsets, Node root, int i)
	{
		if(edge == null || i == edge.size()) return;
		
		BabelSynsetID syn = edge.get(i);
		if(!mapOfWordnetSynsets.containsKey(syn))
		{
			Node currentNode = new Node(syn);
			Graph<Node, DefaultEdge> graph = new DirectedAcyclicGraph<>(DefaultEdge.class);
			graph.addVertex(currentNode);
			
			mapOfWordnetSynsets.put(syn, graph);
			mapOfWordnetSynsets.get(root.getId()).addEdge(root, currentNode);
		}
		else
		{
			DepthFirstIterator<Node, DefaultEdge> dfi = new DepthFirstIterator<>(mapOfWordnetSynsets.get(syn));
			mapOfWordnetSynsets.get(root.getId()).addEdge(root, dfi.next());
		}
		runOnSonsFirstLevel(edge, mapOfWordnetSynsets, root, i+1);
	}

}
