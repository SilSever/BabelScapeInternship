package it.uniroma3.internship.domain;

import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma3.internship.util.Finder;

public class JGraphtTraversalExample {
	
	public static void main(String[] args)
	{
		Finder f = new Finder();
		String s = "bn:00000001n";
		BabelSynsetID syn = f.findByID(s);
		
		System.out.println("Hashcode string: " + s.hashCode());
		System.out.println("Hashcode syn: " + syn.hashCode());
		
//		Graph<String, String> grafo = new DirectedAcyclicGraph<>(String.class); 
//		grafo.addVertex("B");
//		grafo.addVertex("A");
//		grafo.addVertex("C");
//		
//		grafo.addEdge("A", "B");
//		grafo.addEdge("B", "C");
//		grafo.addEdge("C", "A");
//		grafo.addEdge("A", "C");
//		
//		DepthFirstIterator<String, String> dp = new DepthFirstIterator<>(grafo);
//		System.out.println(dp.next());
	}
}