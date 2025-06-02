import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OptionsPanel extends JPanel{
	JLabel options;
	JButton continueButton;
	JButton pointsButton;
	JButton saveButton;
	JButton loadButton;
	JButton restartButton;
	JButton exitButton;
	Font joystix;
	
	public OptionsPanel() {
		this.setPreferredSize(new Dimension(600, 600));
		this.setBackground(new Color(0, 0, 0));
		this.setLayout(null);
		
		// new font import
		try {
			File joystixFile = new File("joystix monospace.otf");
			joystix = Font.createFont(Font.TRUETYPE_FONT, joystixFile);
			joystix = joystix.deriveFont(40f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			System.out.println("Font not found!");
		}
		
		options = new JLabel("Options");
		options.setBounds(190, 0, 300, 50);
		options.setForeground(Color.yellow);
		options.setFont(joystix);
		
		continueButton = new JButton("Continue");
		continueButton.setBounds(100, 90, 400, 50);
		continueButton.setForeground(Color.white);
		continueButton.setFont(joystix);
		continueButton.setFocusable(false);
		continueButton.setOpaque(false);
		continueButton.setContentAreaFilled(false);
		continueButton.setBorderPainted(false);
		
		saveButton = new JButton("Save");
		saveButton.setBounds(100, 170, 400, 50);
		saveButton.setForeground(Color.white);
		saveButton.setFont(joystix);
		saveButton.setFocusable(false);
		saveButton.setOpaque(false);
		saveButton.setContentAreaFilled(false);
		saveButton.setBorderPainted(false);
		
		loadButton = new JButton("Load");
		loadButton.setBounds(100, 250, 400, 50);
		loadButton.setForeground(Color.white);
		loadButton.setFont(joystix);
		loadButton.setFocusable(false);
		loadButton.setOpaque(false);
		loadButton.setContentAreaFilled(false);
		loadButton.setBorderPainted(false);
		
		restartButton = new JButton("Restart");
		restartButton.setBounds(100, 330, 400, 50);
		restartButton.setForeground(Color.white);
		restartButton.setFont(joystix);
		restartButton.setFocusable(false);
		restartButton.setOpaque(false);
		restartButton.setContentAreaFilled(false);
		restartButton.setBorderPainted(false);
		
		pointsButton = new JButton("Points");
		pointsButton.setBounds(100, 410, 400, 50);
		pointsButton.setForeground(Color.white);
		pointsButton.setFont(joystix);
		pointsButton.setFocusable(false);
		pointsButton.setOpaque(false);
		pointsButton.setContentAreaFilled(false);
		pointsButton.setBorderPainted(false);
		
		exitButton = new JButton("Exit");
		exitButton.setBounds(100, 490, 400, 50);
		exitButton.setForeground(Color.white);
		exitButton.setFont(joystix);
		exitButton.setFocusable(false);
		exitButton.setOpaque(false);
		exitButton.setContentAreaFilled(false);
		exitButton.setBorderPainted(false);
		
		this.add(options);
		this.add(continueButton);
		this.add(pointsButton);
		this.add(saveButton);
		this.add(loadButton);
		this.add(restartButton);
		this.add(exitButton);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
	}
}
