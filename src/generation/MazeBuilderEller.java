package generation;

/**
 * This class implements Eller's Algorithm. We assume that we have a maze with
 * no initialized cells with all walls up. We start at the top row (upper left
 * hand corner of maze) and iterate through the matrix first horizontally and
 * then vertically. As we move horizontally and vertically, we randomly delete
 * walls and merge cells using a random decision mechanism. We store each cell
 * in a particular set (by number identity) in order to determine whether a wall
 * should be deleted or not. This implementation is done for each row until we
 * reach the final row, in which we just merge all cells together.
 * 
 */
public class MazeBuilderEller extends MazeBuilder implements Runnable {

	public MazeBuilderEller() {
		super();
		System.out.println("MazeBuilderEller uses Eller's algorithm to generate maze.");
	}

	public MazeBuilderEller(boolean det) {
		super(det);
		System.out.println("MazeBuilderPrim uses Eller's algorithm to generate maze.");
	}

	@Override
	/**
	 * This method is responsible for using Eller's algorithm to generate a maze.
	 */

	protected void generatePathways() {
		int[][] Sets = new int[width][height];
		int i;
		int identity = width; // used for giving set identity
		int x = 0; // used to iterate through rows
		int y = 0; // used to iterate through columns
		int randomPosition = 0;
		Wall eastWall = null; // used to break down right wall
		Wall southWall; // used to break down bottom wall
		while (y != height) {
			// while we have not reached the last row
			// either initialize or copy sets from previous row
			// then populate row
			if (y == 0) {
				// if we are in the first row
				// add each cell in the first row to its own set
				for (i = 0; i < width; i++)
					Sets[i][0] = i;
			}
			// copy top row. If a cell has a wall above it, put it in its own set
			else { // else if we are not in the first row
				for (i = 0; i < width; i++) {
					if (cells.hasWall(i, y, CardinalDirection.North)) {

						// store this cell in a new set
						Sets[i][y] = identity++;
					} else
						// or else, if there is no wall above it, set cell's value to the value above it
						Sets[i][y] = Sets[i][y - 1];
				}
			}

			// Horizontal iteration
			// while you have not reached the last cell of the row
			while (x != width) {

				// check for the last row
				// if we are at the last row, delete the walls if the cells are not the same
				// already
				if (y == (height - 1)) {
					for (i = 0; i < (width - 1); i++) {
						if (Sets[i][y] != Sets[i + 1][y])
							eastWall = new Wall(i, y, CardinalDirection.East);
						cells.deleteWall(eastWall);

					}
					break;
				}
				// if we are not at the last cell of the row
				if (x != (width - 1)) {
					// if the cell we are at does not equal the cell to its right
					if (Sets[x][y] != Sets[x + 1][y]) {
						// get a random number between 0,1

						randomPosition = random.nextIntWithinInterval(0, 1);
					} else { // else if the cell we're at does equal the cell to its right
								// increment to the next cell (to the right)
						x++;
						continue;
					}
				} else { // else if if we are at the last cell of the row, increment to next cell
					x++;
					continue;
				}

				// knock down wall
				// 0 is the decision that we knock down a wall
				if (randomPosition == 0) {
					eastWall = new Wall(x, y, CardinalDirection.East);

					cells.deleteWall(eastWall);

					// set the next door cell value to the cell before it (this is the naming
					// mechanism)
					Sets[x + 1][y] = Sets[x][y];
				} // else if the decision is 1, then just go to next cell to right
				else
					x++;
			}
			// now we reset back to the first cell in a row
			x = 0;

			// Vertical iteration
			// while we are not the last cell in row
			while (x != width) {
				// if we are the second to last row , skip
				if (y == (height - 1))
					break;
				// set variable start col to whatever cell we're at
				int startCol = x;

				// if we are the second to last cell
				if (x == (width - 1)) {
					southWall = new Wall(x, y, CardinalDirection.South);

					cells.deleteWall(southWall);
					break;
				}
				// while loop infinite until broken
				while (true) {
					// if the cell we are at the second to last cell, skip
					if (x == (width - 1))
						break;
					// if the cell we are at has the same value as the cell to its right
					if (Sets[x][y] == Sets[x + 1][y]) {
						// keep moving
						x++;
						continue;
					} else
						break;

				}
				// delete one wall randomly from the set of walls on the bottom
				// startCol is the starting position of whatever row you are in
				randomPosition = random.nextIntWithinInterval(startCol, x);
				southWall = new Wall(randomPosition, y, CardinalDirection.South);

				cells.deleteWall(southWall);
				// keep moving
				x++;

			}
			// increment the row, and do the whole thing again.
			y++;
			x = 0;
		}
	}

}
