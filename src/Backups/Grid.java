/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import javafx.scene.image.Image;

/**
 *
 * @author Alvin
 */
public class Grid {
    
    int difficulty = 28;
    Cell[][] grid = new Cell[9][9];
    HashMap<Integer, Image> glyphs = new HashMap<Integer, Image>();
    
    public Grid() {
        
    }
    
    public Cell[][] getGrid() {
        return this.grid;
    }
    
    public Grid(int difficulty, String symbolURL) {
        this.difficulty = difficulty;
        
                
    }
    
    public void generateEmptyGrid() {
        for(int o = 0; o < 9; o++) {
            for(int i = 0; i < 9; i++) {
                grid[o][i] = new Cell();
                grid[o][i].setSquare(deriveSquare(o, i));
                System.out.println("Created cell @(" + o + "," + i + ") at square" + grid[o][i].getSquare());
            }
        }
    }
    
    public static void printGrid(Cell[][] grid){
        for(int o = 0; o < 9; o++){
            String line = "";
            for(int i = 0; i < 9; i++){
                line += grid[o][i].getValue() + " ";
            }
            System.out.println(line);
        }
        System.out.println();
    }
    

    
    public void bruteForceSolve() {
        boolean isSolved = false;
        int r = 1;
        int c = 0;
        do {
            boolean isValid = false;
            int rN = ThreadLocalRandom.current().nextInt(1, 10);
            grid[r][c].setValue(rN);
            
            //System.out.println("Our random # of the day is... " + rN);
            //System.out.println("Status of checks: " + "1:" + isRowGood(r, c) + ", 2:" + isColumnGood(r, c) + ", 3:" + isSquareGood(r, c));
            if(isRowGood(r, c) && isColumnGood(r, c) && isSquareGood(r, c)) {
                System.out.println("Value works!");
                isValid = true;
            } else if((r != 1 && c != 0)) {
                System.out.println("Value cannot work! Backtracking @ (" + r + ", " + c);
                isValid = false;
                if(c == 0) {
                    r--;
                    c -= 8;
                } else {
                    c--;
                }
            }
            if(isValid) {
                if(c == 8) {
                    c = 0;
                    r++;
                } else {
                    c++;
                }
            } else {
                grid[r][c].setValue(0);
            }
            
            isSolved = (r == 9) ? true: false;
        } while(!isSolved);
        
        grid[6][6].setValue(9);
    }
    
    //Checks
    public boolean isRowGood(int row, int col) {
        for(int cell = 0; cell < 9; cell++) {
            if( (this.grid[row][col] == this.grid[row][cell]) &&
                  col != cell) {
                return false;
            }
        }
        return true;
    }

    public boolean isColumnGood(int row, int col) {
        for(int cell = 0; cell < 9; cell++) {
            if( (this.grid[row][col] == this.grid[cell][col]) && 
                    row != cell) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isSquareGood(int row, int col) {
        for(int cO = 0; cO < 9; cO++) {
            for(int cI = 0; cI < 9; cI++) {
                if(this.grid[row][col].getSquare() == this.grid[cO][cI].getSquare()) {
                    if( (this.grid[row][col].getValue() == this.grid[cO][cI].getValue()) && (cO != row && cI != col)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    
    public int deriveSquare(int row, int col) {
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
}
