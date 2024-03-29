package it.polimi.se2019.model.player;

import it.polimi.se2019.model.Representation;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.powerups.PowerupCard;
import it.polimi.se2019.model.cards.powerups.PowerupCardRep;
import it.polimi.se2019.model.cards.weapons.WeaponCard;
import it.polimi.se2019.model.cards.weapons.WeaponRep;
import it.polimi.se2019.model.player.damagestatus.DamageStatusRep;
import it.polimi.se2019.utils.Color;
import it.polimi.se2019.utils.exceptions.HiddenException;

import java.util.ArrayList;
import java.util.List;

/**
 * A sharable version of all the useful Player information.
 *
 * @author Desno365
 */
public class PlayerRep implements Representation {

	private String playerName;
	private Color.CharacterColorType playerColor;
	private int points;
	private int playerID;
	private int deaths;
	private List<Color.CharacterColorType> damageBoard;
	private List<Color.CharacterColorType> marks;
	private DamageStatusRep damageStatusRep;
	private List<WeaponRep> weaponReps;
	private List<PowerupCardRep> powerupCards;
	private int[] ammo;
	private boolean connected;
	private boolean hidden;
	private boolean flippedBoard;
	private String pgName;


	/**
	 * Create a new PlayerRep with all the information of a Player.
	 *
	 * @param player the player from which the information are extracted.
	 */
	public PlayerRep(Player player) {
		playerName = player.getPlayerName();
		playerColor = player.getPlayerColor();
		points = player.getPlayerBoard().getPoints();
		playerID = player.getPlayerID();
		damageStatusRep = (DamageStatusRep) player.getDamageStatus().getRep();

		damageBoard = new ArrayList<>(player.getPlayerBoard().getDamageBoard().size());
		for (Player player1 : player.getPlayerBoard().getDamageBoard()) {
			damageBoard.add(player1.getPlayerColor());
		}

		marks = new ArrayList<>(player.getPlayerBoard().getMarks().size());
		for (Player player2 : player.getPlayerBoard().getMarks()) {
			marks.add(player2.getPlayerColor());
		}

		powerupCards = new ArrayList<>(player.getPlayerBoard().getPowerupCards().size());
		for (PowerupCard powerupCard : player.getPlayerBoard().getPowerupCards()) {
			powerupCards.add((PowerupCardRep) powerupCard.getRep());
		}

		weaponReps = new ArrayList<>(player.getPlayerBoard().getWeaponCards().size());
		for (WeaponCard weaponCard : player.getPlayerBoard().getWeaponCards()) {
			weaponReps.add((WeaponRep) weaponCard.getRep());
		}

		ammo = new int[AmmoType.values().length];
		for (AmmoType ammoType : AmmoType.values()) {
			ammo[ammoType.ordinal()] = player.getPlayerBoard().getAmmoContainer().getAmmo(ammoType);
		}

		this.flippedBoard = player.getPlayerBoard().isFlipped();
		this.pgName = player.getPgName();
		this.deaths = player.getPlayerBoard().getNumberOfDeaths();

		connected = player.isConnected();

		hidden = false;
	}

	/**
	 * Used to create the hidden PlayerRep.
	 */
	private PlayerRep() {
	}

	/**
	 * Returns a PlayerRep that contains only the information that are available also to the other players.
	 *
	 * @return a new PlayerRep without the sensitive information.
	 */
	public PlayerRep getHiddenPlayerRep() {
		PlayerRep newPlayerRep = new PlayerRep();
		newPlayerRep.playerName = this.playerName;
		newPlayerRep.playerColor = this.playerColor;
		newPlayerRep.playerID = this.playerID;
		newPlayerRep.points = -1; // hidden
		newPlayerRep.damageBoard = new ArrayList<>(damageBoard);
		newPlayerRep.marks = new ArrayList<>(marks);
		newPlayerRep.damageStatusRep = null;//hidden
		newPlayerRep.powerupCards = null; // hidden
		newPlayerRep.weaponReps = new ArrayList<>();
		for (WeaponRep weaponRep : this.weaponReps) {
			if (!weaponRep.isLoaded())
				newPlayerRep.weaponReps.add(weaponRep);
		}
		newPlayerRep.deaths = deaths;
		newPlayerRep.flippedBoard = flippedBoard;
		newPlayerRep.ammo = this.ammo.clone();
		newPlayerRep.connected = this.connected;
		newPlayerRep.hidden = true;
		newPlayerRep.pgName = pgName;
		return newPlayerRep;
	}

	/**
	 * Returns true if the sensitive information are removed.
	 *
	 * @return true if the sensitive information are removed.
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * Returns true if the bord is flipped.
	 *
	 * @return true if the board id flipped.
	 */
	public boolean isFlippedBoard() {
		return flippedBoard;
	}

	/**
	 * Returns the player name.
	 *
	 * @return the player name.
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Returns the pg name.
	 *
	 * @return the pg name.
	 */
	public String getPgName() {
		return pgName;
	}

	/**
	 * Returns the player ID.
	 *
	 * @return the player ID.
	 */
	public int getPlayerID() {
		return playerID;
	}

	public boolean isConnected() {
		return connected;
	}

	/**
	 * Returns the player color.
	 *
	 * @return the player color.
	 */
	public Color.CharacterColorType getPlayerColor() {
		return playerColor;
	}

	/**
	 * Returns the number of deaths
	 *
	 * @return the number of deaths.
	 */
	public int getPlayerDeaths() {
		return deaths;
	}

	/**
	 * Returns the list of player's weapons.
	 *
	 * @return the list of player's weapons.
	 */
	public List<WeaponRep> getWeaponReps() {
		return weaponReps;
	}

	/**
	 * Returns the damage status representation.
	 *
	 * @return the damage status representation.
	 */
	public DamageStatusRep getDamageStatusRep() {
		return damageStatusRep;
	}

	/**
	 * Returns the sensitive information of player's points.
	 *
	 * @return the sensitive information of player's points.
	 * @throws HiddenException if the PlayerRep is hidden and doesn't contain sensitive information.
	 */
	public int getPoints() {
		if (isHidden())
			throw new HiddenException("The value of \"points\" is hidden in this PlayerRep.");
		return points;
	}

	/**
	 * Returns the damage board of this player with all the player names that made the damage.
	 *
	 * @return the damage board of this player with all the player names that made the damage.
	 */
	public List<Color.CharacterColorType> getDamageBoard() {
		return damageBoard;
	}

	/**
	 * Returns the marks of this player with all the player names that made the marks.
	 *
	 * @return the marks of this player with all the player names that made the marks.
	 */
	public List<Color.CharacterColorType> getMarks() {
		return marks;
	}

	/**
	 * Returns the sensitive information of player's powerup cards.
	 *
	 * @return the sensitive information of player's powerup cards.
	 * @throws HiddenException if the PlayerRep is hidden and doesn't contain sensitive information.
	 */
	public List<PowerupCardRep> getPowerupCards() {
		if (isHidden())
			throw new HiddenException("The value of \"powerupCards\" is hidden in this PlayerRep.");
		return powerupCards;
	}

	/**
	 * Returns the total amount of ammo of a particular ammo type this player posses.
	 *
	 * @param ammoType the ammoType to check for.
	 * @return the total amount of red ammo this player posses.
	 */
	public int getAmmo(AmmoType ammoType) {
		return ammo[ammoType.ordinal()];
	}

	public int[] getAmmo() {
		return ammo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		return ((PlayerRep) o).getPlayerName().equals(playerName);
	}

	@Override
	public int hashCode() {
		return playerName.hashCode();
	}
}