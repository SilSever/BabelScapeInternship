package it.uniroma3.internship.ui;

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
 * This class it's used for all operations relavite at reading and writing on file
 * 
 * @author Silvio Severino
 *
 */
public class FileHandler
{
	private static final String LIST_FILE = "wordnetList.txt";

	
	/**
	 * Write on a file
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
	 * Read from file
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
	 * 		   else false
	 */
	public boolean checkFileOfListIsEmpty()
	{
		File file = new File(LIST_FILE);		
		return file.length() == 0;
	}
}
