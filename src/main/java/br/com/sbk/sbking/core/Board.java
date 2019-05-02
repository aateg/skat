package br.com.sbk.sbking.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class Board implements Serializable {

	private Map<Direction, Hand> hands = new HashMap<Direction, Hand>();
	private Direction dealer;

	public Board(Hand northHand, Hand eastHand, Hand southHand, Hand westHand, Direction dealer) {
		hands.put(Direction.NORTH, northHand);
		hands.put(Direction.EAST, eastHand);
		hands.put(Direction.SOUTH, southHand);
		hands.put(Direction.WEST, westHand);

		this.sortAllHands();
		this.dealer = dealer;
	}

	public Direction getDealer() {
		return dealer;
	}

	public Hand getHandOf(Direction direction) {
		return this.hands.get(direction);
	}

	private void sortAllHands() {
		for (Direction direction : Direction.values()) {
			this.getHandOf(direction).sort();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dealer == null) ? 0 : dealer.hashCode());
		result = prime * result + ((hands == null) ? 0 : hands.hashCode());
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
		Board other = (Board) obj;
		if (dealer != other.dealer)
			return false;
		if (hands == null) {
			if (other.hands != null)
				return false;
		} else if (!hands.equals(other.hands))
			return false;
		return true;
	}

}
