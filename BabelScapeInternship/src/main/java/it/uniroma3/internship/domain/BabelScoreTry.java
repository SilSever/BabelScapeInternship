package it.uniroma3.internship.domain;

import it.uniroma1.lcl.babelnet.BabelSynsetID;

/**
 * 
 * @author silvio
 *
 */
public class BabelScoreTry
{
	private BabelSynsetID syn;
	private int distance;
	private int score;
	
	public BabelScoreTry(BabelSynsetID syn, int distance)
	{
		this.setSyn(syn);
		this.distance = distance;
		
		this.score = 1;
	}

	public int getDistance()
	{
		return distance;
	}

	public BabelSynsetID getSyn()
	{
		return syn;
	}

	public void setSyn(BabelSynsetID syn)
	{
		this.syn = syn;
	}

	public void setDistance(int distance)
	{
		this.distance = distance;
	}

	public int getScore()
	{
		return score;
	}

	public void setScore(int score)
	{
		this.score = score;
	}
}
