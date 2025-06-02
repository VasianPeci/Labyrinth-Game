import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.Container;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class LoginPanel extends JPanel{
	
	Image background;
	Font joystix;
	Font ferrum;
	JLabel welcomeLabel;
	JLabel usernameLabel;
	JTextField username;
	JLabel passwordLabel;
	JPasswordField password;
	JButton loginButton;
	JButton registerButton;
	
	public LoginPanel() {
		this.setPreferredSize(new Dimension(600, 600));
		this.setLayout(null);
		
		background = new ImageIcon("background.png").getImage();
		
		// new font import
		try {
			File joystixFile = new File("joystix monospace.otf");
			joystix = Font.createFont(Font.TRUETYPE_FONT, joystixFile);
			joystix = joystix.deriveFont(40f);
			File ferrumFile = new File("ferrum.otf");
			ferrum = Font.createFont(Font.TRUETYPE_FONT, ferrumFile);
			ferrum = ferrum.deriveFont(40f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			System.out.println("Fonti nuk gjendet!");
		}
		
		// set border
		Border border = BorderFactory.createLineBorder(Color.white);
		
		// Username Label & Textfield
		usernameLabel = new JLabel("Set Username:");
		usernameLabel.setBounds(120, 180, 500, 70);
		if(ferrum != null) {
			usernameLabel.setFont(ferrum);
		} else {
			usernameLabel.setFont(new Font("Arial", Font.BOLD, 20));
		}
		usernameLabel.setForeground(Color.white);
		
		username = new JTextField();
		if(ferrum != null) {
			username.setFont(ferrum);
		} else {
			username.setFont(new Font("Arial", Font.BOLD, 20));
		}
		username.setForeground(Color.black);
		username.setBackground(Color.white);
		username.setBounds(100, 240, 400, 60);
		username.setEditable(true);
		username.setBorder(new EmptyBorder(0, 20, 0, 20));
		
		// Password Label & Passwordfield
		passwordLabel = new JLabel("Set Password:");
		passwordLabel.setBounds(120, 330, 500, 70);
		if(ferrum != null) {
			passwordLabel.setFont(ferrum);
		} else {
			passwordLabel.setFont(new Font("Arial", Font.BOLD, 20));
		}
		passwordLabel.setForeground(Color.white);
				
		password = new JPasswordField();
		password.setFont(new Font("Arial", Font.BOLD, 40));
		password.setForeground(Color.black);
		password.setBackground(Color.white);
		password.setBounds(100, 390, 400, 60);
		password.setEditable(true);
		password.setBorder(new EmptyBorder(0, 20, 0, 20));
		
		// welcome label
		welcomeLabel = new JLabel("Welcome");
		welcomeLabel.setBounds(190, 40, 300, 100);
		welcomeLabel.setForeground(Color.black);
		if(joystix != null) {
			welcomeLabel.setFont(joystix);
		} else {
			welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
		}
		
		// login button
		loginButton = new JButton("Login");
		loginButton.setBounds(235, 500, 130, 40);
		loginButton.setBorder(border);
		loginButton.setFocusable(false);
		loginButton.setForeground(Color.white);
		loginButton.setBackground(Color.black);
		if(joystix != null) {
			loginButton.setFont(joystix.deriveFont(20f));
		} else {
			loginButton.setFont(new Font("Arial", Font.BOLD, 20));
		}
		
		// register button
		registerButton = new JButton("Register");
		registerButton.setBounds(450, 530, 130, 40);
		registerButton.setBorder(border);
		registerButton.setFocusable(false);
		registerButton.setForeground(Color.white);
		registerButton.setBackground(Color.black);
		if(joystix != null) {
			registerButton.setFont(joystix.deriveFont(10f));
		} else {
			registerButton.setFont(new Font("Arial", Font.BOLD, 20));
		}
		
		this.add(welcomeLabel);
		this.add(usernameLabel);
		this.add(username);
		this.add(passwordLabel);
		this.add(password);
		this.add(loginButton);
		this.add(registerButton);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.drawImage(background, 0, 0, null);
    }


}
