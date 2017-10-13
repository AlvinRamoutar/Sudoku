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

import java.util.HashMap;

/**
 * Cell class; component of Logic package for Sudoku puzzles
 */
public class Grid {
    
    boolean isLoadedIn = false;
    protected Cell[][] cells = new Cell[9][9];
    private boolean isUsingCustomSymbols;
    private HashMap<Integer, String> symbols = new HashMap<Integer, String>();
    
    /**
     * Constructs a grid while initializing cells within it
     */
    public Grid() {
        for(int o = 0; o < 9; o++) {
            for(int i = 0; i < 9; i++) {
                cells[o][i] = new Cell(deriveSquare(o, i), o, i);
            }
        }
        this.isUsingCustomSymbols = false;
    }
    
    /**
     * Constructs a grid using custom symbols
     * @param symbolFolderURL Location of symbol set
     */
    public Grid(String symbolFolderURL) {
        for(int o = 0; o < 9; o++) {
            for(int i = 0; i < 9; i++) {
                cells[o][i] = new Cell(deriveSquare(o, i), o, i);
            }
        }
        
        //Defines HashMap for custom symbols
        this.isUsingCustomSymbols = true;
        DataHandler dh = new DataHandler();
        String[] symbolsFromFolder = dh.getSymbolURLs(symbolFolderURL);
        for(int i = 1; i < 10; i++) {
            symbols.put(i, symbolsFromFolder[i - 1]);
        }
    }
    
    /**
     * Constructs a grid from a pre-defined supply of cells
     * @param cells Pre-defined supply of cells
     */
    public Grid(Cell[][] cells) {
         System.arraycopy(cells, 0, this.cells, 0, cells.length);
         isUsingCustomSymbols = false;
    }
    
    /**
     * Constructs a grid from another supplied grid with cells organized as a long
     *  code string (from a saved game file)
     * @param rawGrid String of cell data
     * @param iLI Is the grid loaded in
     */
    public Grid(String rawGrid, boolean iLI) {
        this();
        this.isLoadedIn = iLI;
        
        int stringChar = 0;
        for(int o = 0; o < 9; o++) {
            for(int i = 0; i < 9; i++) {
                this.cells[o][i].setValue(
                        Character.getNumericValue(rawGrid.charAt(stringChar)));
                stringChar++;
            }
        }
    }
     
    //Various getters and setters for Grid cells and grid fields
    public Cell[][] getCells() { return this.cells; }
    public void setCells(Cell[][] c) { System.arraycopy(c, 0, this.cells, 0, c.length); }
    public boolean isUsingCustomSymbols() { return this.isUsingCustomSymbols; }
    public String getCellIMGURL(int v) { return this.symbols.get(v); }
    
   
    /**
     * Checks if the row is valid based on supplied cell
     * @param board Grid to validate
     * @param c Cell in which affects the integrity of the row
     * @return True if the row is valid
     */
    public boolean isRowGood(Cell[][] board, Cell c) {
        for(int i = 0; i < 9; i++) {
            if((c.getValue() == board[c.getRow()][i].getValue()) && !board[c.getRow()][i].equals(c)) {
                return false;
            }
        }
        return true;
    }
    /**
     * Checks if the column is valid based on supplied cell
     * @param board Grid to validate
     * @param c Cell in which affects the integrity of the column
     * @return True if the column is valid
     */
    public boolean isColGood(Cell[][] board, Cell c) {
        for(int o = 0; o < 9; o++) {
            if((c.getValue() == board[o][c.getCol()].getValue()) && !board[o][c.getCol()].equals(c)) {
                return false;
            }
        }
        return true;
    }
    /**
     * Checks if a 3x3 grid space (square) is valid based on supplied cell
     * @param board Grid to validate
     * @param c Cell in which affects the integrity of the square
     * @return True if the square is valid
     */
    public boolean isSquareGood(Cell[][] board, Cell c) {
        for(int o = 0; o < 9; o++) {
            for(int i = 0; i < 9; i++) {
                if((board[o][i].getSquare() == c.getSquare()) && !board[o][i].equals(c)) {
                    if(board[o][i].getValue() == c.getValue()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }        
          
    /**
     * Derives the square a cell is in based on its row and column
     * @param row Row of cell
     * @param col Column of cell
     * @return 3x3 square location on grid [1-9]
     */
    private int deriveSquare(int row, int col) {
        int r = row + 3;
        int c = col + 3;
        int square = 0;
        if((r - 3) < 3) {
            //Squares 1, 2, 3
            if((c - 3 < 3)) {
                square = 1;
            } else if((c - 3) < 6) {
                square = 2;
            } else if((c - 3) < 9) {
                square = 3;
            }
        } else if((r - 3) < 6) {
            //Squares 4, 5, 6
            if((c - 3) < 3) {
                square = 4;
            } else if((c - 3) < 6) {
                square = 5;
            } else if((c - 3) < 9) {
                square = 6;
            }
        } else if((r - 3) < 9) {
            //Squares 7, 8, 9
            if((c - 3) < 3) {
                square = 7;
            } else if((c - 3) < 6) {
                square = 8;
            } else if((c - 3) < 9) {
                square = 9;
            }
        }
        return square;
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
    
    /**
     * Equality check between two grids based upon the values of their cells
     * @param obj Grid to compare
     * @return True if the grids are identical
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
                return false;
        }
        if(!(obj instanceof Grid)) {
                return false;
        }        
        Grid someGrid = (Grid)obj;
        
        for(int o = 0; o < 9; o++) {
            for(int i = 0; i < 9; i++) {
                if(this.getCells()[o][i].getValue() != someGrid.getCells()[o][i].getValue()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Squishes all the cell values in a grid into a single, long string of numbers
     * @return A string of numbers containing all cell values
     */
    @Override
    public String toString() {
        String temp = "";
        for(int o = 0; o < 9; o++) {
            for(int i = 0; i < 9; i++) {
                temp += this.cells[o][i].getValue();
            }
        }
        return temp;
    }
}
