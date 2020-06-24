import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);

        Deque d2 = palindrome.wordToDequeArray("persiflage");
        String actual2 = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual2 += d2.removeFirst();
        }
        assertEquals("persiflage", actual2);
    }

    @Test
    public void testIsPalindrome(){
        assertTrue(palindrome.isPalindrome("noon"));
        assertTrue(palindrome.isPalindrome("dad"));
        assertFalse(palindrome.isPalindrome("fat"));
        assertFalse(palindrome.isPalindrome("intelligent"));

        // some corner cases
        assertTrue(palindrome.isPalindrome("a"));
        assertTrue(palindrome.isPalindrome(""));

        //Test new Palindrome method
        assertTrue(palindrome.isPalindrome("flake", new OffByOne()));  //Takes character comparator object
        assertFalse(palindrome.isPalindrome("noon", new OffByOne()));
        assertFalse(palindrome.isPalindrome("told", new OffByOne()));
        assertTrue(palindrome.isPalindrome("cab", new OffByOne()));
        assertTrue(palindrome.isPalindrome("a", new OffByOne()));
    }


}