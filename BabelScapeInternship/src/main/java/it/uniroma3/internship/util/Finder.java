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
import it.uniroma1.lcl.babelnet.data.BabelPointer;
import it.uniroma1.lcl.babelnet.data.BabelSenseSource;
import it.uniroma1.lcl.jlt.util.Language;
import it.uniroma3.internship.io.handler.WordnetNodeHandler;

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
	 * 
	 *  Lexical pointers represent
	 *  relations between word forms, and pertain only to specific words in the source and target synsets.
	 *  We only need semantic pointers of type hypernym, hyponym, meronym and holonym. The rest will just
	 *  be ignored
	 * 
	 * @param synsetID
	 * @return the babelSynsetID outgoingEdges list of a single synsetID
	 */
	public List<BabelSynsetID> getEdge (BabelSynsetID synsetID)
	{
		if(synsetID == null) return null;

		List<BabelSynsetID> edgeSynsetIDs = new LinkedList<>();
		List<BabelSynsetRelation> synsetRelation = synsetID.toSynset()
				.getOutgoingEdges(BabelPointer.ANY_HOLONYM, 
						BabelPointer.ANY_HYPERNYM,
						BabelPointer.ANY_HYPONYM,
						BabelPointer.ANY_MERONYM);
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

	/**
	 * 
	 * @param synsetID
	 * @return the wordnet babelSynsetID outgoingEdges list of a single synsetID
	 * 
	 */
	public List<BabelSynsetID> getWordnetEdge (BabelSynsetID synsetID)
	{
		if(synsetID == null) return null;

		List<BabelSynsetID> edgeSynsetIDs = new LinkedList<>();
		List<BabelSynsetRelation> synsetRelation = synsetID.toSynset().getOutgoingEdges();
		if(synsetRelation != null)
		{
			List<String> wnSyns = this.getAllWordnetSynset();
			synsetRelation.stream().forEach(syn -> 
			{
				if(syn != null)
				{
					if( !syn.getPointer().equals(BabelPointer.REGION) || !syn.getPointer().equals(BabelPointer.REGION_MEMBER)
							|| !syn.getPointer().equals(BabelPointer.TOPIC) || !syn.getPointer().equals(BabelPointer.TOPIC_MEMBER)
							|| !syn.getPointer().equals(BabelPointer.USAGE) || !syn.getPointer().equals(BabelPointer.USAGE_MEMBER))
						if(wnSyns.contains(syn.getBabelSynsetIDTarget().toString())) 
							edgeSynsetIDs.add(syn.getBabelSynsetIDTarget());	
				}
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
		return bs.getID();
	}

	/**
	 * Firstly it checks if already a file exists with a wordnet synsets.
	 * If true, it builds a list from that file, else it builds it scanning Babelnet network
	 * 
	 * @return a List of a wordnet synsets in Babelnet
	 */
	public List<String> getAllWordnetSynset()
	{
		WordnetNodeHandler fileH = new WordnetNodeHandler();

		if(fileH.checkIfEmpty())
		{
			final List<String> wordnetSynString = new LinkedList<>();
			IntStream.range(1, WORDNET_SYNSETS).forEach( i ->
			{
				StringBuilder builder = new StringBuilder();
				builder.append("bn:");
				builder.append(String.format("%08d", i));
				builder.append("n"); //Try to find a noun
				BabelSynsetID syn = this.findByID(builder.toString());
				if(syn == null)
				{
					builder.replace(builder.length()-1, builder.length(), "v"); //Else try to find a verb
					syn = this.findByID(builder.toString());
					if(syn == null)
					{
						builder.replace(builder.length()-1, builder.length(), "a"); //Else try to find an adjective
						syn = this.findByID(builder.toString());
					}
				}
				if(syn != null && !syn.toSynset().getSenses(BabelSenseSource.WN).isEmpty())
					wordnetSynString.add(syn.toString());
			});
			fileH.write(wordnetSynString);
			return wordnetSynString;
		} 
		else
		{
			List<String> wordnetSynID = fileH.read();
			return wordnetSynID;
		}
	}
	
	public String getFirstSenseOf(BabelSynsetID synID)
	{
		Lemmatizer lemm = new Lemmatizer();
		String sense = synID.toSynset().getSenses(Language.EN, BabelSenseSource.WN).get(0).getSimpleLemma().toString();
		return lemm.lemmatize(sense).get(0);
	}
	
	public BabelNet getBabelnet()
	{
		return this.bn;
	}

}
