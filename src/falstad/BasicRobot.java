package falstad;

import falstad.MazeController.UserInput;
import generation.CardinalDirection;
import generation.Cells;

// this class will implement the Robot interface
/**
 * The BasicRobot class designs a robot that can traverse through a maze. This
 * robot has distance sensors in all four directions as well as a room sensor.
 * Within this class we will keep track of the path length and battery levels.
 * The turn and rotate methods will call a MazeController method in order to
 * update the maze view.
 */
public class BasicRobot implements Robot {

	// initialize variables we'll be using
	protected CardinalDirection currDir;
	// the Robot class works on top of a MazeController object
	protected MazeController maze;
	protected MazePanel panel;
	protected Cells cells;
	protected static float energyLevel;
	protected boolean hasStopped;
	protected int[] currentPosition;

	static int pathLength = 0;
	public int odometer;

	// sensors
	protected boolean frontSensor;
	protected boolean backSensor;
	protected boolean leftSensor;
	protected boolean rightSensor;

	// public ManualDriver value;
	public MazeController controller;

	// constructor
	public BasicRobot() {

		maze = null;
		currentPosition = new int[2];

		currentPosition[0] = 0;
		currentPosition[1] = 0;
		// we'll be initializing battery level to 3000
		energyLevel = 3000;

		hasStopped = false;

		// this robot is capable of sensing
		frontSensor = true;
		backSensor = true;
		leftSensor = true;
		rightSensor = true;
		currDir = CardinalDirection.East;

	}

	@Override // CALL MAZECONTROLLER HERE
	// we use the parameter turn to determine how we rotate

	public void rotate(Turn turn) {
		switch (turn) {
		// use switch statements
		case RIGHT:
			// check battery, if there is enough to turn it completes the turn
			if (energyLevel >= 3) {
				currDir = currDir.rotateClockwise();
				// call MazeController to update visualizations
				UserInput key = UserInput.Right;
				maze.operateGameInPlayingState(key);
				// update battery level
				energyLevel -= 3;

			}
			// else the robot has stopped
			else {
				hasStopped = true;
				// UPDATE END SCREEN
				// maze.getPanel().notifyViewerRedraw(this);
				panel.notifyViewerRedraw(maze);
			}
			break;

		case LEFT:
			// check battery, if there is enough to turn it completes the turn
			if (energyLevel >= 3) {
				currDir = currDir.rotateCounterClockwise();
				// call mazecontroller
				UserInput key = UserInput.Left;
				maze.operateGameInPlayingState(key);

				energyLevel -= 3;
			}
			// else the robot has stopped
			else {
				hasStopped = true;
				// END SCREEN UPDATE
				// maze.getPanel().notifyViewerRedraw(this);
				panel.notifyViewerRedraw(maze);
			}
			break;

		case AROUND:
			// check battery, if there is enough to turn it completes the turn
			if (energyLevel >= 6) {
				currDir = currDir.oppositeDirection();
				// MAKE 180 TURN
				maze.operateGameInPlayingState(UserInput.Left);
				maze.operateGameInPlayingState(UserInput.Left);
				// this counts as two quarter turns, thus it costs 6 points
				energyLevel -= 6;
			}
			// else the robot has stopped
			else {
				hasStopped = true;
				// maze.getPanel().notifyViewerRedraw(this);
				panel.notifyViewerRedraw(maze);
			}
			break;
		}
	}

	@Override
	public void move(int distance, boolean manual) {

		while (distance > 0) { // This checks to make sure that the robot still is supposed to move

			// get out current position
			currentPosition = maze.getCurrentPosition();

			if (energyLevel >= 5) {
				// if we are manually going through maze
				if (manual == true) {
					distance = 1;
				}
				// if we do not have a wall directly facing us, we want to continue moving in
				// the same direction
				if (distanceToObstacle(Direction.FORWARD) > 0) {

					// create a switch to see which direction you are facing so you can change
					// your current position
					switch (currDir) {

					case West:
						currentPosition[0]--; // adjust the robot position and then update the visuals
						// call mazecontroller
						// we use key = UserInput.Up because the robot turns around and then moves
						// forward
						maze.operateGameInPlayingState(UserInput.Up);
						break;

					case East:
						currentPosition[0]++; // adjust the robot position and then update the visuals
						maze.operateGameInPlayingState(UserInput.Up);
						break;

					case North:
						currentPosition[1]--; // adjust the robot position and then update the visuals
						maze.operateGameInPlayingState(UserInput.Up);
						break;

					case South:
						currentPosition[1]++; // adjust the robot position and then update the visuals
						maze.operateGameInPlayingState(UserInput.Up);
						break;
					}
					// The maze sets its current position to match whatever the change makes
					maze.setCurrentPosition(currentPosition[0], currentPosition[1]);

					// update battery life
					energyLevel -= 5;
					pathLength++; // update the pathlength
					distance--;
				}
				// else if we hit a wall, stop game
				else {
					hasStopped = true;
					// UPDATE END SCREEN
					// maze.getPanel().notifyViewerRedraw(this);
					panel.notifyViewerRedraw(maze);
				}
			}
			// else if we have no more battery life, stop game
			else {
				hasStopped = true;
			}
		}
	}

	@Override
	public int[] getCurrentPosition() {
		return this.currentPosition;
	}

	@Override
	public void setMaze(MazeController maze) {

		this.maze = maze;
		/*
		 * this.currentPosition = this.maze.getCurrentPosition(); // creates a new cells
		 * object and fills it with the maze cells // by changing mazeConfig from
		 * MazeController to public we are able to obtain maze attributes cells = new
		 * Cells(this.maze.mazeConfig.getWidth(), this.maze.mazeConfig.getHeight());
		 * cells = this.maze.mazeConfig.getMazecells();
		 * 
		 * int[] cd = new int[2]; cd = currDir.getDirection(); // updates currDir
		 * accordingly if (cd[0] == 0 && cd[1] == -1) { currDir =
		 * CardinalDirection.North; }
		 * 
		 * else if (cd[0] == 1 && cd[1] == 0) { currDir = CardinalDirection.East; }
		 * 
		 * else if (cd[0] == 0 && cd[1] == 1) { currDir = CardinalDirection.South; }
		 * 
		 * else { currDir = CardinalDirection.West; }
		 */
	}

	@Override
	public boolean isAtExit() {

		return cells.isExitPosition(this.currentPosition[0], this.currentPosition[1]);
	}

	@Override
	public boolean canSeeExit(Direction direction) throws UnsupportedOperationException {

		if (hasDistanceSensor(direction) == true) {
			if (distanceToObstacle(direction) == Integer.MAX_VALUE) {
				return true;
			}

			else {
				return false;
			}
		}

		else {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public boolean isInsideRoom() throws UnsupportedOperationException {
		return cells.isInRoom(this.currentPosition[0], this.currentPosition[1]);
	}

	@Override
	public boolean hasRoomSensor() {
		return true;
	}

	@Override
	public CardinalDirection getCurrentDirection() {
		return this.maze.getCurrentDirection();
	}

	@Override
	public float getBatteryLevel() {
		return energyLevel;
	}

	@Override
	public void setBatteryLevel(float level) {
		energyLevel = level;
	}

	@Override
	public float getEnergyForFullRotation() {
		return 12;
	}

	@Override
	public float getEnergyForStepForward() {
		return 5;
	}

	@Override
	public boolean hasStopped() {
		return hasStopped;
	}

	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		// if robot has distance sensor in given direction, then rotate to that
		// direction
		if (hasDistanceSensor(direction)) {
			// sensor cost = 1
			setBatteryLevel(energyLevel - 1);
			// get direction for checking walls in that direction
			CardinalDirection cd;
			if (direction == Direction.LEFT) {
				cd = currDir.rotateCounterClockwise();
			}

			else if (direction == Direction.RIGHT) {
				cd = currDir.rotateClockwise();
			}

			else if (direction == Direction.BACKWARD) {
				cd = currDir.oppositeDirection();
			}

			else {
				cd = currDir;
			}

			int count = 0;
			int X = this.maze.getCurrentPosition()[0]; // current x coordinate
			int Y = this.maze.getCurrentPosition()[1]; // current y coordinate

			while (true) {
				if (X < 0 || X >= cells.width || Y < 0 || Y >= cells.height) { // check if we are still within range
					return Integer.MAX_VALUE;
				}

				else {
					switch (cd) { // determine whether there is a wall at each direction
					case North:
						if (cells.hasWall(X, Y, CardinalDirection.North)) {
							return count;
						}
						Y--;
						break;
					case South:
						if (cells.hasWall(X, Y, CardinalDirection.South)) {
							return count;
						}
						Y++;
						break;
					case East:
						if (cells.hasWall(X, Y, CardinalDirection.East)) {
							return count;
						}
						X++;
						break;
					case West:
						if (cells.hasWall(X, Y, CardinalDirection.West)) {
							return count;
						}
						X--;
						break;
					}
					count++;
				}
			}
		} else {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public boolean hasDistanceSensor(Direction direction) {

		if (direction == Direction.RIGHT) {
			return rightSensor;
		}

		else if (direction == Direction.LEFT) {
			return leftSensor;
		}

		else if (direction == Direction.FORWARD) {
			return frontSensor;
		}

		else {
			return backSensor;
		}

	}

	public static int getPathLength() {
		return pathLength;
	}

	@Override

	public int getOdometerReading() {
		return pathLength;

	}

	@Override
	public void resetOdometer() {
		odometer = 0;

	}

	public Object getMaze() {
		Cells mazeCells = this.maze.mazeConfig.getMazecells();

		return mazeCells;
	}

}
