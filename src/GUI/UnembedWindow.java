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
import javax.swing.JSpinner;

import java.awt.Choice;

import javax.swing.JList;

import Embed.NoFileInPayloadException;

public class UnembedWindow extends JFrame {

	private JPanel contentPane;
	private BufferedImage previewerIMG;
	private JPanel mainPanel, panel_2, msgOrFilePanel;
	private JPasswordField passwordField;
	private final ButtonGroup whatToEmbed = new ButtonGroup();
	private Point initialClick;
	private JTextField carrierFileNameTextField;
	protected JTextArea messageTextArea ;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UnembedWindow frame = new UnembedWindow();
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
	public UnembedWindow() {
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
		passwordPanel.setBounds(40, 420, 986, 78);
		mainPanel.add(passwordPanel);
		
		JButton button = new JButton("Browse");
		button.setBounds(357, 337, 121, 23);
		passwordPanel.add(button);
		
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
		
		JButton btnEmbed = new JButton("Unembed");
		btnEmbed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String password = null;
				if(passwordField.getPassword().length != 0) 
					password = new String(passwordField.getPassword());
				String filename = carrierFileNameTextField.getText();
				UnembedWindow window = (UnembedWindow)(btnEmbed.getTopLevelAncestor());
				boolean x;
				try {
					x = UnembedCommandFactory.clickOnUnembed(filename, password, window);
					//window.messageTextArea.setText("hello");
				} catch (Exception e) {
					JOptionPane.showMessageDialog(window, e.getMessage());
					x = false;
					e.printStackTrace();
				}
				
				if(x)
					JOptionPane.showMessageDialog(window, "Data successfully retrieved from file!");
				else
					JOptionPane.showMessageDialog(window, "Data not extracted.");

			}
			});
		btnEmbed.setFont(new Font("Arial", Font.BOLD, 14));
		btnEmbed.setBounds(342, 2, 126, 23);
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
		messageTextArea.setEditable(false);
		messageTextArea.setBounds(10, 26, 448, 273);
		msgEmbedPanel.add(messageTextArea);
		
		JLabel lblWriteMessage = new JLabel("Message:");
		lblWriteMessage.setFont(new Font("Arial", Font.BOLD, 15));
		lblWriteMessage.setForeground(Color.WHITE);
		lblWriteMessage.setBounds(10, 0, 200, 30);
		msgEmbedPanel.add(lblWriteMessage);
		
		JPanel fileEmbedPanel = new JPanel();
		fileEmbedPanel.setBackground(Color.BLACK);
		msgOrFilePanel.add(fileEmbedPanel, "file");
		fileEmbedPanel.setLayout(null);
		
		JButton btnNewButton_1 = new JButton("New button");
		btnNewButton_1.setBounds(147, 176, 155, 61);
		fileEmbedPanel.add(btnNewButton_1);
		
		JButton closeButton = new JButton("");
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent arg0) {
				closeButton.setIcon(new ImageIcon(UnembedWindow.class.getResource("/javax/swing/plaf/metal/icons/ocean/close.gif")));
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				closeButton.setIcon(new ImageIcon(UnembedWindow.class.getResource("/javax/swing/plaf/metal/icons/ocean/close-pressed.gif")));
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				((JFrame)closeButton.getTopLevelAncestor()).dispose();
			}
		});
		closeButton.setIcon(new ImageIcon(UnembedWindow.class.getResource("/javax/swing/plaf/metal/icons/ocean/close.gif")));
		closeButton.setBounds(1032, 11, 27, 23);
		closeButton.setOpaque(false);
		
		mainPanel.add(closeButton);
		
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
		carrierFileNameTextField.setBounds(106, 11, 241, 23);
		panel.add(carrierFileNameTextField);
		carrierFileNameTextField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Input:");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 15));
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBounds(10, 11, 86, 23);
		panel.add(lblNewLabel);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(10, 45, 81, 14);
		panel.add(lblPassword);
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Arial", Font.BOLD, 15));
		
		passwordField = new JPasswordField();
		passwordField.setBounds(106, 43, 241, 20);
		panel.add(passwordField);
		
		JButton switchButton = new JButton("Embed a File");
		switchButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				UnembedCommandFactory.clickEmbedFile();
				((JFrame)switchButton.getTopLevelAncestor()).dispose();
			}
		});
		

		switchButton.setBounds(10, 6, 201, 23);
		mainPanel.add(switchButton);
	}
}
