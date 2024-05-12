package thesimpleton.ui.buttons;

import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.ui.ReadyButtonPanel;
import thesimpleton.utilities.ModLogger;

public class ReadyButton extends CustomButton {
  private static ModLogger logger = TheSimpletonMod.traceLogger;

  public final static String BUTTON_ID = "TheSimpletonMod:ReadyButton";
  public final static String BUTTON_IMG = "ready-button-1";

  public final ReadyButtonPanel panel;

  public ReadyButton(float x, float y, float scale, String label, ReadyButtonPanel panel) {
    super(x, y, scale, BUTTON_IMG, label);
    this.panel = panel;
    logger.trace("ReadyButton instantiated");
  }

  @Override
  public void onClick() {
    logger.trace("ReadyButton.onClick Called");
    panel.onReadyClicked();
  }
}