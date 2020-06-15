/** Creates a DLList object and provides methods to add to front and back and remove
 * from front and back.  Uses arrays as core data structure
 * @param <genType>
 */
public class ArrayDeque<genType>{

    private genType[] items;    // creates array which will hold DLList
    private int size;           //size of DLList, not array
    private int front, back;     //integer for index of front or back of deque

    /** empty list constructor */
    public ArrayDeque(){
        items = (genType[]) new Object[0];     // creates pointer to empty array
        size = 0;
    }

    /** regular constructor */
    public ArrayDeque(genType i){
        items = (genType[]) new Object[8];
        items[0] = i;
        front = 0;
        back = 0;
        size = 1;
    }

    /** constructor to copy list to a new object */
    public ArrayDeque(ArrayDeque other){
        items = (genType[]) new Object[other.size];
        System.arraycopy(other, 0, items, 0, other.size);
        size = other.size;
        front = other.front;
        back = other.back;
    }

    /** add element to beginning of list */
    public void addFirst(genType item){
        if (size == items.length){
            resize(size * 2);
        }
        items[items.length - size] = item;     // adds item to end side of array, but it wil be front of DLList
        front = items.length - size;
        size += 1;

    }

    /** add element to end of list */
    public void addLast(genType item){
        if (size == items.length){
            resize(size * 2);
        }
        items[size] = item;
        back = size;
        size += 1;
    }

    /** return true/false if list is empty or not */
    public boolean isEmpty(){
        if (size() == 0){
            return true;
        }else{
            return false;
        }
    }

    /** returns the size of array */
    public int size(){
        return size;
    }

    public void resize(int capacity){
        genType[] copy = (genType[]) new Object[capacity];
        System.arraycopy(items, 0, copy, 0, size);
        items = copy;
    }

    /** prints out the DLList with space between each element */
    public void printDeque(){
        for (int i = 0; i < size; i += 1){
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }

    /** remove first element of list and return value */
    public genType removeFirst(){
        genType value = items[front];
        items[front] = null;
        size -= 1;
        front = items.length - size;
        return value;
    }

    /** remove last element of list and return value */
    public genType removeLast(){
        genType value = items[back];
        items[back] = null;
        size -= 1;
        back = size - 1;
        return value;
    }

    /** get value of element at index */
    public genType get(int index){
        return items[index];
    }
}


