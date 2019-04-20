package br.com.sbk.sbking.networking.server;

import java.util.Collection;

import org.apache.log4j.Logger;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.models.GameScoreboard;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;

public class MessageSender {

	private Collection<PlayerGameSocket> playerSockets;
	final static Logger logger = Logger.getLogger(MessageSender.class);

	public MessageSender(Collection<PlayerGameSocket> playerSockets) {
		this.playerSockets = playerSockets;
	}

	public void sendDealAll(Deal deal) {
		logger.info("Sending everyone the current deal");
		for (PlayerGameSocket playerSocket : playerSockets) {
			playerSocket.sendDeal(deal);
		}
		logger.info("Finished sending deals.");
	}

	public void sendBoardAll(Board board) {
		logger.info("Sending everyone the current board");
		for (PlayerGameSocket playerSocket : playerSockets) {
			playerSocket.sendBoard(board);
		}
		logger.info("Finished sending boards.");
	}

	public void sendMessageAll(String message) {
		logger.info("Sending everyone the following message: --" + message + "--");
		for (PlayerGameSocket playerSocket : playerSockets) {
			playerSocket.sendMessage(message);
		}
		logger.info("Finished sending messages.");
	}

	public void sendChooserPositiveNegativeAll(Direction chooser) {
		logger.info("Sending everyone the chooser of Positive or Negative: --" + chooser + "--");
		for (PlayerGameSocket playerSocket : playerSockets) {
			playerSocket.sendChooserPositiveNegative(chooser);
		}
		logger.info("Finished sending messages.");
	}

	public void sendChooserGameModeOrStrainAll(Direction chooser) {
		logger.info("Sending everyone the chooser of GameMode or Strain: --" + chooser + "--");
		for (PlayerGameSocket playerSocket : playerSockets) {
			playerSocket.sendChooserGameModeOrStrain(chooser);
		}
		logger.info("Finished sending messages.");
	}

	public void sendPositiveOrNegativeAll(PositiveOrNegative positiveOrNegative) {
		String message = positiveOrNegative.toString().toUpperCase();
		logger.info("Sending everyone : --" + message + "--");
		for (PlayerGameSocket playerSocket : playerSockets) {
			playerSocket.sendPositiveOrNegative(message);
		}
		logger.info("Finished sending messages.");
	}

	public void sendGameModeOrStrainShortDescriptionAll(String currentGameModeOrStrain) {
		String message = currentGameModeOrStrain;
		logger.info("Sending everyone : --" + message + "--");
		for (PlayerGameSocket playerSocket : playerSockets) {
			playerSocket.sendGameModeOrStrain(message);
		}
		logger.info("Finished sending messages.");
	}

	public void sendInitializeDealAll() {
		logger.info("Sending everyone Initialize Deal control");
		for (PlayerGameSocket playerSocket : playerSockets) {
			playerSocket.sendInitializeDeal();
		}
		logger.info("Finished sending controls.");
	}

	public void sendFinishDealAll() {
		logger.info("Sending everyone Finish deal control");
		for (PlayerGameSocket playerSocket : playerSockets) {
			playerSocket.sendFinishDeal();
		}
		logger.info("Finished sending controls.");
	}

	public void sendGameScoreboardAll(GameScoreboard gameScoreboard) {
		logger.info("Sending everyone the Game Scoreboard");
		for (PlayerGameSocket playerSocket : playerSockets) {
			playerSocket.sendGameScoreboard(gameScoreboard);
		}
		logger.info("Finished sending Game Scoreboards.");
	}

	public void sendFinishGameAll() {
		logger.info("Sending everyone Finish Game control");
		for (PlayerGameSocket playerSocket : playerSockets) {
			playerSocket.sendFinishGame();
		}
		logger.info("Finished sending controls.");
	}

	public void sendInvalidRulesetAll() {
		logger.info("Sending everyone Invalid ruleset control");
		for (PlayerGameSocket playerSocket : playerSockets) {
			playerSocket.sendInvalidRuleset();
		}
		logger.info("Finished sending controls.");
	}

	public void sendValidRulesetAll() {
		logger.info("Sending everyone Valid ruleset control");
		for (PlayerGameSocket playerSocket : playerSockets) {
			playerSocket.sendValidRuleset();
		}
		logger.info("Finished sending controls.");
	}

}
