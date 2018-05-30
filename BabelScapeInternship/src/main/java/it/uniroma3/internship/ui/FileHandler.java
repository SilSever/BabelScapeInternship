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

import it.uniroma1.lcl.babelnet.BabelSynsetID;

public class FileHandler
{
	private static final String LIST_FILE = "wordnetList.txt";
	private static final String MAP_FILE = "wordnetMap.txt";

	public void writeList(List<BabelSynsetID> list) 
	{
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(LIST_FILE));)
		{
			for(BabelSynsetID syn : list)
				bw.write(syn.toString() + " ");
			bw.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
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

	public boolean checkFileOfListIsEmpty()
	{
		File file = new File(LIST_FILE);		
		return file.length() == 0;
	}
}
