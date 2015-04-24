package GUI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class STPanel extends JPanel {

    public STPanel() {
        this.setBorder(BorderFactory.createRaisedBevelBorder());
        setBackground(Color.WHITE);
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

        g2d.setColor(getBackground());
        g2d.fill(g.getClipBounds());

        g2d.dispose();
    }
}