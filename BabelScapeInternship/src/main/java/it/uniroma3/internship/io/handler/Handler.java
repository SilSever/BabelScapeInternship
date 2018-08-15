package it.uniroma3.internship.io.handler;

/**
 * 
 * @author Silvio Severino
 *
 */
public interface Handler
{
	/**
	 * 
	 * write an obj in a file
	 * @param obj
	 * 
	 */
	public void write(Object obj);
	
	/**
	 * 
	 * read an obj from a file
	 * @return
	 * 
	 */
	public Object read();
	
	/**
	 * 
	 * check if the file is empty
	 * @return
	 * 
	 */
	public boolean checkIfEmpty();
}
