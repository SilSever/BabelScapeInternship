package it.uniroma3.internship.ui;

import java.util.Scanner;

/**
 * 
 * @author silvio
 *
 */
public class IO
{

	public IO()
	{
	}
	
	@SuppressWarnings("resource")
	public static String getToFind()
	{
		Scanner input = new Scanner(System.in);
		System.out.println("Tell me a sentence: ");
		String toFind = input.nextLine();
		
		return validate(toFind);
	}

	private static String validate(String toFind)
	{
		if(toFind == null || toFind.equals(""))
			return null;
		else
			return toFind.toLowerCase();
	}

}
