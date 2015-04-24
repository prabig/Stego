package GUI;

import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class STAlertPanel extends JTextArea{
    private ArrayList<String> alertsToDisplay;
    //TODO Patrick Add the other classes' alerts here, if any
    final String[] alerts =  new String[]{
                                "Please enter a message to embed\n",
                                "Please Select an Image (.jpeg, .mp3) or Sound (.wav) file\n",
                                "Your message is too large; please shorten to 2000 characters or less\n",
                                "Your mmessage was successfully embedded",
                                "The embedded message was successfully extracted"
    };

    public STAlertPanel(){
    	alertsToDisplay = new ArrayList<String>();
        setOpaque(false);
        setEditable(false);
        setFont(new Font("Sans-Serif", Font.PLAIN, 12));
        setLineWrap(true);
        setWrapStyleWord(true);
        setText("To embed: Type a message up to 2000 characters long and pick an image or music file\n"
        		+ "To deembed: Select an image or audio file\n");
    }
    
    public void updateAlert(int index, boolean addAlert){
        if (addAlert){
        	if (!alertsToDisplay.contains(alerts[index])){
        		alertsToDisplay.add(alerts[index]);
        	}
        	else {
        		System.out.println("You're trying to add an alert (\"" + alerts[index] + "\") already being displayed!");
        	}
        } else {
        	if (alertsToDisplay.contains(alerts[index])){
        		alertsToDisplay.remove(alerts[index]);
        	}
        	else {
        		System.out.println("You're trying to remove an alert that isn't being displayed!");
        	}
        }
        
        
        String text = "";
        for (String alert: alertsToDisplay){
        	text = text.concat(alert);
        }
        
        setText(text);
    }
    
    public void reset(){
    	setText("");
    	alertsToDisplay.clear();
    }
}
