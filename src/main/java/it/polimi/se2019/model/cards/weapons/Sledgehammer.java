package it.polimi.se2019.model.cards.weapons;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.CardinalDirection;
import it.polimi.se2019.utils.QuestionContainer;

import java.util.ArrayList;
import java.util.List;

public class Sledgehammer extends AlternateFireWeapon {
	private List<Coordinates> enemyMovingCoordinates;

	public Sledgehammer(String description, List<AmmoType> reloadPrice) {
		super("Sledgehammer", description, reloadPrice);
		this.maximumSteps = 3;
		this.maximumAlternateSteps = 4;
		this.PRIMARY_DAMAGE = 2;
		this.PRIMARY_MARKS = 0;
		this.SECONDARY_DAMAGE = 3;
		this.SECONDARY_MARKS = 0;
		this.standardDamagesAndMarks = new ArrayList<>();
		this.secondaryDamagesAndMarks = new ArrayList<>();
		this.standardDamagesAndMarks.add(new DamageAndMarks(PRIMARY_DAMAGE, PRIMARY_MARKS));
		this.secondaryDamagesAndMarks.add(new DamageAndMarks(SECONDARY_DAMAGE, SECONDARY_MARKS));

	}


	@Override
	QuestionContainer handlePrimaryFire(int choice) {
		if(getCurrentStep() == 2){
			currentTargets = getPrimaryTargets();
			return getTargetPlayersQnO(currentTargets);
		}
		else if(getCurrentStep() == 3){
			this.target = currentTargets.get(choice);
			primaryFire();
		}
		return null;
	}

	@Override
	QuestionContainer handleSecondaryFire(int choice) {
		if(getCurrentStep() == 2){
			currentTargets = getPrimaryTargets();
			return getTargetPlayersQnO(currentTargets);
		}
		else if(getCurrentStep() == 3){
			this.target = currentTargets.get(choice);
			enemyMovingCoordinates = getEnemyMovingCoordinates();
			return getMovingTargetEnemyCoordinatesQnO(target, enemyMovingCoordinates);
		}
		else if(getCurrentStep() == 4){
			relocateEnemy(target, enemyMovingCoordinates.get(choice));
			secondaryFire();
		}
		return null;
	}

	public void primaryFire() {
		unifiedFire();
	}

	private void unifiedFire(){
		List<DamageAndMarks> damageAndMarksList = isAlternateFireActive() ? secondaryDamagesAndMarks : standardDamagesAndMarks;
		dealDamage(damageAndMarksList, target);
	}

	public void secondaryFire() {
		unifiedFire();
	}

	@Override
	public List<Player> getPrimaryTargets() {
		return getGameMap().reachablePlayers(getOwner(), 0);
	}

	@Override
	public List<Player> getSecondaryTargets() {
		return getPrimaryTargets();
	}

	private List<Coordinates> getEnemyMovingCoordinates(){
		List<Coordinates> possibleMoves = new ArrayList<>();
		possibleMoves.add(getGameMap().getPlayerCoordinates(getOwner()));
		for (CardinalDirection direction: CardinalDirection.values()) {
			Coordinates nextSquare = getGameMap().getCoordinatesFromDirection(getGameMap().getPlayerCoordinates(getOwner()), direction);
			if(nextSquare != null){
				possibleMoves.add(nextSquare);
				Coordinates nextNextSquare = getGameMap().getCoordinatesFromDirection(nextSquare, direction);
				if (nextNextSquare != null){
					possibleMoves.add(nextNextSquare);
				}
			}
		}

		return possibleMoves;
	}


}