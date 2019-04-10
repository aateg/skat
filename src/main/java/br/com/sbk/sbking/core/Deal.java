package br.com.sbk.sbking.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.sbk.sbking.core.exceptions.DoesNotFollowSuitException;
import br.com.sbk.sbking.core.exceptions.PlayedCardInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.exceptions.PlayedHeartsWhenProhibitedException;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;

@SuppressWarnings("serial")
public class Deal implements Serializable {

	private static final int NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND = 13;

	private Board board;
	private int completedTricks;
	private Direction dealer;
	private Direction currentPlayer;
	private Score score;

	private Ruleset ruleset;

	private List<Trick> tricks;
	private Trick currentTrick;

	public Deal(Board board, Ruleset ruleset) {
		this.board = board;
		this.ruleset = ruleset;
		this.dealer = this.board.getDealer();
		this.currentPlayer = this.dealer.getLeaderWhenDealer();
		this.score = new Score(ruleset);
		this.completedTricks = 0;
		this.tricks = new ArrayList<Trick>();
	}

	public Hand getHandOf(Direction direction) {
		return this.board.getHandOf(direction);
	}

	public Trick getCurrentTrick() {
		if (currentTrick == null) {
			return new Trick(currentPlayer);
		} else {
			return currentTrick;
		}
	}

	public Direction getCurrentPlayer() {
		return currentPlayer;
	}

	public int getNorthSouthPoints() {
		return this.score.getNorthSouthPoints();
	}

	public int getEastWestPoints() {
		return this.score.getEastWestPoints();
	}

	public Ruleset getRuleset() {
		return this.ruleset;
	}

	public boolean isFinished() {
		return this.completedTricks == NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND;
	}

	public int getCompletedTricks() {
		return this.completedTricks;
	}

	/**
	 * This method will see if playing the card is a valid move. If it is, it will
	 * play it.
	 * 
	 * @param card Card to be played on the board.
	 */
	public void playCard(Card card) {
		Hand handOfCurrentPlayer = getHandOfCurrentPlayer();

		throwExceptionIfCardIsNotFromCurrentPlayer(handOfCurrentPlayer, card);
		throwExceptionIfStartingATrickWithHeartsWhenRulesetProhibitsIt(card, handOfCurrentPlayer);
		if (currentTrickAlreadyHasCards()) {
			throwExceptionIfCardDoesNotFollowSuit(card, handOfCurrentPlayer);
		}
		if (currentTrickNotStartedYet()) {
			this.currentTrick = startNewTrick();
		}

		moveCardFromHandToCurrentTrick(card, handOfCurrentPlayer);

		if (currentTrick.isComplete()) {
			Direction currentTrickWinner = this.getWinnerOfCurrentTrick();
			currentPlayer = currentTrickWinner;
			updateScoreboard(currentTrickWinner);
			completedTricks++;
		} else {
			currentPlayer = currentPlayer.next();
		}

	}

	private Hand getHandOfCurrentPlayer() {
		return this.board.getHandOf(this.currentPlayer);
	}

	private void throwExceptionIfCardIsNotFromCurrentPlayer(Hand handOfCurrentPlayer, Card card) {
		if (!handOfCurrentPlayer.containsCard(card)) {
			throw new PlayedCardInAnotherPlayersTurnException();
		}
	}

	private void throwExceptionIfStartingATrickWithHeartsWhenRulesetProhibitsIt(Card card, Hand handOfCurrentPlayer) {
		if (this.currentTrickNotStartedYet() && this.ruleset.prohibitsHeartsUntilOnlySuitLeft() && card.isHeart()
				&& !handOfCurrentPlayer.onlyHasHearts()) {
			throw new PlayedHeartsWhenProhibitedException();
		}
	}

	private boolean currentTrickNotStartedYet() {
		return this.currentTrick == null || this.currentTrick.isEmpty() || this.currentTrick.isComplete();
	}

	private boolean currentTrickAlreadyHasCards() {
		return !currentTrickNotStartedYet();
	}

	private void throwExceptionIfCardDoesNotFollowSuit(Card card, Hand handOfCurrentPlayer) {
		if (!this.ruleset.followsSuit(this.currentTrick, handOfCurrentPlayer, card)) {
			throw new DoesNotFollowSuitException();
		}
	}

	private Trick startNewTrick() {
		Trick currentTrick = new Trick(currentPlayer);
		tricks.add(currentTrick);
		boolean isOneOfLastTwoTricks = completedTricks >= (NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND - 2);
		if (isOneOfLastTwoTricks) {
			currentTrick.setLastTwo();
		}
		return currentTrick;
	}

	private void moveCardFromHandToCurrentTrick(Card card, Hand handOfCurrentPlayer) {
		// FIXME Should be a transaction
		handOfCurrentPlayer.removeCard(card);
		currentTrick.addCard(card);
	}

	private Direction getWinnerOfCurrentTrick() {
		return this.ruleset.getWinner(currentTrick);
	}

	private void updateScoreboard(Direction currentTrickWinner) {
		this.score.addTrickToDirection(currentTrick, currentTrickWinner);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((board == null) ? 0 : board.hashCode());
		result = prime * result + completedTricks;
		result = prime * result + ((currentPlayer == null) ? 0 : currentPlayer.hashCode());
		result = prime * result + ((currentTrick == null) ? 0 : currentTrick.hashCode());
		result = prime * result + ((ruleset == null) ? 0 : ruleset.hashCode());
		result = prime * result + ((score == null) ? 0 : score.hashCode());
		result = prime * result + ((tricks == null) ? 0 : tricks.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Deal other = (Deal) obj;
		if (board == null) {
			if (other.board != null)
				return false;
		} else if (!board.equals(other.board))
			return false;
		if (completedTricks != other.completedTricks)
			return false;
		if (currentPlayer != other.currentPlayer)
			return false;
		if (currentTrick == null) {
			if (other.currentTrick != null)
				return false;
		} else if (!currentTrick.equals(other.currentTrick))
			return false;
		if (ruleset == null) {
			if (other.ruleset != null)
				return false;
		} else if (!ruleset.equals(other.ruleset))
			return false;
		if (score == null) {
			if (other.score != null)
				return false;
		} else if (!score.equals(other.score))
			return false;
		if (tricks == null) {
			if (other.tricks != null)
				return false;
		} else if (!tricks.equals(other.tricks))
			return false;
		return true;
	}

	public Direction getDealer() {
		return this.dealer;
	}
	
	public Score getScore() {
		return this.score;
	}

}
