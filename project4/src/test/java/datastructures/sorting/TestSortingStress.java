package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

import static org.junit.Assert.assertTrue;

import java.util.PriorityQueue;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
    
    @Test(timeout=10*SECOND)
    public void testPlaceholder() {
        assertTrue(true);
    }
    
    @Test(timeout = 10*SECOND)
    public void testHeap() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i <200000; i++) {
            heap.insert(i);
        }
        for (int i = 0; i < 200000; i++) {
            assertEquals(i, heap.peekMin());
            assertEquals(i, heap.removeMin());
        }
        
    }
    @Test(timeout = 10*SECOND)
    public void testSortEfficientcyOne() {
        int cap = 10000;
        IList<Integer> list = new DoubleLinkedList<>();

        for (int i = 0; i < cap; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(cap, list);
        assertEquals(cap, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }
    
    @Test(timeout = 15*SECOND) // test different timeout
    public void testSortEfficiencyTwo() {
        int cap = 200000;
        IList<Integer> list = new DoubleLinkedList<>();
        
        for (int i = 0; i < cap; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(cap/2, list);
        assertEquals(cap/2, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals((cap/2) +i, top.get(i));
        }
    }
    
    @Test(timeout = 10*SECOND)
    public void testCompareToJavaHeap(){
        PriorityQueue<Integer> javaHeap = new PriorityQueue<Integer>();
        IPriorityQueue<Integer> heap = this.makeInstance();
        
        for (int i = 0; i < 10000; i++) {
        javaHeap.add(i);
        heap.insert(i);
        }
        
        for (int i = 0; i < 10000; i++) {
            assertEquals(heap.peekMin(), javaHeap.peek());
            assertEquals(heap.removeMin(), javaHeap.remove());
            assertEquals(heap.size(), javaHeap.size());
        }
        
        
    }
    @Test(timeout = 10 * SECOND)
    public void testCompareToJava(){
        PriorityQueue<Integer> javaHeap = new PriorityQueue<Integer>();     
        for (int i = 0; i < 200000; i++) {
        javaHeap.add(i);
        }
        
        for (int i = 0; i < 200000; i++) {
            assertEquals(i, javaHeap.peek());
            assertEquals(i, javaHeap.remove());
        }

    }
}
