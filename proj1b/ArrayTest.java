import org.junit.Test;
import static org.junit.Assert.*;

/** class to help test array Deque */
public class ArrayTest {

    /** test the add first method and remove first method.  It should add item to end of array, but it should
     * be the first item in the Deque.  When it removes first it should return the items in order
     * from the Deque, not necessarily in order from the array
     * Array could looks like  {1, null, null, null, null, 3, 2} and it should return them 3, 2, 1
     */
    @Test
    public void addFirstTest(){
        ArrayDeque<Integer> AList = new ArrayDeque<>(1);
        int[] expected = new int[]{3, 2, 1};
        int[] actual = new int[3];

        AList.addFirst(2);
        AList.addFirst(3);

        for (int i = 0; i < 3; i+=1){
            actual[i] = AList.removeFirst();
        }
        org.junit.Assert.assertArrayEquals(expected, actual);
    }

    /** Test add and remove methods with focus on when values are at the ends of arrays
     * when you remove last and it is at the front of array (index 0), the next remove last
     * should be the value at last position in array
     */
    @Test
    public void removeLastTest(){

    }

}
