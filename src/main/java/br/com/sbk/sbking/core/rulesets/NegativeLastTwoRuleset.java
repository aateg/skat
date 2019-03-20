package br.com.sbk.sbking.core.rulesets;

import br.com.sbk.sbking.core.Trick;
import br.com.sbk.sbking.core.rulesets.abstractClasses.NonHeartsProhibitableDefaltSuitFollowRuleset;

public class NegativeLastTwoRuleset extends NonHeartsProhibitableDefaltSuitFollowRuleset {

	private final int NEGATIVE_LAST_TWO_SCORE_MULTIPLIER = 90;

	@Override
	public int getScoreMultiplier() {
		return NEGATIVE_LAST_TWO_SCORE_MULTIPLIER;
	}

	@Override
	public int getPoints(Trick trick) {
		if (trick.isLastTwo()) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public String getShortDescription() {
		return "Negative last two";
	}

	@Override
	public String getCompleteDescription() {
		return "Avoid the last two tricks";
	}

}
