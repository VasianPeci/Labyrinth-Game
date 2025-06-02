import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class InstructionsPanel extends JPanel{
	
	Image instructions;
	Font joystix;
	JButton continueButton;
	
	public InstructionsPanel() {
		this.setPreferredSize(new Dimension(600, 600));
		this.setLayout(null);
		instructions = new ImageIcon("instructions.png").getImage();
		
		// new font import
		try {
			File joystixFile = new File("joystix monospace.otf");
			joystix = Font.createFont(Font.TRUETYPE_FONT, joystixFile);
			joystix = joystix.deriveFont(40f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			System.out.println("Font not found!");
		}
		
		// set border
		Border border = BorderFactory.createLineBorder(Color.white);
		
		// continue button
		continueButton = new JButton("Continue");
		continueButton.setBounds(220, 500, 160, 40);
		continueButton.setBorder(border);
		continueButton.setFocusable(false);
		continueButton.setForeground(Color.white);
		continueButton.setBackground(Color.black);
		if(joystix != null) {
			continueButton.setFont(joystix.deriveFont(20f));
		} else {
			continueButton.setFont(new Font("Arial", Font.BOLD, 20));
		}
		
		this.add(continueButton);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.drawImage(instructions, 0, 0, null);
    }
}
