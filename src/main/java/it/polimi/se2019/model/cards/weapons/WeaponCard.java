package it.polimi.se2019.model.cards.weapons;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.cards.ActivableCard;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.gamemap.GameMap;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract class that defines the structure of a weapon card. Every subtype of weapon must extend this.
 * The weapons are handled with the help of the **Strategy Pattern*. Each weapon has a predetermined number of
 * "advancement steps", that represent the current state of advancement of the firing process of the weapon.
 * Each time the controller wishes to continue the process of firing of the weapon, it must call the method **doActivationStep**;
 * This will return a {@link QuestionContainer} that the controller must send to the Virtual View of the shooting player.
 * The controller will need to {@link #reset()} the weapon
 * if it has finished firing. The method {@link #reset()} will deload and reset the weapon to its original state.
 *
 * @author Marchingegno
 */
public abstract class WeaponCard extends ActivableCard {

	private final ArrayList<AmmoType> reloadPrice;
	private final int moveDistance; //Standard move for relocation of the player.
	private final int primaryDamage;
	private final int primaryMarks;
	int maximumSteps; //Maximum advancement steps.
	boolean relocationDone; //If the player has already been relocated.
	boolean enemyRelocationDone; //If the enemies have already been relocated. Not sure if this is useful.
	List<DamageAndMarks> standardDamagesAndMarks;
	List<Player> currentTargets;
	Player target;
	private boolean loaded;
	private List<Player> playersHit;


	public WeaponCard(JsonObject parameters) {
		super(parameters.get("name").getAsString(), parameters.get("description").getAsString(), parameters.get("imagePath").getAsString());
		this.standardDamagesAndMarks = new ArrayList<>();
		this.reloadPrice = new ArrayList<>();

		for (JsonElement ammoPrice : parameters.getAsJsonArray("price")) {
			this.reloadPrice.add(AmmoType.valueOf(ammoPrice.getAsString()));
		}

		this.primaryDamage = parameters.get("primaryDamage").getAsInt();
		this.primaryMarks = parameters.get("primaryMarks").getAsInt();
		this.moveDistance = parameters.get("moveDistance").getAsInt();
		this.currentTargets = new ArrayList<>();
		this.playersHit = new ArrayList<>();
		resetCurrentStep();
		loaded = true;
	}

	protected static QuestionContainer getActionTypeQnO(boolean moveActive, boolean shootActive) {
		String question = "Select the type of action you want to execute.";
		List<String> options = new ArrayList<>();
		if (moveActive) {
			options.add(ActionType.MOVE.toString());
		}
		if (shootActive) {
			options.add(ActionType.SHOOT.toString());
		}

		return QuestionContainer.createStringQuestionContainer(question, options);
	}

	/**
	 * Builds a {@link QuestionContainer} that asks which player to target.
	 *
	 * @param targets the target players to choose from.
	 * @return the {@link QuestionContainer}.
	 */
	public static QuestionContainer getTargetPlayersQnO(List<Player> targets) {
		return getTargetPlayersQnO(targets, "Which of the following players do you want to target?");
	}

	public static QuestionContainer getTargetPlayersQnO(List<Player> targets, String question) {
		List<String> options = new ArrayList<>();
		targets.forEach(target -> options.add(target.getPlayerName()));
		return QuestionContainer.createStringQuestionContainer(question, options);
	}

	/**
	 * Builds a {@link QuestionContainer} that ask in which coordinate to move the enemy player.
	 *
	 * @param targetPlayer the player that will be moved.
	 * @param coordinates  the list of coordinates to choose from.
	 * @return the {@link QuestionContainer}.
	 */
	protected static QuestionContainer getMovingTargetEnemyCoordinatesQnO(Player targetPlayer, List<Coordinates> coordinates) {
		return getCoordinatesQno(coordinates, "Where do you want to move " + targetPlayer.getPlayerName() + "?");
	}

	/**
	 * Builds a {@link QuestionContainer} that asks in which coordinate to fire at.
	 *
	 * @param coordinates the list of coordinates to choose from.
	 * @return the {@link QuestionContainer}.
	 */
	protected static QuestionContainer getTargetCoordinatesQnO(List<Coordinates> coordinates) {
		return getCoordinatesQno(coordinates, "Where do you want to fire?");
	}

	protected static QuestionContainer getMoveCoordinatesQnO(List<Coordinates> coordinates) {
		return getCoordinatesQno(coordinates, "Where do you want to move?");
	}

	private static QuestionContainer getCoordinatesQno(List<Coordinates> coordinates, String question) {
		return QuestionContainer.createCoordinatesQuestionContainer(question, new ArrayList<>(coordinates));
	}

	protected static QuestionContainer getCardinalQnO(List<String> availableDirections) {
		String question = "In which direction do you wish to fire?";
		List<String> options = new ArrayList<>(availableDirections);
		return QuestionContainer.createStringQuestionContainer(question, options);
	}

	protected static QuestionContainer getTargetPlayersAndRefusalQnO(List<Player> targets) {
		String question = "Which of the following players do you want to target?";
		List<String> options = new ArrayList<>();
		targets.forEach(target -> options.add(target.getPlayerName()));
		options.add("Nobody");
		return QuestionContainer.createStringQuestionContainer(question, options);
	}

	protected static boolean isThisChoiceRefusal(List listToCheck, int choice) {
		Utils.logWeapon("The player refused.");
		return choice == listToCheck.size();
	}

	@Override
	public boolean canBeActivated() {
		return canPrimaryBeActivated() && isLoaded();
	}

	protected boolean canPrimaryBeActivated() {
		return !getPrimaryTargets().isEmpty();
	}

	GameMap getGameMap() {
		return getGameBoard().getGameMap();
	}

	List<Player> getAllPlayers() {
		return getGameBoard().getPlayers();
	}

	/**
	 * Returns the reload price of this weapon.
	 *
	 * @return Reload price of this weapon.
	 */
	public List<AmmoType> getReloadPrice() {
		return new ArrayList<>(reloadPrice);
	}

	/**
	 * Returns the grab price for this weapon. It consists of the reload price minus the first occurrence.
	 *
	 * @return grab price for this weapon.
	 */
	public List<AmmoType> getGrabPrice() {
		return new ArrayList<>(reloadPrice.subList(1, reloadPrice.size()));
	}

	/**
	 * Returns whether or not the weapon is loaded.
	 *
	 * @return whether or not the weapon is loaded.
	 */
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * Loads the weapon.
	 */
	public void load() {
		//TODO: Implement
		this.loaded = true;
	}

	protected void dealDamageAndConclude(List<DamageAndMarks> damagesAndMarks, List<Player> playersToShoot) {
		dealDamage(damagesAndMarks, playersToShoot);
		concludeActivation();
	}

	protected void dealDamageAndConclude(List<DamageAndMarks> damagesAndMarks, Player... playersToShoot) {
		List<Player> list = Arrays.asList(playersToShoot);
		dealDamageAndConclude(damagesAndMarks, list);
	}

	/**
	 * This method will be called by the damage-dealer methods of the weapons.
	 * The two arrays are ordered in such a way that the i-th playerToShoot will be dealt the i-th damageAndMark.
	 * This method does NOT deload the weapon.
	 * This method does check only the first array's size, so it can happen that playersToShoot is shorter than
	 * damagesAndMarks.
	 *
	 * @param damagesAndMarks the damage and marks for each player.
	 * @param playersToShoot  the array of players that will receive damage and/or marks.
	 */
	protected void dealDamage(List<DamageAndMarks> damagesAndMarks, List<Player> playersToShoot) {
		for (int i = 0; i < playersToShoot.size(); i++) {
			if (playersToShoot.get(i) != null) {
				playersToShoot.get(i).addDamage(getOwner(), damagesAndMarks.get(i).getDamage());
				if (damagesAndMarks.get(i).getDamage() >= 1) {
					playersHit.add(playersToShoot.get(i));
				}
				playersToShoot.get(i).addMarks(getOwner(), damagesAndMarks.get(i).getMarks());
			}
		}
	}

	/**
	 * Primary method of firing of the weapon.
	 */
	protected abstract void primaryFire();

	/**
	 * Handles the primary fire mode of the weapon.
	 * This will be called if currentStep is at least 2.
	 *
	 * @param choice the choice of the player.
	 * @return the {@link QuestionContainer}.
	 */
	abstract QuestionContainer handlePrimaryFire(int choice);

	/**
	 * Get the targets of the primary mode of fire for this weapon.
	 *
	 * @return the targettable players of the primary mode of fire.
	 */
	public abstract List<Player> getPrimaryTargets();

	public List<AmmoType> getFiringCost() {
		return new ArrayList<>();
	}

	/**
	 * Deloads the weapon and reset eventually modified parameters.
	 */
	@Override
	public void reset() {
		super.reset();
		this.loaded = false;
		this.relocationDone = false;
		this.enemyRelocationDone = false;
		this.currentTargets = new ArrayList<>();
		this.target = null;
		this.playersHit = new ArrayList<>();
	}

	protected List<DamageAndMarks> getStandardDamagesAndMarks() {
		return standardDamagesAndMarks;
	}

	protected int getPrimaryDamage() {
		return primaryDamage;
	}

	protected int getPrimaryMarks() {
		return primaryMarks;
	}

	protected int getMoveDistance() {
		return moveDistance;
	}

	protected int getMaximumSteps() {
		return maximumSteps;
	}

	protected QuestionContainer setPrimaryCurrentTargetsAndReturnTargetQnO() {
		currentTargets = getPrimaryTargets();
		return getTargetPlayersQnO(currentTargets);
	}

	protected void relocateEnemy(Player enemy, Coordinates coordinates) {
		getGameMap().movePlayerTo(enemy, coordinates);
	}

	protected void relocateOwner(Coordinates coordinates) {
		getGameMap().movePlayerTo(getOwner(), coordinates);
	}

	public List<Player> getPlayersHit() {
		return new ArrayList<>(playersHit);
	}

	// ####################################
	// OVERRIDDEN METHODS
	// ####################################

	@Override
	public String toString() {
		return getCardName();
	}

	@Override
	public Representation getRep() {
		return new WeaponRep(this);
	}


	// ####################################
	// ENUM
	// ####################################

	protected enum ActionType {
		MOVE,
		SHOOT
	}
}