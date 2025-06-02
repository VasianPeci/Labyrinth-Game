import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainPanel extends JPanel{
	
	Image mainImage;
	Font joystix;
	JLabel labyrinthText;
	JButton startButton;
	JButton loadButton;
	
	public MainPanel() {
		this.setPreferredSize(new Dimension(600, 600));
		this.setLayout(null);
		mainImage = new ImageIcon("mainImage.png").getImage();
		
		// new font import
		try {
			File joystixFile = new File("joystix monospace.otf");
			joystix = Font.createFont(Font.TRUETYPE_FONT, joystixFile);
			joystix = joystix.deriveFont(40f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			System.out.println("Font not found!");
		}
		
		
		// labyrinth text
		labyrinthText = new JLabel("Labyrinth");
		labyrinthText.setBounds(130, 0, 400, 90);
		labyrinthText.setForeground(Color.ORANGE);
		if(joystix != null) {
			labyrinthText.setFont(joystix.deriveFont(50f));
		} else {
			labyrinthText.setFont(new Font("Arial", Font.BOLD, 20));
		}
				
		// start button
		startButton = new JButton("Start Game");
		startButton.setBounds(150, 450, 300, 40);
		startButton.setFocusable(false);
		startButton.setForeground(Color.white);
		startButton.setOpaque(false);
		startButton.setContentAreaFilled(false);
		startButton.setBorderPainted(false);
		if(joystix != null) {
			startButton.setFont(joystix.deriveFont(20f));
		} else {
			startButton.setFont(new Font("Arial", Font.BOLD, 20));
		}
		
		// load button
		loadButton = new JButton("Load Game");
		loadButton.setBounds(150, 510, 300, 40);
		loadButton.setFocusable(false);
		loadButton.setForeground(Color.white);
		loadButton.setOpaque(false);
		loadButton.setContentAreaFilled(false);
		loadButton.setBorderPainted(false);
		if(joystix != null) {
			loadButton.setFont(joystix.deriveFont(20f));
		} else {
			loadButton.setFont(new Font("Arial", Font.BOLD, 20));
		}
		
		this.add(labyrinthText);
		this.add(startButton);
		this.add(loadButton);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.drawImage(mainImage, 0, 0, null);
    }
}
