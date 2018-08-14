
import java.util.NoSuchElementException;
import java.util.EmptyStackException;

public class ListQueue<T> implements Queue<T> {

    private QueueNode bottom = null; // the beginning of the list
    private int size;

    // set a constructor for Queue with a value and a reference.
    private class QueueNode {
        public T value;
        public QueueNode next;

        public QueueNode(T value, QueueNode next) {
            this.value = value;
            this.next = next;
        }
    }
    
    @Override
    public void add(T item) {   
     
        // empty QueueNode store at the beginning
        QueueNode newNode = new QueueNode(item, null); 
        if (bottom==null) {
            bottom = newNode;
        } else {
        
            QueueNode current = this.bottom;
        
            while (current.next!=null) {
                current = current.next; // link to the next QueueNode
            }
            current.next = newNode;
        }
        this.size++;
    }

    @Override
    public T remove() throws NoSuchElementException {
        T temp = this.peek();
        this.bottom = this.bottom.next;
        this.size--;
        return temp;        
    }

    @Override
    public T peek() throws NoSuchElementException {
        if (isEmpty()){
            throw new NoSuchElementException();
        }  
        return bottom.value;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

}
