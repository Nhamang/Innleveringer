package inf1010.assignment;

import static org.testng.Assert.*;
import org.testng.annotations.*;
import java.util.Iterator;


@Test
public class OrderedSetTest extends IfiCollectionCompleteTest {

	@BeforeMethod(alwaysRun=true,
			description="Creates a OrderedSet containing a test-people used by all the tests. Fails if the constructor or add() crashes or throws an exception.")
	public void createListUsedByAllTests() {
		c = new OrderedSet<TestPerson>();
		emptyc = new OrderedSet<TestPerson>();
        for (String name : NAMES)
		    c.add(new TestPerson(name));
	}
}
