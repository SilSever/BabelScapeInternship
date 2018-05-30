package it.uniroma3.internship.ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma3.internship.domain.BabelScore;

public class ReaderWriterFile
{
	
	private static final String MAP_FILE = "mapFile.dat";
	private static final String LIST_FILE = "listFile.dat"; 

	@SuppressWarnings("unchecked")
	public List<Map<BabelSynsetID, BabelScore>> readMapList()
	{
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		List<Map<BabelSynsetID, BabelScore>> read = null;
		try
		{
			fis = new FileInputStream(this.MAP_FILE);
		} catch (FileNotFoundException e){return null;}
		
		try
		{
			ois = new ObjectInputStream(fis);
		} catch (IOException e){}
		
		try
		{
			read = (ArrayList<Map<BabelSynsetID, BabelScore>>)ois.readObject();
		} catch (ClassNotFoundException | IOException e){}
		
		return read;
	}
	public void writeMapList(List<Map<BabelSynsetID, BabelScore>> ID2ScoreList)
	{
		List<Map<BabelSynsetID, BabelScore>> al = ID2ScoreList;
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		try
		{
			fos = new FileOutputStream(this.MAP_FILE);
		} catch (FileNotFoundException e){}
		
		try
		{
			oos = new ObjectOutputStream(fos);
		} catch (IOException e){}
		
		try
		{
			oos.writeObject(al);
		} catch (IOException e){}
		
	}

	
	public List<BabelSynsetID> readList()
	{
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		List<BabelSynsetID> read = null;
		try
		{
			fis = new FileInputStream(this.LIST_FILE);
		} catch (FileNotFoundException e){return null;}
		
		try
		{
			ois = new ObjectInputStream(fis);
		} catch (IOException e){}
		
		try
		{
			read = (ArrayList<BabelSynsetID>)ois.readObject();
		} catch (ClassNotFoundException | IOException e){}
		
		return read;
		
	}
	public void writeList(List<BabelSynsetID> wordnetList)
	{
		List<BabelSynsetID> al = wordnetList;
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		try
		{
			fos = new FileOutputStream(this.LIST_FILE);
		} catch (FileNotFoundException e){}
		
		try
		{
			oos = new ObjectOutputStream(fos);
		} catch (IOException e){}
		
		try
		{
			oos.writeObject(al);
		} catch (IOException e){}
	}

}
