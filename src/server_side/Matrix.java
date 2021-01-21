package server_side;

import java.util.ArrayList;
import java.util.Arrays;

public class Matrix implements Searchable {
	@SuppressWarnings("rawtypes")
	private State initialState;
	@SuppressWarnings("rawtypes")
	private State goalState;
	private int[][] matrix;
	private MatrixState[][] stateMatrix;

	public Matrix(int[][] matrix) {
		this.matrix = matrix;
		stateMatrix = new MatrixState[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length - 1; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				stateMatrix[i][j] = new MatrixState(i + "," + j);
				stateMatrix[i][j].setCost(matrix[i][j]);
			}
		}
	}

	public Matrix() {
		matrix = null;
		initialState = null;
		goalState = null;
	}

	public void setIntialState(String s) {
		String[] loc = s.split(",");
		
		this.initialState = stateMatrix[Integer.parseInt(loc[0])][Integer.parseInt(loc[1])];
	}

	public void setGoalState(String s) {
		String[] loc = s.split(",");
		
		this.goalState = stateMatrix[Integer.parseInt(loc[0])][Integer.parseInt(loc[1])];
	}

	@SuppressWarnings("rawtypes")
	@Override
	public State getInitialState() {
		return this.initialState;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public State getGoalState() {
		return this.goalState;
	}

	public int[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ArrayList<State> getAllPossibleStates(State s) {
		String[] loc = ((String) s.getState()).split(",");
		int sum = 0;
		int row = Integer.parseInt(loc[0]);
		int col = Integer.parseInt(loc[1]);
		State right = null, left = null, up = null, down = null;

		if (row == 0) {
			sum += 3;
		}

		if (row == matrix.length - 1) {
			sum += 7;
		}

		if (col == 0) {
			sum += 5;
		}

		if (col == matrix[row].length - 1) {
			sum += 11;
		}

		switch (sum) {
		case 8:
			right = stateMatrix[row][col + 1];
			down = stateMatrix[row + 1][col];

			break;
		case 3:
			right = stateMatrix[row][col + 1];
			down = stateMatrix[row + 1][col];
			left = stateMatrix[row][col - 1];

			break;
		case 5:
			right = stateMatrix[row][col + 1];
			down = stateMatrix[row + 1][col];
			up = stateMatrix[row - 1][col];

			break;
		case 7:
			right = stateMatrix[row][col + 1];
			up = stateMatrix[row - 1][col];
			left = stateMatrix[row][col - 1];

			break;
		case 11:
			up = stateMatrix[row - 1][col];
			left = stateMatrix[row][col - 1];
			down = stateMatrix[row + 1][col];

			break;
		case 14:
			left = stateMatrix[row][col - 1];
			down = stateMatrix[row + 1][col];

			break;
		case 12:
			up = stateMatrix[row - 1][col];
			right = stateMatrix[row][col + 1];

			break;
		case 18:
			up = stateMatrix[row - 1][col];
			left = stateMatrix[row][col - 1];

			break;
		default:
			up = stateMatrix[row - 1][col];
			left = stateMatrix[row][col - 1];
			right = stateMatrix[row][col + 1];
			down = stateMatrix[row + 1][col];
		}

		ArrayList<State> ans = new ArrayList<State>();
		State[] surr = { right, left, up, down };
		for (State state : surr) {
			if (state != null && state != s.getCameFrom()) {
				ans.add(state);
			}
		}

		return ans;
	}

	@Override
	public String toString() {
		return "Matrix [initialState=" + initialState + ", goalState=" + goalState + ", matrix="
				+ Arrays.toString(matrix) + "]";
	}

}
