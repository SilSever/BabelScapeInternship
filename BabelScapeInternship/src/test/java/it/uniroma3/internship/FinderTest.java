/**
 * 
 */
package it.uniroma3.internship;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.detectlanguage.errors.APIError;

import it.uniroma1.lcl.babelnet.BabelSynsetID;
import it.uniroma3.internship.util.Finder;

/**
 * @author silvio
 *
 */
class FinderTest
{
	private Finder finder;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception
	{
		this.finder = new Finder();
	}

	/**
	 * Test method for {@link it.uniroma3.internship.util.Finder#findByName(java.lang.String)}.
	 * @throws APIError 
	 */
	@Test
	void testFindByNameEmpty() throws APIError
	{
		assertEquals(null, finder.findByName(""));
	}
	@Test
	void testFindByNameNull() throws APIError
	{
		assertEquals(null, finder.findByName(null));
	}
	@Test
	void testFindByNameNotEmpty() throws APIError
	{
		assertNotEquals(0, finder.findByName("giocare").size());
	}

	/**
	 * Test method for {@link it.uniroma3.internship.util.Finder#getEdge(it.uniroma1.lcl.babelnet.BabelSynsetID)}.
	 */
	@Test
	void testGetEdgeNull()
	{
		assertEquals(null,finder.getEdge(null));
	}
	@Test
	void testGetEdgeWithSynsetIDOfBabelNet()
	{
		assertNotEquals(0,finder.getEdge(new BabelSynsetID("bn:03083790n")).size());
	}
}
