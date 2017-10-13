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

import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;
import org.json.simple.JSONObject;

/**
 * Main puzzle object class that handles the manipulation of all logical
 *  puzzle operations, such as inputting values, checks, and validations
 */
public class Puzzle {

    protected Grid activeGrid;
    private Grid solvedGrid;
    final private int difficulty;
    final private String id;
    private LocalTime timeStarted;
    private int duration = 0;
    private int status; //0-incomplete, 1-solved, 2-autosolved, 3-retired
    private boolean showHints = false;
    private Solver s;
    private Pattern p;
    
    /**
     * Creates a new puzzle with a supplied pattern
     * Generates puzzle after puzzle with a new pattern until a Puzzle is created
     *  in which matches the pattern, and is not ambiguous (contains only one
     *  (solution). 
     * @param d Difficulty should the pattern be Random, or invalid
     * @param pattern User-preferred pattern
     */
    public Puzzle(int d, String pattern) {
        boolean isPuzzleGood = false;
        this.difficulty = d;
        
        //Constantly create puzzles until a non-ambiguous case is created
        do {
            s = new Solver();
            this.solvedGrid = new Grid();
            this.activeGrid = new Grid();

            //Merges the solved grid from solver with SolvedGrid.
            this.mergeGrids(s.initSolve(this.solvedGrid), solvedGrid, false);

            //Merges the SolvedGrid with the ActiveGrid.
            this.mergeGrids(solvedGrid, activeGrid, false);

            //Merges the ActiveGrid with the Patterned Grid.
            if (pattern.equals("Random")) {
                p = new Pattern(difficulty);
            } else {
                p = new Pattern(pattern);
            }
            Grid tempPG = p.format(activeGrid);
            this.mergeGrids(tempPG, activeGrid, true);
            
            //Check for ambiguity
            if (s.initAmbiguityCheck(activeGrid) == false) {
                isPuzzleGood = true;
            } else {
                s = null;
                p = null;
            }
        } while (isPuzzleGood == false);
        
        //Puzzle creation done at this point
        this.id = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE - 1) + "_" + this.difficulty;
        this.timeStarted = LocalTime.now();
        this.status = 0;
    }
    
    /**
     * Creates a new puzzle with a supplied pattern and symbol set
     * Generates puzzle after puzzle with a new pattern until a Puzzle is created
     *  in which matches the pattern, and is not ambiguous (contains only one
     *  (solution). 
     * @param d Difficulty should the pattern be Random, or invalid
     * @param pattern User-preferred pattern
     * @param symbolFolderURL Directory containing symbol set
     */
    public Puzzle(int d, String symbolFolderURL, String pattern) {
        boolean isPuzzleGood = false;
        //Now, we begin generating puzzle after puzzle until we create one with
        // a single, valid solution.
        do {
            s = new Solver();
            this.solvedGrid = new Grid();
            this.activeGrid = new Grid(symbolFolderURL);

            //Merges the solved grid from solver with SolvedGrid.
            this.mergeGrids(s.initSolve(this.solvedGrid), solvedGrid, false);

            //Merges the SolvedGrid with the ActiveGrid.
            this.mergeGrids(solvedGrid, activeGrid, false);

            //Merges the ActiveGrid with the Patterned Grid.
            p = new Pattern(pattern);
            Grid tempPG = p.format(activeGrid);
            this.mergeGrids(tempPG, activeGrid, true);
            
            //Check for ambiguity
            if (s.initAmbiguityCheck(activeGrid) == false) {
                isPuzzleGood = true;
            } else {
                s = null;
                p = null;
            }
        } while (isPuzzleGood == false);

        //Puzzle creation done at this point
        this.difficulty = d;
        this.id = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE - 1) + "_" + this.difficulty;
        this.timeStarted = LocalTime.now();
        this.status = 0;
    }

    /**
     * Creates a puzzle from JSON data, used to resume a saved game
     * @param jobj Saved game data formatted in JSON
     */
    public Puzzle(JSONObject jobj) {
        this.id = (String) jobj.get("id");
        this.difficulty = (int) java.lang.Math.toIntExact((Long) jobj.get("difficulty"));
        this.duration = (int) java.lang.Math.toIntExact((Long) jobj.get("duration"));
        this.status = (int) java.lang.Math.toIntExact((Long) jobj.get("status"));
        this.activeGrid = new Grid((String) jobj.get("activeGrid"), true);
        this.solvedGrid = new Grid((String) jobj.get("solvedGrid"), true);
    }
    
    //Various setters and getters for puzzle grids and fields
    public Grid getGrid() { return this.activeGrid; }
    public Grid getSolvedGrid() { return this.solvedGrid; }
    public int getDifficulty() { return this.difficulty; }
    public String getID() { return this.id; }
    public void setStartTime() { this.timeStarted = LocalTime.now(); }
    public int getDuration() { return this.duration; }
    public void setDuration(int d) { this.duration = d; }
    public int getStatus() { return this.status; }
    public void setStatus(int s) { this.status = s; }
    public void setShowHints(boolean sh) { this.showHints = sh; }
    public boolean isShowHints() { return this.showHints; }
    
    /**
     * Resolves supplied status code into a more meaningful status message
     * @return Status message 
     */
    public String resolveStatus() {
        switch (this.getStatus()) {
            case 0:
                return "Incomplete";
            case 1:
                return "Solved";
            case 2:
                return "Auto-Solved";
            case 3:
                return "Retired";
            default:
                return "Unknown";
        }
    }

    /**
     * Performs final rights for the active puzzle object upon its completion
     * Specifically, merges duration and changes status
     * @param d Duration (elapsed time)
     * @return True if puzzle is complete
     */
    public boolean isPuzzleDone(String d) {
        if(this.getGrid().equals(this.getSolvedGrid())) {
            this.setStatus(1);
            this.duration = Integer.parseInt(d.substring(0, d.indexOf(".")));
            return true;
        }
        return false;
    }

    /**
     * Merges two grids' cells
     * @param s Source (solved) Grid
     * @param a Destination (active) Grid
     * @param isPattern True if grid had an active pattern
     */
    private void mergeGrids(Grid s, Grid a, boolean isPattern) {
        //Handles merging of playable grids and stores into 'solved' grid
        if (!isPattern) {
            for (int o = 0; o < 9; o++) {
                for (int i = 0; i < 9; i++) {
                    a.cells[o][i].setValue(s.cells[o][i].getValue());
                    a.cells[o][i].setRow(s.cells[o][i].getRow());
                    a.cells[o][i].setCol(s.cells[o][i].getCol());
                    a.cells[o][i].setSquare(s.cells[o][i].getSquare());
                    a.cells[o][i].setValid(true);
                    a.cells[o][i].setFilled(true);
                }
            }
        //Handles merging patterned grid with active grid
        } else {
            for (int o = 0; o < 9; o++) {
                for (int i = 0; i < 9; i++) {
                    a.cells[o][i].setValue(s.cells[o][i].getValue());
                    a.cells[o][i].setRow(s.cells[o][i].getRow());
                    a.cells[o][i].setCol(s.cells[o][i].getCol());
                    a.cells[o][i].setSquare(s.cells[o][i].getSquare());
                    a.cells[o][i].setValid(s.cells[o][i].isValid());
                    a.cells[o][i].setFilled(s.cells[o][i].isFilled());
                }
            }
        }
    }
    
    /**
     * Compiles active puzzle into parsable JSON to save.
     * @return JSON-formatted puzzle data
     */
    @Override
    public String toString() {
        JSONObject jobj = new JSONObject();
        jobj.put("id", this.id);
        jobj.put("difficulty", this.difficulty);
        jobj.put("duration", this.duration);
        jobj.put("status", this.status);
        jobj.put("activeGrid", this.activeGrid.toString());
        jobj.put("solvedGrid", this.solvedGrid.toString());
        return jobj.toJSONString();
    }
}
