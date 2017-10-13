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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.regex.Pattern;

/**
 * Main Data Manipulation class for Sudoku.
 * Deals with all manipulation for external files, including accessing, creating,
 *  and formatting of them.
 */
public class DataHandler {

    /**
     * Spawns a FileChooser for selecting a file
     * @param stage Primary Stage for enforcing Modal Blocking
     * @param title Title of FileChooser
     * @param extPrompt Extension Prompt
     * @param ext Extension(s) as an array of strings
     * @return User-selected file
     */
    public File openFile(Stage stage, String title, String extPrompt, String... ext) {
        FileChooser fc = new FileChooser();
        fc.setTitle(title);
        
        //Creates and implements filters for file extensions.
        for (int i = 0; i < ext.length; i++) {
            FileChooser.ExtensionFilter extFilter
                    = new FileChooser.ExtensionFilter(extPrompt, "*." + ext[i]);
            fc.getExtensionFilters().add(extFilter);
        }

        File f = fc.showOpenDialog(stage);

        return f;
    }

    /**
     * Spawns a FileChooser specifically for images - validates them as well
     * @param stage Primary Stage for enforcing Modal Blocking
     * @param title Title of FileChooser
     * @return User-selected image
     */
    public Image openImage(Stage stage, String title) {
        FileChooser fc = new FileChooser();
        fc.setTitle(title);

        FileChooser.ExtensionFilter extFilter
                = new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png");
        fc.getExtensionFilters().add(extFilter);

        File f = fc.showOpenDialog(stage);
        
        //Checks if the image is valid, else returns a missing image predefined
        Image img = new Image("..\\media\\missing.jpg");
        if (f != null) {
            img = new Image(f.getPath());
        }

        return img;
    }
    
    /**
     * JSON parser for Sudoku saves; ensures they are valid
     * @param json Sudoku Save to validate
     * @return Validated JSONObject with save data
     */
    public JSONObject openJSON(File json) {

        File f = json;
        JSONObject jobj = new JSONObject();
        JSONParser p = new JSONParser();

        try {
            if (f != null) {
                Object obj = p.parse(new FileReader(f.getPath()));
                jobj = (JSONObject) obj;
            }
        } catch (FileNotFoundException fnfe) {
        } catch (IOException ioe) {
        } catch (ParseException pe) {}

        return jobj;
    }

    /**
     * FileChooser for selecting a directory
     * @param stage Primary Stage for enforcing Modal Blocking
     * @param title Title of FileChooser
     * @return Directory URL path
     */
    public String openDir(Stage stage, String title) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle(title);

        File f = dc.showDialog(stage);
        String dir = "";

        if (f != null) {
            dir = f.toString();
        }

        return dir;
    }

    /**
     * Validates if directory contains valid symbol files
     * @param dir Directory in which contains the 9 symbol images
     * @return True if the symbol set is valid
     */
    public boolean checkDir(String dir) {
        File d = new File(dir);
        boolean isDir = false;
        if (d.exists()) {
            if (d.isDirectory()) {
                isDir = true;
            }
        }
        
        //Ensures symbol filename only contains the number of its cell
        // e.g. for number 7, filename is: '7.jpg'.
        boolean containsSymbols = false;
        Pattern img = Pattern.compile("[1-9](\\.[Jj][Pp][Gg]|\\.[Pp][Nn][Gg])");
        File[] symbols = d.listFiles();
        int imgCount = 0;
        for (File f : symbols) {
            Matcher m = img.matcher(f.getName());
            if (f.isFile() && m.matches()) {
                imgCount++;
            }
        }
        if (imgCount == 9) {
            containsSymbols = true;
        }

        if (isDir && containsSymbols) {
            return true;
        }
        return false;
    }

    /**
     * Retrieves symbol set from folder
     * @param url Directory of symbols
     * @return Sorted array containing symbol images
     */
    public Image[] getSymbols(String url) {
        File d = new File(url);
        File[] files = d.listFiles();
        
        //ArrayList for holding image files as they are found
        ArrayList<File> tmpImgFiles = new ArrayList<File>();
        Pattern img = Pattern.compile("[1-9](\\.[Jj][Pp][Gg]|\\.[Pp][Nn][Gg])");
        for (File i : files) {
            Matcher m = img.matcher(i.getName());
            if (i.isFile() && m.matches()) {
                tmpImgFiles.add(i);
            }
        }
        //Temporary array for sorting image files by name using a comparator
        File[] tmpImgFilesArr = tmpImgFiles.toArray(new File[9]);
        Arrays.sort(tmpImgFilesArr, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                String name1 = f1.getName();
                String name2 = f2.getName();
                Integer num1 = Integer.valueOf(name1.substring(0, name1.indexOf(".")));
                Integer num2 = Integer.valueOf(name2.substring(0, name2.indexOf(".")));
                return num1.compareTo(num2);
            }
        });

        //Assembling array containing sorted symbol set
        Image[] tmpImgArr = new Image[9];
        for (int i = 0; i < 9; i++) {
            String tmpURL = tmpImgFilesArr[i].toURI().toString();
            tmpImgArr[i] = new Image(tmpURL);
        }
        return tmpImgArr;
    }

    /**
     * Same as getSymbols, but for their URLs
     * @param url Directory of symbols
     * @return Sorted array containing symbol URLs
     */
    public String[] getSymbolURLs(String url) {
        File d = new File(url);
        File[] files = d.listFiles();

        //ArrayList for holding image files as they are found
        ArrayList<File> tmpImgFiles = new ArrayList<File>();
        Pattern img = Pattern.compile("[1-9](\\.[Jj][Pp][Gg]|\\.[Pp][Nn][Gg])");
        for (File i : files) {
            Matcher m = img.matcher(i.getName());
            if (i.isFile() && m.matches()) {
                tmpImgFiles.add(i);
            }
        }
        //Temporary array for sorting image files by name using a comparator
        File[] tmpImgFilesArr = tmpImgFiles.toArray(new File[9]);
        Arrays.sort(tmpImgFilesArr, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                String name1 = f1.getName();
                String name2 = f2.getName();
                Integer num1 = Integer.valueOf(name1.substring(0, name1.indexOf(".")));
                Integer num2 = Integer.valueOf(name2.substring(0, name2.indexOf(".")));
                return num1.compareTo(num2);
            }
        });
        
        //Assembling array with image file URLs
        String[] tmpImgFileURLs = new String[9];
        for (int i = 0; i < 9; tmpImgFileURLs[i] = tmpImgFilesArr[i].toURI().toString(), i++);
        return tmpImgFileURLs;
    }

    /**
     * Spawns a FileChooser for saving a particular file
     * @param stage Primary Stage for enforcing Modal Blocking
     * @param title Title of FileChooser
     * @param extPrompt Extension Prompt
     * @param ext Extension to filter by
     * @param name Preferred name of file
     * @param data Data to save (textual)
     */
    public void saveFile(Stage stage, String title, String extPrompt,
            String ext, String name, ArrayList<String> data) {
        FileChooser fc = new FileChooser();
        fc.setTitle(title);
        fc.setInitialFileName(name);

        FileChooser.ExtensionFilter extFilter
                = new FileChooser.ExtensionFilter(extPrompt, "*." + ext);
        fc.getExtensionFilters().add(extFilter);

        File f = fc.showSaveDialog(stage);

        if (f != null) {
            writeFile(f.toString(), data);
        } else {
            System.out.println("File " + name + " could not be saved");
        }
    }

    /**
     * Writes string (textual) information to file
     * @param url Location of file
     * @param data Data to write
     */
    public void writeFile(String url, ArrayList<String> data) {
        File f = new File(url);

        try {
            f.createNewFile();

            FileWriter fw = new FileWriter(f.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for (String line : data) {
                bw.write(line);
                bw.newLine();
            }

            bw.close();

        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    /**
     * Reader for reading String data from file.
     * @param f File containing textual information
     * @return String comprising of data from file
     */
    public String readFromFile(File f) {
        String data = "";
        try {
            if (!f.exists()) {
                //The ONLY way this is possible is the file is manipulated DURING selection...
                throw new FileNotFoundException();
            }

            FileReader fr = new FileReader(f.getAbsoluteFile());
            BufferedReader br = new BufferedReader(fr);

            String line;
            while ((line = br.readLine()) != null) {
                data += line;
            }

            br.close();

        } catch (IOException ioe) {
            System.out.println(ioe);
        }

        return data;
    }

    /**
     * Prepares puzzle JSON as an e-mail attachment (file)
     * @param puzzle JSON text generated by puzzle toString
     * @param email E-Mail to send puzzle to
     */
    public void prepareEmailFile(String puzzle, String email) {
        JSONObject jobj;
        JSONParser p = new JSONParser();

        try {
            Object obj = p.parse(puzzle);
            jobj = (JSONObject) obj;
            ArrayList<String> tmp = new ArrayList<String>();
            tmp.add(jobj.toJSONString());
            writeFile("./tempDoku.sud", tmp);
            Email.main(email, new File("tempDoku.sud"));
        } catch (ParseException pe) {}
    }
}
