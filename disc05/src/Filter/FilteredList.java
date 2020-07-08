package Filter;

import java.util.*;

public class FilteredList<T> implements Iterable<T> {
    private List<T> FL;
    private Predicate<T> pred;

    public FilteredList(List<T> L, Predicate<T> filter){
        FL = L;
        pred = filter;
    }

    @Override
    public Iterator<T> iterator(){
        return new FLIterator(FL, pred);
    }
}
