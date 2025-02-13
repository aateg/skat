package br.com.sbk.sbking.networking.kryonet.messages.servertoclient;

import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.networking.kryonet.messages.SBKingMessage;

public class PositiveOrNegativeChooserMessage implements SBKingMessage {

  private Direction chooser;

  /**
   * @deprecated Kryo needs a no-arg constructor
   */
  @Deprecated
  private PositiveOrNegativeChooserMessage() {
  }

  public PositiveOrNegativeChooserMessage(Direction chooser) {
    this.chooser = chooser;
  }

  @Override
  public Direction getContent() {
    return this.chooser;
  }

}
