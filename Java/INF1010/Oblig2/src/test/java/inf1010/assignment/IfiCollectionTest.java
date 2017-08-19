package inf1010.assignment;

import org.testng.annotations.*;
import static org.testng.Assert.*;

import inf1010.lib.two.IfiCollection;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Iterator;

class TestPerson implements Comparable<TestPerson> {
	String name;

	TestPerson(String name) {
		this.name = name;
	}

	public int compareTo(TestPerson other) {
		return name.compareTo(other.name);
	}

	public String toString() {
		return String.format("TestPerson(\"%s\")", name);
	}

    // Hack.no v.2
    public boolean equals(String name) {
        return this.name.equals(name); 
    }
}

/** TestCase which can be used to test any class implementing IfiCollection. */
public abstract class IfiCollectionTest {

	/**
	 * Used in all test requiring a collection with elements. Must be
	 * initialized in @BeforeMethod and must contain at least three elements
	 * and no duplicates.
	 * 
	 * @see NAMES
	 * */
	protected IfiCollection<TestPerson> c;

	/**
	 * Used in all test requiring an empty collection. Must be initialized in
	 * @BeforeMethod().
	 * */
	protected IfiCollection<TestPerson> emptyc;


	/**
	 * Values used to populate <code>c</code>. It must contain at least 3 items
	 * and no duplicates.
	 * 
	 * @see c
	 */
	protected static String[] NAMES = { "Joshi", "Kristian", "Sigmund", "Martin",
			"Tuva", "Michael", "Erlend", "Bendik", "Stein", "Stein Michael" };

	/**
	 * A person with a name not in <code>NAMES</code>.
	 * 
	 * @see NAMES
	 */
	protected static TestPerson NOT_A_PERSON = new TestPerson("NotAName");

	protected TestPerson[] allElementsAsArray() {
		TestPerson[] allElements = new TestPerson[NAMES.length];
		int x = 0;
		for (String name : NAMES)
			allElements[x++] = new TestPerson(name);
		return allElements;
	}

	protected void assertCompareToEquals(TestPerson actual,
			TestPerson expected, String msg) {
		assertTrue(actual.compareTo(expected) == 0, String.format(
				"%s: %s.compareTo(%s) == 0", msg, actual, expected));
	}

	protected void assertCompareToEquals(TestPerson[] actual,
			TestPerson[] expected, String msg) {
		for (int i = 0; i < actual.length; i++) {
			TestPerson a = actual[i];
			TestPerson e = expected[i];
			assertCompareToEquals(a, e, msg);
		}
	}

	protected void assertCompareToEqualsNoOrder(TestPerson[] actual,
			TestPerson[] expected, String msg) {
		assertEquals(actual.length, expected.length, msg);

		TestPerson[] actualElements = new TestPerson[actual.length];
		System.arraycopy(actual, 0, actualElements, 0, actual.length);

		TestPerson[] expectedElements = new TestPerson[expected.length];
		System.arraycopy(expected, 0, expectedElements, 0, expected.length);

		Arrays.sort(expectedElements);
		Arrays.sort(actualElements);

		assertCompareToEquals(actualElements, expectedElements, msg);
	}


	@Test(groups = { "collection-core" }, description="Tests add(), contains() and size() methods.")
	public void addContainsSize() {

		assertFalse(c.contains(NOT_A_PERSON),
				"contains() on item not in collection returns true");

		for (String name : NAMES) {
			TestPerson p = new TestPerson(name);
            c.add(p);
			assertTrue(c.contains(p), "<" + name
					+ "> has been added to collection but " + "contains(" + name
					+ ") returns false.");
		}

		assertEquals(c.size(), NAMES.length,
				"The size() method does not report the correct size.");

		assertTrue(c.add(NOT_A_PERSON),
				"add() does not return true when the collection is changed.");

		assertFalse(c.add(new TestPerson(NAMES[0])),
				"add() does not return false when adding a value already "
						+ "in the collection.");
		assertEquals(
				c.size(),
				NAMES.length + 1,
				"add() duplicate increases the size of the collection. "
						+ "IfiCollection is not supposed to be able to contain "
						+ "duplicates.");
	}

	@Test(groups = { "collection-core" }, expectedExceptions = NullPointerException.class,
			description="Test if add() throws NullPointerException as documented.")
	public void addNull() {
		c.add(null);
		emptyc.add(null);
	}

	@Test(groups = { "collection-core" }, expectedExceptions = NullPointerException.class,
			description="Test if contains() throws NullPointerException as documented.")
	public void containsNull() {
		c.contains(null);
		emptyc.contains(null);
	}

	@Test(description="Tests if the isEmpty() method works as specified in the interface.")
	public void isEmpty() {
		assertFalse(c.isEmpty(),
				"isEmpty returns true on a non-empty collection");
		assertTrue(emptyc.isEmpty(),
				"isEmpty returns false on an empty collection");
	}

	@Test(dependsOnGroups = { "collection-core" },
			description="Tests if the get(int index) method works as specified in the interface on sorted collections.")
	public void get() {
    
        String[] sortedNames = new String[NAMES.length];
        System.arraycopy(NAMES, 0, sortedNames, 0, NAMES.length);
        Arrays.sort(sortedNames);

        int i = 0;
        for (String name : sortedNames) {
            assertNotNull(c.get(i),"get(i) returns null. This should never happen. In this case it should return TestPerson("+name+").");
            assertEquals(c.get(i++).name,name,
                "get() does not return the correct element for given index,");
        }

	}


	@Test(expectedExceptions = IndexOutOfBoundsException.class,
			description="Test if get(int index) throws IndexOutOfBoundsException as documented.")
	public void getOutOfBounds() {
		c.get(c.size());
        c.get(c.size()+1);
        c.get(-1);
		emptyc.get(1);
	}


	@Test(dependsOnGroups = { "collection-core" },
			description="Tests if clear() removes all elements from the collection.")
	public void clear() {
		c.clear();
		assertEquals(c.size(), 0,
				"clear() does not remove all elements from the collection.");
	}


	@Test(dependsOnGroups = { "collection-core" },
		description="Tests if method toArray yields all the elements inserted in the collection in sorted order with smallest item first.")
	public void toArray() {
		TestPerson[] actualElements = c.toArray(new TestPerson[c.size()]);

		for (int i = 0; i < actualElements.length; i++) {
			assertNotNull(actualElements[i],
					"toArray() - array element at index " + i + " is null");
		}

		TestPerson[] expectedElements = allElementsAsArray();
		assertCompareToEqualsNoOrder(actualElements, expectedElements,
				"toArray() does not return all the elements in the collection.");

		Arrays.sort(expectedElements);
		assertCompareToEquals(actualElements, expectedElements,
				"toArray() does not return the elements in sorted order with "
						+ "the smallest elements first.");


		TestPerson[] inArr = new TestPerson[NAMES.length + 1];
		inArr[NAMES.length] = new TestPerson("TEMP");
		actualElements = c.toArray(inArr);
		assertNull(actualElements[NAMES.length],
				"The the element in the array immediately following the "
				+ "end of the list is not set to null");
	}



	/** Put all items in the collection into an array. */
	protected TestPerson[] iterToArray() {
		LinkedList<TestPerson> l = new LinkedList<TestPerson>();
		for (TestPerson p : c)
			l.add(p);
		return l.toArray(new TestPerson[l.size()]);
	}



	@Test(dependsOnGroups = { "collection-core" },
			expectedExceptions = { NoSuchElementException.class },
			description="Tests if calling next() on a iterator with no more items throws NoSuchElementException.")
	public void iteratorNextNull() {
		emptyc.iterator().next();
	}
	
	
	@Test(dependsOnGroups = { "collection-core" },
			description="Tests if calling next() on a iterator with only one element returns correct value.")
	public void iteratorTestOneElement() {
		
		emptyc.add(new TestPerson("Karen"));
		Iterator <TestPerson> it = emptyc.iterator();
		
		assertTrue(it.hasNext(), "Added one element, the iterators next() returns false!");
		
		TestPerson t = it.next();
		assertEquals(t.name, "Karen", "With one element, the iterator's next return wrong value!");
		assertFalse(it.hasNext(), "With one element in the collection, the iterators hasNext() returns true after one call to next()!");
	}


	@Test(dependsOnGroups = { "collection-core" },
			description="Tests if the iterator yields all the elements inserted in the collection in sorted order with smallest item first.")
	public void iteratorOrder() {
		TestPerson[] expectedElements = allElementsAsArray();

		TestPerson[] actualElements = iterToArray();
		assertCompareToEqualsNoOrder(actualElements, expectedElements,
				"The iterator does not yield all the inserted elements.");

		Arrays.sort(expectedElements);
		assertCompareToEquals(actualElements, expectedElements,
				"The iterator does not yield elements in sorted order with ."
						+ " the smallest element first.");
	}

	
	@Test(dependsOnGroups = { "collection-core" },
				description="Tests that the iterator does not wreck the list.")
	public void iteratorNotWreckingList() {
		
		TestPerson kasper = new TestPerson("Kasper");
		TestPerson jesper = new TestPerson("Jesper");
		TestPerson jonathan = new TestPerson("Jonathan");
		
		emptyc.add(kasper);
		emptyc.add(jesper);
		emptyc.add(jonathan);
		
		Iterator <TestPerson> it = emptyc.iterator();
		it.next(); 
		it.next(); 
		it.next();

		assertTrue(emptyc.size() == 3, "After iterating the iterator, the size is " + emptyc.size() + ". Expected size is " + 3);
		assertTrue(emptyc.contains(kasper), "After iterating the iterator, "+ kasper +", the first element, "
				   + "is no longer in the collection");
		assertTrue(emptyc.contains(jesper), "After iterating the iterator, "+ jesper +" is no longer in the collection");
		assertTrue(emptyc.contains(jonathan), "After iterating the iterator, "+ jonathan +", the last element, "
				   + "is no longer in the collection");
	}
}
