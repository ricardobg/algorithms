import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    
    //Constants to use in bitwise operations on sitesConnection
    private static final byte CONNECT_TOP = 0x1;
    private static final byte CONNECT_BOTTOM = 0x2;
    
    private WeightedQuickUnionUF quickUnion;
    /* byte array for each root, storing if it is connect to the top and bottom */
    private byte[] sitesConnection;
    /* boolean array for each position, storing if it is opened */
    private boolean[] isOpen;
    /* system percolates? */
    private boolean percolates;
    private int n;

    public Percolation(int N)               // create N-by-N grid, with all sites blocked
    {
        if (N <= 0)
            throw new IllegalArgumentException();
        
        this.percolates = false;
        this.n = N;
        
        this.quickUnion = new WeightedQuickUnionUF(N*N);
        
        this.sitesConnection = new byte[N*N];
        this.isOpen = new boolean[N*N];
        
        for (int i = 0; i < N*N; i++) {
            sitesConnection[i] = 0;
            isOpen[i] = false;
        }
        
        /* Set connections of top row and bottom row */
        for (int j = 1; j <= N; j++) {
            sitesConnection[quickUnion.find(getRealIndex(1, j))] |= CONNECT_TOP;
            sitesConnection[quickUnion.find(getRealIndex(N, j))] |= CONNECT_BOTTOM;
        } 
    }

    /**
     * Get index in the quick union structure
     * @return
     */
    private int getRealIndex(int i, int j)
    {
        if (i > n || i < 1 || j < 1 || j > n)
            throw new IndexOutOfBoundsException();
        return (i-1)*n + j - 1;
    }

    public void open(int i, int j)          // open site (row i, column j) if it is not open already
    {
        int index = getRealIndex(i, j);
        
        if (!isOpen[index]) {
            int root = quickUnion.find(index);
            isOpen[index] = true;
            
            /* indices to walk around current position, 4 positions of 2 dimensions */
            int[][] cmp_indices = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };
            for (int[] cmp : cmp_indices) {
                try {
                    int tmp_i = i + cmp[0];
                    int tmp_j = j + cmp[1];
                    int cmp_index = getRealIndex(tmp_i, tmp_j);
                    
                    if (isOpen[cmp_index]) {
                        int tmp_root = quickUnion.find(cmp_index);
                        quickUnion.union(index, cmp_index);
                        sitesConnection[tmp_root] |= sitesConnection[root];
                        /* Updates root */
                        root = quickUnion.find(index);
                        sitesConnection[root] |= sitesConnection[tmp_root];
                        
                    }
                }
                catch (IndexOutOfBoundsException ex) {
                    continue;
                }
            }
            //Check if percolates (bottom at the same root as top)
            percolates |= ((sitesConnection[root] & (CONNECT_BOTTOM | CONNECT_TOP)) == (CONNECT_BOTTOM | CONNECT_TOP));
        }
    }

    public boolean isOpen(int i, int j)     // is site (row i, column j) open?
    {
        int index = getRealIndex(i, j);
        return isOpen[index];
    }

    public boolean isFull(int i, int j)     // is site (row i, column j) full?
    {
        int index = getRealIndex(i, j);
        int root = quickUnion.find(index);
        return isOpen[index] && ((sitesConnection[root] & CONNECT_TOP) > 0);
    }

    public boolean percolates()             // does the system percolate?
    {
        return percolates;
    }

}