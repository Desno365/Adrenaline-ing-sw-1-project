package it.polimi.se2019.model.gameboard;

import it.polimi.se2019.utils.Color;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Desno365
 */
public class GameBoardRepTest {

	private static GameBoard gameBoard;

	private GameBoardRep gameBoardRep;

	@BeforeClass
	public static void oneTimeSetUp() {
		ArrayList<String> playerNames = new ArrayList<>();
		playerNames.add("Test 1");
		playerNames.add("Test 2");
		playerNames.add("Test 3");
		gameBoard = new GameBoard("Medium1Map", playerNames, 8);
		gameBoard.addKillShot(gameBoard.getCurrentPlayer(), true);
		gameBoard.addKillShot(gameBoard.getCurrentPlayer(), true); // double kill
		gameBoard.nextPlayerTurn();
		gameBoard.addKillShot(gameBoard.getCurrentPlayer(), false);
		gameBoard.nextPlayerTurn();
		gameBoard.addKillShot(gameBoard.getCurrentPlayer(), true);
	}

	@Before
	public void setUp() throws Exception {
		gameBoardRep = new GameBoardRep(gameBoard);
	}

	@Test
	public void getRemainingSkulls_initialState_correctOutput() {
		assertEquals(gameBoard.getRemainingSkulls(), gameBoardRep.getRemainingSkulls());
	}

	@Test
	public void getDoubleKills_initialState_correctOutput() {
		List<Color.CharacterColorType> doubleKills = gameBoardRep.getDoubleKills();
		for (int i = 0; i < doubleKills.size(); i++)
			assertEquals(gameBoard.getDoubleKills().get(i).getPlayerColor(), doubleKills.get(i));
	}

	@Test
	public void getKillShoots_initialState_correctOutput() {
		List<KillShotRep> killShotReps = gameBoardRep.getKillShoots();
		for (int i = 0; i < killShotReps.size(); i++) {
			assertEquals(gameBoard.getKillShots().get(i).getPlayer().getPlayerName(), killShotReps.get(i).getPlayerName());
			assertEquals(gameBoard.getKillShots().get(i).isOverkill(), killShotReps.get(i).isOverkill());
		}
	}

	@Test
	public void getCurrentPlayer_initialState_correctOutput() {
		assertEquals(gameBoard.getCurrentPlayer().getPlayerName(), gameBoardRep.getCurrentPlayer());
	}

	@Test
	public void getNumberOfPlayers_initialState_correctOutput() {
		assertEquals(gameBoard.getPlayers().size(), gameBoardRep.getNumberOfPlayers());
	}
}