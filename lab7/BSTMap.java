
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

public class BSTMap<Key extends Comparable<Key>, Value> implements Map61B<Key, Value> {
    private TreeNode root;

    /** naked data structure for node in tree */
    private class TreeNode{
        private Key key;
        private Value val;
        private int size;           // number of nodes in subtree
        private TreeNode left, right;   //left and right subtrees

        public TreeNode(Key k, Value v, int s){
            key = k;
            val = v;
            size = s;
        }
    }

    /** constructor for BTSMap */
    public BSTMap(){
    }

    /** empties all keys and values from tree */
    @Override
    public void clear(){
        root = null;
    }

    /** returns true/false if Map contains the key K
     * uses get method to see if there is a return or not
     */
    @Override
    public boolean containsKey(Key k){
        if (k == null) throw new IllegalArgumentException("argument is null");
        else return get(k) != null;
    }

    /** finds and returns the value at key K */
    @Override
    public Value get(Key k){
        return get(root, k);
    }

    /** helper method which takes in a node from treemap */
    private Value get(TreeNode node, Key k){
        if (k == null) throw new IllegalArgumentException("argument is null");
        if (node == null) return null;
        int cmp = k.compareTo(node.key);
        if (cmp > 0){
            return get(node.right, k);
        }else if (cmp < 0){
            return get(node.left, k);
        }else {
            return node.val;
        }
    }

    /** return size of map, calls method which takes in TreeNode as a parameter */
    @Override
    public int size(){
        return size(root);
    }

    /** helper method which takes in a node of the tree map */
    private int size(TreeNode node){
        if (node == null){
            return 0;
        }else {
            return node.size;
        }
    }

    /** put a new key K and value V into map */
    @Override
    public void put(Key k, Value v){
        if (k == null) throw new IllegalArgumentException("argument is null");
        root = put(root, k, v);
    }

    /** helper method which takes in node of tree */
    private TreeNode put(TreeNode node, Key k, Value v){
        if (node == null) return new TreeNode(k, v, 1);
        int cmp = k.compareTo(node.key);
        if (cmp > 0){
            node.right = put(node.right, k, v);
        }else if (cmp < 0){
            node.left = put(node.left, k, v);
        }else {
            node.val = v;
        }
        node.size = 1 + size(node.right) + size(node.left);
        return node;
    }

    /** prints out content of BTSMap in order of keys */
    public void printInOrder(){
        printInOrder(root);
    }

    /** helper method which takes in node of tree */
    private void printInOrder(TreeNode node){
        if (node == null){
            return;
        }
        if (node.left == null){
            System.out.print(node.val + " ");
            return;
        }
        printInOrder(node.left);
        System.out.print(node.val + " ");
        printInOrder(node.right);
    }

    /** returns set view of keys in the map */
    @Override
    public Set<Key> keySet(){
        Set<Key> keyset = new HashSet<>();
        for (Key key : this){                   //BTSMap is an iterable and order of set doesn't matter
            keyset.add(key);
        }
        return keyset;
    }

    @Override
    public Value remove(Key k){
        if (!containsKey(k)){
            return null;
        }
        Value returnValue = get(k);
        root = remove(root, k);
        return returnValue;
    }

    @Override
    public Value remove(Key k, Value v){
        if (!containsKey(k)){
            return null;
        }
        return null;
    }

    /** first uses compare to find the correct node to remove
     * if the node has 0 or 1 children, then replace it with that child
     * if there are 2 children, replace it with the right child and reassgin left child to right child
     */
    private TreeNode remove(TreeNode node, Key k){
        if (node == null) {
            return null;
        }
        int cmp = k.compareTo(node.key);
        if (cmp < 0){
            node.left = remove(node.left, k);
        }else if (cmp > 0){
            node.right = remove(node.right, k);
        }else{
            if (node.right == null) return node.left;   //0 or 1 child
            if (node.left == null) return node.right;   // 0 or 1 child
            TreeNode temp = node;                       // temp points to the node to be removed
            node = min(temp.right);                     //reset pointer to the min of the right side
            node.right = deleteMin(temp.right);         //set pointer of new node to deleted node's right branch, minus min node
            node.left = temp.left;                  // set left child to be left child of new node
        }
        node.size = 1 + size(node.left) + size(node.right);
        return node;
    }

    /** finds the minimum value in the tree */
    private TreeNode min(TreeNode node){
        if (node.left == null) return node;
        else return min(node.left);
    }

    /** deletes the min key under this node */
    private TreeNode deleteMin(TreeNode node){
        if (node.left == null) return node.right;
        node.left = deleteMin(node.left);
        node.size = 1 + size(node.left) + size(node.right);
        return node;
    }

    /** iterator method returns a new iterator object from an iterator class */
    @Override
    public Iterator<Key> iterator() {
        return new BTSMapIterator(root);
    }

    /** creates iterator object for BTSMap */
    private class BTSMapIterator<Key> implements Iterator<Key>{

        /** stack to hold each node */
        private Stack<TreeNode> stack = new Stack<>();

        /** constructor for the iterator object */
        public BTSMapIterator(TreeNode root){
            pushToLeft(root);       //start at root
        }

        public boolean hasNext(){
            return !stack.empty();
        }

        public Key next(){
            TreeNode node = stack.pop();
            pushToLeft(node.right);
            return (Key) node.key;
        }

        /** pushes nodes into stack
         * will put root in then put each node going down to the left until the end
         * the next time it is called in NEXT(), it will have returned some values from the stack
         * and then call the method again on the right which will go down each branch as it continues up
         * the tree on one side   */
        private void pushToLeft(TreeNode node){
            if (node != null){
                stack.push(node);
                pushToLeft(node.left);
            }
        }
    }

}
