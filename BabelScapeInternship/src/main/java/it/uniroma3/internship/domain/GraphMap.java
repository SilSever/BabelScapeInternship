package it.uniroma3.internship.domain;

import java.util.Map;
import java.util.Set;

import it.uniroma1.lcl.babelnet.BabelSynsetID;

/**
 * 
 * @author silvio severino
 *
 */
public class GraphMap
{
	private Map<BabelSynsetID,BabelScore> synID2score;
	
	public GraphMap(Map<BabelSynsetID,BabelScore> synID2score)
	{
		this.synID2score = synID2score;
	}

	public Map<BabelSynsetID, BabelScore> getSynID2score()
	{
		return synID2score;
	}

	public void setSynID2score(Map<BabelSynsetID, BabelScore> synID2score)
	{
		this.synID2score = synID2score;
	}

	public Set<BabelSynsetID> getKeySet()
	{
		return this.synID2score.keySet();
	}
}
