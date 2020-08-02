import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyTrieSet implements TrieSet61B{

    private class CharacterNode{
        HashMap<Character, CharacterNode> map;  //holds the character / pointer to next relationship
        boolean isKey;                          //Determines if this letter is the end of a word

        public CharacterNode(boolean isKey){
            this.isKey = isKey;
            map = new HashMap<>();
        }
    }

    private CharacterNode root;  //root node will be blank
    private static final boolean ENDKEY = true;

    /** constructor for TrieSet class */
    public MyTrieSet(){
        root = new CharacterNode(!ENDKEY);
    }

    @Override
    public void clear() {
        root = new CharacterNode(!ENDKEY);
    }

    /** returns true/false if trie contains a given string
     *
     * @param key
     */
    @Override
    public boolean contains(String key) {
        if (key == null || key.length() < 1) {
            return false;
        }
        CharacterNode curr = root;
        for (int c = 0; c < key.length(); c++){
            char letter = key.charAt(c);
            if (!curr.map.containsKey(letter)){
                return false;
            }
            curr = curr.map.get(letter);
        }
        return true;
    }

    /** add a new key (string) to the trie
     *
     * @param key
     */
    @Override
    public void add(String key) {
        if (key == null || key.length() < 1) {
            return;
        }
        CharacterNode current = root;
        for (int character = 0; character < key.length(); character++) {
            char letter = key.charAt(character);
            if (current.map.containsKey(letter)) {
                current = current.map.get(letter);
            }else{
                CharacterNode next = new CharacterNode(!ENDKEY);
                current.map.put(letter, next);
                current = next;
            }
        }
        current.isKey = ENDKEY;
    }

    /** returns a list of Strings that all begin with the given prefix
     *
     */
    @Override
    public List<String> keysWithPrefix(String prefix) {
        if (!contains(prefix)){
            return null;
        }
        CharacterNode curr = root;
        for (int c = 0; c < prefix.length(); c++){
            char letter = prefix.charAt(c);
            curr = curr.map.get(letter);
        }
        ArrayList<String> sl = new ArrayList<>();
        collect(curr, prefix, sl);
        return sl;
    }

    /** method to collect keys in Trie into a list of strings
     * used for keysWithPrefix method, and also just keys method
     */
    private void collect(CharacterNode node, String pre, List<String> list){
        if (node == null) return;
        if (node.isKey){
            list.add(pre);
        }
        for (char c : node.map.keySet()) {
            CharacterNode next = node.map.get(c);
            collect(next, pre + c, list);
        }

    }

    @Override
    public String longestPrefixOf(String key) {
        return null;
    }
}
