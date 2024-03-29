package it.polimi.se2019.utils;

/**
 * This class contains all the information to execute an action, and it does so by splitting it into atomic actions.
 *
 * @author Marchingegno
 */
public class MacroAction {

	private final String name;
	private final int numOfMovements;
	private final boolean grab;
	private final boolean reload;
	private final boolean shoot;
	private boolean moved;
	private boolean grabbed;
	private boolean reloaded;
	private boolean shot;

	public MacroAction(int numOfMovements, boolean grab, boolean reload, boolean shoot, String name) {
		this.numOfMovements = numOfMovements;
		this.grab = grab;
		this.reload = reload;
		this.shoot = shoot;
		this.name = name;
		refill();
	}


	public int getNumOfMovements() {
		return numOfMovements;
	}

	public boolean isMove() {
		return numOfMovements > 0;
	}

	public boolean isGrab() {
		return grab;
	}

	public boolean isReload() {
		return reload;
	}

	public boolean isShoot() {
		return shoot;
	}


	/**
	 * Following the natural order of the action, it advances the MacroAction and returns the next ActionType to execute.
	 *
	 * @return the next ActionType to execute.
	 */
	public ActionType getNextActionToExecuteAndAdvance() {
		if (isMove() && !moved) {
			moved = true;
			return ActionType.MOVE;
		} else if (isGrab() && !grabbed) {
			grabbed = true;
			return ActionType.GRAB;
		} else if (isReload() && !reloaded) {
			reloaded = true;
			return ActionType.RELOAD;
		} else if (isShoot() && !shot) {
			shot = true;
			return ActionType.SHOOT;
		}
		refill();
		return ActionType.END;
	}

	private void refill() {
		if (isMove())
			moved = false;
		if (isGrab())
			grabbed = false;
		if (isReload())
			reloaded = false;
		if (isShoot())
			shot = false;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name + "[" +
				"numOfMovements = " + numOfMovements +
				", grab = " + grab +
				", reload = " + reload +
				", shoot = " + shoot +
				']';
	}

	/**
	 * Builds the string associated with this MacroAction.
	 * @return the string representing the MacroAction.
	 */
	public String getMacroActionString() {
		StringBuilder myBuilder = new StringBuilder();

		for (int i = 0; i < this.numOfMovements; i++) {
			myBuilder.append(">");
		}

		if (isGrab()) {
			myBuilder.append("G");
		}

		if (isReload()) {
			myBuilder.append("R");
		}

		if (isShoot()) {
			myBuilder.append("S");
		}

		return myBuilder.toString();
	}
}