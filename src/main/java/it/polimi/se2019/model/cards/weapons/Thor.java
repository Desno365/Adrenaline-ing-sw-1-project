package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.List;

public class Thor extends OptionalEffectsWeapon {

	public Thor(String description, List<AmmoType> reloadPrice) {
		super("T.H.O.R.", description, reloadPrice);
	}


	public void primaryFire() {
	}

	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		return null;
	}

	@Override
	public List<Player> getPrimaryTargets() {
		return null;
	}

	@Override
	public void optionalEffect1() {

	}

	@Override
	public void optionalEffect2() {

	}

}