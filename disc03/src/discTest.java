import org.junit.Test;
import org.junit.Assert.*;

public class discTest {
    /** test discussion 3 problems with SLList, create a new SLList first */
    SLList s = new SLList(2);   // should be list with just 2

    @Test
    public void testAddRemove(){
        SLList s = new SLList(2);   // should be list with just 2
        s.addFirst(6);
        s.addFirst(5);

        int[] expected = new int[]{5, 6, 2};
        int[] actual = new int[3];

        for (int i = 0; i < 3; i++){
            actual[i] = s.removeFirst();
        }

        org.junit.Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testInsert(){
        SLList s = new SLList(2);   // should be list with just 2
        s.addFirst(6);
        s.addFirst(5);

        int[] expected = new int[]{5, 10, 6, 2};
        int[] actual = new int[4];

        s.insert(10, 1);

        for (int i = 0; i < 4; i++){
            actual[i] = s.removeFirst();
        }
        org.junit.Assert.assertArrayEquals(expected, actual);

        SLList s2 = new SLList(2);   // should be list with just 2
        s2.addFirst(6);
        s2.addFirst(5);

        int[] expected2 = new int[]{5, 6, 2, 10};
        int[] actual2 = new int[4];

        s2.insert(10, 7);

        for (int j = 0; j < 4; j++){
            actual2[j] = s2.removeFirst();
        }
        org.junit.Assert.assertArrayEquals(expected2, actual2);
    }

    @Test
    public void testReverse(){
        SLList s = new SLList(2);
        s.addFirst(6);
        s.addFirst(5);

        int[] expected = new int[]{2, 6, 5};
        int[] actual = new int[3];
        s.reverse();

        for (int i = 0; i < 3; i++){
            actual[i] = s.removeFirst();
        }
        org.junit.Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testArrayInsert(){
        int[] arr = new int[]{5, 9, 14, 15};
        int[] expected = new int[]{5, 9, 6, 14, 15};

        int[] actual = SLList.insert(arr, 6, 2);
        org.junit.Assert.assertArrayEquals(expected, actual);

    }

    @Test
    public void testArrayReverse(){
        int[] arr = new int[]{1, 2, 3, 4, 5};
        int[] expected = new int[]{5, 4, 3, 2, 1};
        SLList.reverse(arr);
        org.junit.Assert.assertArrayEquals(arr, expected);
    }

    @Test
    public void testReplicate(){
        int[] arr = new int[]{4, 3, 2, 1};
        int [] expected = new int[]{4, 4, 4, 4, 3, 3, 3, 2, 2, 1};

        int[] actual = SLList.replicate(arr);
        org.junit.Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten(){
        int[][] arr = new int[][]{{1, 2, 3}, {}, {7, 8}};
        int[] expected = new int[]{1, 2, 3, 7, 8};

        int[] actual = flatten.flatten(arr);
        org.junit.Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testSkip(){
        IntList arr = new IntList(10, null);
        for (int i = 9; i > 0; i -= 1){
            arr = new IntList(i, arr);
        }

        int[] expected = new int[]{1, 3, 6, 10};
        int[] actual = new int[4];

        arr.skippify();

        for (int j = 0; j < actual.length; j+=1){
            actual[j] = arr.get(j);
        }
        org.junit.Assert.assertArrayEquals(actual, expected);
    }

    @Test
    public void testDilsans(){
        IntList arr = new IntList(10, null);   // IntList (1, 2, 3, 4, ...)
        for (int i = 9; i > 0; i -= 1){
            arr = new IntList(i, arr);
        }
        int y = 5;
        int[] expected = new int[]{1, 2, 3, 4, 6, 7, 8, 9, 10};
        int[] actual = new int[9];

        IntList.dilsans(arr, y);

        for (int j = 0; j < actual.length; j+=1){
            actual[j] = arr.get(j);
        }
        org.junit.Assert.assertArrayEquals(actual, expected);
    }

}
