import edu.princeton.cs.algs4.Queue;

import org.junit.Test;
import static org.junit.Assert.*;


public class TestSortAlgs {

    @Test
    public void testQuickSort() {
        Queue<String> q = new Queue<>();
        q.enqueue("Deborah");
        q.enqueue("Christine");
        q.enqueue("Abby");
        q.enqueue("Elizabeth");
        q.enqueue("Beth");
        q.enqueue("Fiona");

        q = QuickSort.quickSort(q);
        assertTrue(isSorted(q));
    }

    @Test
    public void testMergeSort() {
        Queue<String> q = new Queue<>();
        q.enqueue("Deborah");
        q.enqueue("Christine");
        q.enqueue("Abby");
        q.enqueue("Elizabeth");
        q.enqueue("Beth");
        q.enqueue("Fiona");

        q = MergeSort.mergeSort(q);
        assertTrue(isSorted(q));

    }

    @Test
    public void testRandomInput(){
        Queue<Integer> q = new Queue<>();
        for (int i = 0; i < 1000; i++){
            q.enqueue(StdRandom.uniform(1000));
        }
        q = QuickSort.quickSort(q);
        //q = MergeSort.mergeSort(q);
        assertTrue(isSorted(q));
    }

    /**
     * Returns whether a Queue is sorted or not.
     *
     * @param items  A Queue of items
     * @return       true/false - whether "items" is sorted
     */
    private <Item extends Comparable> boolean isSorted(Queue<Item> items) {
        if (items.size() <= 1) {
            return true;
        }
        Item curr = items.dequeue();
        Item prev = curr;
        while (!items.isEmpty()) {
            prev = curr;
            curr = items.dequeue();
            if (curr.compareTo(prev) < 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args){
        Queue<Integer> q = new Queue<>();
        for (int i = 0; i < 1000000; i++){
            q.enqueue(StdRandom.uniform(1000));
        }
        Stopwatch sw = new Stopwatch();
        //q = MergeSort.mergeSort(q);
        q = QuickSort.quickSort(q);
        System.out.println(sw.elapsedTime());
    }
}
