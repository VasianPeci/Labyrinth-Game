import java.util.random.*;

public class Labyrinth {
	int[][] labyrinthNetwork = new int[20][20];
	// 0 represents the road to be followed by the player, 1 represents the walls which you cannot pass, 2 represents the treasures and 3 is the way out
	
	int row_start;
	int row_end;
	int col_start;
	int col_end;
	
	public Labyrinth(int[][] labyrinthNetwork, int row_start, int row_end, int col_start, int col_end) {
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 20; j++) {
				if(this.labyrinthNetwork[i][j] != labyrinthNetwork[i][j]) {
					this.labyrinthNetwork[i][j] = labyrinthNetwork[i][j];
				}
			}
		}
		this.row_start = row_start;
		this.row_end = row_end;
		this.col_start = col_start;
		this.col_end = col_end;
	}
	
	public void generateLabyrinth(int row_position, int col_position) {
		if(row_position == row_end-3 && row_end < 20) {
			row_start++;
			row_end++;
		} else if(col_position == col_end-3 && col_end < 20) {
			col_start++;
			col_end++;
		} else if(row_position == row_start+3 && row_start > 0) {
			row_start--;
			row_end--;
		} else if(col_position == col_start+3 && col_start > 0) {
			col_start--;
			col_end--;
		}
	}
	
	public boolean isWall(int row_position, int col_position) {
		if(labyrinthNetwork[row_position][col_position] == 1) {
			return true;
		}
		return false;
	}
	
	public boolean isTreasure(int row_position, int col_position) {
		if(labyrinthNetwork[row_position][col_position] == 2) {
			return true;
		}
		return false;
	}
	
	public boolean isExit(int row_position, int col_position) {
		if(labyrinthNetwork[row_position][col_position] == 3) {
			return true;
		}
		return false;
	}
}
