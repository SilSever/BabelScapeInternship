package it.uniroma3.internship.domain;

import it.uniroma1.lcl.babelnet.BabelSynsetID;

public class Node
{
	private BabelSynsetID id;
	private boolean visited;
	private int depth;
	
	public Node(BabelSynsetID id)
	{
		this.id = id;
		this.visited = false;
		this.depth = 0;
	}

	public BabelSynsetID getId()
	{
		return id;
	}

	public void setId(BabelSynsetID id)
	{
		this.id = id;
	}

	public boolean isVisited()
	{
		return visited;
	}

	public void changeVisited()
	{
		if(this.visited) 
			this.visited = false;
		else 
			this.visited = true;
	}
	
	public int getDepth()
	{
		return this.depth;
	}
	
	public void buildUpDepth()
	{
		this.depth += 1;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		Node n = (Node)obj;
		return this.id.equals(n.getId());
	}
	
	@Override
	public int hashCode()
	{
		return this.id.hashCode();
	}
	
}
