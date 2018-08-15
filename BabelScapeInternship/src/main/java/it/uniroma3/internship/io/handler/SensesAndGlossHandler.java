package it.uniroma3.internship.io.handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Silvio Severino
 *
 */
public class SensesAndGlossHandler implements Handler
{
	private static final String LIST_SENSES_AND_GLOSSES_FILE = "sensesAndGlosses.txt";

	/**
	 * 
	 * Write in a text file the sensesAndGlosses graph
	 * @param List of sensesAndGlosses
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void write(Object obj)
	{
		List<String> sensesAndGlosses = (List<String>) obj;

		try(BufferedWriter bw = new BufferedWriter(new FileWriter(LIST_SENSES_AND_GLOSSES_FILE));)
		{
			for(String syn : sensesAndGlosses)
				if(syn != null)
					bw.write(syn + " ");
			bw.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Read from a txt file the sensesAndGlosses list
	 * @return
	 * 
	 */
	@Override
	public List<String> read()
	{
		List<String> list = new LinkedList<>();
		String str = "";

		try(BufferedReader br = new BufferedReader(new FileReader(LIST_SENSES_AND_GLOSSES_FILE))){
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
	 * @return true if sensesAndGlosses.txt is empty
	 * 		   false otherwise
	 */
	@Override
	public boolean checkIfEmpty()
	{
		File file = new File(LIST_SENSES_AND_GLOSSES_FILE);		
		return file.length() == 0;
	}

}
