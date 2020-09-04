package bearmaps.proj2ab;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestTrie {

    @Test
    public void testAddGet(){
        MyTrieSet<Integer> trie = new MyTrieSet<>();
        trie.add("Hello", 1);
        trie.add("Goodbye", 256);
        trie.add("nooooo", 54);
        assertTrue(trie.contains("Hello"));
        assertTrue(trie.contains("Goodbye"));
        assertFalse(trie.contains("bulldog"));

        int expected = 256;
        int actual = trie.get("Goodbye");
        assertEquals(actual, expected);
    }

    @Test
    public void testCollectValues(){
        MyTrieSet<Integer> trie = new MyTrieSet<>();
        trie.add("dog", 23);
        trie.add("cat", 43);
        trie.add("fish", 67);
        trie.add("cancel", 12);
        trie.add("calf", 4);

        ArrayList<Integer> actual = (ArrayList) trie.valuesWithPrefix("ca");
        assertTrue(actual.contains(43));
        assertTrue(actual.contains(12));
        assertTrue(actual.contains(4));

        ArrayList<Integer> actual2 = (ArrayList) trie.valuesWithPrefix("fish");
        assertTrue(actual2.contains(67));

    }

    @Test
    public void testGet(){
        MyTrieSet<List<String>> trie = new MyTrieSet<>();
        ArrayList<String> lst = new ArrayList<>();
        lst.add("hello");
        lst.add("goodbye");
        lst.add("fuck");
        trie.add("fuck", lst);
        ArrayList<String> lstTwo = (ArrayList) trie.get("fuck");
        assertEquals(lst, lstTwo);

        if (trie.contains("fuck")){
            ArrayList<String> copy = (ArrayList) trie.get("fuck");
            copy.add("yay");
        }else {
            ArrayList<String> newlist = new ArrayList<>();
            newlist.add("yay");
            trie.add("yay", newlist);
        }

        assertTrue(trie.get("fuck").contains("yay"));
    }

}
