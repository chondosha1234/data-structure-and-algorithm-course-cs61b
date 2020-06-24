/** Makes a singly linked list (SLList) with first instance variable,
 * uses IntNode class as a naked recursive data structure with instance variables
 * item and next
 */
public class SLList {

    private class IntNode{
        public int item;
        public IntNode next;
        public IntNode(int item, IntNode next){
            this.item = item;
            this.next = next;
        }
    }

    private IntNode first;
    private int size;

    /** constructs empty list */
    public SLList(){
        first = new IntNode(0, null);  // ??
        size = 1;
    }

    /** constructor for SLList */
    public SLList(int i){
        first = new IntNode(i, null);
        size = 1;
    }

    /** add element to beginning of list */
    public void addFirst(int x){
        first = new IntNode(x, first);
        size += 1;
    }

    /** remove and return first element in list */
    public int removeFirst(){
        int value = first.item;
        first = first.next;
        size -= 1;
        return value;
    }

    /** insert element into list at index 'position'
     * uses iteration
     */
    public void insert(int x, int position){
        if (first == null || position == 0){
            addFirst(x);
            return;
        }
        IntNode current = first;
        while (position > 0 && current.next != null){
            position -= 1;
            current = current.next;
        }
        IntNode newNode = new IntNode(x, current.next);
        current.next = newNode;
    }

    public void insert2(int x, int position){
        IntNode p = first;
        while (position != 0){
            if (p.next == null){
                break;
            }
            p = p.next;
            position -= 1;
        }
        p.next = new IntNode(p.item, p.next);
        if (position > 0){
            p.next.item = x;
        }else {
            p.item = x;
        }
    }

    /** Reverses order of list */
    public void reverse(){
        SLList other = new SLList();
        other.removeFirst();

        while (this.first != null) {
            other.addFirst(this.removeFirst());
        }
        this.first = other.first;
    }

    /** these methods don't use SLLists, they are practicing array methods from discussion 3
     * and can be conveniently tested as static methods
     */
    /** insert element into index 'position' in an array.  nondestructive*/
    public static int[] insert(int[] arr, int item, int position){
        int[] copy = new int[arr.length + 1];
        System.arraycopy(arr, 0, copy, 0, position);
        copy[position] = item;
        System.arraycopy(arr, position, copy, position+1, arr.length - position);
        return copy;
    }

    /** reverses an array's order. destructive */
    public static void reverse(int[] arr){
        for (int i = 0; i < arr.length/2; i++){
            int temp = arr[i];
            arr[i] = arr[arr.length-1-i];
            arr[arr.length-1-i] = temp;
        }
    }

    /** replaces each number N in array with N copies of itself
     * nondestructive method
     */
    public static int[] replicate(int[] arr){
        int sumOfArray = 0;
        int index = 0;
        for (int i = 0; i < arr.length; i++){     // get sum of integers which will be length of new array
            sumOfArray += arr[i];
        }

        int[] newArr = new int[sumOfArray];       // make new array with length equal to sum of array

        while (index < sumOfArray) {                //keeps track of indices of new array
            for (int j = 0; j < arr.length; j++) {  // cycles through original array's values
                int N = arr[j];
                while (N > 0) {                     //adds number N to new array N number of times
                    newArr[index] = arr[j];
                    index += 1;
                    N -= 1;
                }
            }
        }
        return newArr;
    }
}
