package datastructures.concrete.dictionaries;

import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;
    int capacity;
    int size;
    // You're encouraged to add extra fields (and helper methods) though!

    public ArrayDictionary() {
        capacity = 10;
        size = 0;
        pairs = makeArrayOfPairs(capacity);
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }

    @Override
    public V get(K key) {
        for (int i = 0; i < size; i++) {
            Pair<K, V> pair = pairs[i];
            if (pair.key == key || pairs[i].key.equals(key)) {
                return pairs[i].value;
            }
        }
        throw new NoSuchKeyException();
    }

    @Override
    public void put(K key, V value) {
        Pair<K, V> newPair = new Pair<>(key, value);
        // it contains key
        for (int i = 0; i < size; i++) {
            
            if (pairs[i].key == key || (pairs[i].key != null && pairs[i].key.equals(key))) {
                pairs[i] = newPair;
                return;
            }
        }

        // ensureCapacity
        this.ensureCapacity();

        // array doesn't contain key
        pairs[size] = new Pair<>(key, value);
        size++;

    }

    public void ensureCapacity() {
        if (this.size >= capacity) {
            capacity *= 2;
            Pair<K, V>[] newPairs = makeArrayOfPairs(capacity);
            // copy elements to the new dic 
            for (int i = 0; i < size; i++) {
                newPairs[i] = pairs[i];
            }
            pairs = newPairs;
        }
    }

    @Override
    public V remove(K key) {
        // the problem is it cannot access the pairs(real pairs)
        // it contains key
        // if there is no such a key
        for (int i = 0; i < size; i++) {
            if (pairs[i].key == key || pairs[i].key.equals(key)) {
                V temp = pairs[i].value;
                // move the last element to the front and reduce the size in order to eliminate
                // it implicitly removes by reducing the size
                pairs[i] = pairs[size - 1];
                pairs[size - 1] = null;
                size--;
                return temp;
            }
        }
        throw new NoSuchKeyException();
    }

    @Override
    public boolean containsKey(K key) {

        for (int i = 0; i < size; i++) {
            if (pairs[i].key == key || (pairs[i].key != null && pairs[i].key.equals(key))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return this.size;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;
        
        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        return new ArrayDictionaryIterator<>(this.pairs, this.size()); // need to add the value in the dictionary
    }

    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        
        private Pair<K, V>[] pair;
        private int index;
        private int size;

        public ArrayDictionaryIterator(Pair<K, V>[] pairDict, int size) {
            this.pair = pairDict;
            this.size = size;
            this.index = 0;
        }

        public boolean hasNext() {
            
            if (index >= size) {
                return false;
            }
            
            return !(pair[index] == null);
        }

        public KVPair<K, V> next() {
            // first check if there is next space to move cursor
            // return current cursor
            // move cursor to the next index(it just increments index)
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            
            Pair<K, V> tempPair = pair[index];
            KVPair<K, V> tempKVpair = new KVPair<K, V>(tempPair.key, tempPair.value);
            index++;
            
            return tempKVpair;
        }
    }
}
