/*******************************************************************************
* Author: Alvin Ramoutar (991454918)
* Developed for: Jonathan Penava & Sheridan College (JAVA 2 FINAL)
* Last Updated: April 19th, 2017
* SUDOKU
*   Sudoku is a JavaFXML application meant to deliver a comfortable and 
*   intuitive Sudoku gameplay experience. 
*   Sudoku in itself is a puzzle game sharing similar properties to Latin
*   Squares; the objective being to completely fill a grid while abiding by
*   certain rules.
*******************************************************************************/

package logic;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Puzzle Solver class which implements solving and validation functionality
 * Implemented by studying Algorithm X, and the Dancing Links Java implementation
 * by Rafalio
 */
public class Solver {
    
    ArrayList<Cell[][]> ambiguousCount = new ArrayList<Cell[][]>();
    Grid solved = new Grid();
    private boolean solutionAttained = false;
    int ambiguousSolutions = 0;
    
    /**
     * Initializes a solve by pre-genning a first row (saves time)
     * @param grid Grid to solve
     * @return Solved grid
     */
    public Grid initSolve(Grid grid) {
        genRandFirstRow(grid.cells);
        solve(grid.cells, 0);
        return solved;
    } 
    
    /**
     * Recursive method that utilizes Dancing Links to solve Sudoku Puzzles
     * A simple brute-force approach, in which iterates through indices of
     *  a grid, tests a valid value, and continues. Should an error appear
     *  somewhere during the solve, then rollback until the last successful
     *  cell/validation, and try again.
     * @param board Grid to solve (cells)
     * @param indice Current index iterating through
     */
    private void solve(Cell[][] board, int indice){
        //When the final index is reached, grid is solved. Store as solved grid.
        if(indice == 81) {
            for(int o = 0; o < 9; o++) {
                for(int i = 0; i < 9; i++) {
                    solved.cells[o][i].setValue(board[o][i].getValue());
                    solved.cells[o][i].setRow(board[o][i].getRow());
                    solved.cells[o][i].setCol(board[o][i].getCol());
                    solved.cells[o][i].setSquare(board[o][i].getSquare());
                    solved.cells[o][i].setValid(true);
                }
            }
            solutionAttained = true;
        } else { //Use recursion to contuously iterate through indexes until 81
            int row = indice / 9;
            int col = indice % 9;
            
            //Proceed to pre-filled cells
            if(solutionAttained) {
            } else if(board[row][col].getValue() != 0) {
                solve(board, indice+1);
            } else {
                //Currently at a location that requires a value, try all possibilities
                for(int i = 1; i <= 9; i++){
                    if(consistent(board, row, col, i)){
                        board[row][col].setValue(i);
                        solve(board,indice+1);
                        board[row][col].setValue(0);
                    }   
                }
            }
        //If this point is reached, there is no valid solution for the grid
        }
    }
    
    /**
     * Initializes an ambiguity check with a supplied grid
     * @param grid Grid to check ambiguity of
     * @return True if ambiguous (multiple solutions)
     */
    public boolean initAmbiguityCheck(Grid grid) {
        Cell[][] tempGrid = new Cell[9][9];
        System.arraycopy(grid.cells, 0, tempGrid, 0, grid.cells.length);
        ambiguityCheck(tempGrid, 0);
        if(ambiguousSolutions == 1) {
            return false; //Puzzle has only 1 valid solution (good).
        } else {
            return true; //Various solutions exist for the same puzzle (bad).
        }
    }
    
    /**
     * Performs an ambiguity check by using similar logic as Solver
     * @param board Grid to solve (cells)
     * @param indice Current index iterating through
     */
    private void ambiguityCheck(Cell[][] board, int indice){
        //Increment solution count if a valid one is attained
        if(indice == 81) {
            ambiguousSolutions++;
        } else { //Recursive solve
            int row = indice / 9;
            int col = indice % 9;

            if(ambiguousSolutions > 2) {
            } else if(board[row][col].getValue() != 0) {
                ambiguityCheck(board, indice+1);
            } else {
                for(int i = 1; i <= 9; i++){
                    if(consistent(board, row, col, i)){
                        board[row][col].setValue(i);
                        ambiguityCheck(board,indice+1);
                        board[row][col].setValue(0);
                    }   
                }
            }
        }
    }
    
    /**
     * Pre-generates the first row of an empty grid with values to speed up solving
     * @param grid Empty grid that needs a pre-genned first row
     */
    public void genRandFirstRow(Cell[][] grid) {
        ArrayList<Integer> tempList = new ArrayList<Integer>();
        for(int x = 1; x < 10; tempList.add(x), x++);
        Collections.shuffle(tempList);
        for(int c = 0; c < 9; grid[0][c].setValue(tempList.get(c)), c++);
    }

      
    /**
     * Validates the board's consistency from the placement of a cell into an index
     * Used by solvers above to validate cell value
     * @param board Board to check consistency of
     * @param row Row of affected cell
     * @param col Column of affected cell
     * @param c Value of affected cell
     * @return True if the cell does not spoil the board
     */
    private boolean consistent(Cell[][] board, int row, int col, int c) {        
        //Checks columns and rows          
        for(int i = 0; i < 9; i++){
            if(board[row][i].getValue() == c) return false;
            if(board[i][col].getValue() == c) return false;
        }
        
        //Checks a 3x3 square segment of the grid
        int rowStart = row - row % 3; 
        int colStart = col - col % 3;
        for(int m = 0; m < 3; m++){
            for(int k = 0; k < 3; k++){
                if(board[rowStart + k][colStart + m].getValue() == c) return false;
            }
        }
        return true;
    }
    
    /**
     * Used for debugging grids, prints to console a somewhat-grid-shaped
     *  matrice of cell values
     * @param g Grid to print
     * @deprecated Only used for debugging
     */
    @Deprecated
    public void printSolution(Cell[][] g){
        for(int i = 0; i < 9; i++){
            String row = "";
            for(int j = 0; j < 9; j++){
                row += g[i][j].getValue() + " ";
            }
            System.out.println(row);
        }
        System.out.println();
    }
}
