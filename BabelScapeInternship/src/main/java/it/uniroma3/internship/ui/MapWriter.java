package it.uniroma3.internship.ui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma3.internship.domain.BabelScore;
import it.uniroma3.internship.domain.GraphMap;

public class MapWriter
{
	private final static String FILE_NAME = "output.txt";
	private final static String FILE_NAME_R = "outputR.txt";
	private List<Map<BabelSynsetID, BabelScore>> map;

	public MapWriter(List<Map<BabelSynsetID, BabelScore>> graphMap)
	{
		this.map = graphMap;
	}

	public void write()
	{
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));
			for(Map<BabelSynsetID, BabelScore> map : this.map) 
			{
				for(BabelSynsetID syn : map.keySet())
				{
					writer.append(syn.toString() + "->" +
							"\tn: " + syn.toSynset().getMainSense() + 
							"\td: " + map.get(syn).getDistance() +
							"\tg: " + map.get(syn).getGrade() +
							"\ts: " + map.get(syn).getScore() + "\n");
				}
			}
		}catch(IOException e) {

		}
	}

	public void Write2()
	{
		BufferedWriter writer = null;
		try
		{
			writer = new BufferedWriter(new FileWriter(FILE_NAME_R));
			recursiveWrite(writer, 0);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("Fine stampa");
	}
	private void recursiveWrite(BufferedWriter writer, int i) throws IOException
	{
		if(i == 1) return;
		Map<BabelSynsetID, BabelScore> map = this.map.get(i);
		for(BabelSynsetID syn : map.keySet())
		{
			writer.append(syn.toString() + "->" +
					"\tr: " + map.get(syn).getRoot().toString() +
					"\tn: " + syn.toSynset().getMainSense() + 
					"\td: " + map.get(syn).getDistance() +
					"\tg: " + map.get(syn).getGrade() +
					"\ts: " + map.get(syn).getScore() + "\n");
			recursiveWrite(writer, i+1);
		}
		//Forse bisogna mettere il nodo per indicare la radice
		//Non viene visualizzato root -> child -> second
	}

}
