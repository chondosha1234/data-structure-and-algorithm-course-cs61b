package OfficeHours;

import java.util.Iterator;

public class OHIterator implements Iterator<OHRequest> {

    private OHRequest curr;
    private OHRequest temp;

    public OHIterator(OHRequest queue){
        curr = queue;
    }

    public boolean isGood(String description){
        return description != null && description.length() > 5;
    }

    @Override
    public boolean hasNext(){
        return curr != null;
    }

    @Override
    public OHRequest next(){

        if (isGood(curr.description)) {
            temp = curr;
            curr = curr.next;
            return temp;
        } else {
            curr = curr.next;
            return null;
        }
    }
}
