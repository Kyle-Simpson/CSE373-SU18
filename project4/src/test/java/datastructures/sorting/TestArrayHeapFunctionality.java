package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import org.junit.Test;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testInsert() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertEquals(0, heap.size());
        heap.insert(10);
        assertEquals(10, heap.peekMin());
        
        heap.insert(9); 
        assertEquals(9, heap.peekMin());
        
        heap.insert(8); 
        assertEquals(8, heap.peekMin());
        
        heap.insert(7); 
        assertEquals(7, heap.peekMin());
        
        heap.insert(6); 
        assertEquals(6, heap.peekMin());
        
        heap.insert(5); 
        assertEquals(5, heap.peekMin());
        
        heap.insert(4); 
        assertEquals(4, heap.peekMin());
        
        // Duplicate
        heap.insert(3); 
        assertEquals(3, heap.peekMin());
        
        heap.insert(3); 
        assertEquals(3, heap.peekMin());
        
        heap.insert(2); 
        assertEquals(2, heap.peekMin());
        
        heap.insert(1); 
        assertEquals(1, heap.peekMin());
        
        heap.insert(0); 
        assertEquals(0, heap.peekMin());
        
        assertEquals(12, heap.size());
            
    }
    
    @Test(timeout=SECOND*10)
    public void testInsertWithManyElements() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        
        for (int i=10000; i>0; i--) {
            
            heap.insert(i); 
            assertEquals(i, heap.peekMin());
        }
        
        assertEquals(1, heap.peekMin());
        assertEquals(10000, heap.size());
    }
    
    @Test(timeout=SECOND*10)
    public void testInsertWithManyElementsInOrder() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        
        for (int i=0; i<10000; i++) {
            
            heap.insert(i); 
            assertEquals(0, heap.peekMin());
        }
        
        assertEquals(0, heap.peekMin());
        assertEquals(10000, heap.size());
    }
    
    
    @Test(timeout=SECOND*10)
    public void testInsertWithManyRandomElements() {
        
        Random r = new Random();
        int min = 0;
        IPriorityQueue<Integer> heap = this.makeInstance();
        
        heap.insert(0);
        for (int i=100000; i>0; i--) {
            
            int v = r.nextInt();
            if (v < min) {
                min = v;
            }
            
            heap.insert(v);
        
        }
        
        assertEquals(min, heap.peekMin());
        assertEquals(100001, heap.size());
    }
    
    @Test
    public void testRemoveWithManyElementsInOrder() {
        IPriorityQueue<Integer> heap = null;
        
        heap = this.makeInstance();
        int n = 1000;
        for (int i=0; i<=n; i++) {
            
            heap.insert(i); 
 
            assertEquals(0, heap.peekMin());
        }
        
        for (int i=0; i<=n; i++) {
            int v = heap.removeMin();
            assertEquals(i, v);         
        }
        
        assertEquals(0, heap.size());
    }
    
    @Test
    public void testRemove() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        
        heap.insert(10);
        heap.insert(9);
        heap.insert(8);
        heap.insert(7);
        heap.insert(6);
        heap.insert(5);
        
        assertEquals(5, heap.removeMin());
        assertEquals(6, heap.peekMin());
        
        assertEquals(6, heap.removeMin());
        assertEquals(7, heap.peekMin());
        
        assertEquals(7, heap.removeMin());
        assertEquals(8, heap.peekMin());
        
        assertEquals(8, heap.removeMin());
        assertEquals(9, heap.peekMin());
        
        assertEquals(9, heap.removeMin());
        assertEquals(10, heap.peekMin());
        
        assertEquals(10, heap.removeMin());
        assertEquals(0, heap.size());
        assertTrue(heap.isEmpty());
    }
    
    
    @Test(timeout=SECOND)
    public void testChar(){
        IPriorityQueue<Character> heap = this.makeInstance();
        heap.insert('a');
        heap.insert('b');
        heap.insert('c');
        heap.insert('d');
        heap.insert('e');
        
        assertEquals('a', heap.removeMin());
        assertEquals('b', heap.removeMin());
        assertEquals('c', heap.removeMin());
        assertEquals('d', heap.removeMin());
        assertEquals('e', heap.removeMin());
        
    }
    
    
    
    
    @Test(timeout=SECOND)
    public void testString() {
        IPriorityQueue<String> heap = this.makeInstance();
        heap.insert("abcd");
        heap.insert("efgh");
        heap.insert("ijk");
        heap.insert("lmnopq");
        heap.insert("rstuv");
        heap.insert("wxyz");
        
        assertEquals("abcd", heap.removeMin());
        assertEquals("efgh", heap.removeMin());   
        assertEquals("ijk", heap.removeMin());   
        assertEquals("lmnopq", heap.removeMin());
        assertEquals("rstuv", heap.removeMin());
        assertEquals("wxyz", heap.removeMin());
    }
    
    @Test(timeout=SECOND)
    public void testEmptyString(){
        IPriorityQueue<String> heap = this.makeInstance();
        heap.insert("a");
        heap.insert("");
        heap.insert("b");
        assertEquals("", heap.removeMin());
        assertEquals("a", heap.removeMin());
        assertEquals("b", heap.removeMin());
        
    }
    
    @Test(timeout=SECOND)
    public void testCompareToJavaHeap(){
        PriorityQueue<Integer> javaHeap = new PriorityQueue<Integer>();
        IPriorityQueue<Integer> heap = this.makeInstance();
        int n = 2000;
        for (int i = 0; i < n; i++) {
            javaHeap.add(i);
            heap.insert(i);
        }
        
        for (int i = 0; i < n; i++) {
            int myHeapMin = heap.removeMin(); 
            int javaHeapMin = javaHeap.remove(); 

            assertEquals(myHeapMin, javaHeapMin);
            assertEquals(heap.size(), javaHeap.size());
        }       
    }
    
    @Test(timeout=SECOND)
    public void testMixedRemoveInsertCompareToJavaHeap(){
        PriorityQueue<Integer> javaHeap = new PriorityQueue<Integer>();
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(1);
        heap.insert(30);
        heap.insert(14);
        heap.insert(22);
        heap.insert(7);
        assertEquals(1, heap.removeMin());
        assertEquals(7, heap.removeMin());
        assertEquals(14, heap.removeMin());
        heap.insert(11);
        heap.insert(6);
        heap.insert(29);
        heap.insert(12);
        assertEquals(6, heap.removeMin());
        assertEquals(11, heap.removeMin());
        assertEquals(12, heap.removeMin());
        assertEquals(22, heap.removeMin());
        assertEquals(29, heap.removeMin());
        heap.insert(1);
        assertEquals(1, heap.removeMin());
        heap.insert(2);
        heap.insert(3);
        heap.insert(4);
        assertEquals(2, heap.removeMin());
        assertEquals(3, heap.removeMin());
        assertEquals(4, heap.removeMin());
        assertEquals(30, heap.removeMin());
        assertTrue(heap.isEmpty());
        
        javaHeap.add(1);
        javaHeap.add(30);
        javaHeap.add(14);
        javaHeap.add(22);
        javaHeap.add(7);
        assertEquals(1, javaHeap.remove());
        assertEquals(7, javaHeap.remove());
        assertEquals(14, javaHeap.remove());
        javaHeap.add(11);
        javaHeap.add(6);
        javaHeap.add(29);
        javaHeap.add(12);
        assertEquals(6, javaHeap.remove());
        assertEquals(11, javaHeap.remove());
        assertEquals(12, javaHeap.remove());
        assertEquals(22, javaHeap.remove());
        assertEquals(29, javaHeap.remove());
        javaHeap.add(1);
        assertEquals(1, javaHeap.remove());
        javaHeap.add(2);
        javaHeap.add(3);
        javaHeap.add(4);
        assertEquals(2, javaHeap.remove());
        assertEquals(3, javaHeap.remove());
        assertEquals(4, javaHeap.remove());
        assertEquals(30, javaHeap.remove());
        assertTrue(javaHeap.isEmpty());

    }
    
    @Test(timeout=SECOND)
    public void testRepeat(){
        
        IPriorityQueue<Integer> heap = this.makeInstance();

        heap.insert(1);
        heap.insert(1);
        heap.insert(1);
        heap.insert(1);
        heap.insert(2);
        heap.insert(2);
        assertEquals(1, heap.removeMin());
        assertEquals(1, heap.removeMin());
        assertEquals(1, heap.removeMin());
        assertEquals(1, heap.removeMin());
        assertEquals(2, heap.removeMin());
        assertEquals(2, heap.removeMin());
        
    }
    
    @Test(timeout=SECOND)
    public void testRemoveEmpty() {

        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.removeMin();
            fail("Expected EmptyContainerException");
        }
        catch (EmptyContainerException error) {
            //do nothing
        }
    }

    @Test(timeout=SECOND)
    public void testPeakEmpty() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.peekMin();
            fail("Expected EmptyContainerException");
        }
        catch (EmptyContainerException error) {
            //do nothing
        }
    }
    
    @Test(timeout=SECOND)
    public void testNullInsert() {

        IPriorityQueue<Integer> heap = this.makeInstance();
        
        try {
            heap.insert(null);
            fail("Expected an IllegalArgumentException");
        }
        catch (IllegalArgumentException error) {
            //do nothing
        }
    }
    
    @Test(timeout=SECOND)
    public void testNegative(){
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = -2; i<3; i++) {
            heap.insert(i);
            assertEquals(i+3, heap.size());
            
        }
        for (int i = -2; i<3; i++) {
            assertEquals(i, heap.removeMin());
            assertEquals(-i+2, heap.size());
        }
    }
}
