package it.uniroma3.internship.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import com.detectlanguage.DetectLanguage;
import com.detectlanguage.errors.APIError;

import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelNetQuery;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma1.lcl.babelnet.BabelSynsetRelation;
import it.uniroma1.lcl.babelnet.data.BabelSenseSource;
import it.uniroma1.lcl.jlt.util.Language;
import it.uniroma3.internship.ui.FileHandler;

/**
 * 
 * @author silvio
 *
 */
public class Finder
{
	private static final int WORDNET_SYNSETS = 118000;
	private BabelNet bn;


	public Finder()
	{
		this.bn = BabelNet.getInstance();
	}

	/**
	 * @param toFind
	 * @return a list of synsetIDs found of a search of a concept
	 * @throws APIError 
	 */
	public List<BabelSynsetID> findByName (String toFind)
	{
		if(toFind == null || toFind.equals("")) return null;

		List<BabelSynset> synsetList = new LinkedList<BabelSynset>();
		BabelNetQuery query = new BabelNetQuery.Builder(toFind)
				.source(BabelSenseSource.WN)
				.from(getLanguage(toFind))
				.to(Arrays.asList(Language.EN,Language.IT,Language.ES,Language.FR,Language.DE))
				.build();
		synsetList = bn.getSynsets(query);

		List<BabelSynsetID> synsetIDList = new LinkedList<BabelSynsetID>();
		if(!synsetList.isEmpty())
			synsetList.forEach(syn -> synsetIDList.add(syn.getID()));
		return synsetIDList;
	}

	public List<String> findByOnlyName (String toFind)
	{
		if(toFind == null || toFind.equals("")) return null;

		List<BabelSynset> synsetList = new LinkedList<BabelSynset>();
		BabelNetQuery query = new BabelNetQuery.Builder(toFind)
				.source(BabelSenseSource.WN)
				.from(getLanguage(toFind))
				.to(Arrays.asList(Language.EN,Language.IT,Language.ES,Language.FR,Language.DE))
				.build();
		synsetList = bn.getSynsets(query);

		List<String> synsetIDList = new LinkedList<>();
		if(!synsetList.isEmpty())
			synsetList.forEach(syn -> synsetIDList.add(syn.getID().toString()));
		return synsetIDList;
	}

	/**
	 * 
	 * @param toFind
	 * @return the Language enum using detectedLanguage's api
	 * @throws APIError
	 */
	private Language getLanguage(String toFind)
	{
		DetectLanguage.apiKey = "17ae8721755b7449d8187d077fbc3792";
		Language l = null;

		try{
			l = Language.valueOf(DetectLanguage.simpleDetect(toFind).toUpperCase());
		}catch(APIError e) {
			e.printStackTrace();
		}
		return l;
	}

	/**
	 * 
	 * @param synsetID
	 * @return the babelSynsetID outgoingEdges list of a single synsetID
	 */
	public List<BabelSynsetID> getEdge (BabelSynsetID synsetID)
	{
		if(synsetID == null) return null;

		List<BabelSynsetID> edgeSynsetIDs = new LinkedList<>();
		List<BabelSynsetRelation> synsetRelation = synsetID.getOutgoingEdges();
		if(synsetRelation != null)
		{
			synsetRelation.forEach(syn -> 
			{
				if(syn != null) 
					edgeSynsetIDs.add(syn.getBabelSynsetIDTarget());	
			});
		}
		return edgeSynsetIDs;
	}

	public List<String> getStringEdge (String synsetID)
	{
		if(synsetID == null) return null;

		List<String> edgeSynsetIDs = new LinkedList<>();
		BabelSynsetID synS = new BabelSynsetID(synsetID);
		List<BabelSynsetRelation> synsetRelation = synS.getOutgoingEdges();
		if(synsetRelation != null)
		{
			synsetRelation.forEach(syn -> 
			{
				if(syn != null) 
					edgeSynsetIDs.add(syn.getBabelSynsetIDTarget().toString());	
			});
		}
		return edgeSynsetIDs;
	}

	/**
	 * 
	 * @param toFind
	 * @return a synsetID from an ID
	 */
	public BabelSynsetID findByID(String toFind)
	{
		BabelSynset bs = this.bn.getSynset(new BabelSynsetID(toFind));
		return bs == null ? null : bs.getID();
	}
	
	/**
	 * Initially it checks if already a file exists with a wordnet synsets.
	 * If true, it builds a list from that file, else it builds it scanning Babelnet network
	 * 
	 * @return a List of a wordnet synsets in Babelnet
	 */
	public List<BabelSynsetID> getAllWordnetSynset()
	{
		FileHandler fileH = new FileHandler();
		
		if(fileH.checkFileOfListIsEmpty())
		{
			final List<BabelSynsetID> wordnetSyn = new LinkedList<>();
			IntStream.range(1, WORDNET_SYNSETS).forEach( i ->
			{
				StringBuilder builder = new StringBuilder();
				builder.append("bn:");
				builder.append(String.format("%08d", i));
				builder.append("n");

				BabelSynset syn = this.bn.getSynset(new BabelSynsetID(builder.toString()));
				if(syn != null && !syn.getSenses(BabelSenseSource.WN).isEmpty())
				{
					wordnetSyn.add(syn.getID());
				}
			});
			System.out.println("Sono in finder: "+wordnetSyn.size());
			fileH.writeList(wordnetSyn);
			return wordnetSyn;
		} 
		else
		{
			final List<BabelSynsetID> wordnetSyn2 = new LinkedList<>();
			List<String> wordnetSynID = fileH.readList();
			wordnetSynID.parallelStream().forEach(elem -> wordnetSyn2.add(this.findByID(elem)));
			return wordnetSyn2;
		}
	}

	public BabelNet getBabelnet()
	{
		return this.bn;
	}

}
