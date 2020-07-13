import org.junit.Test;
import static org.junit.Assert.*;

public class TestUnionFind {

    @Test
    public void testParent(){

    }

    @Test
    public void testFindPathComp(){
        UnionFind WQU = new UnionFind(10);
        WQU.union(1, 2);      // connected 1-2   (2 is root)
        WQU.union(3, 2);      //connected 3-2-1   (2 is root)
        WQU.union(4, 3);     // connected 4-3-2-1  (2 is root)
        WQU.union(9, 8);    //connected 9-8 (8 is root)
        int parentNine = WQU.parent(9);
        assertEquals(parentNine, 8);

        WQU.union(9, 4);    // connected root of 9 (8) to root of 4 (2) and 4 and 9 should also be compressed to 2
        int newParentFour = WQU.parent(4);
        int newParentNine = WQU.parent(9);
        assertEquals(newParentFour, 2);
        assertEquals(newParentNine, 2);

        assertEquals(WQU.parent(8), 2);
        assertEquals(WQU.sizeOf(4), 6);

    }


    @Test
    public void testUnion(){
        UnionFind WQU = new UnionFind(10);
        WQU.union(1, 2);
        WQU.union(3, 2);
        WQU.union(4, 3);            // at this point   1-2-3-4 connected
        int size4 = WQU.sizeOf(4);
        int size1 = WQU.sizeOf(1);
        int size5 = WQU.sizeOf(5);
        assertEquals(4, size4);
        assertEquals(size1, size4);
        assertEquals(1, size5);
        assertNotEquals(size1, size5);

        boolean c1 = WQU.connected(1, 2);
        boolean c2 = WQU.connected(1, 4);
        boolean c3 = WQU.connected(2, 6);
        assertTrue(c1);
        assertTrue(c2);
        assertFalse(c3);

    }
}
