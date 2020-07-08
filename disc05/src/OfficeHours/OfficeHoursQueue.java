package OfficeHours;

import java.util.Iterator;

public class OfficeHoursQueue implements Iterable<OHRequest>{

    private OHRequest q;

    public OfficeHoursQueue(OHRequest queue){
        q = queue;
    }

    public Iterator<OHRequest> iterator(){
       // return new OHIterator(q);
        return new TYIterator(q);
    }

    public static void main(String[] args){
        /** OHRequest s1 = new OHRequest("Failing my test for get in arrayDeque, NPE", "Pam", null);
        OHRequest s2 = new OHRequest("conceptual: what is dynamic method selection", "Michael", s1);
        OHRequest s3 = new OHRequest("git: what does checkout do", "Jim", s2);
        OHRequest s4 = new OHRequest("help", "Dwight", s3);
        OHRequest s5 = new OHRequest("debugging get(i)", "Creed", s4);

        OfficeHoursQueue q = new OfficeHoursQueue(s5);
        for (OHRequest request : q){
            if (request == null){
                continue;
            }
            System.out.println(request.name);
        } */

        OHRequest r1 = new OHRequest("help me", "Dave", null);
        OHRequest r2 = new OHRequest("i'm bored", "john", r1);
        OHRequest r3 = new OHRequest("thank u", "Maria", r2);
        OHRequest r4 = new OHRequest("thank u", "Mary", r3);

        OfficeHoursQueue q2 = new OfficeHoursQueue(r4);
        for (OHRequest request : q2){
            System.out.println(request.name);
        }
    }
}
