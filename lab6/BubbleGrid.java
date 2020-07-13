import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class BubbleGrid {

    private int[][] grid;  // 2D array for bubbles on ceiling.  1 is bubble, 0 is empty
    private WeightedQuickUnionUF stuckBubbles;  //connect bubbles
    private int len;
    private int virtualBubble;    //virtual bubble to connect stuck bubbles to

    /** constructor for BubbleGrid, is passed a 2D array
     * grid should be square   */
    public BubbleGrid(int[][] grid){
        this.grid = grid;
        len = grid.length;
        stuckBubbles = new WeightedQuickUnionUF(len * len + 1);
        virtualBubble = len * len;

        for (int i = 0; i < len; i++){
            for (int j = 0; j < len; j++){
                if (grid[i][j] == 1){
                    int location = toUF(i, j);
                    stuckBubbles.union(location, virtualBubble);
                }
            }
        }
    }

    private int toUF(int row, int col){
        return (col * len) + row;
    }

    /** return t/f is bubble is stuck
     * a bubble is stuck if it is at top of grid, or orthogonally connected to a stuck bubble
     */
    public boolean isStuck(int row, int col){
        int location = toUF(row, col);
        return stuckBubbles.connected(location, virtualBubble);
    }

    //stuck update class?

    /** returns array of falling bubbles at each time T = i (index)
     * i.e.   return [0,4]  0 fall after first dart, 4 bubbles fall after second dart
     * darts is a 2D array, each element is an array of 2 elements, representing grid positions [row, col]
     * darts thrown in sequence -- darts[T] at time T
     */
    public int[] popBubbles(int[][] darts){
        int[] results = new int[darts.length];  //one dimensional array to return results
        int t = 0;                              //index for results, represents time
        for (int[] dart : darts){
            grid[dart[0]][dart[1]] = 0;    // dart pops a bubble
            results[t] = 0;      //change
        }
    }
}
