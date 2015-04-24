package GUI;


import javax.swing.*;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.filechooser.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.DocumentFilter;


public class STUI extends JFrame {

    private static STUI instance;
    
    private BufferedImage previewerIMG = null;
    private File payloadBG = null;
    private JTextArea input;
    private JFileChooser imgChooser;
    private JPanel imagePanel;
    private STAlertPanel alertPanel;
    private JRadioButton otp;
    private JRadioButton hauffman;
    private JRadioButton lzw;

    public STUI() {

        setTitle("Secure Transmit");
        setSize(new Dimension(600 + 12 * 3, 670 + 12 * 2));
        setResizable(false);
        setLocationRelativeTo(null);
        setUndecorated(false); //alt+f4 to close atm
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initialize();
    }

    //D:< Don't touch mah GUI! *Waves knife*
    private void initialize() {
        Container c = getContentPane();
        GroupLayout layout = new GroupLayout(c);
        c.setLayout(layout);

        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        
        //Message Input Panel
        STPanel message = new STPanel();
            input = new JTextArea();
            input.setFont(new Font("Sans-Serif", Font.PLAIN, 12));
            input.setLineWrap(true);
            input.setWrapStyleWord(true);
            int greyshade = 135;
            input.setBackground(new Color(greyshade, greyshade, greyshade));
            DefaultStyledDocument inputDoc = new DefaultStyledDocument();
            inputDoc.setDocumentFilter(new DocumentFilter() {
                int maxChars = 2000;
                
                //http://docs.oracle.com/javase/tutorial/uiswing/components/generaltext.html#filter
                @Override
                public void insertString(FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
                    if ((fb.getDocument().getLength() + str.length()) <= maxChars) {
                        super.insertString(fb, offs, str, a);
                    }
                }

                @Override
                public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {
                    if ((fb.getDocument().getLength() + str.length() - length) <= maxChars) {
                        super.replace(fb, offs, length, str, a);
                    }
                }
            });
            input.setDocument(inputDoc);
            JScrollPane inputPane = new JScrollPane(input, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            inputPane.setMaximumSize(new Dimension(300 - 12 * 2, 500 - 12 * 2));
            inputPane.setMinimumSize(inputPane.getMaximumSize());
            
            message.add(Box.createRigidArea(new Dimension(0, 12 * 1)));
            message.add(inputPane);
        
        //Image Chooser and Previewer Panel
        STPanel preview = new STPanel();
            preview.setLayout(new BorderLayout());
            JPanel imageBGPanel = new JPanel();
            imageBGPanel.setLayout(new BoxLayout(imageBGPanel, BoxLayout.Y_AXIS));
            imageBGPanel.setMaximumSize(new Dimension(300 - 12 * 2, 300 - 12 * 2));
            imageBGPanel.setBackground(Color.BLACK);
            
            imagePanel = new JPanel() {                
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    
                    if(previewerIMG != null){
                        int max = this.getMaximumSize().width;
                        int imgWidth = previewerIMG.getWidth();
                        int imgHeight = previewerIMG.getHeight();
                        
                        Image resized = previewerIMG;
                        
                        if (imgWidth > max || imgHeight > max){
                            if (imgWidth >= imgHeight){
                                resized = previewerIMG.getScaledInstance(max, -1, Image.SCALE_SMOOTH);
                            } else{
                                resized = previewerIMG.getScaledInstance(-1, max, Image.SCALE_SMOOTH);
                            }
                        }
                        
                        setSize(resized.getWidth(null), resized.getHeight(null));
                        g.drawImage(resized, 0, 0, null);
                    }           
                }
            };
            imagePanel.setMaximumSize(imageBGPanel.getMaximumSize());
            imagePanel.setOpaque(false);
            imagePanel.setAlignmentX(CENTER_ALIGNMENT);
                        
            imgChooser = new JFileChooser();
                imgChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                imgChooser.setFileFilter(new ImageFileFilter());
                imgChooser.setAcceptAllFileFilterUsed(false);
            JButton browseButton = new JButton("Browse");
            browseButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleImageChooser();
                }                
            });
            browseButton.setAlignmentX(RIGHT_ALIGNMENT);
            
            preview.add(Box.createRigidArea(new Dimension(0, 12)), BorderLayout.NORTH);
            JPanel borderPanelC = new JPanel();
                borderPanelC.setLayout(new BoxLayout(borderPanelC, BoxLayout.PAGE_AXIS));
                borderPanelC.setOpaque(false);
                borderPanelC.add(imageBGPanel);
                    imageBGPanel.add(imagePanel);
            preview.add(borderPanelC, BorderLayout.CENTER);
            JPanel borderPanelS = new JPanel();
                borderPanelS.setLayout(new BoxLayout(borderPanelS, BoxLayout.PAGE_AXIS));
                borderPanelS.setOpaque(false);
                borderPanelS.add(Box.createRigidArea(new Dimension(0, 12)));
                JPanel borderPanelSS = new JPanel();
                    borderPanelSS.setLayout(new BoxLayout(borderPanelSS, BoxLayout.PAGE_AXIS));
                    borderPanelSS.setMaximumSize(new Dimension(300 - 12 * 2, 50 - 12 * 2));
                    borderPanelSS.setAlignmentX(CENTER_ALIGNMENT);
                    borderPanelSS.setOpaque(false);
                    borderPanelSS.add(browseButton);
                borderPanelS.add(borderPanelSS);
                borderPanelS.add(Box.createRigidArea(new Dimension(0, 12)));
            preview.add(borderPanelS, BorderLayout.SOUTH);
        
        //Submit Panel - Alerts and Deembed/Embed Buttons
        STPanel submit = new STPanel();
        	submit.setLayout(new GroupLayout(submit));
        	submit.setMaximumSize(new Dimension(300, 125));
        	submit.setMinimumSize(new Dimension(300, 125));
        	
        	alertPanel = new STAlertPanel();
        		alertPanel.setMaximumSize(new Dimension(submit.getMaximumSize().width - 12 * 2, (int) (submit.getMaximumSize().height * 0.75 - 12 * 2)));
        		alertPanel.setMinimumSize(alertPanel.getMaximumSize());
        	JButton embedButton = new JButton("Embed");
        		embedButton.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						prepareAndDoEmbed();
					}
        		});
        	JButton deembedButton = new JButton("De-Embed");
        		deembedButton.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						prepareAndDoDeembed();
					}
        			
        		});
    		GroupLayout submitLayout = (GroupLayout) submit.getLayout();
    			submitLayout.setAutoCreateContainerGaps(true);
    			submitLayout.setAutoCreateGaps(true);
    			submitLayout.linkSize(SwingConstants.HORIZONTAL, embedButton, deembedButton);
    			int gap = (int) (alertPanel.getMaximumSize().getWidth() - deembedButton.getMaximumSize().getWidth() * 2);
    			submitLayout.setHorizontalGroup(submitLayout.createParallelGroup()
    					.addComponent(alertPanel, GroupLayout.Alignment.LEADING)
    					.addGroup(submitLayout.createSequentialGroup()
    							.addComponent(embedButton)
    							.addGap(gap)
    							.addComponent(deembedButton)
						)
				);
    			submitLayout.setVerticalGroup(submitLayout.createSequentialGroup()
    					.addComponent(alertPanel)
    					.addGroup(submitLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
    							.addComponent(embedButton)
    							.addGap(gap)
    							.addComponent(deembedButton)
    					)
				);
        
		STPanel encryption = new STPanel();
			encryption.setLayout(new BorderLayout());
			encryption.setMaximumSize(new Dimension(145, 145));
			JPanel encCase = new JPanel();
				encCase.setOpaque(false);
				encCase.setMaximumSize(new Dimension((int) encryption.getMaximumSize().getWidth() - 12 * 2, (int) encryption.getMaximumSize().getHeight() - 12 * 2));
				encCase.setLayout(new BoxLayout(encCase, BoxLayout.PAGE_AXIS));
				JLabel encPrompt = new JLabel("Encryption");
				otp = new JRadioButton("One-time Pass");
        			otp.setOpaque(false);
        			otp.setSelected(true);
        			otp.setActionCommand("onetimepass");
        		ButtonGroup encGroup = new ButtonGroup();
        			encGroup.add(otp);
        		encCase.add(encPrompt);
        		encCase.add(otp);
    		encryption.add(Box.createRigidArea(new Dimension(0,12)), BorderLayout.NORTH);
    		encryption.add(Box.createRigidArea(new Dimension(12,0)), BorderLayout.WEST);
    		encryption.add(Box.createRigidArea(new Dimension(0,12)), BorderLayout.SOUTH);
    		encryption.add(Box.createRigidArea(new Dimension(12,0)), BorderLayout.EAST);
    		encryption.add(encCase, BorderLayout.CENTER);
        	
        STPanel compression = new STPanel();
        	compression.setLayout(new BorderLayout());
        	compression.setMaximumSize(new Dimension(145, 145));
        	JPanel comCase = new JPanel();
        		comCase.setOpaque(false);
        		comCase.setMaximumSize(new Dimension((int) compression.getMaximumSize().getWidth() - 12 * 2, (int) compression.getMaximumSize().getHeight() - 12 * 2));
        		comCase.setLayout(new BoxLayout(comCase, BoxLayout.PAGE_AXIS));
	        	JLabel comPrompt = new JLabel("Compression");
	        	hauffman = new JRadioButton("Hauffman");
	        		hauffman.setOpaque(false);
	        		hauffman.setSelected(true);
	        		hauffman.setActionCommand("hauffman");
	        	lzw = new JRadioButton("LZW");
	        		lzw.setOpaque(false);
	        		lzw.setSelected(true);
	        		lzw.setActionCommand("lzw");
	        	ButtonGroup comGroup = new ButtonGroup();
	        		comGroup.add(hauffman);
	        		comGroup.add(lzw);
        		comCase.add(comPrompt);
        		comCase.add(hauffman);
        		comCase.add(lzw);
    		compression.add(Box.createRigidArea(new Dimension(0,12)), BorderLayout.NORTH);
    		compression.add(Box.createRigidArea(new Dimension(12,0)), BorderLayout.WEST);
    		compression.add(Box.createRigidArea(new Dimension(0,12)), BorderLayout.SOUTH);
    		compression.add(Box.createRigidArea(new Dimension(12,0)), BorderLayout.EAST);
    		compression.add(comCase, BorderLayout.CENTER);
       
        
        STPanel passcode = new STPanel();
        
        STPanel secauth = new STPanel();

        //TODO Jasmine Remove sizes from add methods
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(message, 300, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createSequentialGroup()
                .addComponent(encryption, 145, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(10)
                .addComponent(compression, 145, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createParallelGroup()
                .addComponent(preview, 300, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(submit, 300, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createSequentialGroup()
                .addComponent(passcode, 145, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(10)
                .addComponent(secauth, 145, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(message, 500, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createSequentialGroup()
                .addComponent(preview, 350, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(25)
                .addComponent(submit, 125, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addGap(25)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(encryption, 145, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(compression, 145, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(passcode, 145, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(secauth, 145, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));

        c.setBackground(new Color(0, 0, 0));
    }
    
    private void prepareAndDoEmbed(){
    	alertPanel.reset();
    	String messageToEncrypt = input.getText();
    		if (messageToEncrypt == null || messageToEncrypt.equals("")){
    			alertPanel.updateAlert(0, true);
    			return;
    		}
		if (payloadBG == null){
			alertPanel.updateAlert(1, true);
		}
		String EncryptionMode = otp.isSelected()? otp.getActionCommand():"error"; // other excryption method to be added in phase I or so
		String compressionMode = hauffman.isSelected()? hauffman.getActionCommand(): lzw.getActionCommand();
		
		//TODO Patrick, use these fields and payloadBG to activate the embed class's stuff
    }
    
    private void prepareAndDoDeembed(){
    	alertPanel.reset();
    	
    	if (payloadBG == null){
			alertPanel.updateAlert(1, true);
		}
    	String EncryptionMode = otp.isSelected()? otp.getActionCommand():"error"; // other excryption method to be added in phase I or so
		String compressionMode = hauffman.isSelected()? hauffman.getActionCommand(): lzw.getActionCommand();
		
    	//TODO Patrick, use these fields and payloadBG to activate the deembed class's stuff
    }
    
    private void handleImageChooser(){
        int response = imgChooser.showOpenDialog(getContentPane());
        
        if (response == JFileChooser.APPROVE_OPTION){
        	payloadBG = imgChooser.getSelectedFile();
            
            String ext = null;
            String s = payloadBG.getName();
            int i = s.lastIndexOf('.');

            if (i > 0 && i < s.length() - 1) {
                ext = s.substring(i + 1).toLowerCase();
                
                if (ext == null){
                    return;
                }
                
                if (ext.equals("png") || ext.equals("bmp")){
                	try{
                    	previewerIMG = ImageIO.read(payloadBG);
                    } catch(IOException e){
                        return;
                    }
                } else {
                	//TODO Jasmine add Music image
                }
            }
            
        }
        
        imagePanel.repaint();
    }
    
    public void alertSuccessfulEmbed(){
    	//TODO Patrick call this once embedding is finished
    	alertPanel.updateAlert(2, true);
    }
    
    public void alertSuccessfulDeembed(String message){
    	//TODO Patrick call this with the extracted message
    	alertPanel.updateAlert(3, true);
    	input.setText(message);
    }
    
    public STAlertPanel getAlertPanel(){
        return this.alertPanel;
    }

    public static void main(String[] args) {

        STUI main = new STUI();
        instance = main;
        main.setVisible(true);

    }
}