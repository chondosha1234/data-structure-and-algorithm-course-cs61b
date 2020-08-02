package bearmaps.proj2ab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {

    /** class for node in heap, has 2 instance variables for key and priority */
    private class Node{
        T key;
        double priority;
        int index;       //keep track of node index, to avoid linear search time

        public Node(T key, double priority){
            this.key = key;
            this.priority = priority;
        }

        /** for changing priority */
        public void setPriority(double priority){
            this.priority = priority;
        }

        /** 2 nodes are equals if their keys are equal, regardless of priority */
        @Override
        public boolean equals(Object o){
            if (o == null || o.getClass() != this.getClass()) {
                return false;
            } else {
                return ((ArrayHeapMinPQ.Node) o).key.equals(key);
            }
        }

        @Override
        public int hashCode(){
            return key.hashCode();
        }
    }

    private ArrayList<Node> heap; //will represent our heap
    private HashMap<T, Integer> keyIndexMap;

    public ArrayHeapMinPQ(){
        heap = new ArrayList<>();
        keyIndexMap = new HashMap<>();
        heap.add(0, null); //index 0 is always null for convenience
    }

    /** parent child relationship can be determined by the index value in array
     * according to invariants of heap structure
     */
    /** returns index of parent to the given key */
    private int parent(Node node){
        return node.index / 2;
    }

    /** return the index of left child of given key */
    private int leftChild(Node node){
        int child = node.index * 2;
        if (child > size()){
            return 0;
        }else{
            return child;
        }
    }

    /** return index of the right child of given key */
    private int rightChild(Node node){
        int child = node.index * 2 + 1;
        if (child > size()){
            return 0;
        }else{
            return child;
        }
    }

    /**   */
    @Override
    public void add(T item, double priority) {
        Node newItem = new Node(item, priority);   //create new item
        heap.add(newItem);                      //add item to end of heap
        newItem.index = size();
        swim(newItem);                      //swim item up the heap to appropriate place
        keyIndexMap.put(item, newItem.index); //put the item in hashmap with its current index after swimming
    }

    /** change position of node by swimming up the heap after being added */
    private void swim(Node node){
        Node parent = heap.get(parent(node));  //get parent node, temp variable for parent
        if (parent == null){
            return;
        }
        if (node.priority < parent.priority){   //if node priority greater than parent, switch places
            int nodeIndex = node.index;
            int parentIndex = parent(node);
            heap.set(parentIndex, node);
            heap.set(nodeIndex, parent);
            switchIndices(parent, node);
            swim(node);                     //continue swimming if necessary
        }
    }

    /**  check to see if key is contained in heap */
    @Override
    public boolean contains(T item) {
        if (isEmpty()){
            return false;
        }
        return keyIndexMap.containsKey(item); //should be log(n) time or faster
    }

    /** returns true/false if heap is empty or not */
    public boolean isEmpty(){
        return size() <= 0;
    }

    /** return the smallest key in PQ, should be at front of array list  */
    @Override
    public T getSmallest() {
        if (size() == 0) {
            throw new NoSuchElementException("PQ is empty");
        }
        return heap.get(1).key;   //smallest item will always be at index 1
    }

    /** remove and return smallest key in PQ, after removing it from front (index 1)
     * there needs to be a replacement based on the next priority
     */
    @Override
    public T removeSmallest() {
        if (size() == 0) {
            throw new NoSuchElementException("PQ is empty");
        }else if (size() == 1){
            T smallest = heap.get(1).key;
            heap.remove(1);
            return smallest;
        }
        T smallest = heap.get(1).key;     //get smallest key to return later
        Node lastItem = heap.get(size());  //last item in array (heap)
        heap.remove(size());  //remove last item
        heap.set(1, lastItem);   //make last item root
        lastItem.index = 1;  //set its index to 1
        sink(lastItem);     //sink the item from root to its appropriate place
        return smallest;
    }

    /** sink an item from root down the heap.  opposite of swim method */
    private void sink(Node node){
        Node leftChild = heap.get(leftChild(node));
        Node rightChild = heap.get(rightChild(node));
        Node successor;
        if (leftChild == null && rightChild == null){
            return;
        }
        if (rightChild == null){
            successor = leftChild;
        }else if (leftChild == null){
            successor = rightChild;
        }else if (leftChild.priority > rightChild.priority){  //find which child should switch, the min should be selected
            successor = rightChild;
        }else{
            successor = leftChild;
        }
        if (node.priority > successor.priority){  //check if node should sink lower
            int nodeIndex = node.index;
            int childIndex = successor.index;
            heap.set(childIndex, node);
            heap.set(nodeIndex, successor);
            switchIndices(node, successor);    //automatically switch indices while sinking
            sink(node);
        }

    }

    /** return size of PQ heap */
    @Override
    public int size() {
        return heap.size()-1;   //just return size of array list
    }

    /** find the index of a node in the heap */
    private int indexOfNode(T key){
        return keyIndexMap.get(key);
    }

    /** method to switch indices in object and in hashmap when Nodes switch places */
    private void switchIndices(Node a, Node b){
        int temp = a.index;
        a.index = b.index;
        b.index = temp;
        keyIndexMap.put(a.key, a.index);
        keyIndexMap.put(b.key, b.index);
    }

    /** this method will move key's index, can be used to put new key in smallest position
     * and move other keys in that process
     */
    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("PQ does not contain " + item);
        }
        Node target = heap.get(indexOfNode(item));
        boolean sink = target.priority < priority;    //compare priorities, to know if target should move up or down heap
        target.priority = priority;
        if (sink){
            sink(target);
        }else{
            swim(target);
        }
    }
}
