package it.uniroma3.internship.io.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

/**
 * 
 * @author Silvio Severino
 *
 */
public class GraphHandler implements Handler
{
	private static final String GRAPH_FILE = "wordnetGraph.ser";
	
	/**
	 * 
	 * Write in a serial file the wordnet graph
	 * @param graph
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void write(Object obj)
	{
		Graph<String, DefaultEdge> graph = (Graph<String, DefaultEdge>) obj;
		FileOutputStream fos;
		try
		{
			fos = new FileOutputStream(GRAPH_FILE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(graph);
			oos.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * Read from a serial file the wordnet graph
	 * @return
	 * 
	 */
	@SuppressWarnings({ "unchecked", "resource" })
	@Override
	public Graph<String, DefaultEdge> read()
	{
		FileInputStream fin;
		Graph<String,DefaultEdge> graph = null;
		try
		{
			fin = new FileInputStream(GRAPH_FILE);
			ObjectInputStream ois = new ObjectInputStream(fin);
			graph = (Graph<String, DefaultEdge>) ois.readObject();
		} catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return graph;
	}


	/**
	 * 
	 * @return true if wordnetGraph.ser is empty
	 * 		   false otherwise
	 */
	@Override
	public boolean checkIfEmpty()
	{
		File file = new File(GRAPH_FILE);		
		return file.length() == 0;
	}
}
