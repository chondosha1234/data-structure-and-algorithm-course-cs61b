package bearmaps.proj2ab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyTrieSet<Value> implements TrieSet61B{

    private class CharacterNode{
        HashMap<Character, CharacterNode> map;  //holds the character / pointer to next relationship
        boolean isKey;                          //Determines if this letter is the end of a word
        Value val;

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
     *doesn't have to be key, can be just a prefix
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

    /** method to return boolean if trie contains a full key */
    public boolean containsKey(String key) {
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
        if (curr.isKey){
            return true;
        }else {
            return false;
        }
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


    /** add method, but also add a value associated with the key */
    public void add(String key, Value val){
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
        current.val = val;
    }

    /** retrieve a value being stored at end of a key */
    public Value get(String key){
        if (key == null || key.length() < 1){
            return null;
        }
        CharacterNode current = root;
        for (int character = 0; character < key.length(); character++){
            char letter = key.charAt(character);
            if (current.map.containsKey(letter)) {
                current = current.map.get(letter);
            }else{
                return null;
            }
        }
        return current.val;
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

    public List<Value> valuesWithPrefix(String prefix){
        if (!contains(prefix)){
            return null;
        }
        CharacterNode curr = root;
        for (int c = 0; c < prefix.length(); c++){
            char letter = prefix.charAt(c);
            curr = curr.map.get(letter);
        }
        ArrayList<Value> sl = new ArrayList<>();
        collectValues(curr, sl);
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

    /** collect method, but for values instead of keys */
    private void collectValues(CharacterNode node, List<Value> list){
        if (node == null) return;
        if (node.isKey){
            list.add(node.val);
        }
        for (char c : node.map.keySet()){
            CharacterNode next = node.map.get(c);
            collectValues(next, list);
        }
    }

    @Override
    public String longestPrefixOf(String key) {
        return null;
    }
}
