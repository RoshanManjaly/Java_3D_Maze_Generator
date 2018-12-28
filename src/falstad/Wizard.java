package falstad;

import falstad.Robot.Turn;
import generation.CardinalDirection;

/**
 * CRC Card: Responsibilities: • Looks for neighboring cells closest to the exit
 * • Drives the robot to the exit
 * 
 * Collaborators: • BasicRobot • ManualDriver • MazeController •
 * CardinalDirection
 * 
 * 
 * This algorithm keeps track of the robot's neighbors that are closest to the
 * exit. Using a distance object, the driver allows the robot to keep looking
 * for the best neighbor to move towards.
 */

/**
 * @author roshanmanjaly
 *
 */
public class Wizard extends ManualDriver {

	public Wizard(MazeController maze) {
		super(maze);

	}

	@Override
	public boolean drive2Exit() throws Exception {
		// while the robot is still moving and has not reached the exit, get the nearest
		// neighbor
		while (!robot.hasStopped() && !robot.isAtExit()) {
			CardinalDirection nearestNeighbor = CardinalDirection.getDirection(trackNearestNeighbor()[0],
					trackNearestNeighbor()[1]);

			// if the nearest neighbor is in the current direction of the robot, move
			// forward
			if (nearestNeighbor == robot.getCurrentDirection()) {
				robot.move(1, false);
				// if the nearest neighbor is in the opposite direction, turn around
			} else if (nearestNeighbor == robot.getCurrentDirection().oppositeDirection()) {
				robot.rotate(Turn.AROUND);
				robot.move(1, false);
				// if the nearest neighbor is to the left,turn left
			} else if (nearestNeighbor == robot.getCurrentDirection().rotateClockwise()) {
				robot.rotate(Turn.LEFT);
				robot.move(1, false);
				// if the nearest neighbor is to the right, turn right
			} else if (nearestNeighbor == robot.getCurrentDirection().oppositeDirection().rotateClockwise()) {
				robot.rotate(Turn.RIGHT);
				robot.move(1, false);
			}
		}
		if (robot.isAtExit()) {
			return true;
		}
		return false;
	}

	// method to track the closest neighbor towards the exit
	/**
	 * 
	 * This method is here to help the drive2exit method. The returns the location
	 * of the neighbor closest to the exit.
	 */
	private int[] trackNearestNeighbor() {
		int[] position = null;
		try {
			// record current position of robot
			position = robot.getCurrentPosition();
		} catch (Exception e) {

		}
		// store each direction coordinates into directions
		int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
		int minDistance = Integer.MAX_VALUE;
		// create an array storing the position with the minimum distance
		int[] closestNeighbor = directions[0];
		// coord is an array of x and y positions
		for (int[] coord : directions) {

			// if the robot has a wall in this direction, check the next direction
			// and add this distance of the neighbor into the minDist array
			if ((robot.cells.hasWall(position[0], position[1], CardinalDirection.getDirection(coord[0], coord[1])))) {
				int X = position[0] + coord[0];
				int Y = position[1] + coord[1];
				int currentDist = distance.getDistance(X, Y);
				if (currentDist < minDistance) {
					minDistance = currentDist;
					closestNeighbor = coord;
				}
			}
		}

		return closestNeighbor;
	}

}
