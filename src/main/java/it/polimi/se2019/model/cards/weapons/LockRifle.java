package it.polimi.se2019.model.cards.weapons;


import it.polimi.se2019.model.player.Player;

import java.util.List;

public final class LockRifle extends OptionalEffect {

	public LockRifle(String description, ArrayList<AmmoType> reloadPrice) {
		super(description, reloadPrice);
	}


	public void primaryFire(List<Player> playersToShoot) {
	}

	public void optionalEffect1() {
	}

	public void optionalEffect2() {
	}

}