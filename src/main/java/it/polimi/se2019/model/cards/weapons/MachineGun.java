package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.player.Player;

import java.util.List;

public final class MachineGun extends OptionalEffect {

	public MachineGun(String description, ArrayList<AmmoType> reloadPrice) {
		super(description, reloadPrice);
	}


	public void primaryFire(List<Player> playersToShoot) {
	}

	public void optionalEffect1() {
	}

	public void optionalEffect2() {
	}

}