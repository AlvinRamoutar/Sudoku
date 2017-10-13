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

import static gui.Window.getPStage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import logic.AudioPlayer;
import logic.DataHandler;
import static logic.TabDataHolder.*;
import org.json.simple.JSONObject;

/**
 * Document controller for Main Sudoku window.
 * Manages all actions and events correlating to the Main Window; specifically
 *  the Start and Options tab.
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    GridPane grid;

    @FXML
    TabPane tabPane;
    
    @FXML
    Accordion mainMenu;
    
    //Definitions for FXML Objects on Start Tab
    @FXML
    Tab startTab;
    @FXML
    AnchorPane startAnchor;
    @FXML
    ChoiceBox patternSelect;
    @FXML
    TextField difficultySelect;
    @FXML
    TextField customSymbolsURL;
    @FXML
    Button symbolURLBrowse;
    @FXML
    Button startNewGame;
    @FXML
    TextField loadGameURL;
    @FXML
    Button browseForSave;
    @FXML
    TextArea savedGameDetails;
    @FXML
    Button resumeGame;

    //Definitions for FXML Objects on Options Tab
    @FXML
    Tab optionsTab;
    @FXML
    TextField menuBackgroundURL, gameBackgroundURL;
    @FXML
    Button menuBackgroundBrowse, gameBackgroundBrowse;
    @FXML
    Button applyMenuBG, applyGameBG;
    @FXML
    ChoiceBox tabSelect;
    @FXML
    Label appliedSettingsStatus;
    @FXML
    TextField audioURL;
    @FXML
    Slider volumeSlider;

    AudioPlayer ap;
    
    public static FXMLDocumentController fxmldc;
    
    /**
     * Initialization method for FXML.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Working Directory = "
                + System.getProperty("user.dir"));

        //Populates ChoiceBox with patterns
        patternSelect.setItems(FXCollections.observableArrayList(
                "Random", "Spring", "Star"));
        patternSelect.setValue("Random");
        
        fxmldc = this;
        TitledPane tp = exitTPCreator();
        mainMenu.getPanes().add(tp);
    }
    
    /**
     * Executes various operations necessary to start a new game.
     */
    @FXML
    public void startGame() {
        DataHandler dh = new DataHandler();

        //Verify directory of symbols
        boolean verified = true;
        String csu = customSymbolsURL.getText();
        if (!csu.equals("")) {
            if (!dh.checkDir(csu)) {
                verified = false;
                customSymbolsURL.setText("Invalid Symbol Set! Check 'Help'.");
            }
        }
        
        //Grabs user-selected pattern. Patterns dont have diffculty.
        String pattern = patternSelect.getSelectionModel().getSelectedItem().toString();
        int difficulty = Integer.parseInt(difficultySelect.getText());
        if (pattern.equals("Random")) {
            if (difficulty < 28 || difficulty > 80) {
                difficultySelect.setText("INVD");
                verified = false;
            }
        } else {
            difficulty = 0;
        }

        //Begin creation of Sudoku Game tab for current game session.
        if (verified) {
            try {
                if (csu.equals("")) {
                    tempPuzzleConstruct.add(difficulty + "," + pattern);
                } else {
                    tempPuzzleConstruct.add(difficulty + "," + csu + "," + pattern);
                }
                gameCount++;
                Tab newTab = new Tab();
                AnchorPane getTab = (AnchorPane) FXMLLoader.load(this.getClass().getResource("FXMLGame.fxml"));
                newTab.setContent(getTab);
                newTab.setText("Game " + difficulty + "D" + gameCount);
                tabPane.getTabs().add(tabPane.getTabs().size() - 1, newTab);
                tabPane.getSelectionModel().select(tabPane.getTabs().size() - 2);
                updateTabList();
            } catch (IOException ioe) {
                System.out.println("Error loading FXML for Sudoku Game Tab: " + ioe);
            }
        }
    }

    /**
     * Launches a FileChooser from DataHandler for symbol directory
     */
    @FXML
    private void browseForSymbolSet() {
        DataHandler dh = new DataHandler();
        String url = dh.openDir(getPStage(), "Browse for Custom Symbol Directory");
        customSymbolsURL.setText(url);
    }
    
    /**
     * Grabs input difficulty setting
     */
    @FXML
    private void selectDifficulty() {
        try {
            int difficulty = Integer.parseInt(difficultySelect.getText());
        } catch (NumberFormatException nfe) {
            difficultySelect.setText("32");
        }
    }
    
    /**
     * Launches a FileChooser from DataHandler for retrieving a saved game file
     */
    @FXML
    private void browseSaveURL() {
        DataHandler dh = new DataHandler();
        File s = dh.openFile(Window.getPStage(), "Load Saved Game", "Sudoku Save", "sud");
        JSONObject saveData = dh.openJSON(s);
        loadGameURL.setText(s.toPath().toString());
        savedGameDetails.setText("ID: " + saveData.get("id") + "\n"
                + "Difficulty: " + saveData.get("difficulty") + "\n"
                + "Duration: " + saveData.get("duration") + "\n"
                + "Status Code: " + saveData.get("status") + "\n"
                + "Active Grid Code: " + saveData.get("activeGrid"));
    }
    
    /**
     * Executes various operations necessary to resume a game from file
     */
    @FXML
    private void resumeGame() {
        if (!savedGameDetails.getText().contains("null") && !loadGameURL.getText().equals("")) {
            try {
                //Parse JSON save first with DataHandler
                DataHandler dh = new DataHandler();
                JSONObject saveData = dh.openJSON(new File(loadGameURL.getText()));
                
                tempPuzzleJSONConstruct.add(saveData);
                gameCount++;
                Tab newTab = new Tab();
                AnchorPane getTab = (AnchorPane) FXMLLoader.load(
                        this.getClass().getResource("FXMLGame.fxml"));
                newTab.setContent(getTab);
                newTab.setText("Game " + saveData.get("difficulty") + "D" + gameCount);
                tabPane.getTabs().add(tabPane.getTabs().size() - 1, newTab);
                tabPane.getSelectionModel().select(tabPane.getTabs().size() - 2);
                updateTabList();
            } catch (IOException ioe) {
                System.out.println("Error loading FXML for Sudoku Game Tab: " + ioe);
            }
        }
    }

    /**
     * Launches a FileChooser from Data Handler for a custom menu background image
     */
    @FXML
    public void menuBackgroundBrowse() {
        DataHandler dh = new DataHandler();
        File bg = dh.openFile(Window.getPStage(), "Browse for Menu Background Image", "Image Files", "jpg", "png");
        menuBackgroundURL.setText(bg.toPath().toString());
    }

    /**
     * Launches a FileChooser from Data Handler for a custom game tab background image
     */
    @FXML
    public void gameBackgroundBrowse() {
        DataHandler dh = new DataHandler();
        File bg = dh.openFile(Window.getPStage(), "Browse for Game Background Image", "Image Files", "jpg", "png");
        gameBackgroundURL.setText(bg.toPath().toString());
    }

    /**
     * Populates the Tab Selection choicebox with active game tabs.
     */
    @FXML
    public void updateTabList() {
        String lastTab = "";
        String[] tabs = new String[tabPane.getTabs().size() - 2];
        for (int i = 1; i < tabPane.getTabs().size() - 1; i++) {
            tabs[i - 1] = tabPane.getTabs().get(i).getText();
            lastTab = tabs[i - 1];
        }
        tabSelect.setItems(FXCollections.observableArrayList(tabs));
        tabSelect.setValue(lastTab);
    }
    
    /**
     * Applies the menu background image change via CSS
     */
    @FXML
    public void applyMenuBG() {
        try {   //Format URI for Windows
            File f = new File(menuBackgroundURL.getText());
            String tmpURL = f.toURI().toString();
            tmpURL = tmpURL.replace("file:/", "file:///");
            startAnchor.setStyle("-fx-background-image: url(\"" + tmpURL + "\");"
                    + " -fx-background-size: 800; -fx-background-position: center;");
            appliedSettingsStatus.setText("Menu Background changed Successfully");
        } catch (Exception e) {
            appliedSettingsStatus.setText("Could not change Menu Background.");
        }
    }
    
    /**
     * Applies the game tab background image change via CSS
     */
    @FXML
    public void applyGameBG() {
        String gameTab;
        try {    //Format URI for Windows
            gameTab = tabSelect.getSelectionModel().getSelectedItem().toString();
            File f = new File(gameBackgroundURL.getText());
            String tmpURL = f.toURI().toString();
            tmpURL = tmpURL.replace("file:/", "file:///");
            
            //Grabs the user-chosen tab and alters its CSS w. background image
            for (int i = 0; i < tabPane.getTabs().size(); i++) {
                if (tabPane.getTabs().get(i).getText().equals(gameTab)) {
                    AnchorPane a = (AnchorPane) tabPane.getTabs().get(i).getContent();
                    a.setStyle("-fx-background-image: url(\"" + tmpURL + "\"); "
                            + "-fx-background-size: 800; -fx-background-position: center;");
                }
            }
            appliedSettingsStatus.setText("Game Background changed Successfully");
        } catch (Exception e) {
            appliedSettingsStatus.setText("Could not change Game Tab  Background.");
        }
    }

    /**
     * Launches a FileChooser from DataHandler for selecting background music
     */
    @FXML
    public void audioBrowse() {
        DataHandler dh = new DataHandler();
        File f = dh.openFile(Window.getPStage(), "Browse for Music", "Audio Files", "mp3");
        audioURL.setText(f.toPath().toString());
    }
    
    /**
     * Applies the selected audio file to the active AudioPlayer object
     */
    @FXML
    public void applyAudio() {
        try {
            File f = new File(audioURL.getText());
            if(ap == null)
                ap = new AudioPlayer(f.toURI().toString());
            ap.changeSong(f.toURI().toString());
            ap.play();
            appliedSettingsStatus.setText("Background Audio changed Successfully");
        } catch (Exception e) {
            appliedSettingsStatus.setText("Could not set Background Audio.");
        }
    }
    
    /**
     * Changes the volume of the current playing audio from AudioPlayer
     */
    @FXML
    public void changeVolume() {
        try {
            ap.setVolume(volumeSlider.getValue());
            appliedSettingsStatus.setText("Background Audio Volume set to " + Math.round(volumeSlider.getValue()*100));
        } catch (Exception e) {
            appliedSettingsStatus.setText("Could not set Background Audio Volume.");
        }
    }
    
    /**
     * Stops the current audio
     */
    @FXML
    public void stopMusic() {
        ap.stop();
    }

    /**
     * Restarts the current audio
     */
    @FXML
    public void playMusic() {
        ap.play();
    }
    
    /**
     * Attaining the current FXMLDocumentController object stored as class variable.
     * Not too much of a problem; only one exists for the duration of the app.
     * Used to gain access to instance method removeTab.
     * @return The current (and only) FXMLDocumentController instance
    */
    public static FXMLDocumentController getFXMLDC() {
        return fxmldc;
    }
    
    /**
     * Removes the active (selected) tab
     */
    public void removeTab() {
        tabPane.getTabs().remove(tabPane.getSelectionModel().getSelectedItem());
    }

    /**
     * Creates a TitledPane with just a title as an exit button for main menu
     * @return TitledPane without a body whose onClick terminates application
     */
    private TitledPane exitTPCreator() {
        Label label = new Label("EXIT");
        label.setOnMouseClicked(e -> Window.getPStage().close());
        TitledPane tp = new TitledPane();
        tp.getStyleClass().add("menuOption");
        tp.setGraphic(label);
        return tp;
    }
  
}