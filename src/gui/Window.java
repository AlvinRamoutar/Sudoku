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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main launchable for application
 * Creates an FXML Application
 */
public class Window extends Application {

    public static Stage pStage;
    
    /**
     * Initializes the stage
     * @param stage Primary Stage
     * @throws Exception Any exception thrown throughout the stack from children nodes
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
                
        pStage = stage;
        stage.setScene(scene);
        stage.setTitle("Sudoku");
        stage.getIcons().add(new Image("media/icon.png"));
        stage.setWidth(806);
        stage.setHeight(629);
        stage.setResizable(false);
        stage.show(); 
    }

    /**
     * Returns the current primary stage created by launchable
     * @return Primary Stage
     */
    public static Stage getPStage() {
        return pStage;
    }
    
    /**
     * Executes launchable
     * @param args 
     */
    public static void main(String[] args) {
        launch(args);
    }
}
