package datastructures.concrete;

import datastructures.interfaces.IDisjointSet;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;

/**
 * See IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private int[] pointers = null; // Initialize
    private IDictionary<T, Integer> setMap = new ChainedHashDictionary<>();

    // However, feel free to add more methods and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.

    @SuppressWarnings("unchecked")
    public ArrayDisjointSet() {
        pointers = new int[0]; // initialize the size
    }

    @Override
    public void makeSet(T item) {
        int[] newPointers = new int[this.setMap.size() + 1];
        // store the pointers in one set
        System.arraycopy(pointers, 0, newPointers, 0, this.setMap.size());
        // set the values to the set
        pointers = newPointers;
        // only one number in set, no other pointers
        pointers[setMap.size()] = -1;
        setMap.put(item, setMap.size());
    }

    @Override
    public int findSet(T item) {
        if (!setMap.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        return find(setMap.get(item));
    }

    // get the pointer position
    private int find(int index) {
        if (pointers[index] < 0) {
            return index;
        }
        return find(pointers[index]); // set itself
    }

    @Override
    public void union(T item1, T item2) {
        if (!setMap.containsKey(item1)) {
            throw new IllegalArgumentException("Not contains " + item1);
        }

        if (!setMap.containsKey(item2)) {
            throw new IllegalArgumentException("Not contains " + item2);
        }
        // reset two groups
        int repItem1 = findSet(item1);
        int repItem2 = findSet(item2);

        if (repItem1 == repItem2) {
            throw new IllegalArgumentException(item1 + " and " + item2 + "belongs to same set!");
        }
        if (size(repItem1) >= size(repItem2)) {
            // update the size and pointer values
            int temp = size(repItem2);
            pointers[repItem2] = repItem1;
            updateSize(repItem1, temp);
        } else { // size(repItem1) < size(repItem2)
            int temp = size(repItem1);
            pointers[repItem1] = repItem2;
            updateSize(repItem2, temp);
        }
    }

    private int size(int index) {
        if (!(pointers[index] < 0)) {
            throw new IllegalArgumentException(String.format("index: %d, pointer: %d", index, pointers[index]));
        }

        return -1 * pointers[index]; // turn back the initial size
    }

    // resize sets
    private int updateSize(int index, int additionalSize) {
        if (!(pointers[index] < 0)) {
            throw new IllegalArgumentException(String.format("index: %d, pointer: %d", index, pointers[index]));
        }

        pointers[index] -= additionalSize;

        return pointers[index];
    }
}
