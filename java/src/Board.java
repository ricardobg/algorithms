import java.util.Iterator;

public class Board {
    
    private int[][] blocks; 
    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) 
    {
        this.blocks = new int[blocks.length][blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++)
                this.blocks[i][j] = blocks[i][j]; 
        }
    }
                                            
    public int dimension()                 // board dimension N
    {
        return blocks.length;
    }
    
    private static int abs(int val) {
        return val >= 0 ? val : -val;
    }
    public int hamming()                   // number of blocks out of place
    {
        int ham = 0;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                if (blocks[i][j] == 0)
                    continue;
                ham += (blocks[i][j] - (i*blocks.length + j + 1)) == 0 ? 0 : 1;
            }
        }
        return ham;
    }
    
    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        int man = 0;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                if (blocks[i][j] == 0)
                    continue;
                int x = (blocks[i][j] - 1) % blocks.length;
                int y = (blocks[i][j] - 1) / blocks.length;
                man += abs(x - j) + abs(y - i);
            }
        }
        return man;
    }
    
    public boolean isGoal()                // is this board the goal board?
    {
        return hamming() == 0;
    }
    
    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    {
        Board copy = new Board(blocks);
        copy.blocks[0][0] = blocks[1][1];
        copy.blocks[1][1] = blocks[0][0];
        return copy;
    }
    
    public boolean equals(Object y)        // does this board equal y?
    {
        Board cmp = (Board) y;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                if (blocks[i][j] != cmp.blocks[i][j])
                    return false;
            }
        }
        return true;
    }
    
    private class IterableBoard implements Iterable<Board>
    {
        private class BoardIterator implements Iterator<Board>
        {
           
            private final Board boards[] = new Board[4];
            private int position = 0;
            public BoardIterator() {
                static int moves[][] = { {-1,0}, {1,0}, {0,-1}, {0, 1} }; 
                boards[0] = boards[1] = boards[2] = boards[3] = null;
                //Find zero position
                int zero_i = 0, zero_j = 0;
                for (int i = 0; i < blocks.length; i++)
                    for (int j = 0; j < blocks.length; j++)
                        if (blocks[i][j] == 0) {
                            zero_i = i;
                            zero_j = j;
                            i = blocks.length;
                            break;
                        }
                for (int[] move : moves) {
                    int swap_i = move[0] + zero_i;
                    int swap_j = move[1] + zero_j;
                    if (swap_i < 0 || swap_i >= blocks.length || swap_j < 0 || swap_j >= blocks.length)
                        continue;
                }
            }
            
            @Override
            public boolean hasNext() {
                
                return false;
            }

            @Override
            public Board next() {
                // TODO Auto-generated method stub
                return null;
            }
            
        }
        @Override
        public Iterator<Board> iterator() {
            return new BoardIterator();
        }
        
    }
    
    public Iterable<Board> neighbors()     // all neighboring boards
    {
       
    }
    
    public String toString()               // string representation of this board (in the output format specified below)
    {
        
    }

    public static void main(String[] args) // unit tests (not graded)
    {
        
    }
}
