package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.network.message.DefaultActionMessage;
import it.polimi.se2019.network.message.MoveActionMessage;
import it.polimi.se2019.utils.ActionType;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.server.Event;
import it.polimi.se2019.view.server.VirtualView;

/**
 * This class is in a lower level than GameController. It handles the logic relative
 * @author Marchingegno
 */
public class TurnController{

	private VirtualViewsContainer virtualViewsContainer;
	private WeaponController weaponController;
	private Model model;


	public TurnController(Model model, VirtualViewsContainer virtualViewsContainer) {
		this.virtualViewsContainer = virtualViewsContainer;
		this.model = model;
		this.weaponController = new WeaponController();
	}


	public void displayPossibleMoves(){

	}

	public void performPowerup(int indexOfPowerup) {
	}


	public void performMacroaction(int indexOfMacroAction) {
	}

	void processEvent(Event event) {
		//TODO: Control veridicity of the message.

		VirtualView virtualView = event.getVirtualView();
		String playerName = virtualView.getNickname();

		Utils.logInfo("TurnController: processing this event " + event.toString());
		switch(event.getMessage().getMessageType()){
			case ACTION:
				model.setNextMacroAction(playerName, ((DefaultActionMessage)event.getMessage()).getContent());
				handleNextAction(virtualView);
				break;
			case GRAB_AMMO:
				model.grabAmmoCard(playerName, ((DefaultActionMessage)event.getMessage()).getContent());
				handleNextAction(virtualView);
				virtualViewsContainer.sendUpdatedReps();
				break;
			case GRAB_WEAPON:
				model.grabWeaponCard(playerName, ((DefaultActionMessage)event.getMessage()).getContent());
				handleNextAction(virtualView);
				virtualViewsContainer.sendUpdatedReps();
				break;
			case MOVE:
				model.movePlayerTo(playerName, ((MoveActionMessage) event.getMessage()).getCoordinates().get(0));
				handleNextAction(virtualView);
				virtualViewsContainer.sendUpdatedReps();
				break;
			case RELOAD:
				model.reloadWeapon(playerName, ((DefaultActionMessage)event.getMessage()).getContent());
				handleNextAction(virtualView);
				virtualViewsContainer.sendUpdatedReps();
				break;
			default: Utils.logError("Received wrong type of message: " + event.toString(), new IllegalStateException());
		}

	}

	private void handleNextAction(VirtualView playerVirtualView) {
		ActionType actionType = model.getNextActionToExecute(playerVirtualView.getNickname());
		switch (actionType){
			case MOVE:
				playerVirtualView.askMove(model.getReachableCoordinatesOfTheCurrentPlayer());
				break;
			case GRAB:
				playerVirtualView.askGrab();
				break;
			case RELOAD:
				playerVirtualView.askReload();
				break;
			case SHOOT:
				playerVirtualView.askShoot();
				break;
			case END:
				//The MacroAction is already refilled.
				handleEnd(playerVirtualView);
				break;
			default:
				Utils.logError("This action type cannot be processed.", new IllegalStateException());
				break;
		}
	}

	private void handleEnd(VirtualView playerVirtualView) {
		String playerName = playerVirtualView.getNickname();
		if(model.doesThePlayerHaveActionsLeft(playerName)){
			playerVirtualView.askAction();
		} else {
			playerVirtualView.askEnd();
		}
	}
}