package datastructures;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;


/**
 * This class should contain all the tests you implement to verify that
 * your 'delete' method behaves as specified.
 *
 * This test _extends_ your TestDoubleLinkedList class. This means that when
 * you run this test, not only will your tests run, all of the ones in
 * TestDoubleLinkedList will also run.
 *
 * This also means that you can use any helper methods defined within
 * TestDoubleLinkedList here. In particular, you may find using the
 * 'assertListMatches' and 'makeBasicList' helper methods to be useful.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDeleteFunctionality extends TestDoubleLinkedList {

    
    /**
     * Note: We use 1 second as the default timeout for many of our tests.
     *
     * One second is typically extremely generous: most of your tests should
     * finish in milliseconds. If one of your tests is timing out, you're almost
     * certainly doing something wrong.
     */
	
	

    @Test(timeout=SECOND, expected = IndexOutOfBoundsException.class)
    public void testDeleteNodeInEmptyList() {
        //delete node in a empty list
        IList<String> list = new DoubleLinkedList<>();
        list.delete(0);
    }
    
    
    
    @Test(timeout=SECOND)
    public void testDeleteOneNode() {
    	IList<String> list = new DoubleLinkedList<>();
    	list.add("a");
    	list.add("b");
        list.delete(1);
        
        this.assertListMatches(new String[] {"a"}, list);
    }
    
    @Test(timeout=SECOND)
    public void testRemoveOneNode() {
    	IList<String> list = new DoubleLinkedList<>();
    	list.add("a");
    	list.add("b");
    	list.remove();
    	
        this.assertListMatches(new String[] {"a"}, list);
    }
    
    @Test(timeout=SECOND)
    public void testDeleteTwoNode() {
        // test if delete two nodes per time
        IList<String> list =  makeBasicList();
        list.delete(0);
        list.delete(1);
        this.assertListMatches(new String[] {"b"}, list);
        
    }
    
    @Test(timeout=SECOND)
    public void testDeleteOneNodeCheckReference() {
        IList<String> list = makeBasicList();
        list.delete(0);
        assertEquals(0, list.indexOf("b"));
    }
    
    @Test(timeout=SECOND)
    public void testDeleteLastNode() {
        IList<String> list = makeBasicList();
        list.delete(list.size()-1);           
        assertEquals(list.size()-1, list.indexOf("b"));   
        assertEquals(2, list.size());
        this.assertListMatches(new String[] {"a", "b"}, list);
        list.add("cc");
        list.add("dd");
        assertEquals(2, list.indexOf("cc"));
        assertEquals(3, list.indexOf("dd"));
        assertEquals(4, list.size());
        this.assertListMatches(new String[] {"a", "b", "cc", "dd"}, list);
    }
    
    @Test(timeout=SECOND)
    public void testDeleteLastNodeUntilEmpty() {
        IList<String> list = makeBasicList();
        int initListSize = list.size();
        for (int i = 0; i < initListSize; i++) {
            list.delete(list.size() - 1);
        }
        assertEquals(0, list.size());
    }
    @Test(timeout=SECOND)
    public void testDeleteFirstNodeUntilEmpty() {
        IList<String> list = makeBasicList();
        int initListSize = list.size();
        for (int i = 0; i < initListSize; i++) {
            list.delete(0);
        }
        assertEquals(0, list.size());
    }
    @Test(timeout=SECOND, expected = IndexOutOfBoundsException.class)
    public void testDeleteThrowException() {
        IList<String> list = makeBasicList();
        list.delete(list.size()); 
    }
    
    @Test(timeout=SECOND, expected = IndexOutOfBoundsException.class)
    public void testDeleteThrowException2() {
        IList<String> list = makeBasicList();
        list.delete(-1);    
    }
    
    @Test(timeout=SECOND)
    public void testDeleteUntilTheFirstIndex() {
        IList<String> list = makeBasicList();
        for (int i = list.size()-1; i >= 1; i--) {
            list.delete(i);
        }
        assertEquals(0, list.indexOf("a"));
        assertEquals(1, list.size());
        this.assertListMatches(new String[] {"a"}, list);
    }
    
    @Test(timeout=SECOND)
    public void testDeleteOnlyHasTheLastIndex() {
        // testing the deletion from the first index until the last one.
        IList<String> list = makeBasicList();
        int initSize = list.size();
        for (int i = 0; i < initSize - 1; i++) {
            list.delete(0);
        }
        assertEquals(1, list.size());
        assertEquals(0, list.indexOf("c"));
    }
}
