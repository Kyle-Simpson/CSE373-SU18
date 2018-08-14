package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods, see the source
 * code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        Node<T> newNode = new Node<>(item);
        if (this.size() == 0) {
            front = newNode;
        } else {
            back.next = newNode;
        }
        newNode.prev = back;
        back = newNode;
        this.size++;
    }

    @Override
    public T remove() {
        if (this.size == 0) {
            throw new EmptyContainerException();
        }
        T temp = back.data;
        if (front.next == null) {
            // if there is only one node
            front = null;
        } else {
            back.prev.next = null;
        }
        back = back.prev;
        this.size--;
        return temp;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> pointer = movePointerHelper(index);
        if (index == 0) {
            return front.data;
        } else if (index == size - 1) {
            return back.data;
        }
        return pointer.data;
    }
    
    @Override
    public void set(int index, T item) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> pointer = movePointerHelper(index); // move pointer to the index
        Node<T> newNode = new Node<>(item);
        if (index == 0) {
            if (size == 1) {
                front = newNode;
                back = front;
                return;
            } 
            newNode.next = front.next;
            front.next.prev = newNode;
            front = newNode;
            return;
        } else if (index == size - 1) {
            newNode.prev = back.prev;
            back.prev.next = newNode;
            back = newNode;
            return;
        } else if (index == size) {
            throw new IndexOutOfBoundsException();
        }
        newNode.prev = pointer.prev;
        newNode.next = pointer.next;
        pointer.prev.next = newNode;
        pointer.next.prev = newNode;
        return;

    }

    @Override
    public void insert(int index, T item) {
        if (index < 0 || index >= size + 1) {
            throw new IndexOutOfBoundsException();
        }
        // pointer moves to the place will be inserted new node
        // and it might be size
        Node<T> newNode = new Node<>(item);
        if (index == size) {
            this.add(item); // note if index and size are zero, it also works
            return;
        } else if (index == 0) {
            newNode.next = front;
            front.prev = newNode;
            front = newNode;
            size++;
            return;
        }
        Node<T> pointer = movePointerHelper(index);
        newNode.next = pointer;
        newNode.prev = pointer.prev;
        pointer.prev.next = newNode;
        pointer.prev = newNode;
        size++;
        return;
    }

    /**
     * This method helps move pointer more efficiently If index is close to front,
     * then pointer moves from front Otherwise, it moves from back.
     * 
     * @param index
     * @return
     */
    public Node<T> movePointerHelper(int index) {
        // move pointer to the place right on the index
        if (index < size / 2) {
            int i = 0;
            Node<T> current = front;
            while (current != null) {
                if (i == index) {
                    return current;
                }
                current = current.next;
                i++;
            }
        } else if (index >= size / 2) {  // contains case that index is 0 and size is 1
            int i = size - 1;
            Node<T> current = back;
            while (current != null) {
                if (i == index) {
                    return current;
                }
                current = current.prev;
                i--;
            }
        }
        return null;
    }

    @Override
    public T delete(int index) {
        // if index is out of bound
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        // if the node is at front or back
        if (index == 0) {
            T temp = front.data;
            // if there is only one node left
            if (size == 1) {
                front = null;
                back = null;
                size--;
                return temp;
            }
            front.next.prev = null;
            front = front.next;
            size--;
            return temp;
        } else if (index == size - 1) {
            T temp = back.data;
            back.prev.next = null;
            back = back.prev;
            size--;
            return temp;
        }
        // else move the cursor 
        Node<T> pointer = movePointerHelper(index);
        T temp = pointer.data;
        pointer.prev.next = pointer.next;
        pointer.next.prev = pointer.prev;
        size--;
        return temp;
    }

    @Override
    public int indexOf(T item) {
        Node<T> current = front;
        // check the first two
        // but what if out of index
        int index = 0;
        while (index <= size - 1) {
            if (current.data == item) { // if item and current is null
                return index;
            } else if (current.data != null && current.data.equals(item)) {
                return index;
            } else {
                current = current.next;
                index++;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean contains(T other) {
        return !(this.indexOf(other) == -1);
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at; returns 'false'
         * otherwise.
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the iterator to
         * advance one element forward.
         *
         * @throws NoSuchElementException
         *             if we have reached the end of the iteration and there are no more
         *             elements to look at.
         */
        public T next() {
            // if next is null
            if (current == null) {
                throw new NoSuchElementException();
            }
            T temp = current.data;
            current = current.next;
            return temp;
        }
    }
}
