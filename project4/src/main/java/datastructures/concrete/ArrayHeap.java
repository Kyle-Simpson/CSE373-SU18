package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    // Feel free to add more fields and constants.
    
    // Set an initial array and the size number in field
    private T[] heap;
    private int size;

    public ArrayHeap() {
        size = 0;
        heap = makeArrayOfT(10); // initialize the size as 10
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int heapSize) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[heapSize]);
    }

    @Override
    public T removeMin() {
     
        if (heap[0] == null) {
            throw new EmptyContainerException("Empty container exception: heap is empty!");
        }
        // when the heap is not null, check if there's a minimum number, and let the last position empty
        // since we need to remove a minimum number
        T result = peekMin();
        heap[0]  = heap[this.size-1];
        heap[this.size-1] = null;
        this.size--;
        
        // percolate down
        int index = 0;
        boolean found = false;
        // System.out.println(heap[index]); // to debug
        
        while (!found && hasChild(index)) {
            // we assume the minimum child is the first child.
            int minChild = firstChild(index);
            // find smallest child
            int begin = minChild+1;
            int end = minChild+4;
            for (int i = begin; i<end; i++) {
                
                if (heap[i] == null) {
                    break; // do nothing, stop
                }
                
                // i <= this.size
                if (heap[i].compareTo(heap[minChild]) < 0) {
                    minChild = i;
                }
            }
            // compare the position and swap the position if it find the minimum number
            if (heap[index].compareTo(heap[minChild]) > 0) {
                swap(index, minChild);
                index = minChild;
                
            }
            else {
                found = true; //found the right location; stop the loop
            }
        }
        
        return result;
    }
    
    @Override
    public T peekMin() {
        if (this.size == 0) {
            throw new EmptyContainerException("The heap is empty!");
        }
        return heap[0];
    }

    @Override
    public void insert(T item) {
        
        if (item == null) {
            throw new IllegalArgumentException("The item cannot be null!");
        }
        ensureCapacity();
        int index = size;
        heap[index] = item;
        
        boolean found = false;
        
        while (!found && hasParent(index)) {
            int parent = parent(index);
            
            if (heap[index].compareTo(heap[parent]) <=0) {
                // child smaller than parent
                swap(index, parent);
                index = parent(index);
            }
            else {
                found = true; // found it. Stop the loop
            }
        }    
        size++;
    }

    @Override
    public int size() {
        return this.size;
    }
    
    // get the position of parent
    private int parent(int index) {
        return (index - 1) / NUM_CHILDREN;
    }
    
    private boolean hasParent(int index) {        
        return index > 0;
    }
    
    // get the first child position
    private int firstChild(int index) {        
        return index * NUM_CHILDREN +1;
    }
    // if or not contain any children
    private boolean hasChild(int index) {       
        return firstChild(index) < this.size;
       
    }
    
    private void ensureCapacity() {
        if (this.size == this.heap.length) {
            int capacity = this.heap.length * 2;
            T[] newHeap = makeArrayOfT(capacity);
            // copy elements to the new dic 
            for (int i = 0; i < size; i++) {
                newHeap[i] = heap[i];
            }
            heap = newHeap;
        }
    }
    
    private void swap(int index1, int index2) {       
        T temp = heap[index1];
        heap[index1] = heap[index2];
        heap[index2] = temp;
    } 
}
