package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF connection;   //manages connections between cells, needs size N^2 (+2)
    private boolean[][] grid;
    private int size = 0;
    private final int N;
    private final int virtualTop;      //virtual top position off of grid, to connect all first row to
    private final int virtualBottom;  //virtual bottom position to connect last row to

    /** Constructor for class.  N is the dimension of grid (N-by-N)
     * create a 2D array for grid, initially set to all closed (0)
     * 1 will represent open, 2 will be open and full
     * */
    public Percolation(int N){
        this.N = N;                         //variable for easy access to grid dimensions
        grid = new boolean[N][N];                              // 2 dimensional grid
        connection = new WeightedQuickUnionUF(N*N + 2);    //UF structure with N^2 sites
        virtualTop = N*N;                           //set top to the second to last integer in UF
        virtualBottom = N*N + 1;                    //set bottom to last integer in UF
    }

    /** open the site (ROW, COL) if it is not open already
     * Row and Col indices are between 0 and N-1
     * */
    public void open(int row, int col){
        if (!isOpen(row, col)){          // if cell not open, then open it
            grid[row][col] = true;
            size += 1;
        }
        if (row == 0){          //if cell is in top row, connect it to the virtual top
            int locationT = TwoIntoOneD(row, col);
            connection.union(locationT, virtualTop);
        }
        checkNeighbors(row, col);

        if (row == (N-1)){      //if cell is in bottom row and full, connect it to virtual bottom
            if (isFull(row, col)) {
                int locationB = TwoIntoOneD(row, col);
                connection.union(locationB, virtualBottom);
            }
        }
    }

    /** checks the 4 neighbor cells around a cell in grid
     * connects the cell with any open cells around it
     * checks to make sure it is targeting a valid sell (with validate)
     * row is Y axis, and col is X axis
     * */
    private void checkNeighbors(int row, int col){
        int location = TwoIntoOneD(row, col);
        if (validate(col-1) && isOpen(row, (col-1))){   //check cell to left
            connection.union(location, location - N);
        }
        if (validate(col+1) && isOpen(row, (col+1))){    //check cell to the right
            connection.union(location, location + N);
        }
        if (validate(row-1) && isOpen(row-1, col)){    //check row above
            connection.union(location, location - 1);
        }
        if (validate(row+1) && isOpen((row+1), col)) {    //check row below
            connection.union(location, location + 1);
            if (isFull(row, col)) {                             //if the current location is filled check bottom
                if (row + 1 == N - 1) {
                    connection.union(location + 1, virtualBottom);   //connect to virtual bottom
                }
            }
        }
    }

    /** checks to make sure a dimension (row or col) is within the grid */
    private boolean validate(int dimension){
        return (dimension >= 0) && (dimension < N);
    }

    /** takes 2D coordinates and transforms them into 1D for the UF ADT */
    private int TwoIntoOneD(int row, int col){
        return (col * N) + row;
    }

    /** return t/f if site is open already  */
    public boolean isOpen(int row, int col){
        return grid[row][col];
    }

    /** return t/f if site is full */
    public boolean isFull(int row, int col){
        int location = TwoIntoOneD(row, col);
        return connection.connected(location, virtualTop);
    }

    /** return number of open sites */
    public int numberOfOpenSites(){
        return size;
    }

    /** return t/f if system percolates, i.e. there is a top-bottom connection of open sites */
    public boolean percolates(){
        return connection.connected(virtualTop, virtualBottom);
    }


}
