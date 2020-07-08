package Filter;

import java.util.*;

public class IteratorOfIterators implements Iterator {
    private Iterator current;
    private ArrayList<Iterator<Integer>> I = new ArrayList<>();

    public IteratorOfIterators(List<Iterator<Integer>> a) {
        for (Iterator<Integer> It : a){
            I.add(It);
        }
        current = I.
    }

    @Override
    public boolean hasNext() {

    }

    @Override
    public int next() {

    }
}

