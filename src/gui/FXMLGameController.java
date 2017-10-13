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

package gui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import logic.*;
import static logic.TabDataHolder.*;
import org.json.simple.JSONObject;

/**
 * Document controller for Sudoku Game tabs.
 * Manages all actions and events correlating to the game window.
 */
public class FXMLGameController implements Initializable {

    private Puzzle p;

    @FXML
    private Label durationText, puzzleIDText, difficultyText, statusText;
    
    //TextFields for every cell
    @FXML
    private TextField c00, c01, c02, c03, c04, c05, c06, c07, c08,
            c10, c11, c12, c13, c14, c15, c16, c17, c18,
            c20, c21, c22, c23, c24, c25, c26, c27, c28,
            c30, c31, c32, c33, c34, c35, c36, c37, c38,
            c40, c41, c42, c43, c44, c45, c46, c47, c48,
            c50, c51, c52, c53, c54, c55, c56, c57, c58,
            c60, c61, c62, c63, c64, c65, c66, c67, c68,
            c70, c71, c72, c73, c74, c75, c76, c77, c78,
            c80, c81, c82, c83, c84, c85, c86, c87, c88;
    private TextField[][] cTFs = new TextField[9][9];
    
    @FXML
    private ToggleButton showHints;
    private boolean isPlayerANoob = true;
    
    //Objects for Stopwatch
    private Timeline timeline;
    private DoubleProperty timeSeconds = new SimpleDoubleProperty();
    private Duration time = Duration.ZERO;


    //Sidebar messageBox components
    @FXML
    VBox messageBox;
    @FXML
    Label messageTitle;
    @FXML
    Label messageBody, messageBody2;
    @FXML
    TextField messageInput;
    @FXML
    Button messageYes;
    @FXML
    Button messageNo;
    
    //Sidebar Symbol Legend
    @FXML
    GridPane symbolLegend;
    @FXML
    ImageView s1, s2, s3, s4, s5, s6, s7, s8, s9;

    /**
     * Initialization method for FXML.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Grab information necessary to create puzzle from TabDataHolder.
        if (!tempPuzzleConstruct.isEmpty()) {
            String[] data = tempPuzzleConstruct.get(tempPuzzleConstruct.size() - 1).split(",");
            if (data.length == 3) {
                p = new Puzzle(Integer.parseInt(data[0]), data[1], data[2]);
            } else {
                p = new Puzzle(Integer.parseInt(data[0]), data[1]);
            }
        } else {
            JSONObject jData = tempPuzzleJSONConstruct.get(0);
            p = new Puzzle(jData);
        }
        
        //Set sidebar stats for Puzzle
        puzzleIDText.setText(p.getID());
        statusText.setText(p.resolveStatus());
        difficultyText.setText((p.getDifficulty() == 0) ? "Custom" : p.getDifficulty() + "");

        //Creates and starts a stopwatch using FX Timeline.
        durationText.textProperty().bind(timeSeconds.asString());
        timeline = new Timeline(
                new KeyFrame(Duration.millis(100), (ActionEvent t) -> {
                    Duration duration = ((KeyFrame) t.getSource()).getTime();
                    time = time.add(duration);
                    timeSeconds.set(time.toSeconds() + p.getDuration());
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        if(p.getStatus() == 0)
            timeline.play();
        
        
        //Array for accessing TextFields
        TextField[][] tempcTFs = {{c00, c01, c02, c03, c04, c05, c06, c07, c08},
        {c10, c11, c12, c13, c14, c15, c16, c17, c18},
        {c20, c21, c22, c23, c24, c25, c26, c27, c28},
        {c30, c31, c32, c33, c34, c35, c36, c37, c38},
        {c40, c41, c42, c43, c44, c45, c46, c47, c48},
        {c50, c51, c52, c53, c54, c55, c56, c57, c58},
        {c60, c61, c62, c63, c64, c65, c66, c67, c68},
        {c70, c71, c72, c73, c74, c75, c76, c77, c78},
        {c80, c81, c82, c83, c84, c85, c86, c87, c88}};
        cTFs = tempcTFs.clone();        
        
        //Populates grid in window with puzzle.
        for (int o = 0; o < 9; o++) {
            for (int i = 0; i < 9; i++) {
                cTFs[o][i].setText((p.getGrid().getCells()[o][i].getValue() == 0)
                        ? " " : p.getGrid().getCells()[o][i].getValue() + "");
                if (p.getGrid().isUsingCustomSymbols()) {
                    setCellIMG(cTFs[o][i]);
                    cTFs[o][i].setText(" ");
                }
                if (p.getGrid().getCells()[o][i].isValid()) {
                    cTFs[o][i].setEditable(false);
                }           
                cTFs[o][i].setPrefWidth(64);
                cTFs[o][i].setPrefHeight(64);
            }
        }
        
        ImageView[] s = {s1, s2, s3, s4, s5, s6, s7, s8, s9};
        if(p.getGrid().isUsingCustomSymbols()) {
            setSymbolLegend(s);
        }
    }

    /**
     * Validates and handles submission of a cell's input
     * @param e Capturing key of keyboard input
     */
    @FXML
    public void submitCell(KeyEvent e) {
        Node node = (Node) e.getSource();
        String data = (String) (node.getUserData() + "");
        String value = (data + "").substring(1);
        int r = Integer.parseInt(value.charAt(0) + "");
        int c = Integer.parseInt(value.charAt(1) + "");
        String rawVal = cTFs[r][c].getText().trim();
        
        //If key pressed is not ALT (Markup) or DELETE (Clear)
        if ((e.getCode() != e.getCode().ALT) && (e.getCode() != e.getCode().DELETE)
                && !p.getGrid().getCells()[r][c].isMarkup()) {
            if (isPlayerANoob) {
                resetHighlighting(false);
            }
            
            //If input is not blank, and if the current cell isn't already valid
            if (!rawVal.equals("") && !p.getGrid().getCells()[r][c].isValid()) {
                try {
                    int val = Integer.parseInt(cTFs[r][c].getText().trim());
                    if (val < 1 || val > 9) {
                        throw new NumberFormatException();
                    }
                    
                    //Push input to active grid
                    p.getGrid().getCells()[r][c].setValue(val);
                    p.getGrid().getCells()[r][c].setFilled(true);
                    
                    //Perform checks on the validity of the input value.
                    boolean isRowGood = p.getGrid().isRowGood(p.getGrid().getCells(), p.getGrid().getCells()[r][c]);
                    boolean isColGood = p.getGrid().isColGood(p.getGrid().getCells(), p.getGrid().getCells()[r][c]);
                    boolean isSquareGood = p.getGrid().isSquareGood(p.getGrid().getCells(), p.getGrid().getCells()[r][c]);
                    
                    //If the input value is the correct value (matches solved grid)
                    if (val == p.getSolvedGrid().getCells()[r][c].getValue()) {
                        p.getGrid().getCells()[r][c].setValid(true);
                        if (p.getGrid().isUsingCustomSymbols()) {
                            setCellIMG(cTFs[r][c]);
                            cTFs[r][c].setText(" ");
                        }
                        cTFs[r][c].setEditable(false);
                    } else { //Clear cell, and/or highlight bad cells.
                        if (isPlayerANoob) {
                            highlightErrors(r, c, isRowGood, isColGood, isSquareGood);
                        }
                        p.getGrid().getCells()[r][c].setValue(0);
                        p.getGrid().getCells()[r][c].setFilled(false);
                    }
                } catch (NumberFormatException nfe) {
                    //If input is not a number, or an invalid number
                    if (!p.getGrid().getCells()[r][c].isMarkup()) {
                        cTFs[r][c].setText(" ");
                    }
                }
                
            //If input is not a number, or an invalid number   
            } else {
                if (!p.getGrid().getCells()[r][c].isMarkup() && !p.getGrid().getCells()[r][c].isValid()) {
                    cTFs[r][c].setText(" ");
                }
            }
            
            //Check after every input if it completes the puzzle
            isPuzzleFinished();
            
        //Submit a markup value to markup cell, or converts it to one   
        } else if ((e.getCode() == e.getCode().ALT) && (!p.getGrid().getCells()[r][c].isValid())) {
            resetHighlighting(false);
            highlightMarkup(r, c, rawVal);
            p.getGrid().getCells()[r][c].setFilled(false);
            p.getGrid().getCells()[r][c].setValue(0);
            
        //Clears a cell    
        } else if (e.getCode() == e.getCode().DELETE) {
            p.getGrid().getCells()[r][c].setMarkup(false);
            resetHighlighting(false);
            cTFs[r][c].setText(" ");
        }
    }

    /**
     * Toggle whether or not to show hints (highlighting incorrect parts of board)
     */
    @FXML
    public void setShowHints() {
        if (showHints.isSelected()) {
            isPlayerANoob = true;
        } else {
            isPlayerANoob = false;
            resetHighlighting(false);
        }
    }
    
    /**
     * Launches a website with help information.
     * Also the only use of AWT. Spare me please!
     */
    @FXML
    public void showHelp() {
        try {
            java.awt.Desktop.getDesktop().browse(new URL("http://alvinr.ca/sudoku").toURI());
        } catch (MalformedURLException ex) {
        } catch (URISyntaxException ex) {
        } catch (IOException ex) {
        }
    }

    /**
     * Spawns message box on sidebar to confirm Retire of game
     */
    @FXML
    public void retireMessageBox() {
        messageBox.setVisible(true);
        messageTitle.setText("Retire");
        messageBody.setText("Are you sure you want to retire?");
        messageInput.setVisible(false);
        messageYes.setText("Yes");
        messageYes.setOnAction((ActionEvent e) -> {
            FXMLDocumentController.getFXMLDC().removeTab();
        });
        messageNo.setText("No");
        messageNo.setOnAction((ActionEvent e) -> {
            resetMessageBox();
        });
    }

    /**
     * Spawns message box on sidebar to confirm Auto-Solve of game
     */
    @FXML
    public void autoSolveMessageBox() {
        messageBox.setVisible(true);
        messageTitle.setText("Autosolve");
        messageBody.setText("Are you sure you want to auto-solve?");
        messageInput.setVisible(false);
        messageYes.setText("Yes");
        messageYes.setOnAction((ActionEvent e) -> {
            //Iterate through all cells in the active grid, and set them equal to
            //  the solved grid.
            for (int o = 0; o < 9; o++) {
                for (int i = 0; i < 9; i++) {
                    resetHighlighting(true);
                    p.getGrid().getCells()[o][i].setMarkup(false);
                    cTFs[o][i].setText(p.getSolvedGrid().getCells()[o][i].getValue() + "");
                    p.getGrid().getCells()[o][i].setValue(p.getSolvedGrid().getCells()[o][i].getValue());
                    p.getGrid().getCells()[o][i].setValid(true);
                    if(p.getGrid().isUsingCustomSymbols()) {
                        setCellIMG(cTFs[o][i]);
                        cTFs[o][i].setText(" ");
                    }
                    p.isPuzzleDone(timeSeconds.get() + "");
                    p.setStatus(2);
                    statusText.setText("Auto-Solved");
                    timeline.stop();
                    resetMessageBox();
                }
            }
        });
        messageNo.setText("No");
        messageNo.setOnAction((ActionEvent e) -> {
            resetMessageBox();
        });
    }

    /**
     * Spawns message box on sidebar to confirm E-Mail Puzzle to friend.
     */
    @FXML
    public void emailMessageBox() {
        messageBox.setVisible(true);
        messageTitle.setText("E-Mail puzzle to Friend");
        messageBody.setText("Enter a valid e-mail below");
        messageBody2.setText("*app may freeze temporarily");
        messageInput.setText("example@email.com");
        messageYes.setText("Send");
        messageYes.setOnAction((ActionEvent e) -> {
            //Grabs JSON formatted text from puzzle, and creates an e-mail with
            //  DataHandler.
            String email = messageInput.getText().trim();
            DataHandler dh = new DataHandler();
            dh.prepareEmailFile(p.toString(), email);
            resetMessageBox();
        });
        messageNo.setText("Scrap");
        messageNo.setOnAction((ActionEvent e) -> {
            resetMessageBox();
        });
    }

    /**
     * Resets message box on sidebar to defaults and invisible
     */
    public void resetMessageBox() {
        messageTitle.setText("{Title}");
        messageBody.setText("{Body}");
        messageBody2.setText(" ");
        messageInput.setText(" ");
        messageYes.setText("Yes");
        messageNo.setText("No");
        messageTitle.setVisible(true);
        messageBody.setVisible(true);
        messageInput.setVisible(true);
        messageYes.setVisible(true);
        messageNo.setVisible(true);
        messageBox.setVisible(false);
        messageYes.setOnAction(null);
        messageNo.setOnAction(null);
    }
    
    
    /**
     * Constructs the symbol legend on the bottom of the sidebar
     */
    private void setSymbolLegend(ImageView[] s) {
        for(int i = 0; i < 9; i++) {
            s[i].setImage(new Image(p.getGrid().getCellIMGURL(i+1)));
        }
        symbolLegend.setVisible(true);
    }

    /**
     * Sets an Icon as the cell's background image 
     * @param c TextField of cell
     */
    public void setCellIMG(TextField c) {
        if (!c.getText().trim().equals("")) {
            String tmpURL = p.getGrid().getCellIMGURL(Integer.parseInt(c.getText().trim()));
            tmpURL = tmpURL.replace("file:/", "file:///");
            System.out.println(tmpURL);
            String tmpStyle = "-fx-background-image: url(\"" + tmpURL + "\"); "
                    + "-fx-background-size: 64; -fx-background-repeat: no-repeat;"
                    + " -fx-background-position: center;";
            c.setStyle(tmpStyle);
        }
    }

    /**
     * Saves the current game to a location of user's choice via FileChooser
     *  from DataHandler
     */
    public void saveGame() {
        DataHandler dh = new DataHandler();
        p.setDuration((int) timeSeconds.get());
        ArrayList<String> tmp = new ArrayList<String>();
        tmp.add(p.toString());
        dh.saveFile(Window.getPStage(), "Save current Sudoku Game", "Sudoku Save",
                "sud", p.getID(), tmp);
    }
    
    
    /**
     * Highlights grid areas where incorrect
     * @param row Row of grid
     * @param col Column of grid
     * @param r Is the row invalid
     * @param c Is the column invalid
     * @param s Is the square invalid
     */
    public void highlightErrors(int row, int col, boolean r, boolean c, boolean s) {
        Node node = cTFs[row][col];
        node.getStyleClass().add("badCell");
        if (!r) {
            for (int x = 0; x < 9; x++) {
                Node node2 = cTFs[row][x];
                node2.getStyleClass().add("affectedCell");
            }
        }
        if (!c) {
            for (int x = 0; x < 9; x++) {
                Node node2 = cTFs[x][col];
                node2.getStyleClass().add("affectedCell");
            }
        }
        if (!s) {
            for (int o = 0; o < 9; o++) {
                for (int i = 0; i < 9; i++) {
                    if (p.getGrid().getCells()[o][i].getSquare()
                            == p.getGrid().getCells()[row][col].getSquare()) {
                        Node node2 = cTFs[o][i];
                        node2.getStyleClass().add("affectedCell");
                    }
                }
            }
        }
    }

    /**
     * Simple highlights for a Markup cell
     * @param r Row of grid
     * @param c Column of grid
     * @param rV Raw input from grid
     */
    public void highlightMarkup(int r, int c, String rV) {
            cTFs[r][c].getStyleClass().add("markupCell");
            p.getGrid().getCells()[r][c].setMarkup(true);
            cTFs[r][c].setText(rV);
    }
    
    
    /**
     * Resets all highlighting on the board
     * @param bf Brute-force reset, will also clear markup
     */
    public void resetHighlighting(boolean bf) {
        for (int o = 0; o < 9; o++) {
            for (int i = 0; i < 9; i++) {
                Node node = cTFs[o][i];
                if (!p.getGrid().getCells()[o][i].isMarkup() || bf) {
                    node.getStyleClass().clear();
                    node.getStyleClass().add("cell");
                }
            }
        }
    }

    /**
     * Stops stopwatch and changes puzzle status upon completion
     */
    public void isPuzzleFinished() {
        if (p.isPuzzleDone(timeSeconds.get() + "")) {
            statusText.setText("Complete");
            timeline.stop();
        }
    }
}