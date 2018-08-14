package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;

    // You're encouraged to add extra fields (and helper methods) though!
    private int capacity; // internal size
    private int pairNumber; // number of key

    public ChainedHashDictionary() {
        chains = makeArrayOfChains(10);
        capacity = chains.length;
        pairNumber = 0;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    @Override
    public V get(K key) {
        int hashedIndex = hash(key);
        // if arr on the chain is not null
        if (chains[hashedIndex] != null && chains[hashedIndex].containsKey(key)) {
            return chains[hashedIndex].get(key);
        } else {
            // the arr on the chain is null
            throw new NoSuchKeyException("Cannot find the key " + key + " at " + hashedIndex);
        }
    }

    @Override
    public void put(K key, V value) {
        // Process key
        int hashedIndex;

        if (key == null) {
            hashedIndex = 0;
        } else {
            hashedIndex = hash(key);
        }
        // if the array dictionary is null
        if (chains[hashedIndex] == null) {
            chains[hashedIndex] = new ArrayDictionary<K, V>();
            chains[hashedIndex].put(key, value);
            pairNumber++;
            return;
        }
        // if there is already the key in the array dictionary
        if (chains[hashedIndex].containsKey(key)) {
            // replace the old value
            chains[hashedIndex].put(key, value);
            // size doesn't change
            return;
        }
        chains[hashedIndex].put(key, value);
        pairNumber++;

        // overload check after putting will reduce the runtime
        processOverload();
    }

    public void processOverload() {
        double overLoadFactor = 1.0; // it means it needs 100 elements in a basic size 10 array
        // dictionary to have a resize
        double sizeCapRatio = pairNumber * 1.0 / capacity;
        if (sizeCapRatio > overLoadFactor) {
            resize();
        }
    }

    public void resize() {
        int originalCapacity = capacity;
        capacity *= 2;
        IDictionary<K, V>[] newChains = makeArrayOfChains(capacity);
        // we better use iterator here
        // but iterator will change its position
        int index = 0;
        while (index < originalCapacity) {
            if (chains[index] == null) {
                index++;
            } else {
                Iterator<KVPair<K, V>> ite = chains[index].iterator();
                while (ite.hasNext()) {
                    // deep copy through the array dictionary
                    KVPair<K, V> pair = ite.next();
                    K key = pair.getKey();
                    V value = pair.getValue();
                    int hashedIndex = hash(key);
                    // if key is null, no difference
                    if (newChains[hashedIndex] == null) {
                        newChains[hashedIndex] = new ArrayDictionary<K, V>();
                    }
                    newChains[hashedIndex].put(key, value);
                }
                // after deep copy through the array dictionary
                index++;
            }
        }
        chains = newChains; // make a reference to the new chain
    }

    public int hash(K key) {
        // if key is null, then return the default zero.
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % capacity);
    }

    @Override
    public V remove(K key) {
        // search for the index
        int hashedIndex = hash(key);
        // search for the pair in this index
        if (chains[hashedIndex] != null && chains[hashedIndex].containsKey(key)) {
            pairNumber--;
            return chains[hashedIndex].remove(key);
        }
        throw new NoSuchKeyException("Remove Failure: No such key");

    }

    @Override
    public boolean containsKey(K key) {
        int hashedIndex = hash(key);
        // search for the pair in this index
        if (chains[hashedIndex] != null && chains[hashedIndex].containsKey(key)) {
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return pairNumber;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration state. You can
     * add as many fields as you want. If it helps, our reference implementation
     * uses three (including the one we gave you).
     *
     * 2. Before you try and write code, try designing an algorithm using pencil and
     * paper and run through a few examples by hand.
     *
     * We STRONGLY recommend you spend some time doing this before coding. Getting
     * the invariants correct can be tricky, and running through your proposed
     * algorithm using pencil and paper is a good way of helping you iron them out.
     *
     * 3. Think about what exactly your *invariants* are. As a reminder, an
     * *invariant* is something that must *always* be true once the constructor is
     * done setting up the class AND must *always* be true both before and after you
     * call any method in your class.
     *
     * Once you've decided, write them down in a comment somewhere to help you
     * remember.
     *
     * You may also find it useful to write a helper method that checks your
     * invariants and throws an exception if they're violated. You can then call
     * this helper method at the start and end of each method if you're running into
     * issues while debugging.
     *
     * (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators are meant to be
     * lightweight and so should not be copying the data contained in your
     * dictionary to some other data structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary instance
     * inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        int index;
        Iterator<KVPair<K, V>> iterator = null; // iterator of KVPair

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            index = 0; // chain pointer while checking hasNext()
            iterator = null;
        }

        @Override
        public boolean hasNext() {
            while (index < chains.length) {
                if (chains[index] == null) {
                    index++;
                }
                else if (chains[index] != null && iterator == null) {
                    iterator = chains[index].iterator();
                    if (chains[index] != null && iterator.hasNext()) {
                        return true;
                    } else if (chains[index] != null && !iterator.hasNext()) {
                        iterator = null;
                        index++;
                    }
                }
                else if (chains[index] != null && iterator != null) {
                    if (chains[index] != null && iterator.hasNext()) {
                        return true;
                    } else if (chains[index] != null && !iterator.hasNext()) {
                        iterator = null;
                        index++;
                    }
                }
            }
            return false;
        }

        @Override
        public KVPair<K, V> next() {
            // Descrption of next()
            // 1. return the current pair
            // 2. move the cursor to the next pair
            // if ite == null -> initial status, return null and move the cursor to the next
            // if ite != null -> return ite.next() then move the cursor to the next
            // if it doesn't have available next()
            if (hasNext()) {
                return iterator.next();
            } else {
                throw new NoSuchElementException("Iterator cannot find the next");
            }
        }
    }
}
