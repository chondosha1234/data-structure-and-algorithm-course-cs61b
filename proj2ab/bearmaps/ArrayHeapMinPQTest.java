package bearmaps;

import org.junit.Test;
import static org.junit.Assert.*;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.StdRandom;

public class ArrayHeapMinPQTest {

    /** tests basics of initializing object, add, size, removeSmallest
     * which implies that swim, sink methods work */
    @Test
    public void basicTest(){
        ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();
        assertEquals(0, heap.size());
        heap.add(1, 1.0);
        heap.add(10, 2.0);
        heap.add(5, 3.0);
        assertEquals(3, heap.size());
        int first = heap.removeSmallest();
        assertEquals(5, first);
        int second = heap.removeSmallest();
        assertEquals(10, second);
        assertEquals(1, heap.size());
    }

    @Test
    public void testContains(){
        ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();
        heap.add(10, 1.0);
        assertTrue(heap.contains(10));
        assertFalse(heap.contains(25));
        heap.add(100, 2.0);
        heap.add(20, 1.5);
        heap.add(43, 1.3);
        heap.add(87, 1.2);
        assertTrue(heap.contains(20));
        assertTrue(heap.contains(87));
    }

    @Test
    public void testChangePriority(){
        ArrayHeapMinPQ<String> heap = new ArrayHeapMinPQ<>();
        heap.add("hello", 1.0);
        heap.add("goodbye", 2.0);
        heap.add("wait", 3.0);     //should be at top of the heap
        heap.changePriority("goodbye", 4.0);  //now hello should be top of heap
        String top = heap.removeSmallest();
        assertEquals(top, "goodbye");
    }

    /** timing tests */
    public static void main(String[] args){
        ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();
        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < 100000; i += 1) {
            heap.add(i, StdRandom.uniform());
            if (i > 10) {
               /* if (i % 3 == 0) {
                    heap.removeSmallest();
                }*/
                if (i % 7 == 0) {
                    heap.changePriority(StdRandom.uniform(i), StdRandom.uniform());
                }
            }
        }
        System.out.println("Total time elapsed: " + sw.elapsedTime() +  " seconds.");

        NaiveMinPQ<Integer> heap2 = new NaiveMinPQ<>();
        Stopwatch sw2 = new Stopwatch();
        for (int i = 0; i < 100000; i += 1) {
            heap2.add(i, StdRandom.uniform());
            if (i > 10) {
                /*if (i % 3 == 0) {
                    heap2.removeSmallest();
                } */
                if (i % 7 == 0) {
                    heap2.changePriority(StdRandom.uniform(i), StdRandom.uniform());
                }
            }
        }
        System.out.println("Total time elapsed: " + sw2.elapsedTime() +  " seconds.");

    }

}
