package OfficeHours;

public class OHRequest {
    public String description;
    public String name;
    public OHRequest next;

    public OHRequest(String d, String n, OHRequest nxt){
        description = d;
        name = n;
        next = nxt;
    }
}
