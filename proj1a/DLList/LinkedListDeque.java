/** Creates a DLList object and provides methods to add to front and back and remove
 * from front and back.
 * @param <genType>
 */
public class LinkedListDeque<genType>{

    /** naked recursive structure for DLList.  It has instance variables for item, previous and next */
    private class TypeNode{
        public TypeNode prev;    // pointer to item before current item
        public genType item;       // current item
        public TypeNode next;   // pointer to item after current item

        /** Constructor for naked recursive structure.  Takes the item at index and sets
         * pointer to previous and next indices */
        public TypeNode(TypeNode p, genType i, TypeNode n){
            prev = p;
            item = i;
            next = n;
        }
    }

    private int size;            // variable to cache size
    private TypeNode sentinel;   // one sentinel for circular DLList
    genType sentinelValue;

    /** Creates empty list */
    public LinkedListDeque(){
        sentinel = new TypeNode(null, sentinelValue, null);
        size = 0;
    }

    /** Constructor for class */
    public LinkedListDeque(genType i){
        sentinel = new TypeNode(null, sentinelValue, null);      // set sentinel to arbitrary value 0 and null before and after
        sentinel.next = new TypeNode(sentinel, i, sentinel); // sentinel is before and after to create circle
        sentinel.prev = sentinel.next;                      // when created previous and next are the same value
        size = 1;
    }

    /** Creates a copy of DLList, which is a separate object and pointer */
    public LinkedListDeque(LinkedListDeque other){
        TypeNode p = other.sentinel.next;                           // pointer to other DLList
        sentinel = new TypeNode(null, sentinelValue, null);  // create sentinel for copy list
        for (int i = 1; i <= other.size(); i += 1){                 //cycle through pointer to other to add values to new list
            this.addLast(p.item);
            p = p.next;
        }
    }

    /** adds item to beginning of list, should take constant time */
    public void addFirst(genType item){
        sentinel.next = new TypeNode(sentinel, item, sentinel.next);
        size += 1;
    }

    /** adds item to end of list, should take constant time */
    public void addLast(genType item){
        sentinel.prev = new TypeNode(sentinel.prev, item, sentinel);
        size += 1;
    }

    /** returns True/False if list is empty or not */
    public boolean isEmpty(){
        if (size() == 0){
            return true;
        }else{
            return false;
        }
    }

    /** returns integer number of items in list, should use constant time */
    public int size(){
        return size;
    }

    /** prints contents of DLList with space between each item and newline after */
    public void printDeque(){
        TypeNode copy = sentinel.next;
        while (copy != sentinel && copy != null){
            System.out.print(copy.item + " ");
            copy = copy.next;                   // ???
        }
        System.out.println();
    }

    /** remove item at beginning and return it, use constant time */
    public genType removeFirst(){
        genType value = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        size -= 1;
        return value;
    }

    /** remove item at end and return it, use constant time */
    public genType removeLast(){
        genType value = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        size -= 1;
        return value;
    }

    /** returns item at specific index in list, must use iteration */
    public genType get(int index){
        genType value;
        TypeNode current = sentinel.next;
        while (index != 0){
            current = current.next;
            index -= 1;
        }
        value = current.item;
        return value;
    }

    /** same as other get method, but uses recursion */
    public genType getRecursive(int index){
        if (index == 0){
            return sentinel.next.item;
        }else{
            LinkedListDeque<genType> copyList = new LinkedListDeque<>(this);
            copyList.removeFirst();
            return copyList.getRecursive(index-1);
        }
    }

}