package it.uniroma3.internship.domain;

import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

public class prova
{

	public static void main(String[] args)
	{		
		Graph<String, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);	
		
		String a = "A";
		String b = "B";
		String c = "C";
		String d = "D";
		
		graph.addVertex(a);
		graph.addVertex(b);
		graph.addVertex(c);
		graph.addVertex(d);
		
		
		graph.addEdge(a, b);
		graph.addEdge(a, c);
		graph.addEdge(a, d);
		
		graph.addEdge(b, a);
		graph.addEdge(b, c);
		graph.addEdge(b, d);
		
		graph.addEdge(c, a);
		graph.addEdge(c, b);
		graph.addEdge(c, d);

		graph.addEdge(d, a);
		graph.addEdge(d, b);
		
		PageRank<String, DefaultEdge> pr = new PageRank<>(graph);
		Map<String, Double> map = pr.getScores();
		
		map.keySet().forEach(elem -> {
			System.out.println(elem + "--->" + map.get(elem));
		});
	}

}
