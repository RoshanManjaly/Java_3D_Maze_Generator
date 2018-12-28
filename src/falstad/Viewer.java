package falstad;

/**
 * Interface to specify all functionality provided by a viewer to the maze
 * model. The Viewer is used to uniformly treat the MazeView, the
 * FirstPersonDrawer and the MapDrawer by the Maze class. Viewers can register
 * with a maze and get notified via method calls if a corresponding event is
 * triggered on the GUI.
 * 
 * @author Kemper
 *
 */
public interface Viewer {
	/**
	 * Increases the map if the map is on display.
	 */
	public void incrementMapScale();

	/**
	 * Shrinks the map if the map is on display.
	 */
	public void decrementMapScale();

}
