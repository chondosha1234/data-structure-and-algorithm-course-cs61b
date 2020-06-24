public class OffByN implements CharacterComparator{

    private int n;
    /** constructor takes an integers and makes an object whose equals method
     * is compares characters if they are separated by N
     */
    public OffByN(int n){
         this.n = n;
    }

    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x-y) == n;
    }
}
