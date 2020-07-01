package es.datastructur.synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(10);
        int[] expected = new int[]{5, 10, 15};
        int[] actual = new int[3];
        arb.enqueue(5);
        arb.enqueue(10);
        arb.enqueue(15);

        for (int i = 0; i < 3; i++){
            actual[i] = arb.dequeue();
        }
        //test enqueue and dequeue methods
        assertArrayEquals(expected, actual);

        int fcExpected = 0;
        int fcActual = arb.fillCount();
        //test to make sure the fillCount is changed after dequeue
        assertEquals(fcExpected, fcActual);

        int x = 500;
        int y;
        arb.enqueue(500);
        y = arb.peek();
        //test to make sure peek works
        assertEquals(x, y);

        int count = 1;
        int actualCount = arb.fillCount();
        //test to make sure peek doesn't remove
        assertEquals(count, actualCount);
    }
}
