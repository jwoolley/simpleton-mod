package thesimpleton.orbs;

import com.badlogic.gdx.graphics.Texture;
import thesimpleton.TheSimpletonMod;

public class SimpletonEmptyOrbSlot extends CustomEmptyOrbSlot {

  public static final String FOREGROUND_IMG_PATH = "empty1";
  public static final String BACKGROUND_IMG_PATH = "empty2";

  private static Texture foregroundImage;
  private static Texture backgroundImage;

  public SimpletonEmptyOrbSlot(float cX, float cY) {
    super(cX, cY);
  }

  @Override
  public Texture getForegroundImage() {
    if (foregroundImage == null) {
      foregroundImage = TheSimpletonMod.loadTexture(
          TheSimpletonMod.getResourcePath(SimpletonOrbHelper.getUiPath(FOREGROUND_IMG_PATH)));
    }
    return foregroundImage;
  }

  @Override
  public Texture getBackgroundImage() {
    if (backgroundImage == null) {
      backgroundImage = TheSimpletonMod.loadTexture(
          TheSimpletonMod.getResourcePath(SimpletonOrbHelper.getUiPath(BACKGROUND_IMG_PATH)));
    }
    return backgroundImage;
  }
}
