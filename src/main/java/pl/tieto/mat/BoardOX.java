package pl.tieto.mat;

public class BoardOX {
	private String[] board;
	private boolean endGame;
	private String winner;

	public String[] getBoard() {
		return board;
	}

	public BoardOX() {
		super();
		this.board = new String[9];
		this.endGame = false;
		this.winner = "";
	}

	public void setBoard(String[] board) {
		this.board = board;
	}

	public void updateBoad(int position, String symbol) {
		board[position] = symbol;
		checkWin(symbol);
	}

	private void checkWin(String sym) {
		if(sym.equals(board[0]) && sym.equals(board[3]) && sym.equals(board[6])) {
			winner = sym;
		}
		if(sym.equals(board[1]) && sym.equals(board[4]) && sym.equals(board[7])) {
			winner = sym;
		}
		if(sym.equals(board[2]) && sym.equals(board[5]) && sym.equals(board[8])) {
			winner = sym;
		}
		if(sym.equals(board[0]) && sym.equals(board[1]) && sym.equals(board[2])) {
			winner = sym;
		}
		if(sym.equals(board[3]) && sym.equals(board[4]) && sym.equals(board[5])) {
			winner = sym;
		}
		if(sym.equals(board[6]) && sym.equals(board[7]) && sym.equals(board[8])) {
			winner = sym;
		}
		if(sym.equals(board[0]) && sym.equals(board[4]) && sym.equals(board[8])) {
			winner = sym;
		}
		if(sym.equals(board[2]) && sym.equals(board[4]) && sym.equals(board[6])) {
			winner = sym;
		}
		
	}


	public boolean isEndGame() {
		return endGame;
	}

	public void setEndGame(boolean endGame) {
		this.endGame = endGame;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

}
