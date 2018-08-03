package it.uniroma3.internship.ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
/**
 * This class it's used for all operations relavite at reading and writing on file
 * 
 * @author Silvio Severino
 *
 */
public class FileHandler
{
	private static final String GRAPH_FILE = "wordnetGraph.ser";
	private static final String LIST_FILE = "wordnetList.txt";


	/**
	 * Write on a file the wordnet synsets list
	 * 
	 * @param list of wordnetSynsets
	 */
	public void writeList(List<String> list) 
	{
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(LIST_FILE));)
		{
			for(String syn : list)
				bw.write(syn + " ");
			bw.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Read from file the wordnet synsets list
	 * @return the list of wordnetSynsets
	 */
	public List<String> readList()
	{
		List<String> list = new LinkedList<>();
		String str = "";

		try(BufferedReader br = new BufferedReader(new FileReader(LIST_FILE))){
			while((str = br.readLine()) != null)
				list.addAll(Arrays.asList(str.split(" ")));
			br.close();
		}catch(IOException e) {
			e.printStackTrace();
		}

		return list;
	}


	/**
	 * 
	 * @return true if wordnetList.txt is empty.
	 * 		   false otherwise
	 */
	public boolean checkListFileIsEmpty()
	{
		File file = new File(LIST_FILE);		
		return file.length() == 0;
	}


	/**
	 * Write in a serial file the wordnet graph
	 * @param graph
	 */
	public void writeGraph(Graph<String, DefaultEdge> graph)
	{
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
	 * Read from a serial file the wordnet graph
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "resource" })
	public Graph<String, DefaultEdge> readGraph()
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
	public boolean checkGraphFileIsEmpty()
	{
		File file = new File(GRAPH_FILE);		
		return file.length() == 0;
	}
}
