public class Player {
	int row_position;
	int col_position;
	int points;
	int[] pointsArray;
	int arrayCount;
	String gameUsername;
	int status = 0; // 0 - is playing, 1 - has won, 2 - has lost
	
	public Player(){
		this.row_position = 1;
		this.col_position = 1;
		this.points = 0;
	}
	
	public Player(int row_position, int col_position, int points){
		this.row_position = row_position;
		this.col_position = col_position;
		this.points = points;
	}
}
