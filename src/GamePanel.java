import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements KeyListener{
	Image road;
	Image wall;
	Image treasure;
	Image exitImage;
	Image character;
	Image gameOver;
	Image winningBackground;
	ImageIcon optionsIcon;
	int row_position;
	int col_position;
	int[][] labyrinthNetwork = new int[20][20];
	GameController controller;
	Labyrinth labyrinth;
	Player player;
	JLabel pointsLabel;
	JLabel timeLabel;
	JLabel finalPoints;
	Font joystix;
	JButton optionsButton;
	long startTime;
	long endTime;
	String url = ""; // this is the url of the database which establishes the connection, it has to be filled by the person reading this
	String username = ""; // this is the username of the database being used, it has to be filled by the person reading this
	String password = ""; // this is the password of the database being used, it has to be filled by the person reading this
	
	public GamePanel(GameController controller) {
		this.setPreferredSize(new Dimension(600, 600));
		this.setLayout(null);
		this.controller = controller;
		this.labyrinth = controller.labyrinth;
		this.player = controller.player;
		this.row_position = player.row_position;
		this.col_position = player.col_position;
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 20; j++) {
				this.labyrinthNetwork[i][j] = controller.labyrinth.labyrinthNetwork[i][j];
			}
		}
		
		// new font import
		try {
			File joystixFile = new File("joystix monospace.otf");
			joystix = Font.createFont(Font.TRUETYPE_FONT, joystixFile);
			joystix = joystix.deriveFont(30f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			System.out.println("Font not found!");
		}
		
		finalPoints = new JLabel();
		finalPoints.setForeground(Color.white);
		finalPoints.setFont(joystix);
		finalPoints.setBounds(170, 450, 300, 50);
		
		timeLabel = new JLabel();
		timeLabel.setForeground(Color.white);
		timeLabel.setFont(joystix);
		timeLabel.setBounds(170, 500, 300, 50);
		
		startTime = System.currentTimeMillis();
		
		pointsLabel = new JLabel("Points: " + player.points);
		pointsLabel.setForeground(Color.white);
		pointsLabel.setFont(joystix);
		pointsLabel.setBounds(10, 0, 300, 50);
		this.add(pointsLabel);
				
		road = new ImageIcon("road.png").getImage();
		wall = new ImageIcon("wall.png").getImage();
		treasure = new ImageIcon("treasure.png").getImage();
		exitImage = new ImageIcon("exitImage.png").getImage();
		character = new ImageIcon("character.png").getImage();
		gameOver = new ImageIcon("gameover.png").getImage();
		winningBackground = new ImageIcon("winningBackground.png").getImage();
		optionsIcon = new ImageIcon("optionsIcon.png");
		
		optionsButton = new JButton();
		optionsButton.setIcon(optionsIcon);
		optionsButton.setBounds(545, 5, 50, 50);
		optionsButton.setBorder(new javax.swing.border.LineBorder(Color.WHITE, 2, true));
		optionsButton.setBackground(Color.LIGHT_GRAY);
		this.add(optionsButton);
		
		addKeyListener(this);
	}
	
	@Override
	public void addNotify() {
	    super.addNotify();
	    requestFocusInWindow();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        this.remove(pointsLabel);
        
        pointsLabel = new JLabel("Points: " + player.points);
		pointsLabel.setForeground(Color.white);
		pointsLabel.setFont(joystix);
		pointsLabel.setBounds(10, 0, 300, 50);
		this.add(pointsLabel);
        
        labyrinth.generateLabyrinth(row_position, col_position);
        
        for(int i = labyrinth.row_start; i < labyrinth.row_end; i++) {
        	for(int j = labyrinth.col_start; j < labyrinth.col_end; j++) {
        		if(labyrinthNetwork[i][j] == 0) {
        			g2d.drawImage(road, (j-labyrinth.col_start)*50, (i-labyrinth.row_start)*50, null);
        		} else if(labyrinthNetwork[i][j] == 1) {
        			g2d.drawImage(wall, (j-labyrinth.col_start)*50, (i-labyrinth.row_start)*50, null);
        		} else if(labyrinthNetwork[i][j] == 2) {
        			g2d.drawImage(treasure, (j-labyrinth.col_start)*50, (i-labyrinth.row_start)*50, null);
        		} else if(labyrinthNetwork[i][j] == 3) {
        			g2d.drawImage(exitImage, (j-labyrinth.col_start)*50, (i-labyrinth.row_start)*50, null);
        		}
        	}
        }
        
        g2d.drawImage(character, (col_position-labyrinth.col_start)*50, (row_position-labyrinth.row_start)*50, null);
        
        if(labyrinth.isWall(row_position, col_position)) {
			player.status = 2;
			this.remove(pointsLabel);
			this.remove(optionsButton);
			g2d.drawImage(gameOver, 0, 0, null);
			removeKeyListener(this);
			// sound effect
			try {
	            File audioFile = new File("Game Over  Sound Effect.wav");
	            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
	            Clip clip = AudioSystem.getClip();
	            clip.open(audioStream);
	            clip.start();
	        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException exc) {
	            System.out.println(exc);
	        }
		} else if(labyrinth.isTreasure(row_position, col_position)) {
			player.points++;
			labyrinthNetwork[row_position][col_position] = 0;
			labyrinth.labyrinthNetwork[row_position][col_position] = 0;
			// sound effect
			try {
	          File audioFile = new File("treasuresound.wav");
	          AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
	          Clip clip = AudioSystem.getClip();
	          clip.open(audioStream);
	          clip.start();
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException exc) {
				System.out.println(exc);
			}
			this.repaint();
		} else if(labyrinth.isExit(row_position, col_position)) {
			player.status = 1;
			
			player.arrayCount++;
			int[] tempArray = new int[player.arrayCount-1];
			for(int i = 0; i < player.arrayCount-1; i++) {
				tempArray[i] = player.pointsArray[i];
			}
			player.pointsArray = new int[player.arrayCount];
			StringBuilder pointsArrayString = new StringBuilder("{\"matrix\": [[");
			for(int i = 0; i < player.arrayCount-1; i++) {
				player.pointsArray[i] = tempArray[i];
				pointsArrayString.append(player.pointsArray[i] + ", ");
			}
			player.pointsArray[player.arrayCount-1] = player.points;
			pointsArrayString.append(player.pointsArray[player.arrayCount-1] + "]]}");
			
			//db connection
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(url, username, password);
				String sql = "UPDATE PLAYERS SET pointsArray = ?, arrayCount = ? WHERE username = ?";
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, pointsArrayString.toString());
				statement.setInt(2, player.arrayCount);
				statement.setString(3, player.gameUsername);
				statement.executeUpdate();
			} catch(SQLException exc) {
				JOptionPane.showMessageDialog(null, "Database connection is not possible: " + exc.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
			}
			
			this.remove(pointsLabel);
			this.remove(optionsButton);
			finalPoints.setText("Points: " + player.points + "/" + "6");
			
			endTime = System.currentTimeMillis();

	        // Time Calculation
	        long time = endTime - startTime;
	        long minutes = (time / 1000) / 60;
	        long seconds = (time / 1000) % 60;

	        // Format as mm:ss
	        String formattedTime = String.format("%02d:%02d", minutes, seconds);
			timeLabel.setText("Time: " + formattedTime);
			
			this.add(finalPoints);
			this.add(timeLabel);
			g2d.drawImage(winningBackground, 0, 0, null);
			removeKeyListener(this);
			// sound effect
			try {
				File audioFile = new File("win.wav");
		        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
		        Clip clip = AudioSystem.getClip();
		        clip.open(audioStream);
		        clip.start();
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException exc) {
				System.out.println(exc);
			}
		}
    }

	@Override
	public void keyTyped(KeyEvent e) {
	
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			row_position--;
			this.repaint();
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			row_position++;
			this.repaint();
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			col_position--;
			this.repaint();
		} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			col_position++;
			this.repaint();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
}
