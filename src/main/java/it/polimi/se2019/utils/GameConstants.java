package it.polimi.se2019.utils;

/**
 * Contains all the constants of the game.
 *
 * @author Desno365
 * @author Marchingegno
 */
public class GameConstants {

	public static final int MAX_PLAYERS = 5;
	public static final int DEATH_DAMAGE = 11;
	public static final int OVERKILL_DAMAGE = 12;
	public static final int MAX_MARKS_PER_PLAYER = 3;
	public static final int MAX_WEAPON_CARDS_PER_PLAYER = 3;
	public static final int MAX_POWERUP_CARDS_PER_PLAYER = 3;
	public static final int MAX_AMMO_PER_AMMO_TYPE = 3;
	public static final int INITIAL_AMMO_PER_AMMO_TYPE = 1;
	public static final int MIN_SKULLS = 5;
	public static final int MAX_SKULLS = 8;
	public static final int MIN_PLAYERS = 3;
	public static final int NUMBER_OF_ACTIONS_PER_TURN = 2;
	public static final int FRENZY_BEFORE_NUMBER_OF_ACTION_PER_TURN = 2;
	public static final int FRENZY_AFTER_NUMBER_OF_ACTION_PER_TURN = 1;
	public static final int FIRST_BLOOD_SCORE = 1;
	public static final ImmutableListWithDefaultValue<Integer> KILLSHOT_SCORES = new ImmutableListWithDefaultValue<>(new Integer[]{8, 6, 4, 2}, 1);
	public static final ImmutableListWithDefaultValue<Integer> PLAYER_SCORES = new ImmutableListWithDefaultValue<>(new Integer[]{8, 6, 4, 2}, 1);
	public static final ImmutableListWithDefaultValue<Integer> FRENZY_SCORES = new ImmutableListWithDefaultValue<>(new Integer[]{2}, 1);
	public static final int MEDIUM_DAMAGE_THRESHOLD = 3;
	public static final int HIGH_DAMAGE_THRESHOLD = 6;
	public static final int MAX_NICKNAME_LENGTH = 16;
	public static final int MAX_NUM_OF_WEAPONS_IN_SPAWN_SQUARE = 3;
	public static final int NUM_OF_ROWS_IN_SQUARE = 9;
	public static final int NUM_OF_COLUMNS_IN_SQUARE = 17;

	//ONLY FOR TESTING PURPOSES.
	public static final int DEBUG_DAMAGE_OVERLOAD = 0; // LEAVE TO 0 FOR A NORMAL GAME.
	public static final boolean DEBUG_DONOTEND = false; // LEAVE TO false FOR A NORMAL GAME.


	/**
	 * Since it's an utility class it can't be instantiated.
	 */
	private GameConstants() {
		throw new IllegalStateException("Cannot create an instance of this utility class.");
	}

	public enum MapType {
		SMALL_MAP("SmallMap", "Small map with 10 squares."),
		MEDIUM1_MAP("Medium1Map", "Medium map with 11 squares."),
		MEDIUM2_MAP("Medium2Map", "Bonus map with 11 squares."),
		BIG_MAP("BigMap", "Big map with 12 squares.");

		private String mapName;

		private String description;

		MapType(String mapName, String description) {
			this.mapName = mapName;
			this.description = description;
		}

		public String getMapName() {
			return mapName;
		}

		public String getDescription() {
			return description;
		}
	}
}
