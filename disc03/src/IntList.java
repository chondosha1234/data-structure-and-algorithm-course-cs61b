public class IntList {
    public int first;
    public IntList rest;
    public IntList(int f, IntList r){
        this.first = f;
        this.rest = r;
    }

    public static IntList list(){ return null;}

    public int get(int i) {
        if (i == 0) {
            return first;
        }
        return rest.get(i - 1);
    }

    /** skippify will change the IntList to keep values and skipping others
     * It starts from first element, skips 1, then skips 2, 3 and so on
     */
    public void skippify(){
        IntList p = this;
        int n = 1;          //skipping variable
        while (p != null){
            IntList next = p.rest;
            for(int i = 0; i < n; i++){
                if(next == null){
                    break;
                }
                next = next.rest;
            }
            p.rest = next;
            p = p.rest;
            n += 1;
        }
    }

    /** returns a copy of IntList x without any y in it, nondestructive */
    public static IntList ilsans(IntList x, int y){
        if(x == null){
            return null;
        }
        if(x.first == y){
            return ilsans(x.rest, y);
        }
        return new IntList(x.first, ilsans(x.rest, y));
    }

    /** same as ilsans but destructive */
    public static IntList dilsans(IntList x, int y){
        if (x == null){
            return null;
        }
        x.rest = dilsans(x.rest, y);
        if (x.first == y){
            return x.rest;
        }
        return x;
    }
}
