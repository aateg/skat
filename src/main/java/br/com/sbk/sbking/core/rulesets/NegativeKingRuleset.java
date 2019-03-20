package br.com.sbk.sbking.core.rulesets;

import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.rulesets.abstractClasses.NonHeartsProhibitableDefaltSuitFollowRuleset;

public class NegativeKingRuleset extends NonHeartsProhibitableDefaltSuitFollowRuleset {

	private final int NEGATIVE_KING_SCORE_MULTIPLIER = 160;

	@Override
	public int getScoreMultiplier() {
		return NEGATIVE_KING_SCORE_MULTIPLIER;
	}

	@Override
	public int getPoints(Trick trick) {
		if (trick.hasKingOfHearts()) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public String getShortDescription() {
		return "Negative king";
	}

	@Override
	public String getCompleteDescription() {
		return "Avoid the King of Hearts";
	}

}
