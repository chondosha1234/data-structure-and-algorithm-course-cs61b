import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();
    static CharacterComparator offByFive = new OffByN(5);

    @Test
    public void testEqualChars(){
        char x = 'a';
        char y = 'b';
        char z = 't';
        assertTrue(offByOne.equalChars(x, y));
        assertFalse(offByOne.equalChars(x, z));
        assertFalse(offByOne.equalChars(y, z));
    }

    @Test
    public void testOffByN(){
        assertTrue(offByFive.equalChars('a', 'f'));
        assertTrue(offByFive.equalChars('f', 'a'));
        assertFalse(offByFive.equalChars('a', 'b'));
        assertFalse(offByFive.equalChars('a', 'a'));
    }
}