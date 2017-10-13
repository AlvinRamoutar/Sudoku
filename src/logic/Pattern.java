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

import java.util.concurrent.ThreadLocalRandom;

/**
 * Main Class containing various patterns to shape a grid into
 */
public class Pattern {
    
    private boolean[][] grid = new boolean[9][9];
    private int difficulty;
    
    /**
     * Initializes the construction of a pattern based on user selection
     * @param p User-selected pattern
     */
    Pattern(String p) {
        this.difficulty = 28;
        initPattern();
        switch(p) {
            case "Spring": spring(); break;
            case "Star": star(); break;
            default: random(); break;
        }
    }
    
    /**
     * Initializes a randomized grid based upon supplied difficulty
     * @param d Difficulty between 28-80. Correlates to # of guesses given
     */
    Pattern(int d) {
        if(d < 28 || d > 80) {
            this.difficulty = 28;
        } else {
            this.difficulty = d;
        }
        initPattern();
        random();
    }
    
 
    /*
        - - - N - N - - -
        - N - N N N - N -
        - - N - - - N - -
        - - N N - N N - -
        - N - - - - - N -
        - - N N - N N - -
        - - N - - - N - -
        - N - N N N - N -
        - - - N - N - - -
    */
    private void spring() {
        grid[0][3] = true;
        grid[0][5] = true;
        
        grid[1][1] = true;
        grid[1][3] = true;
        grid[1][4] = true;
        grid[1][5] = true;
        grid[1][7] = true;
        
        grid[2][2] = true;
        grid[2][6] = true;
        
        grid[3][2] = true;
        grid[3][3] = true;
        grid[3][5] = true;
        grid[3][6] = true;
        
        grid[4][1] = true;
        grid[4][7] = true;
        
        grid[5][2] = true;
        grid[5][3] = true;
        grid[5][5] = true;
        grid[5][6] = true;
        
        grid[6][2] = true;
        grid[6][6] = true;
        
        grid[7][1] = true;
        grid[7][3] = true;
        grid[7][4] = true;
        grid[7][5] = true;
        grid[7][7] = true;
        
        grid[8][3] = true;
        grid[8][5] = true;
        
    }   

    /*
        - - - - N - - - -
        - N - N - N - N -
        - - N N - N N - -
        - N N - - - N N -
        N - - - - - - - N
        - N N - - - N N -
        - - N N - N N - -
        - N - N - N - N -
        - - - - N - - - -
    */
    private void star() {
        grid[0][4] = true;
        
        grid[1][1] = true;
        grid[1][3] = true;
        grid[1][5] = true;
        grid[1][7] = true;
        
        grid[2][2] = true;
        grid[2][3] = true;
        grid[2][5] = true;
        grid[2][6] = true;
        
        grid[3][1] = true;
        grid[3][2] = true;
        grid[3][6] = true;
        grid[3][7] = true;
        
        grid[4][0] = true;
        grid[4][8] = true;
        
        grid[5][1] = true;
        grid[5][2] = true;
        grid[5][6] = true;
        grid[5][7] = true;
        
        grid[6][2] = true;
        grid[6][3] = true;
        grid[6][5] = true;
        grid[6][6] = true;
        
        grid[7][1] = true;
        grid[7][3] = true;
        grid[7][5] = true;
        grid[7][7] = true;
        
        grid[8][4] = true;
    }
    
    /**
     * Randomly selects cells as guesses, everything else being clear
     */
    private void random() {
        int validCells = 0;

        do {
            int r = ThreadLocalRandom.current().nextInt(0, 9);
            int c = ThreadLocalRandom.current().nextInt(0, 9);
            if(grid[r][c] != true) {
                grid[r][c] = true;
                validCells++;
            }
        } while(validCells < this.difficulty);
    }    
    
    /**
     * Configures supplied activeGrid to the pattern
     * @param g activeGrid from puzzle
     * @return activeGrid with pattern formatted (only cells with guesses remain)
     */
    public Grid format(Grid g) {
        Grid formattedGrid = g;
        for(int o = 0; o < 9; o++) {
            for(int i = 0; i < 9; i++) {
                if(this.grid[o][i] != true) {
                    g.cells[o][i].setValue(0);
                    g.cells[o][i].setValid(false);
                    g.cells[o][i].setFilled(false);
                }
            }
        }
        return formattedGrid;
    }

    /**
     * Initializes a pattern by clearing the grid
     */
    private void initPattern() {
        for(int o = 0; o < 9; o++) {
            for(int i = 0; i < 9; i++) {
                grid[o][i] = false;
            }
        }
    }
    
}

