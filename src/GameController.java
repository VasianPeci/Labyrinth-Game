import java.awt.Color;
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
import javax.swing.JOptionPane;

public class GameController {
	
	Player player;
	Labyrinth labyrinth;
	int[] pointsArray;
	String url = ""; // this is the url of the database which establishes the connection, it has to be filled by the person reading this
	String username = ""; // this is the username of the database being used, it has to be filled by the person reading this
	String password = ""; // this is the password of the database being used, it has to be filled by the person reading this
	
	public void finishGame(GameFrame gameFrame, MainPanel mainPanel, GamePanel gamePanel, int status) {
		if(status == 1) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			gameFrame.remove(gamePanel);
			gameFrame.add(mainPanel);
			gameFrame.revalidate();
			gameFrame.repaint();
			player.status = 0;
		} else if(status == 2) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			gameFrame.remove(gamePanel);
			gameFrame.add(mainPanel);
			gameFrame.revalidate();
			gameFrame.repaint();
			player.status = 0;
		}
		
	}
	
	public void saveGame(GameFrame gameFrame) {
		// db connection
				Connection connection = null;
				try {
					connection = DriverManager.getConnection(url, username, password);
				} catch(SQLException exc) {
					JOptionPane.showMessageDialog(null, "Database connection is not possible: " + exc.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
				}
				try {
					String sql = "UPDATE PLAYERS SET row_position = ?, col_position = ?, row_start = ?, col_start = ?, row_end = ?, col_end = ?, points = ?, labyrinthMatrix = ? WHERE username = ?";
					PreparedStatement statement = connection.prepareStatement(sql);
					statement.setInt(1, gameFrame.gamePanel.row_position);
					statement.setInt(2, gameFrame.gamePanel.col_position);
					statement.setInt(3, gameFrame.controller.labyrinth.row_start);
					statement.setInt(4, gameFrame.controller.labyrinth.col_start);
					statement.setInt(5, gameFrame.controller.labyrinth.row_end);
					statement.setInt(6, gameFrame.controller.labyrinth.col_end);
					statement.setInt(7, gameFrame.controller.player.points);
					
					StringBuilder labyrinth_data = new StringBuilder("{\"matrix\": [");
					for(int i = 0; i < 20; i++) {
						labyrinth_data.append("[");
						for(int j = 0; j < 19; j++) {
							labyrinth_data.append(gameFrame.controller.labyrinth.labyrinthNetwork[i][j] + ", ");
						}
						labyrinth_data.append(gameFrame.controller.labyrinth.labyrinthNetwork[i][19] + "]");
						if (i < 19) {
					        labyrinth_data.append(", ");
					    }
					}
					labyrinth_data.append("]}");
					
					statement.setString(8, labyrinth_data.toString());
					statement.setString(9, gameFrame.gameUsername);
					
					statement.executeUpdate();
					
					statement.close();
					connection.close();
					JOptionPane.showMessageDialog(null, "Game saved!", "Info", JOptionPane.PLAIN_MESSAGE);
				} catch (SQLException exc) {
					JOptionPane.showMessageDialog(null, "Game NOT saved!", "Database Error", JOptionPane.ERROR_MESSAGE);
				}
				
				gameFrame.remove(gameFrame.optionsPanel);
				gameFrame.add(gameFrame.gamePanel);
				gameFrame.gamePanel.optionsButton.setBackground(Color.LIGHT_GRAY);
				gameFrame.optionsPanel.saveButton.setForeground(Color.white);
				gameFrame.optionsPanel.saveButton.setOpaque(false);
				gameFrame.optionsPanel.saveButton.setContentAreaFilled(false);
				gameFrame.revalidate();
				gameFrame.repaint();
	}
	
	public void loadGame(GameFrame gameFrame) {
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
					statement.setString(1, gameFrame.gameUsername);
					ResultSet result = statement.executeQuery();
					
					if(result.next()) {
						//start game
						gameFrame.controller.player = new Player(result.getInt("row_position"), result.getInt("col_position"), result.getInt("points"));
						for(int i = 0; i < 20; i++) {
							for(int j = 0; j < 20; j++) {
								String query = String.format("SELECT JSON_VALUE(labyrinthMatrix, '$.matrix[%d][%d]') AS VALUE FROM PLAYERS WHERE username = ?", i, j);
								PreparedStatement statement2 = connection.prepareStatement(query);
								statement2.setString(1, gameFrame.gameUsername);
								ResultSet result2 = statement2.executeQuery();
								result2.next();
								gameFrame.labyrinthNetwork[i][j] = result2.getInt("value");
								result2.close();
								statement2.close();
							}
						}
						gameFrame.controller.labyrinth = new Labyrinth(gameFrame.labyrinthNetwork, result.getInt("row_start"), result.getInt("row_end"), result.getInt("col_start"), result.getInt("col_end"));
						gameFrame.controller.player.gameUsername = gameFrame.gameUsername;
						gameFrame.controller.player.arrayCount = gameFrame.arrayCount;
						gameFrame.controller.player.pointsArray = new int[gameFrame.controller.player.arrayCount];
						connection = null;
						try {
							connection = DriverManager.getConnection(url, username, password);
							for(int i = 0; i < gameFrame.controller.player.arrayCount; i++) {
								String sql2 = String.format("SELECT JSON_VALUE(pointsArray, '$.matrix[0][%d]') AS VALUE FROM PLAYERS WHERE username = ?", i);
								PreparedStatement statement2 = connection.prepareStatement(sql2);
								statement2.setString(1, gameFrame.gameUsername);
								ResultSet result2 = statement2.executeQuery();
								result2.next();
								gameFrame.controller.player.pointsArray[i] = result2.getInt("value");
								statement2.close();
								result2.close();
							}
							connection.close();
						} catch (SQLException exc) {
							JOptionPane.showMessageDialog(null, "Error in database!", "Error", JOptionPane.ERROR_MESSAGE);
						}
						gameFrame.gamePanel = new GamePanel(gameFrame.controller);
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

						// check if game status changes
						new Thread(() -> {
				            while (true) {
				                if (gameFrame.controller.player.status == 2) {
				                	gameFrame.controller.finishGame(gameFrame, gameFrame.mainPanel, gameFrame.gamePanel, gameFrame.controller.player.status);
				                	gameFrame.mainPanel.loadButton.setForeground(Color.white);
				                	gameFrame.mainPanel.loadButton.setOpaque(false);
				                	gameFrame.mainPanel.loadButton.setContentAreaFilled(false);
				                } else if(gameFrame.controller.player.status == 1) {
				                	gameFrame.controller.finishGame(gameFrame, gameFrame.mainPanel, gameFrame.gamePanel, gameFrame.controller.player.status);
				                	gameFrame.arrayCount++;
				                	gameFrame.mainPanel.loadButton.setForeground(Color.white);
				                	gameFrame.mainPanel.loadButton.setOpaque(false);
				                	gameFrame.mainPanel.loadButton.setContentAreaFilled(false);
				                }
				    			
				                try {
				                    Thread.sleep(10);
				                } catch (InterruptedException exc) {
				                    exc.printStackTrace();
				                }
				            }
				        }).start();
					}
					statement.close();
					connection.close();
				} catch (SQLException exc) {
					JOptionPane.showMessageDialog(null, "Game NOT loaded!", "Database Error", JOptionPane.ERROR_MESSAGE);
				}
	}
	
	public int[] sortPoints(int[] points) {
	    if (points == null || points.length == 0) {
	        return new int[0];
	    }

	    int n = points.length;
	    for (int i = 0; i < n - 1; i++) {
	        for (int j = 0; j < n - 1 - i; j++) {
	            if (points[j] < points[j + 1]) {
	                int temp = points[j];
	                points[j] = points[j + 1];
	                points[j + 1] = temp;
	            }
	        }
	    }

	    return points;
	}
}
