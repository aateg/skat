package br.com.sbk.sbking.gui.painters;

import static br.com.sbk.sbking.gui.constants.FrameConstants.TABLE_WIDTH;

import java.awt.Container;
import java.awt.Point;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.elements.ChoosePositiveOrNegativeElement;
import br.com.sbk.sbking.gui.elements.GameScoreboardElement;
import br.com.sbk.sbking.gui.elements.HandWhileChoosingElement;
import br.com.sbk.sbking.gui.elements.WaitingForChooserElement;
import br.com.sbk.sbking.gui.elements.YouArePlayerElement;
import br.com.sbk.sbking.gui.models.KingGameScoreboard;
import br.com.sbk.sbking.networking.client.SBKingClient;

public class WaitingForChoosingPositiveOrNegativePainter implements Painter {

	private Direction myDirection;
	private Direction chooserDirection;
	private SBKingClient sbKingClient;
	private KingGameScoreboard gameScoreboard;

	public WaitingForChoosingPositiveOrNegativePainter(Direction myDirection, Direction chooserDirection,
			SBKingClient sbKingClient, KingGameScoreboard gameScoreboard) {
		this.myDirection = myDirection;
		this.chooserDirection = chooserDirection;
		this.sbKingClient = sbKingClient;
		this.gameScoreboard = gameScoreboard;
	}

	@Override
	public void paint(Container contentPane) {
		int xCenterLocation = TABLE_WIDTH - 160;
		int yCenterLocation = 110;
		Point centerOfScoreboardPosition = new Point(xCenterLocation, yCenterLocation);
		new GameScoreboardElement(gameScoreboard, contentPane, centerOfScoreboardPosition);
		if (myDirection != chooserDirection) {
			YouArePlayerElement.add(this.myDirection, contentPane);
			WaitingForChooserElement.add(contentPane, chooserDirection, "Positive or Negative.");
		} else {
			ChoosePositiveOrNegativeElement choosePositiveOrNegativeElement = new ChoosePositiveOrNegativeElement(
					contentPane, this.sbKingClient);
			choosePositiveOrNegativeElement.add();
		}

		new HandWhileChoosingElement(contentPane, this.sbKingClient.getCurrentBoard(), this.myDirection);

		contentPane.validate();
		contentPane.repaint();
	}

}
