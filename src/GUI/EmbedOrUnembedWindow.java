package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import java.awt.Color;

import javax.swing.SpringLayout;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;

import java.awt.GridLayout;

import net.miginfocom.swing.MigLayout;

import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;

import java.awt.SystemColor;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EmbedOrUnembedWindow extends JFrame {

	private JPanel contentPane;
	private Point initialClick;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmbedOrUnembedWindow frame = new EmbedOrUnembedWindow();
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
	public EmbedOrUnembedWindow() {
		setTitle("Secure Transmit");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 470);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(50, 50, 50, 50));
		panel.setBackground(Color.BLACK);
		contentPane.add(panel);
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				initialClick = arg0.getPoint();
				getComponentAt(initialClick);
			}
		});
		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				JFrame frame = (JFrame)(panel.getTopLevelAncestor());
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
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(55, 55, 586, 334);
		panel_1.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_1.setBackground(SystemColor.windowBorder);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JButton btnEmbed = new JButton("Embed");
		btnEmbed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				EmbedOrUnembedCommandFactory.selectEmbed((EmbedOrUnembedWindow)btnEmbed.getTopLevelAncestor());
				
			}
		});
		btnEmbed.setFont(new Font("Arial", Font.BOLD, 18));

		btnEmbed.setBounds(63, 92, 462, 50);
		panel_1.add(btnEmbed);
		
		JButton btnUnembed = new JButton("Unembed");
		btnUnembed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				EmbedOrUnembedCommandFactory.selectUnembed((EmbedOrUnembedWindow)btnUnembed.getTopLevelAncestor());
			}
		});
		btnUnembed.setFont(new Font("Arial", Font.BOLD, 18));
		btnUnembed.setBounds(63, 191, 462, 50);
		panel_1.add(btnUnembed);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.setBounds(640, 11, 34, 34);
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
		btnNewButton.setOpaque(false);
		
		panel.add(btnNewButton);
	}
}
