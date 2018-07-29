package it.uniroma3.internship.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma1.lcl.babelnet.BabelSynsetRelation;
import it.uniroma1.lcl.babelnet.data.BabelPointer;
import it.uniroma3.internship.domain.BabelScore;

/**
 * This class it's used for build the map of graphs relative at the wordnet nodes in BabelNet
 * 
 * @author Silvio Severino
 *
 */
public class Builder
{
	private Finder finder;
	private Graph<BabelSynsetID, DefaultEdge> graph;
	private Map<BabelSynsetID, Graph<BabelSynsetID, DefaultEdge>> mapOfWordnetSynsets;

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
	 * @return Per adesso ritorna una lista contenente tutti i synsets di wordnet. 
	 * Idealmente è nata per cercare tutti i nodi di wordnet e costruire i rispettivi grafi
	 */
	public Map<BabelSynsetID, Graph<BabelSynsetID, DefaultEdge>> getWordnetBabelnetGraphs()
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


	/*
	 * Mi sono reso conto che i nodi di wordnet contenuti nella lista WordnetList.txt non hanno figli di wordnet.
	 * Per questo motivo devo modificare l'algoritmo andandomi a creare una mappa contenente tutti i nodi di wordnet
	 * e avente come valore un grafo che abbia tutti i nodi a distanza due dal nodo di wordnet. 
	 * 
	 * Posso modificare buildMapOfWordnetGraph, sfruttandolo solamente per aggiungere i nuovi nodi alla mappa e richiamare
	 * buildRecursiveGraph per creare il grafo.
	 * 
	 */
	private void recursiveWordnetGraph(BabelSynsetID oldNode, int deep)
	{
		if(!this.graph.containsVertex(oldNode))
			this.graph.addVertex(oldNode);

		List<BabelSynsetID> syns = this.finder.getEdge(oldNode);
		
		for(int i = 0; i < syns.size(); i++)
			syns.forEach(child -> 
			{
				if(child != null)
				{
					if(!this.graph.containsVertex(child))
						this.graph.addVertex(child);

					if(!child.equals(oldNode))
						this.graph.addEdge(oldNode, child);

					if(deep < 1)
						recursiveWordnetGraph(child, deep+1);
				}
			});
	}

	public void prova()
	{
		BabelSynset syn = this.finder.getBabelnet().getSynset(new BabelSynsetID("bn:00000002n"));
		try
		{
			Map<String, Integer> map = this.walk(syn);
			System.out.println("Ha finito walk");
			map.keySet().stream().forEach(elem ->
			{
				System.out.println(elem + "--->" + map.get(elem));
			});
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Map<String, Integer> walk(BabelSynset source) throws IOException {
		int depth = 2;
		final Map<String, Integer> neighbours = new HashMap<>();
		neighbours.put(source.getID().toString(), 0);

		final Queue<BabelSynset> queue = new LinkedList<>();
		queue.add(source);

		while (!queue.isEmpty()) {
			final BabelSynset synset = queue.remove();
			final List<BabelSynsetRelation> edges = synset.getOutgoingEdges(BabelPointer.GLOSS_DISAMBIGUATED);
			for (final BabelSynsetRelation edge : edges) {
				int step = neighbours.get(synset.getID().toString());
				if (!neighbours.containsKey(edge.getTarget()) && Math.abs(step) < depth) {
					int level = (step == 0) ?
							(edge.getPointer().isHypernym() ? +1 : -1) :
								Integer.signum(step) * (Math.abs(step) + 1);
							neighbours.put(edge.getTarget(), level);
							queue.add(edge.getBabelSynsetIDTarget().toSynset());
				}
			}
		}

		//        neighbours.remove(source.getID().toString());
		return neighbours;
	}

}
