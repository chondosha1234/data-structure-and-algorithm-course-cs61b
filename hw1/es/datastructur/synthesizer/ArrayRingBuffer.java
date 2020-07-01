package es.datastructur.synthesizer;
import java.util.Iterator;


public class ArrayRingBuffer<T> implements BoundedQueue<T>{
    /* Index for the next dequeue or peek. */
    private int first;
    /* Index for the next enqueue. */
    private int last;
    /* Variable for the fillCount. */
    private int fillCount;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        rb = (T[]) new Object[capacity];
        fillCount = 0;
        first = 0;
        last = 0;
    }

    /** return the capacity of ring buffer */
    @Override
    public int capacity() {
        return rb.length;
    }

    /** return the number of items in queue */
    @Override
    public int fillCount() {
        return fillCount;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow").
     */
    @Override
    public void enqueue(T x) {
        rb[last] = x;     // invariant: item added to end will be at position last (one after the prev value)
        fillCount += 1;
        last += 1;
        if (last == rb.length){    // if last gets to end of array, circle back to beginning
            last = 0;
        }
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T dequeue() {
        T value = rb[first];
        rb[first] = null;    // remove item from array
        fillCount -= 1;       // minus fillCount because item removed
        first += 1;
        if (first == rb.length){    //if you get to end of array, first will circle back to beginning
            first = 0;
        }
        return value;
    }

    /**
     * Return oldest (first) item, but don't remove it. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T peek() {
        return rb[first];
    }

    // TODO: When you get to part 4, implement the needed code to support
    //       iteration and equals.
}
    // TODO: Remove all comments that say TODO when you're done.
