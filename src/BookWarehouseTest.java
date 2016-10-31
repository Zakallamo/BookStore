import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * 
 * @author Johny Löfgren
 * 
 */
public class BookWarehouseTest extends TestCase {
	
	public String searchString, numberOfBooksToAdd, numberOfBooksToRemove;

	@Before
	public void setUp() throws Exception {
		searchString = "How To Spend Money";
		numberOfBooksToAdd = "3";
		numberOfBooksToRemove = "2";
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
