package logic;


import java.util.ArrayList;
import java.util.Collections;

/**
 * Implemented by studying Algorithm X, and the Dancing Links Java implementation
 * by Rafalio: https://github.com/rafalio/dancing-links-java
 * @author Alvin
 */

public class Solver {
    
    ArrayList<Cell[][]> solvedGrids = new ArrayList<Cell[][]>();
    
    public void initSolve(Cell[][] grid) {
        genRandFirstRow(grid);
        solve(grid, 0);
    } 
    
    private void solve(int[][] board, int ind){
        if(ind == 81){
            System.out.println("Solution via naive algorithm found: ");
            printSolution(board);
            System.out.println();
        }
        else{
            int row = ind / 9;
            int col = ind % 9;
                System.out.println("IND IS: " + ind + " @ R" + row + ", C" + col + ", but is C equal to " + ind);
            
            // Advance forward on cells that are prefilled
            if(board[row][col] != 0) solve(board, ind+1);
            else{
                // we are positioned on something we need to fill in. Try all possibilities
                for(int i = 1; i <= 9; i++){
                    if(consistent(board, row, col, i)){
                        board[row][col] = i;
                        solve(board,ind+1);
                        board[row][col] = 0; // unmake move
                    }   
                }
            }
            // no solution
        }
    }
    
    public void genRandFirstRow(Cell[][] grid) {
        ArrayList<Integer> tempList = new ArrayList<Integer>();
        for(int x = 1; x < 10; tempList.add(x), x++);
        Collections.shuffle(tempList);
        for(int c = 0; c < 9; grid[0][c].setValue(tempList.get(c)), c++);
    }

      
    // Check whether putting "c" into index "ind" leaves the board in a consistent state
    private boolean consistent(int[][] board, int row, int col, int c) {        
        // check columns/rows
        for(int i = 0; i < 9; i++){
            if(board[row][i] == c) return false;
            if(board[i][col] == c) return false;
        }
        
        // Check subsquare
        
        int rowStart = row - row % 3; 
        int colStart = col - col % 3;
        
        for(int m = 0; m < 3; m++){
            for(int k = 0; k < 3; k++){
                if(board[rowStart + k][colStart + m] == c) return false;
            }
        }
        return true;
    }
    
    //Used for testing, delete later.
    public void printSolution(int[][] result){
        for(int i = 0; i < 9; i++){
            String ret = "";
            for(int j = 0; j < 9; j++){
                ret += result[i][j] + " ";
            }
            System.out.println(ret);
        }
        System.out.println();
    }

}
