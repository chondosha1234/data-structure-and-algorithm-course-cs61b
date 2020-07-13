

public class UnionFind {

    // TODO - Add instance variables?
    int[] parents;

    /* Creates a UnionFind data structure holding n vertices. Initially, all
       vertices are in disjoint sets. */
    public UnionFind(int n) {
        // TODO
        parents = new int[n];
        for (int i = 0; i < n; i++){
            parents[i] = -1;
        }
    }

    /* Throws an exception if v1 is not a valid index. */
    private void validate(int vertex) {
        // TODO
        if (vertex >= parents.length || vertex < 0){
            throw new IllegalArgumentException();
        }
    }

    /* Returns the size of the set v1 belongs to.
    * if the root has a negative value indicating size, return abs value */
    public int sizeOf(int v1) {
        // TODO
        validate(v1);
        return Math.abs(parent(find(v1)));
    }

    /* Returns the parent of v1. If v1 is the root of a tree, returns the
       negative size of the tree for which v1 is the root. */
    public int parent(int v1) {
        // TODO
        validate(v1);
        return parents[v1];
    }

    /* Returns true if nodes v1 and v2 are connected. */
    public boolean connected(int v1, int v2) {
        // TODO
        validate(v1);
        validate(v2);
        return find(v1) == find(v2);
    }

    /* Connects two elements v1 and v2 together. v1 and v2 can be any valid 
       elements, and a union-by-size heuristic is used. If the sizes of the sets
       are equal, tie break by connecting v1's root to v2's root. Unioning a 
       vertex with itself or vertices that are already connected should not 
       change the sets but may alter the internal structure of the data. */
    public void union(int v1, int v2) {
        // TODO
        validate(v1);
        validate(v2);
        int v1Root = find(v1);
        int v2Root = find(v2);
        if (v1Root != v2Root){
            if (sizeOf(v1) <= sizeOf(v2)){
                parents[v2Root] -= sizeOf(v1);
                pathCompress(v1, v2Root);
                parents[v1Root] = v2Root;
            }else{
                parents[v1Root] -= sizeOf(v2);
                pathCompress(v2, v1Root);
                parents[v2Root] = v1Root;
            }
        }
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. */
    public int find(int vertex) {
        // TODO
        validate(vertex);
        if (parent(vertex) < 0){
            return vertex;
        }
        return find(parent(vertex));
    }

    /* takes VERTEX in union and compresses the chain to its own root to the ROOT of
      the other vertex in the union function
     */
    private void pathCompress(int vertex, int root){
        validate(root);
        while (parent(vertex) >= 0){
            parents[vertex] = root;
            vertex = parent(vertex);
        }
    }

}
