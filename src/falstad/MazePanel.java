package falstad;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Iterator;
import falstad.Constants.StateGUI;
//import falstad.FirstPersonDrawer.FloatPair;
import falstad.FirstPersonDrawer.RangePair;
import generation.CardinalDirection;
import generation.Cells;
import generation.MazeConfiguration;
/**
 * Add functionality for double buffering to an AWT Panel class. Used for
 * drawing a maze.
 * 
 * @author pk
 *
 */
public class MazePanel extends Panel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * Panel operates a double buffer see
	 * http://www.codeproject.com/Articles/2136/Double-buffer-in-standard-Java-AWT
	 * for details
	 */
	// bufferImage can only be initialized if the container is displayable,
	// uses a delayed initialization and relies on client class to call
	// initBufferImage()
	// before first use
	private Image bufferImage;
	private Graphics2D graphics; // obtained from bufferImage,
	// graphics is stored to allow clients to draw on same graphics object
	// repeatedly
	// has benefits if color settings should be remembered for subsequent drawing
	// operations

	// private Graphics graphic;

	// ADD OBJECTS FOR REFACTORING
	MazeController controller;
	MazeConfiguration mazeConfig;
	Cells seencells;
	RangePair rp;
	public MazePanel color;

	/**
	 * Constructor. Object is not focusable.
	 */
	public MazePanel() {
		setFocusable(false);
		bufferImage = null; // bufferImage initialized separately and later
		graphics = null;

	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	/**
	 * Method to draw the buffer image on a graphics object that is obtained from
	 * the superclass. The method is used in the MazeController. Warning: do not
	 * override getGraphics() or drawing might fail.
	 */
	void update() {
		paint(getGraphics());
	}

	/**
	 * Draws the buffer image to the given graphics object. This method is called
	 * when this panel should redraw itself.
	 */
	@Override
	public void paint(Graphics g) {
		if (null == g) {
			System.out.println("MazePanel.paint: no graphics object, skipping drawImage operation");
		} else {
			g.drawImage(bufferImage, 0, 0, null);
		}
	}

	public void initBufferImage() {
		bufferImage = createImage(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
		if (null == bufferImage) {
			System.out.println("Error: creation of buffered image failed, presumedly container not displayable");
		}
	}

	/**
	 * Obtains a graphics object that can be used for drawing. The object internally
	 * stores the graphics object and will return the same graphics object over
	 * multiple method calls. To make the drawing visible on screen, one needs to
	 * trigger a call of the paint method, which happens when calling the update
	 * method.
	 * 
	 * @return graphics object to draw on, null if impossible to obtain image
	 */
	public Graphics getBufferGraphics() {
		if (null == graphics) {
			// instantiate and store a graphics object for later use
			if (null == bufferImage)
				initBufferImage();
			if (null == bufferImage)
				return null;
			graphics = (Graphics2D) bufferImage.getGraphics();
			if (null == graphics) {
				System.out.println(
						"Error: creation of graphics for buffered image failed, presumedly container not displayable");
			}
			// success case

			// System.out.println("MazePanel: Using Rendering Hint");
			// For drawing in FirstPersonDrawer, setting rendering hint
			// became necessary when lines of polygons
			// that were not horizontal or vertical looked ragged
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		}

		return graphics;
	}

	// ADDED REFACTORED METHODS *****************************************

	// FOR MAPDRAWER *****************************

	// ADDED FROM MAPDRAWER
	int view_width = 400;
	int view_height = 400;
	int map_unit = 128;
	int map_scale = 10;
	int step_size = map_unit / 4;

	public void redraw(Graphics gc, StateGUI state, int px, int py, int view_dx, int view_dy, int walk_step,
			int view_offset, RangeSet rset, int ang) {
		// dbg("redraw") ;
		if (state != StateGUI.STATE_PLAY)
			return;
		if (null != controller) {
			if (controller.isInMapMode()) {
				draw_map(gc, px, py, walk_step, view_dx, view_dy, controller.isInShowMazeMode(),
						controller.isInShowSolutionMode());
				draw_currentlocation(gc, view_dx, view_dy);
			}
		}
	}

	public void draw_map(Graphics gc, int px, int py, int walk_step, int view_dx, int view_dy, boolean showMaze,
			boolean showSolution) {
		// dimensions of the maze

		final int mazew = mazeConfig.getWidth();
		final int mazeh = mazeConfig.getHeight();

		gc.setColor(Color.white);

		// determine offsets for x and y
		int vx = px * map_unit + map_unit / 2;
		vx += viewd_unscale(view_dx * (step_size * walk_step));
		int vy = py * map_unit + map_unit / 2;
		vy += viewd_unscale(view_dy * (step_size * walk_step));
		int offx = -vx * map_scale / map_unit + view_width / 2;
		int offy = -vy * map_scale / map_unit + view_height / 2;

		// compute minimum for x,y
		int xmin = -offx / map_scale;
		int ymin = -offy / map_scale;
		if (xmin < 0)
			xmin = 0;
		if (ymin < 0)
			ymin = 0;

		// compute maximum for x,y
		int xmax = (view_width - offx) / map_scale + 1;
		int ymax = (view_height - offy) / map_scale + 1;
		if (xmax >= mazew)
			xmax = mazew;
		if (ymax >= mazeh)
			ymax = mazeh;

		// iterate over integer grid between min and max of x,y
		for (int y = ymin; y <= ymax; y++)
			for (int x = xmin; x <= xmax; x++) {
				int nx1 = x * map_scale + offx;
				int ny1 = view_height - 1 - (y * map_scale + offy);
				int nx2 = nx1 + map_scale;
				int ny2 = ny1 - map_scale;
				boolean theCondition = (x >= mazew) ? false
						: ((y < mazeh) ? mazeConfig.hasWall(x, y, CardinalDirection.North)
								: mazeConfig.hasWall(x, y - 1, CardinalDirection.South));

				gc.setColor(seencells.hasWall(x, y, CardinalDirection.North) ? Color.white : Color.gray);
				if ((seencells.hasWall(x, y, CardinalDirection.North) || showMaze) && theCondition)
					gc.drawLine(nx1, ny1, nx2, ny1);

				theCondition = (y >= mazeh) ? false
						: ((x < mazew) ? mazeConfig.hasWall(x, y, CardinalDirection.West)
								: mazeConfig.hasWall((x - 1), y, CardinalDirection.East));

				gc.setColor(seencells.hasWall(x, y, CardinalDirection.West) ? Color.white : Color.gray);
				if ((seencells.hasWall(x, y, CardinalDirection.West) || showMaze) && theCondition)
					gc.drawLine(nx1, ny1, nx1, ny2);
			}

		if (showSolution) {
			draw_solution(gc, offx, offy, px, py);
		}
	}

	// added for draw_map()
	final int viewd_unscale(int x) {
		return x >> 16;
	}

	public void draw_currentlocation(Graphics gc, int view_dx, int view_dy) {
		gc.setColor(Color.red);
		// draw oval of appropriate size at the center of the screen
		int ctrx = view_width / 2; // center x
		int ctry = view_height / 2; // center y
		int cirsiz = map_scale / 2; // circle size
		gc.fillOval(ctrx - cirsiz / 2, ctry - cirsiz / 2, cirsiz, cirsiz);
		// draw a red arrow with the oval to indicate direction
		int arrlen = 7 * map_scale / 16; // arrow length
		int aptx = ctrx + ((arrlen * view_dx) >> 16);
		int apty = ctry - ((arrlen * view_dy) >> 16);
		int arrlen2 = map_scale / 4;
		int aptx2 = ctrx + ((arrlen2 * view_dx) >> 16);
		int apty2 = ctry - ((arrlen2 * view_dy) >> 16);
		// int ptoflen = map_scale/8; // unused
		int ptofx = -(arrlen2 * view_dy) >> 16;
		int ptofy = -(arrlen2 * view_dx) >> 16;
		// now the drawing
		gc.drawLine(ctrx, ctry, aptx, apty);
		gc.drawLine(aptx, apty, aptx2 + ptofx, apty2 + ptofy);
		gc.drawLine(aptx, apty, aptx2 - ptofx, apty2 - ptofy);
	}

	public void draw_solution(Graphics gc, int offx, int offy, int px, int py) {

		if (!mazeConfig.isValidPosition(px, py)) {
			dbg(" Parameter error: position out of bounds: (" + px + "," + py + ") for maze of size "
					+ mazeConfig.getWidth() + "," + mazeConfig.getHeight());
			return;
		}
		// current position on the solution path (sx,sy)
		int sx = px;
		int sy = py;
		int distance = mazeConfig.getDistanceToExit(sx, sy);

		gc.setColor(Color.yellow);

		// while we are more than 1 step away from the final position
		while (distance > 1) {
			// find neighbor closer to exit (with no wall in between)
			int[] neighbor = mazeConfig.getNeighborCloserToExit(sx, sy);
			if (null == neighbor)
				return; // error
			// scale coordinates, original calculation:
			// x-coordinates
			// nx1 == sx*map_scale + offx + map_scale/2;
			// nx1+ndx == sx*map_scale + offx + map_scale/2 + dx*map_scale ==
			// (sx+dx)*map_scale + offx + map_scale/2;
			// y-coordinates
			// ny1 == view_height-1-(sy*map_scale + offy) - map_scale/2;
			// ny1+ndy == view_height-1-(sy*map_scale + offy) - map_scale/2 + -dy *
			// map_scale == view_height-1 -((sy+dy)*map_scale + offy) - map_scale/2
			// current position coordinates
			int nx1 = sx * map_scale + offx + map_scale / 2;
			int ny1 = view_height - 1 - (sy * map_scale + offy) - map_scale / 2;
			// neighbor position coordinates
			int nx2 = neighbor[0] * map_scale + offx + map_scale / 2;
			int ny2 = view_height - 1 - (neighbor[1] * map_scale + offy) - map_scale / 2;
			gc.drawLine(nx1, ny1, nx2, ny2);

			// update loop variables for current position (sx,sy) and distance d for next
			// iteration
			sx = neighbor[0];
			sy = neighbor[1];
			distance = mazeConfig.getDistanceToExit(sx, sy);

		}
	}

	// added for draw solution
	public void dbg(String str) {
		// : change this to a logger
		System.out.println("MapDrawer:" + str);
	}

	// ********************************************************* end of mapdrawer

	// Refactor DEFAULTVIEWER METHODS **********************************

	public void redraw2(Graphics gc, StateGUI state, int px, int py, int view_dx, int view_dy, int walk_step,
			int view_offset, RangeSet rset, int ang) {
		dbg("redraw");
	}

	// *********************

	/**
	 * Notify all registered viewers to redraw their graphics
	 * 
	 * @param mazeController
	 *            
	 */
	// MOVE THIS TO MAZE PANEL? ***********************
	final ArrayList<Viewer> views = new ArrayList<Viewer>();

	@SuppressWarnings("deprecation")
	protected void notifyViewerRedraw(MazeController mazeController) {
		// go through views and notify each one
		Iterator<Viewer> it = mazeController.views.iterator();
		while (it.hasNext()) {
			Viewer v = it.next();
			Graphics g = getBufferGraphics();
			// viewers draw on the buffer graphics
			if (null == g) {
				System.out.println(
						"Maze.notifierViewerRedraw: can't get graphics object to draw on, skipping redraw operation");
			} else {
				// WILL NEED TO CHANGE GRAPHICS TO MAZEPANEL SOMEHOW
				((DefaultViewer) v).redraw(g, mazeController.state, mazeController.px, mazeController.py,
						mazeController.viewdx, mazeController.viewdy, mazeController.walkStep, Constants.VIEW_OFFSET,
						mazeController.rset, mazeController.angle);
			}

		}
		// update the screen with the buffer graphics
		update();
	}

	// REFACTOR MAZECONTROLLER METHODS ************************


	/*
	 * protected void notifyViewerRedraw() { // go through views and notify each one
	 * Iterator<Viewer> it = views.iterator() ; while (it.hasNext()) { Viewer v =
	 * it.next() ; Graphics g = this.getBufferGraphics() ;
	 * 
	 * // viewers draw on the buffer graphics if (null == g) { System.out.
	 * println("Maze.notifierViewerRedraw: can't get graphics object to draw on, skipping redraw operation"
	 * ) ; } else { // WILL NEED TO CHANGE GRAPHICS TO MAZEPANEL SOMEHOW v.redraw(g,
	 * state, px, py, viewdx, viewdy, walkStep, Constants.VIEW_OFFSET, rset, angle)
	 * ; }
	 * 
	 * } // update the screen with the buffer graphics this.update() ; }
	 * 
	 * 
	 * /* public Object getPanel() { return panel ; }
	 * 
	 */

	// ***************************** end of mazecontroller methods

}
