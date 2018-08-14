import java.util.Arrays;
import java.util.EmptyStackException;

public class ArrayStack<T> implements Stack<T> {
    // set private fields with initial number 10, and an object;
    private static final int CAPACITY = 10;
    private T[] arr = (T[]) new Object[CAPACITY];
    private int index = -1; //0 == empty
    
    @Override
    public void push(T item) {
        if (index==arr.length-1) {
            // make a double size for array if the array is over 10;
            T[] doubleArr = Arrays.copyOf(arr, arr.length * 2);
            arr = doubleArr;
        }
        arr[index + 1] = item;
        index++;
    }

    @Override
    public T pop() throws EmptyStackException {
        if (index==-1) {
            throw new EmptyStackException();
        }if (index<(arr.length/4)-1) {
            T[] halfArr = Arrays.copyOf(arr, arr.length / 2);
            arr = halfArr;            
        }
        T removeItem = arr[index];
        index--;
        
        return removeItem;
    }

    @Override
    public T peek() throws EmptyStackException {
        if (index==-1) {
            throw new EmptyStackException();
        }
        return arr[index];
    }

    @Override
    public int size() {
        if (index==-1) {
            return 0;
        } return index + 1;
    }

    @Override
    public boolean isEmpty() {
        return index == -1; // true
    }
}
