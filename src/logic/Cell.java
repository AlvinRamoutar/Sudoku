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


/**
 * Cell class; component of Logic package for Sudoku puzzles
 */
public class Cell {
    
    private boolean isValid = false;
    private boolean isFilled;
    private boolean isMarkup;
    private int value;
    private int square;
    private int row;
    private int col;
    
    /**
     * Constructs a new cell
     * @param s 3x3 square location on grid
     * @param r Row
     * @param c Column
     */
    Cell(int s, int r, int c) {
        this.value = 0;
        this.square = s;
        this.row = r;
        this.col = c;
        this.isFilled = false;
        this.isMarkup = false;
    }
    
    /**
     * Sets value of cell
     * @param value Cell value between 1-9
     */
    public void setValue(int value) {
        this.value = value;
        if(value == 0) {
            isFilled = false;
        } else {
            isFilled = true;
        }
    }
    public int getValue() { return this.value; }
    
    //Various getters and setters for fields
    public int getRow() { return this.row; }
    public int getCol() { return this.col; }
    public int getSquare() { return this.square; }
    public void setRow(int r) { this.row = r; }
    public void setCol(int c) { this.col = c; }
    public void setSquare(int s) { this.square = s; }
    public void setValid(boolean v) { this.isValid = v; }
    public boolean isValid() { return this.isValid; }
    public void setFilled(boolean v) { this.isFilled = v; }
    public boolean isFilled() { return this.isFilled; }
    public void setMarkup(boolean v) { this.isMarkup = v; }
    public boolean isMarkup() { return this.isMarkup; }
    
    /**
     * Checks cell equality between two cells based upon their location on the grid
     * @param obj Cell to compare
     * @return True if the cells are equal
     */
    @Override
    public boolean equals(Object obj) {
        Cell tempCell = (Cell)obj;
        if((this.getRow() == tempCell.getRow()) &&
                (this.getCol() == tempCell.getCol())) {
            return true;
        } else {
            return false;
        }
    }
}
