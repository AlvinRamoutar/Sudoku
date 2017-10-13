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

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * AudioPlayer class that creates FXML Media Players
 */
public class AudioPlayer {
    
    MediaPlayer player;
    
    /**
     * Constructor for an Audio Player
     * @param songURI Formatted URI of audio file location
     */
    public AudioPlayer(String songURI) {
        player = new MediaPlayer(new Media(songURI));
        player.play();
    }
    
    public void stop() {
        player.stop();
    }
    
    public void play() {
        this.stop();
        player.play();
    }
    
    public void setVolume(double v) {
        player.setVolume(v);
    }
    
    public void changeSong(String songURI) {
        player.dispose();
        player = new MediaPlayer(new Media(songURI));
        this.play();
    }
    
    
}
