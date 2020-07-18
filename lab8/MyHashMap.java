import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;


public class MyHashMap<K extends Comparable<K>, V> implements Map61B<K, V>{

    /** private class to create an object to hold key value pair in each index in map */
    private class Pair{
        private K key;
        private V value;

        public Pair(K key, V value){
            this.key = key;
            this.value = value;
        }
    }

    private HashSet<K> keySet = new HashSet<>();  //keeps track of keys for easy access for keySet method
    private ArrayList<Pair>[] hashes; // array which holds Lists, each list will have Pair objects which hold key/value pairs
    private int initialSize = 16;   //default initial size is 16 unless changed
    private double loadFactor = 0.75;  //default load factor unless changed

    public MyHashMap(){
        hashes = new ArrayList[initialSize];
        for (int i = 0; i < initialSize; i++){
            hashes[i] = new ArrayList<>();
        }

    }

    public MyHashMap(int is){
        initialSize = is;
        hashes = new ArrayList[initialSize];
        for (int i = 0; i < initialSize; i++){
            hashes[i] = new ArrayList<>();
        }
    }

    public MyHashMap(int is, double lf){
        initialSize = is;
        loadFactor = lf;
        hashes = new ArrayList[initialSize];
        for (int i = 0; i < initialSize; i++){
            hashes[i] = new ArrayList<>();
        }
    }

    /** if a resize is needed, create a new array of lists and put each element back into array
     * use the put method, because the index in new array may be different,
     * because M (number of buckets) is different
     */
    private void resize(){
        ArrayList<Pair>[] newHashes = new ArrayList[hashes.length * 2];
        ArrayList<Pair>[] copyHashes = hashes;
        for (int i = 0; i < newHashes.length; i++){
            newHashes[i] = new ArrayList<>();
        }

        hashes = newHashes;
        for (ArrayList<Pair> list : copyHashes){
            for (Pair pair : list){
                put(pair.key, pair.value);
            }
        }
    }

    /** determine if the load factor is exceeded
     * takes the number of items N (size) divided by number of buckets M (length) and
     * compares to the load factor
     */
    private boolean checkLoadFactor(){
        return size()/hashes.length > loadFactor;
    }
    
    @Override
    public void clear() {
        for (ArrayList list : hashes){
            list = null;
        }
    }

    @Override
    public boolean containsKey(K key) {
        int hash = (key.hashCode() & 0x7FFFFFFF) % hashes.length;       //get hashcode of the key
        if (hashes[hash] == null){     //if that index is null it is not in map
            return false;
        }
        for (Pair p : hashes[hash]){  //if there are element in list then check each key and compare to search key
            if (p.key == key){
                return true;
            }
        }
        return false;    //if it is not found, return false
    }

    @Override
    public V get(K key) {
        int hash = (key.hashCode() & 0x7FFFFFFF) % hashes.length;
        if (hashes[hash] == null){
            return null;
        }
        for (Pair p : hashes[hash]){
            if (p.key == key){
                return p.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        int total = 0;
        for (ArrayList list : hashes){
            total += list.size();
        }
        return total;
    }

    @Override
    public void put(K key, V value) {
        if (checkLoadFactor()) {      //check to see if resize is needed
            resize();
        }

        int hash = (key.hashCode() & 0x7FFFFFFF) % hashes.length;
        if (containsKey(key)){          //check if key already in the map
            if (value == get(key)){    //if it has the same value, pass
                return;
            }else{
                for (Pair p : hashes[hash]){   //if it is a new value, find the exact key and replace
                    if (p.key == key){
                        p.value = value;
                    }
                }
            }
        }else {
            Pair p = new Pair(key, value);   //if its new key, create a pair and add it to that hash index
            hashes[hash].add(p);
            keySet.add(key);         //add key to keySet for later reference
        }

    }

    @Override
    public Set<K> keySet() {
        return keySet;
    }

    @Override
    public V remove(K key) {
        return (V) new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        return (V) new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return keySet.iterator();
    }

}
