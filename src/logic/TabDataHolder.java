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
import org.json.simple.JSONObject;

/**
 * Holds data when transferring between FXMLDocumentController and FXMLGameController
 */
public class TabDataHolder {
    
    public static ArrayList<String> tempPuzzleConstruct = new ArrayList<String>();
    public static ArrayList<JSONObject> tempPuzzleJSONConstruct = new ArrayList<JSONObject>();
    public static int gameCount = 0;

}