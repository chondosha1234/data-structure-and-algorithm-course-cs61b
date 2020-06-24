/** Creates a DLList object and provides methods to add to front and back and remove
 * from front and back.  Uses arrays as core data structure
 * @param <genType>
 */
public class ArrayDeque<genType> implements Deque<genType>{

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
        front = 0;                      //front and back will both be the one item in list at creation
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
    @Override
    public void addFirst(genType item){
        if (size == items.length){              //checks to see if array needs to be made larger
            resize(size * 2);
        }
        items[items.length - size] = item;     // adds item to end of array, but it will be front of DLList
        front = items.length - size;            // change front to new item
        size += 1;
    }

    /** add element to end of list */
    @Override
    public void addLast(genType item){
        if (size == items.length){              //checks to see if array needs to be made larger
            resize(size * 2);
        }
        back += 1;
        items[back] = item;   // adds item to back end of list in array
        size += 1;
    }

    /** checks if usage ratio is low enough to resize   if R < 0.25 then array should half */
    public boolean checkRatio(){
        double s = size;
        double length = items.length;
        double R = s / length;
        return R < 0.25;
    }

    /** returns the size of array */
    @Override
    public int size(){
        return size;
    }

    public void resize(int capacity){
        genType[] copy = (genType[]) new Object[capacity];
        System.arraycopy(items, front, copy, 0, size);
        if (front > back){
            System.arraycopy(items, 0, copy, items.length - front, back + 1);
        }
        front = 0;
        back = size - 1;
        items = copy;
    }

    /** prints out the DLList with space between each element */
    @Override
    public void printDeque(){
        if (front != 0) {                                       //if front is not at index 0, need to print those first
            for (int i = front; i < items.length; i += 1) {
                System.out.print(items[i] + " ");
            }
        }
        for (int j = 0; j <= back; j += 1){
            System.out.print(items[j] + " ");
        }
        System.out.println();
    }

    /** remove first element of list and return value */
    @Override
    public genType removeFirst(){
        genType value = items[front];
        items[front] = null;
        front += 1;
        if (front == items.length){
            front = 0;
        }
        size -= 1;
        if (checkRatio() && !isEmpty()) {                         //check to see if need to make array smaller after removal
            int newSize = (items.length / 2);
            resize(newSize);
        }
        return value;
    }

    /** remove last element of list and return value */
    @Override
    public genType removeLast(){
        genType value = items[back];
        items[back] = null;
        back -= 1;
        if (back < 0){
            back = items.length - 1;
        }
        size -= 1;
        if (checkRatio() && !isEmpty()) {                         //check to see if need to make array smaller after removal
            int newSize = (items.length / 2);
            resize(newSize);
        }
        return value;
    }

    /** get value of element at index */
    @Override
    public genType get(int index){
        int distanceFromZero = items.length - front;     // distance of front item to end of array
        if (front == 0) {                               // if front is items[0], then just use basic indexing
            return items[index];
        }else if (index >= (distanceFromZero)){        // if the index is higher than the number of items at end of array
            return items[index - (distanceFromZero)];
        }else {
            return items[items.length - (distanceFromZero - index)];  // index is lower than number of items at end of array
        }
    }
}


