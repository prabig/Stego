package GUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.UIManager;

import java.awt.SystemColor;

import javax.swing.JLabel;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.JToggleButton;
import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.border.BevelBorder;
import javax.swing.JCheckBox;
import javax.swing.JPasswordField;

import java.awt.CardLayout;

import javax.swing.BoxLayout;

import java.awt.FlowLayout;

import javax.swing.AbstractButton;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextArea;
import javax.swing.JFormattedTextField;

import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import Embed.BadFilenameException;
import Embed.PadSelectionException;

public class EmbedWindow extends JFrame {

	private JPanel contentPane;
	private BufferedImage previewerIMG;
	private JPanel mainPanel, panel_2, msgOrFilePanel;
	private JPasswordField passwordField;
	private final ButtonGroup whatToEmbed = new ButtonGroup();
	private Point initialClick;
	private JTextField carrierFileNameTextField;
	private JTextField saveToTextField;
	private JTextArea messageTextArea ;
	protected JFormattedTextField fileTextField;
	private JRadioButton rdbtnMessage;
	private final ButtonGroup compressionButtonGroup = new ButtonGroup();
	private final ButtonGroup encryptionButtonGroup = new ButtonGroup();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmbedWindow frame = new EmbedWindow();
					frame.setUndecorated(true);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public EmbedWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1095, 577);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		mainPanel = new JPanel();
		mainPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				initialClick = arg0.getPoint();
				getComponentAt(initialClick);
			}
		});
		mainPanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				JFrame frame = (JFrame)(mainPanel.getTopLevelAncestor());
				//Component frame arg0.getComponent();
				int thisX = frame.getLocation().x;
				int thisY = frame.getLocation().y;
				
				int xMoved = (thisX + arg0.getX()) - (thisX + initialClick.x);
				int yMoved = (thisY + arg0.getY()) - (thisY + initialClick.y);
				
				int X = thisX + xMoved;
				int Y = thisY + yMoved;
				
				frame.setLocation(X,Y);
			}
		});
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		mainPanel.setBorder(new EmptyBorder(50, 10, 10, 10));
		mainPanel.setBackground(Color.BLACK);
		contentPane.add(mainPanel);
		mainPanel.setLayout(null);
		
//		JPanel imagePreviewPanel = new JPanel(){                
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                
//                if(previewerIMG != null){
//                    int max = this.getMaximumSize().width;
//                    int imgWidth = previewerIMG.getWidth();
//                    int imgHeight = previewerIMG.getHeight();
//                    
//                    Image resized = previewerIMG;
//                    
//                    if (imgWidth > max || imgHeight > max){
//                        if (imgWidth >= imgHeight){
//                            resized = previewerIMG.getScaledInstance(max, -1, Image.SCALE_SMOOTH);
//                        } else{
//                            resized = previewerIMG.getScaledInstance(-1, max, Image.SCALE_SMOOTH);
//                        }
//                    }
//                    
//                    setSize(resized.getWidth(null), resized.getHeight(null));
//                    g.drawImage(resized, 0, 0, null);
//                }
//                else {
//                	try {
//						previewerIMG = ImageIO.read(new File("C:\\Users\\prabi_000\\Downloads\\Untitled Diagram.jpg"));
//						g.drawImage(previewerIMG, 0, 0, null);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//                }
//            }
//        };
//		imagePreviewPanel.setBounds(10, 36, 468, 290);
		
		JPanel passwordPanel = new JPanel();
		passwordPanel.setLayout(null);
		passwordPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		passwordPanel.setBackground(SystemColor.windowBorder);
		passwordPanel.setBounds(40, 420, 365, 78);
		mainPanel.add(passwordPanel);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Arial", Font.BOLD, 15));
		lblPassword.setBounds(10, 11, 81, 14);
		passwordPanel.add(lblPassword);
		
		JButton button = new JButton("Browse");
		button.setBounds(357, 337, 121, 23);
		passwordPanel.add(button);
		
		JCheckBox chckbxPassword = new JCheckBox("");
		chckbxPassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(!chckbxPassword.isSelected()) {
					passwordField.setEditable(false);
					passwordField.setText("");
				} 
				else {
					passwordField.setEditable(true);
					passwordField.requestFocus();
				}
			}
		});
		chckbxPassword.setBounds(20, 44, 21, 23);
		chckbxPassword.setOpaque(false);
		passwordPanel.add(chckbxPassword);
		
		passwordField = new JPasswordField();
		passwordField.setEditable(false);
		passwordField.setBounds(47, 47, 151, 20);
		passwordPanel.add(passwordField);
		
		JPanel embedPanel = new JPanel();
		embedPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		embedPanel.setBackground(SystemColor.windowBorder);
		embedPanel.setBounds(538, 40, 488, 369);
		mainPanel.add(embedPanel);
		embedPanel.setLayout(null);
		
		JPanel whatToEmbedPanel = new JPanel();
		whatToEmbedPanel.setBorder(new EmptyBorder(0, 5, 0, 0));
		whatToEmbedPanel.setBackground(Color.BLACK);
		whatToEmbedPanel.setBounds(10, 332, 468, 26);
		embedPanel.add(whatToEmbedPanel);
		whatToEmbedPanel.setLayout(null);
		
		JLabel lblWhatToEmbed = new JLabel("What to Embed:    ");
		lblWhatToEmbed.setBounds(5, 3, 126, 18);
		lblWhatToEmbed.setForeground(Color.WHITE);
		lblWhatToEmbed.setFont(new Font("Arial", Font.BOLD, 15));
		whatToEmbedPanel.add(lblWhatToEmbed);
		
		
		rdbtnMessage = new JRadioButton("Message");
		rdbtnMessage.setBounds(131, 1, 77, 23);
		rdbtnMessage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(rdbtnMessage.isSelected()) {
					CardLayout cl = (CardLayout) msgOrFilePanel.getLayout();
					cl.show(msgOrFilePanel, "message");
					Enumeration<AbstractButton> buttons =compressionButtonGroup.getElements();
					while(buttons.hasMoreElements()) {
						JRadioButton but = (JRadioButton)buttons.nextElement();
						but.getModel().setEnabled(true);
						if(but.getActionCommand().equals("None")) {
							but.setSelected(true);
						}
						
					}
					
				}
			}
		});
		rdbtnMessage.setSelected(true);
		whatToEmbed.add(rdbtnMessage);
		rdbtnMessage.setFont(new Font("Arial", Font.BOLD, 12));
		rdbtnMessage.setForeground(Color.WHITE);
		rdbtnMessage.setOpaque(false);
		rdbtnMessage.setActionCommand("Message");
		whatToEmbedPanel.add(rdbtnMessage);
		
		JRadioButton rdbtnFile = new JRadioButton("File");
		rdbtnFile.setBounds(208, 1, 45, 23);
		rdbtnFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(rdbtnFile.isSelected()) {
					CardLayout cl = (CardLayout) msgOrFilePanel.getLayout();
					cl.show(msgOrFilePanel, "file");
					compressionButtonGroup.clearSelection();
					Enumeration<AbstractButton> buttons =compressionButtonGroup.getElements();
					while(buttons.hasMoreElements()) {
						buttons.nextElement().getModel().setEnabled(false);
					}
				}
			}
		});
		whatToEmbed.add(rdbtnFile);
		rdbtnFile.setFont(new Font("Arial", Font.BOLD, 12));
		rdbtnFile.setForeground(Color.WHITE);
		rdbtnFile.setActionCommand("File");
		rdbtnFile.setOpaque(false);
		whatToEmbedPanel.add(rdbtnFile);
		
		JButton btnEmbed = new JButton("Embed");
		btnEmbed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String message = messageTextArea.getText();
				String payloadFileName = fileTextField.getText();

				String encryptionStr = encryptionButtonGroup.getSelection().getActionCommand();
				boolean passwordSelected = chckbxPassword.isSelected();
				String password = null;
				if(passwordSelected) {
					password = new String(passwordField.getPassword());
				}
				String carrierFilename = carrierFileNameTextField.getText();
				String saveTo = saveToTextField.getText();	
				
				
				
				if(whatToEmbed.getSelection().getActionCommand().equals("File")) {
					boolean x = true;
					try {
						x = EmbedCommandFactory.embedFile(payloadFileName, carrierFilename, saveTo, password, encryptionStr);
					} catch (PadSelectionException e) {
						JOptionPane.showMessageDialog(btnEmbed, e.getMessage());
						e.printStackTrace();
					} catch (BadFilenameException e) {
						JOptionPane.showMessageDialog(btnEmbed, e.getMessage());
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(btnEmbed, e.getMessage());
						e.printStackTrace();
					}
					if(x) {
						JOptionPane.showMessageDialog(btnEmbed.getTopLevelAncestor(), "File embedded successfully!");
					}
					else {
						JOptionPane.showMessageDialog(btnEmbed.getTopLevelAncestor(), "File embed did not work. Please check all inputs and try again.");
					}
				}
				else { //message
					String compressionStr = compressionButtonGroup.getSelection().getActionCommand();
					boolean x;
					try {
						x = EmbedCommandFactory.embedMessage(message, carrierFilename, saveTo, password, encryptionStr, compressionStr);
					} catch (BadFilenameException e) {
						JOptionPane.showMessageDialog(btnEmbed, e.getMessage());
						x = false;
						e.printStackTrace();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(btnEmbed, e.getMessage());
						x = false;
						e.printStackTrace();
					}
					
					if(x) {
						JOptionPane.showMessageDialog(btnEmbed.getTopLevelAncestor(), "Message embedded successfully!");
					}
					else {
						JOptionPane.showMessageDialog(btnEmbed.getTopLevelAncestor(), "Message embed did not work. Please check all inputs and try again.");
					}
				}
			}
			});
		btnEmbed.setFont(new Font("Arial", Font.BOLD, 14));
		btnEmbed.setBounds(379, 2, 89, 23);
		whatToEmbedPanel.add(btnEmbed);
		
		msgOrFilePanel = new JPanel();
		msgOrFilePanel.setBounds(10, 11, 468, 310);
		embedPanel.add(msgOrFilePanel);
		msgOrFilePanel.setLayout(new CardLayout(0, 0));
		
		JPanel msgEmbedPanel = new JPanel();
		msgEmbedPanel.setBackground(Color.BLACK);
		msgOrFilePanel.add(msgEmbedPanel, "message");
		msgEmbedPanel.setLayout(null);
		
		messageTextArea = new JTextArea();
		messageTextArea.setBounds(10, 26, 448, 273);
		msgEmbedPanel.add(messageTextArea);
		
		JLabel lblWriteMessage = new JLabel("Write Message:");
		lblWriteMessage.setFont(new Font("Arial", Font.BOLD, 15));
		lblWriteMessage.setForeground(Color.WHITE);
		lblWriteMessage.setBounds(10, 0, 200, 30);
		msgEmbedPanel.add(lblWriteMessage);
		
		JPanel fileEmbedPanel = new JPanel();
		fileEmbedPanel.setBackground(Color.BLACK);
		msgOrFilePanel.add(fileEmbedPanel, "file");
		fileEmbedPanel.setLayout(null);
		
		JButton btnBrowseForPayload = new JButton("Browse");
		btnBrowseForPayload.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				File payload = EmbedCommandFactory.clickBrowseToPickEmbedPayload();
				fileTextField.setText(payload.getAbsolutePath());
			}
		});
		btnBrowseForPayload.setBounds(358, 276, 100, 23);
		fileEmbedPanel.add(btnBrowseForPayload);
		
		fileTextField = new JFormattedTextField();
		fileTextField.setBounds(10, 277, 338, 20);
		fileEmbedPanel.add(fileTextField);
		
		JPanel panel_8 = new JPanel();
		panel_8.setLayout(null);
		panel_8.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_8.setBackground(SystemColor.windowBorder);
		panel_8.setBounds(415, 420, 611, 78);
		mainPanel.add(panel_8);
		
		JLabel lblCompression = new JLabel("Compression:");
		lblCompression.setForeground(Color.WHITE);
		lblCompression.setFont(new Font("Arial", Font.BOLD, 15));
		lblCompression.setBounds(10, 11, 107, 14);
		panel_8.add(lblCompression);
		
		JButton button_1 = new JButton("Browse");
		button_1.setBounds(357, 337, 121, 23);
		panel_8.add(button_1);
		
		JRadioButton rdbtnFixedHuffman = new JRadioButton("FixedHuffman");
		compressionButtonGroup.add(rdbtnFixedHuffman);
		rdbtnFixedHuffman.setActionCommand("FixedHuffman");
		rdbtnFixedHuffman.setForeground(Color.WHITE);
		rdbtnFixedHuffman.setOpaque(false);
		rdbtnFixedHuffman.setFont(new Font("Arial", Font.PLAIN, 11));
		rdbtnFixedHuffman.setBounds(123, 8, 107, 23);
		//rdbtnNewRadioButton.setOpaque(false);
		panel_8.add(rdbtnFixedHuffman);
		
		JRadioButton rdbtnStatisticalHuffman = new JRadioButton("StatisticalHuffman");
		compressionButtonGroup.add(rdbtnStatisticalHuffman);
		rdbtnStatisticalHuffman.setActionCommand("StatisticalHuffman");
		rdbtnStatisticalHuffman.setForeground(Color.WHITE);
		rdbtnStatisticalHuffman.setOpaque(false);
		rdbtnStatisticalHuffman.setFont(new Font("Arial", Font.PLAIN, 11));
		rdbtnStatisticalHuffman.setBounds(232, 8, 127, 23);
		panel_8.add(rdbtnStatisticalHuffman);
		
		JRadioButton rdbtnLZW = new JRadioButton("LZW");
		compressionButtonGroup.add(rdbtnLZW);
		rdbtnLZW.setActionCommand("LZW");
		rdbtnLZW.setForeground(Color.WHITE);
		rdbtnLZW.setOpaque(false);
		rdbtnLZW.setFont(new Font("Arial", Font.PLAIN, 11));
		rdbtnLZW.setBounds(361, 8, 61, 23);
		panel_8.add(rdbtnLZW);
		
		JLabel lblEncryption = new JLabel("Encryption:");
		lblEncryption.setForeground(Color.WHITE);
		lblEncryption.setFont(new Font("Arial", Font.BOLD, 15));
		lblEncryption.setBounds(10, 42, 107, 14);
		panel_8.add(lblEncryption);
		
		JRadioButton rdbtnPlaintext = new JRadioButton("Plaintext");
		rdbtnPlaintext.setActionCommand("Plaintext");
		rdbtnPlaintext.setSelected(true);
		encryptionButtonGroup.add(rdbtnPlaintext);
		rdbtnPlaintext.setFont(new Font("Arial", Font.PLAIN, 11));
		rdbtnPlaintext.setForeground(Color.WHITE);
		rdbtnPlaintext.setOpaque(false);
		rdbtnPlaintext.setBounds(123, 39, 77, 23);
		panel_8.add(rdbtnPlaintext);
		
		JRadioButton rdbtnInternalPadding = new JRadioButton("InternalPadding");
		encryptionButtonGroup.add(rdbtnInternalPadding);
		rdbtnInternalPadding.setActionCommand("InternalPadding");
		rdbtnInternalPadding.setFont(new Font("Arial", Font.PLAIN, 11));
		rdbtnInternalPadding.setOpaque(false);
		rdbtnInternalPadding.setForeground(Color.WHITE);
		rdbtnInternalPadding.setBounds(206, 37, 112, 23);
		panel_8.add(rdbtnInternalPadding);
		
		JRadioButton rdbtnExternalOnetimePad = new JRadioButton("ExternalOnetimePad");
		encryptionButtonGroup.add(rdbtnExternalOnetimePad);
		rdbtnExternalOnetimePad.setActionCommand("ExternalOnetimePad");
		rdbtnExternalOnetimePad.setFont(new Font("Arial", Font.PLAIN, 11));
		rdbtnExternalOnetimePad.setOpaque(false);
		rdbtnExternalOnetimePad.setForeground(Color.WHITE);
		rdbtnExternalOnetimePad.setBounds(322, 37, 141, 23);
		panel_8.add(rdbtnExternalOnetimePad);
		
		JRadioButton rdbtnNone = new JRadioButton("None");
		rdbtnNone.setSelected(true);
		compressionButtonGroup.add(rdbtnNone);
		rdbtnNone.setActionCommand("None");
		rdbtnNone.setOpaque(false);
		rdbtnNone.setForeground(Color.WHITE);
		rdbtnNone.setFont(new Font("Arial", Font.PLAIN, 11));
		rdbtnNone.setBounds(424, 8, 61, 23);
		panel_8.add(rdbtnNone);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent arg0) {
				btnNewButton.setIcon(new ImageIcon(EmbedWindow.class.getResource("/javax/swing/plaf/metal/icons/ocean/close.gif")));
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnNewButton.setIcon(new ImageIcon(EmbedWindow.class.getResource("/javax/swing/plaf/metal/icons/ocean/close-pressed.gif")));
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				((JFrame)btnNewButton.getTopLevelAncestor()).dispose();
			}
		});
		btnNewButton.setIcon(new ImageIcon(EmbedWindow.class.getResource("/javax/swing/plaf/metal/icons/ocean/close.gif")));
		btnNewButton.setBounds(1032, 11, 27, 23);
		btnNewButton.setOpaque(false);
		
		mainPanel.add(btnNewButton);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_1.setBackground(SystemColor.windowBorder);
		panel_1.setBounds(40, 40, 488, 369);
		mainPanel.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblChooseACarrier = new JLabel("Choose a Carrier File:");
		lblChooseACarrier.setForeground(Color.WHITE);
		lblChooseACarrier.setFont(new Font("Arial", Font.BOLD, 15));
		lblChooseACarrier.setBounds(10, 11, 215, 14);
		panel_1.add(lblChooseACarrier);
		//panel_1.add(imagePreviewPanel);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new EmptyBorder(0, 0, 0, 0));
		panel.setBackground(Color.BLACK);
		panel.setBounds(10, 290, 468, 68);
		panel_1.add(panel);
		
		JButton btnBrowseForCarrier = new JButton("Open");
		btnBrowseForCarrier.setFont(new Font("Arial", Font.BOLD, 14));
		btnBrowseForCarrier.setBounds(357, 11, 101, 23);
		btnBrowseForCarrier.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				File carrier = EmbedCommandFactory.clickBrowseCarrierInput();
				try {
					carrierFileNameTextField.setText(carrier.getAbsolutePath());
				} 
				catch(NullPointerException e) {
					return;
				}
			}
		});
		panel.add(btnBrowseForCarrier);
		
		carrierFileNameTextField = new JTextField();
		carrierFileNameTextField.setBounds(74, 11, 273, 23);
		panel.add(carrierFileNameTextField);
		carrierFileNameTextField.setColumns(10);
		
		saveToTextField = new JTextField();
		saveToTextField.setColumns(10);
		saveToTextField.setBounds(74, 37, 273, 23);
		panel.add(saveToTextField);
		
		JButton btnBrowseOutput = new JButton("Save As");
		btnBrowseOutput.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				File outputFile = EmbedCommandFactory.clickBrowseSaveTo();
				try {
					saveToTextField.setText(outputFile.getAbsolutePath());
				} 
				catch(NullPointerException e) {
					return;
				}
			}
		});
		btnBrowseOutput.setFont(new Font("Arial", Font.BOLD, 14));
		btnBrowseOutput.setBounds(357, 37, 101, 23);
		panel.add(btnBrowseOutput);
		
		JLabel lblNewLabel = new JLabel("Input:");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 15));
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBounds(10, 11, 86, 23);
		panel.add(lblNewLabel);
		
		JLabel lblOutput = new JLabel("Save To:");
		lblOutput.setForeground(Color.WHITE);
		lblOutput.setFont(new Font("Arial", Font.BOLD, 15));
		lblOutput.setBounds(10, 37, 86, 23);
		panel.add(lblOutput);
		
		JButton btnNewButton_1 = new JButton("Unembed a File");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				EmbedCommandFactory.clickUnembedFile();
				((JFrame)btnNewButton_1.getTopLevelAncestor()).dispose();
			}
		});
		btnNewButton_1.setBounds(10, 6, 201, 23);
		mainPanel.add(btnNewButton_1);
		
	}
}
