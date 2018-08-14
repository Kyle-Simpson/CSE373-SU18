package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;
import org.junit.Test;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Random;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testThrowsException() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        try {
            Searcher.topKSort(-1, list);
            fail("Exception throw expected for negative values.");
        } catch (IllegalArgumentException err) {
            // Do nothing
        }
    }
    
    // need edit later
    @Test(timeout=SECOND)
    public void testSimpleSorting() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }        
        IList<Integer> result = Searcher.topKSort(5, list);
        assertEquals(5, result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(5 + i, result.get(i));
        }      
    }
    
    @Test(timeout=SECOND)
    public void testLargeSort() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 1000; i > 0; i--) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(100, list);
        assertEquals(100, top.size());
        for (int i = 0; i < top.size(); i++) {          
            assertEquals(901 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testInt() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(5);
        list.add(1);
        list.add(7);
        list.add(8);
        IList<Integer> top = Searcher.topKSort(3, list);
        assertEquals(3, top.size());
        assertEquals(5, top.get(0));
        assertEquals(7, top.get(1));
        assertEquals(8, top.get(2));
    }
    
    @Test(timeout=SECOND)
    public void testRepeat() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 3; i++) {
            list.add(7);
            list.add(3);
            list.add(0);
        }
        IList<Integer> top = Searcher.topKSort(4, list);
        assertEquals(3, top.get(0));
        assertEquals(7, top.get(1));
        assertEquals(7, top.get(2));
        assertEquals(7, top.get(3));
    }
    
    @Test(timeout=SECOND)
    public void testKValue() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(5);
        list.add(1);
        list.add(7);
        list.add(9);
        IList<Integer> top = Searcher.topKSort(10, list);
        assertEquals(1, top.get(0));
        assertEquals(5, top.get(1));
        assertEquals(7, top.get(2));
        assertEquals(9, top.get(3));
    }
    
    @Test(timeout = SECOND)
    public void testString() {

        IList<String> list = new DoubleLinkedList<>();
        list.add("bcd");
        list.add("abc");
        list.add("def");
        list.add("cde");
        list.add("efg");
        list.add("fgh");
        list.add("ghi");
        list.add("hij");
        list.add("ijk");
        list.add("jkl");

        IList<String> top = Searcher.topKSort(10, list);
        

        IList<String> sortedlist = new DoubleLinkedList<>();
        sortedlist.add("abc");
        sortedlist.add("bcd");
        sortedlist.add("cde");
        sortedlist.add("def");
        sortedlist.add("efg");
        sortedlist.add("fgh");
        sortedlist.add("ghi");
        sortedlist.add("hij");
        sortedlist.add("ijk");
        sortedlist.add("jkl");
        
        for (int i = 0; i < 10; i++) {
            assertEquals(sortedlist.remove(), top.remove());
        }
    }
    
    @Test(timeout = SECOND)
    public void testAlternating() {

        IList<Integer> list = new DoubleLinkedList<>();
        list.add(12);
        list.add(4);
        list.add(20);
        list.add(2);
        list.add(17);
        list.add(16);
        list.add(15);
        list.add(18);
        list.add(19);
        list.add(3);
        list.add(1);
        list.add(1);
        list.add(4);
        list.add(3);
        list.add(7);

        IList<Integer> top = Searcher.topKSort(15, list);

        IList<Integer> sortedlist = new DoubleLinkedList<>();
        sortedlist.add(1);
        sortedlist.add(1);
        sortedlist.add(2);
        sortedlist.add(3);
        sortedlist.add(3);
        sortedlist.add(4);
        sortedlist.add(4);
        sortedlist.add(7);
        sortedlist.add(12);
        sortedlist.add(15);
        sortedlist.add(16);
        sortedlist.add(17);
        sortedlist.add(18);
        sortedlist.add(19);
        sortedlist.add(20);
        
        for (int i=0; i < 15; i++) {
            assertEquals(top.remove(), sortedlist.remove());
        }
    }
    
    @Test(timeout = SECOND)
    public void testLargeKCompare() {
        
        IList<Integer> list = new DoubleLinkedList<>();
        Random r = new Random();
        int n = 100000;
        int[] arr = new int[n];
        int k = 20000;
        
        for (int i=0; i<n; i++) {
            
            int v = r.nextInt();
            list.add(v);
            arr[i] = v;
        }
        
        Arrays.sort(arr);
        for (int i = 0; i < arr.length / 2; i++)
        {
            int temp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = temp;
        }
        
        int[] topArr = new int[k];
        
        for (int i=0; i<topArr.length; i++) {
            
            topArr[topArr.length-1-i] = arr[i];
        }
        
        IList<Integer> top = Searcher.topKSort(k, list);
        
        for (int i=0; i<k; i++) {
            
            assertEquals(topArr[i], top.get(i));
        }
    }
}
