package it.polimi.se2019.model.cards.powerups;

import it.polimi.se2019.model.cards.Card;
import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;

/**
 * This abstract class implements the powerup card
 *
 * @author MarcerAndrea
 */
public abstract class PowerupCard extends Card {

	protected AmmoType associatedAmmo;

	public PowerupCard(AmmoType associatedAmmo, String description) {
		super(description);
		this.associatedAmmo = associatedAmmo;
	}

	/**
	 * Activates the powerup.
	 *
	 * @param activatingPlayer player who as activated the powerup.
	 */
	public abstract void activatePowerup(Player activatingPlayer);

	/**
	 * Returns the ammo associated with the card.
	 *
	 * @return the ammo associated with the card.
	 */
	public AmmoType getAssociatedAmmo() {
		return associatedAmmo;
	}
}