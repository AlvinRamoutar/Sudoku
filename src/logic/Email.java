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

import java.io.File;
import java.util.Properties;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * E-Mail creator and sender class.
 * Constructs email objects and sends them via javasudoku@outlook.com
 * Just a reminder that this account may have to be verified from time to time
 *  because of the lax of security (set on Outlook's site so that I can use SMTP)
 */
public class Email {
    
    /**
     * Default mail construction
     * @param email E-mail of recipient 
     * @param puzzle JSON-formatted puzzle save
     */
    public static void main(String email, File puzzle) {
        sendEmail("Help solve this Sudoku!",
                "Hey there! Someone used you as their helpline for solving this particular sudoku problem." +
                    "This e-mail was send by Java Sudoku.",
                "javasudoku@outlook.com",
                email,
                "prog24178",
                puzzle);
    }
       
    /**
     * Sends mail via SMTP through hotmail account javasudoku@outlook.com
     * @param subject Subject of message
     * @param body Body of message
     * @param from Address sending from (account originated)
     * @param to Recipient
     * @param password Password of sending account
     * @param f Sudoku save file attachment
     */
    private static void sendEmail(String subject, String body, String from, String to, String password, File f) {
        try {
            //Construct the message network properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp-mail.outlook.com");
            props.put("mail.smtp.port", "587");
            Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
              @Override
              protected PasswordAuthentication getPasswordAuthentication() {
                  return new PasswordAuthentication(from, password);
              }
            });
            
            //Create the message
            Message message = new MimeMessage(session);
            message.setSubject(subject);
            
            // Create the message body
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            
            //Create multipart
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart); 
            
            //Add attachment
            //Default directory is Project Directory when dealing with Data Source Objects.
            String filename = f.toPath().toString();
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new javax.activation.DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
            
            //Complete message
            message.setContent(multipart);
            
            //Address the message
            Address fromAddress = new InternetAddress(from);
            Address toAddress = new InternetAddress(to);
            message.setFrom(fromAddress);
            message.setRecipient(Message.RecipientType.TO, toAddress);
            
            //Send the message
            Transport transport = session.getTransport();
            transport.connect(from, password);
            transport.sendMessage(message, message.getAllRecipients());
            
        } catch(MessagingException me) {
            System.out.println(me);
        }
    }
}