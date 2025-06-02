import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GameFrame extends JFrame implements MouseListener{
	
	LoginPanel loginPanel;
	RegisterPanel registerPanel;
	InstructionsPanel instructionsPanel;
	MainPanel mainPanel;
	GamePanel gamePanel;
	OptionsPanel optionsPanel;
	ImageIcon background;
	String url = ""; // this is the url of the database which establishes the connection, it has to be filled by the person reading this
	String username = ""; // this is the username of the database being used, it has to be filled by the person reading this
	String password = ""; // this is the password of the database being used, it has to be filled by the person reading this
	String gameUsername;
	Font joystix;
	boolean running;
	GameController controller;
	int arrayCount;
	int[][] labyrinthNetwork = {
		    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		    {1, 0, 0, 0, 1, 0, 2, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1},
		    {1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 1, 2, 1},
		    {1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1},
		    {1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1},
		    {1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1},
		    {1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 2, 0, 0, 1, 0, 1},
		    {1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1},
		    {1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1},
		    {1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1},
		    {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1},
		    {1, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
		    {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1},
		    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1},
		    {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1},
		    {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1},
		    {1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1},
		    {1, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 1, 2, 1, 0, 1},
		    {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
		    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1}
	};
	// 0 represents the road to be followed by the player, 1 represents the walls which you cannot pass, 2 represents the treasures and 3 is the way out
	
	public GameFrame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Labyrinth Game");
		this.setResizable(false);
		this.setIconImage(getIconImage());
		
		background = new ImageIcon("background.png");
		this.setIconImage(background.getImage());
		
		controller = new GameController();
		loginPanel = new LoginPanel();
		registerPanel = new RegisterPanel();
		instructionsPanel = new InstructionsPanel();
		mainPanel = new MainPanel();
		optionsPanel = new OptionsPanel();
		this.loginPanel.loginButton.addMouseListener(this);
		this.loginPanel.registerButton.addMouseListener(this);
		this.registerPanel.loginButton.addMouseListener(this);
		this.registerPanel.registerButton.addMouseListener(this);
		this.instructionsPanel.continueButton.addMouseListener(this);
		this.mainPanel.startButton.addMouseListener(this);
		this.mainPanel.loadButton.addMouseListener(this);
		this.optionsPanel.continueButton.addMouseListener(this);
		this.optionsPanel.saveButton.addMouseListener(this);
		this.optionsPanel.loadButton.addMouseListener(this);
		this.optionsPanel.restartButton.addMouseListener(this);
		this.optionsPanel.pointsButton.addMouseListener(this);
		this.optionsPanel.exitButton.addMouseListener(this);
		
		this.add(loginPanel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
	}
	
	public void paint() {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == this.loginPanel.registerButton) {
			this.remove(loginPanel);
			this.add(registerPanel);
			this.revalidate();
			this.repaint();
		} else if(e.getSource() == this.registerPanel.loginButton) {
			this.remove(registerPanel);
			this.add(loginPanel);
			this.revalidate();
			this.repaint();
		} else if(e.getSource() == this.instructionsPanel.continueButton) {
			try {
	            File audioFile = new File("transition_explosion.wav");
	            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
	            Clip clip = AudioSystem.getClip();
	            clip.open(audioStream);
	            clip.start();
	        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException exc) {
	        	exc.printStackTrace();
	        }
			this.remove(instructionsPanel);
			this.add(mainPanel);
			this.revalidate();
			this.repaint();
		} else if(e.getSource() == this.loginPanel.loginButton) {
			// db connection
			try {
				Connection connection = DriverManager.getConnection(url, username, password);
				String sql = "SELECT * FROM PLAYERS WHERE username = ? AND password = ?";
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, this.loginPanel.username.getText());
				statement.setString(2, new String(this.loginPanel.password.getPassword()));
				ResultSet result = statement.executeQuery();
				if(result.next()) {
					// sound effect
					try {
			            File audioFile = new File("transition_explosion.wav");
			            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			            Clip clip = AudioSystem.getClip();
			            clip.open(audioStream);
			            clip.start();
			        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException exc) {
			            System.out.println(exc);
			        }
										
					gameUsername = this.loginPanel.username.getText();
					arrayCount = result.getInt("arrayCount");
										
					this.remove(loginPanel); 
					this.add(instructionsPanel);
					this.revalidate();
					this.repaint();
				} else {
					JOptionPane.showMessageDialog(null, "Username or Password does not match!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				result.close();
				statement.close();
				connection.close();
			} catch (SQLException exc) {
				JOptionPane.showMessageDialog(null, "Database connection is not possible: " + exc.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if(e.getSource() == this.registerPanel.registerButton) {
			// db connection
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(url, username, password);
			} catch(SQLException exc) {
				JOptionPane.showMessageDialog(null, "Database connection is not possible: " + exc.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
			}
			try {
				String sql = "SELECT * FROM PLAYERS WHERE username = ?";
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, this.registerPanel.username.getText());
				ResultSet result = statement.executeQuery();
				if(!result.next()) {
					sql = "INSERT INTO PLAYERS(username, password, row_position, col_position, row_start, col_start, row_end, col_end, points, labyrinthMatrix, pointsArray, arrayCount) VALUES(?, ?, 1, 1, 0, 0, 12, 12, 0, '{\"matrix\": [[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1], [1, 0, 0, 0, 1, 0, 2, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1], [1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 1, 2, 1], [1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1], [1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1], [1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1], [1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 2, 0, 0, 1, 0, 1], [1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1], [1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1], [1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1], [1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1], [1, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1], [1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1], [1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1], [1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1], [1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1], [1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1], [1, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 1, 2, 1, 0, 1], [1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1], [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1]]}', '{\"matrix\": [[]]}', 0)";
					statement = connection.prepareStatement(sql);
					statement.setString(1, this.registerPanel.username.getText());
					statement.setString(2, new String(this.registerPanel.password.getPassword()));
					statement.executeUpdate();
					JOptionPane.showMessageDialog(null, "Your registration was successful!", "Info", JOptionPane.PLAIN_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "A user with this username exists, try again!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				result.close();
				statement.close();
				connection.close();
			} catch (SQLException exc) {
				JOptionPane.showMessageDialog(null, "Username or Password cannot be empty!", "Database Error", JOptionPane.ERROR_MESSAGE);
			}
	} else if(e.getSource() == this.mainPanel.startButton) {
		this.controller.player = new Player();
		this.controller.labyrinth = new Labyrinth(labyrinthNetwork, 0, 12, 0, 12);
		controller.player.gameUsername = gameUsername;
		controller.player.arrayCount = arrayCount;
		controller.player.pointsArray = new int[controller.player.arrayCount];
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
			for(int i = 0; i < controller.player.arrayCount; i++) {
				String sql2 = String.format("SELECT JSON_VALUE(pointsArray, '$.matrix[0][%d]') AS VALUE FROM PLAYERS WHERE username = ?", i);
				PreparedStatement statement2 = connection.prepareStatement(sql2);
				statement2.setString(1, gameUsername);
				ResultSet result2 = statement2.executeQuery();
				result2.next();
				controller.player.pointsArray[i] = result2.getInt("value");
				statement2.close();
				result2.close();
			}
			connection.close();
		} catch (SQLException exc) {
			JOptionPane.showMessageDialog(null, "Error in database!", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		gamePanel = new GamePanel(this.controller);
		this.remove(mainPanel);
		// sound effect
		try {
            File audioFile = new File("transition_explosion.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException exc) {
            System.out.println(exc);
        }
		this.add(gamePanel);
		this.gamePanel.optionsButton.addMouseListener(this);
		this.revalidate();
		this.repaint();
		
		running = true;
				
		// checks if game status changes
		new Thread(() -> {
            while (running) {
                if (controller.player.status == 2) {
                    controller.finishGame(this, mainPanel, gamePanel, controller.player.status);
                    this.mainPanel.startButton.setForeground(Color.white);
        			this.mainPanel.startButton.setOpaque(false);
        			this.mainPanel.startButton.setContentAreaFilled(false);
                } else if(controller.player.status == 1) {
                	controller.finishGame(this, mainPanel, gamePanel, controller.player.status);
                	arrayCount++;
                	running = false;
                	this.mainPanel.startButton.setForeground(Color.white);
        			this.mainPanel.startButton.setOpaque(false);
        			this.mainPanel.startButton.setContentAreaFilled(false);
                }
    			
                try {
                    Thread.sleep(10);
                } catch (InterruptedException exc) {
                    exc.printStackTrace();
                }
            }
        }).start();
	} else if(e.getSource() == this.mainPanel.loadButton) {
		controller.loadGame(this);
		this.remove(this.mainPanel);
		// sound effect
		try {
            File audioFile = new File("transition_explosion.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException exc) {
            System.out.println(exc);
        }
		this.add(this.gamePanel);
		this.gamePanel.optionsButton.addMouseListener(this);
		this.revalidate();
		this.repaint();
	} else if(e.getSource() == this.gamePanel.optionsButton) {
		this.add(optionsPanel);
		this.remove(gamePanel);
		this.revalidate();
		this.repaint();
	} else if(e.getSource() == this.optionsPanel.continueButton) {
		this.remove(optionsPanel);
		this.add(gamePanel);
		this.gamePanel.optionsButton.setBackground(Color.LIGHT_GRAY);
		this.optionsPanel.continueButton.setForeground(Color.white);
		this.optionsPanel.continueButton.setOpaque(false);
		this.optionsPanel.continueButton.setContentAreaFilled(false);
		this.revalidate();
		this.repaint();
		this.gamePanel.requestFocusInWindow();
	} else if(e.getSource() == this.optionsPanel.saveButton) {
		controller.saveGame(this);
	} else if(e.getSource() == this.optionsPanel.restartButton) {
		this.remove(optionsPanel);
		this.remove(gamePanel);
		this.controller.player = new Player();
		this.controller.labyrinth = new Labyrinth(labyrinthNetwork, 0, 12, 0, 12);
		
		this.optionsPanel.restartButton.setForeground(Color.white);
		this.optionsPanel.restartButton.setOpaque(false);
		this.optionsPanel.restartButton.setContentAreaFilled(false);
		
		gamePanel = new GamePanel(this.controller);
		// sound effect
		try {
            File audioFile = new File("transition_explosion.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException exc) {
            System.out.println(exc);
        }
		this.add(gamePanel);
		this.gamePanel.optionsButton.addMouseListener(this);
		this.revalidate();
		this.repaint();
		
		running = true;
		
		// checks if game status changes
		new Thread(() -> {
            while (running) {
                if (controller.player.status == 2) {
                    controller.finishGame(this, mainPanel, gamePanel, controller.player.status);
                } else if(controller.player.status == 1) {
                	controller.finishGame(this, mainPanel, gamePanel, controller.player.status);
                	arrayCount++;
                	running = false;
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException exc) {
                    exc.printStackTrace();
                }
            }
        }).start();
	} else if(e.getSource() == this.optionsPanel.loadButton) {
		controller.loadGame(this);
		
		this.remove(optionsPanel);
		this.gamePanel.optionsButton.addMouseListener(this);
		this.add(gamePanel);
		this.gamePanel.optionsButton.setBackground(Color.LIGHT_GRAY);
		this.optionsPanel.loadButton.setForeground(Color.white);
		this.optionsPanel.loadButton.setOpaque(false);
		this.optionsPanel.loadButton.setContentAreaFilled(false);
		this.revalidate();
		this.repaint();
	} else if (e.getSource() == this.optionsPanel.pointsButton) {
	    JPanel pointsPanel = new JPanel();
	    pointsPanel.setPreferredSize(new Dimension(600, 600));
	    pointsPanel.setBackground(Color.black);
	    pointsPanel.setLayout(null);

	    JLabel pointsLabel = new JLabel("Past points");
	    pointsLabel.setForeground(Color.white);
	    
	    // new font import
	    try {
	        File joystixFile = new File("joystix monospace.otf");
	        joystix = Font.createFont(Font.TRUETYPE_FONT, joystixFile);
	        joystix = joystix.deriveFont(30f);
	    } catch (FontFormatException | IOException exc) {
	        System.out.println("Font not found!");
	    }
	    pointsLabel.setFont(joystix);
	    pointsLabel.setBounds(100, 50, 400, 50);

	    controller.sortPoints(controller.player.pointsArray);

	    JPanel labelPanel = new JPanel();
	    labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
	    labelPanel.setBackground(Color.black);

	    for (int i = 0; i < controller.player.pointsArray.length; i++) {
	        JLabel pointLabel = new JLabel("Points: " + controller.player.pointsArray[i] + "/6");
	        pointLabel.setForeground(Color.white);
	        pointLabel.setFont(joystix);
	        labelPanel.add(pointLabel);
	    }

	    JScrollPane scrollPane = new JScrollPane(labelPanel);
	    scrollPane.setBounds(50, 120, 500, 400);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

	    JButton closeButton = new JButton("X");
	    closeButton.setBounds(500, 10, 80, 80);
	    closeButton.setBackground(Color.red);
	    closeButton.setForeground(Color.white);
	    closeButton.setFont(new Font("Arial", Font.BOLD, 20));
	    closeButton.setBorderPainted(false);
	    closeButton.setFocusPainted(false);
	    closeButton.setContentAreaFilled(false);
	    closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

	    closeButton.addActionListener(e2 -> {
	        this.remove(pointsPanel);
	        this.optionsPanel.pointsButton.setForeground(Color.white);
			this.optionsPanel.pointsButton.setOpaque(false);
			this.optionsPanel.pointsButton.setContentAreaFilled(false);
	        this.add(optionsPanel); 
	        this.revalidate();
	        this.repaint();
	    });

	    pointsPanel.add(pointsLabel);
	    pointsPanel.add(scrollPane);
	    pointsPanel.add(closeButton);

	    this.remove(optionsPanel);
	    this.add(pointsPanel);
	    this.revalidate();
	    this.repaint();
	} else if(e.getSource() == this.optionsPanel.exitButton) {
		System.exit(0);
	}
}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(e.getSource() == this.loginPanel.loginButton) {
			this.loginPanel.loginButton.setForeground(Color.black);
			this.loginPanel.loginButton.setBackground(Color.white);
			this.loginPanel.loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.loginPanel.registerButton) {
			this.loginPanel.registerButton.setForeground(Color.black);
			this.loginPanel.registerButton.setBackground(Color.white);
			this.loginPanel.registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.registerPanel.loginButton) {
			this.registerPanel.loginButton.setForeground(Color.black);
			this.registerPanel.loginButton.setBackground(Color.white);
			this.registerPanel.loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.registerPanel.registerButton) {
			this.registerPanel.registerButton.setForeground(Color.black);
			this.registerPanel.registerButton.setBackground(Color.white);
			this.registerPanel.registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.instructionsPanel.continueButton) {
			this.instructionsPanel.continueButton.setForeground(Color.black);
			this.instructionsPanel.continueButton.setBackground(Color.white);
			this.instructionsPanel.continueButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.mainPanel.startButton) {
			this.mainPanel.startButton.setOpaque(true);
			this.mainPanel.startButton.setContentAreaFilled(true);
			this.mainPanel.startButton.setForeground(Color.black);
			this.mainPanel.startButton.setBackground(Color.white);
			this.mainPanel.startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.mainPanel.loadButton) {
			this.mainPanel.loadButton.setOpaque(true);
			this.mainPanel.loadButton.setContentAreaFilled(true);
			this.mainPanel.loadButton.setForeground(Color.black);
			this.mainPanel.loadButton.setBackground(Color.white);
			this.mainPanel.loadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.optionsPanel.continueButton) {
			this.optionsPanel.continueButton.setOpaque(true);
			this.optionsPanel.continueButton.setContentAreaFilled(true);
			this.optionsPanel.continueButton.setForeground(Color.black);
			this.optionsPanel.continueButton.setBackground(Color.white);
			this.optionsPanel.continueButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.optionsPanel.saveButton) {
			this.optionsPanel.saveButton.setOpaque(true);
			this.optionsPanel.saveButton.setContentAreaFilled(true);
			this.optionsPanel.saveButton.setForeground(Color.black);
			this.optionsPanel.saveButton.setBackground(Color.white);
			this.optionsPanel.saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.optionsPanel.loadButton) {
			this.optionsPanel.loadButton.setOpaque(true);
			this.optionsPanel.loadButton.setContentAreaFilled(true);
			this.optionsPanel.loadButton.setForeground(Color.black);
			this.optionsPanel.loadButton.setBackground(Color.white);
			this.optionsPanel.loadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.optionsPanel.restartButton) {
			this.optionsPanel.restartButton.setOpaque(true);
			this.optionsPanel.restartButton.setContentAreaFilled(true);
			this.optionsPanel.restartButton.setForeground(Color.black);
			this.optionsPanel.restartButton.setBackground(Color.white);
			this.optionsPanel.restartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.optionsPanel.pointsButton) {
			this.optionsPanel.pointsButton.setOpaque(true);
			this.optionsPanel.pointsButton.setContentAreaFilled(true);
			this.optionsPanel.pointsButton.setForeground(Color.black);
			this.optionsPanel.pointsButton.setBackground(Color.white);
			this.optionsPanel.pointsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.optionsPanel.exitButton) {
			this.optionsPanel.exitButton.setOpaque(true);
			this.optionsPanel.exitButton.setContentAreaFilled(true);
			this.optionsPanel.exitButton.setForeground(Color.black);
			this.optionsPanel.exitButton.setBackground(Color.white);
			this.optionsPanel.exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() == this.gamePanel.optionsButton) {
			this.gamePanel.optionsButton.setOpaque(true);
			this.gamePanel.optionsButton.setContentAreaFilled(true);
			this.gamePanel.optionsButton.setForeground(Color.black);
			this.gamePanel.optionsButton.setBackground(Color.white);
			this.gamePanel.optionsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if(e.getSource() == this.loginPanel.loginButton) {
			this.loginPanel.loginButton.setForeground(Color.white);
			this.loginPanel.loginButton.setBackground(Color.black);
		} else if(e.getSource() == this.loginPanel.registerButton) {
			this.loginPanel.registerButton.setForeground(Color.white);
			this.loginPanel.registerButton.setBackground(Color.black);
		} else if(e.getSource() == this.registerPanel.loginButton) {
			this.registerPanel.loginButton.setForeground(Color.white);
			this.registerPanel.loginButton.setBackground(Color.black);
		} else if(e.getSource() == this.registerPanel.registerButton) {
			this.registerPanel.registerButton.setForeground(Color.white);
			this.registerPanel.registerButton.setBackground(Color.black);
		} else if(e.getSource() == this.instructionsPanel.continueButton) {
			this.instructionsPanel.continueButton.setForeground(Color.white);
			this.instructionsPanel.continueButton.setBackground(Color.black);
		} else if(e.getSource() == this.mainPanel.startButton) {
			this.mainPanel.startButton.setForeground(Color.white);
			this.mainPanel.startButton.setOpaque(false);
			this.mainPanel.startButton.setContentAreaFilled(false);
		} else if(e.getSource() == this.mainPanel.loadButton) {
			this.mainPanel.loadButton.setForeground(Color.white);
			this.mainPanel.loadButton.setOpaque(false);
			this.mainPanel.loadButton.setContentAreaFilled(false);
		} else if(e.getSource() == this.optionsPanel.continueButton) {
			this.optionsPanel.continueButton.setForeground(Color.white);
			this.optionsPanel.continueButton.setOpaque(false);
			this.optionsPanel.continueButton.setContentAreaFilled(false);
		} else if(e.getSource() == this.optionsPanel.saveButton) {
			this.optionsPanel.saveButton.setForeground(Color.white);
			this.optionsPanel.saveButton.setOpaque(false);
			this.optionsPanel.saveButton.setContentAreaFilled(false);
		} else if(e.getSource() == this.optionsPanel.loadButton) {
			this.optionsPanel.loadButton.setForeground(Color.white);
			this.optionsPanel.loadButton.setOpaque(false);
			this.optionsPanel.loadButton.setContentAreaFilled(false);
		} else if(e.getSource() == this.optionsPanel.restartButton) {
			this.optionsPanel.restartButton.setForeground(Color.white);
			this.optionsPanel.restartButton.setOpaque(false);
			this.optionsPanel.restartButton.setContentAreaFilled(false);
		} else if(e.getSource() == this.optionsPanel.pointsButton) {
			this.optionsPanel.pointsButton.setForeground(Color.white);
			this.optionsPanel.pointsButton.setOpaque(false);
			this.optionsPanel.pointsButton.setContentAreaFilled(false);
		} else if(e.getSource() == this.optionsPanel.exitButton) {
			this.optionsPanel.exitButton.setForeground(Color.white);
			this.optionsPanel.exitButton.setOpaque(false);
			this.optionsPanel.exitButton.setContentAreaFilled(false);
		} else if(e.getSource() == this.gamePanel.optionsButton) {
			this.gamePanel.optionsButton.setBackground(Color.LIGHT_GRAY);
		}
	}

}
