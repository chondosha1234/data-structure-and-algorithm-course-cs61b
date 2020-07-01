package es.datastructur.synthesizer;

/** creates interface for a bounded queue.  Can only add items to the end, and can only remove items
 * from the front. has a max capacity and cannot add items once it is full.
 * @param <T>
 */
public interface BoundedQueue<T> {

    int capacity(); //return size of buffer
    int fillCount(); //return number of items in buffer
    void enqueue(T x); //add item to end of queue
    T dequeue();    //delete and return item from front
    T peek();     // return but not delete item from front

    /** check if buffer is empty   ( fillCount == 0 ) */
    default boolean isEmpty(){
        return fillCount() == 0;
    }

    /** check if buffer is full  ( fillCount == capacity ) */
    default boolean isFull(){
        return fillCount() == capacity();
    }
}
