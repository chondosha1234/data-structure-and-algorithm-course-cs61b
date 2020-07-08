package OfficeHours;


public class TYIterator extends OHIterator {

    public TYIterator(OHRequest queue){
        super(queue);
    }

    @Override
    public OHRequest next(){
        if (isGood(curr.description) && curr.description.contains("thank u")){
            temp = curr;
            curr = curr.next.next;
            return temp;
        }
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
