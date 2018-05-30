package it.uniroma3.internship.domain;

import it.uniroma1.lcl.babelnet.BabelSynsetID;

/**
 * 
 * @author silvio
 *
 */
public class BabelScore
{
	private BabelSynsetID syn;
	private BabelSynsetID root;
	private int distance;
	private int grade;
	private int score;
	
	public BabelScore(BabelSynsetID syn, BabelSynsetID root, int grade, int distance)
	{
		this.syn = syn;
		this.root = root;
		this.distance = distance;
		this.grade = grade;
		
		this.score = grade*distance;
	}
	
	

	public BabelSynsetID getSyn()
	{
		return syn;
	}

	public void setSyn(BabelSynsetID syn)
	{
		this.syn = syn;
	}
	
	public BabelSynsetID getRoot()
	{
		return root;
	}

	public void setRoot(BabelSynsetID root)
	{
		this.root = root;
	}

	public int getDistance()
	{
		return distance;
	}

	public void setDistance(int distance)
	{
		this.distance = distance;
	}

	public int getGrade()
	{
		return grade;
	}

	public void setGrade(int grade)
	{
		this.grade = grade;
	}

	public int getScore()
	{
		return score;
	}

	public void setScore(int score)
	{
		this.score = score;
	}
	
	@Override 
	public boolean equals(Object obj)
	{
		if(obj == null) return false;
		
		BabelScore bs = (BabelScore)obj;
		return this.syn.equals(bs.syn);
	}
	
	@Override
	public int hashCode()
	{
		return this.syn.hashCode();
	}
}
