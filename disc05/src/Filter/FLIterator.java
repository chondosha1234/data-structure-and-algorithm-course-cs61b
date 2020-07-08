package Filter;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class FLIterator<T> implements Iterator<T> {
    List<T> list;
    Predicate<T> pred;
    int index;

    public FLIterator(List<T> l, Predicate<T> p){
        list = l;
        pred = p;
        index = 0;
    }

    public boolean hasNext(){
        while (index < list.size() && !pred.test(list.get(index))){
            index += 1;
        }
        return index < list.size();
    }

    public T next(){
        if (!hasNext()){
            throw new NoSuchElementException;
        }
        index += 1;
        return list.get(index-1);
    }
}
