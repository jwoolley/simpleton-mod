package thesimpleton.ui.buttons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.ui.buttons.Button;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;

abstract public class CustomButton extends Button {
  private boolean isHidden = true;
  private boolean isDisabled = true;

  private static final String BUTTONS_UI_PATH = "customui/buttons/";
  private Logger logger = TheSimpletonMod.logger;

  // TODO: load UI text and Texture by ID

  public CustomButton(final float x, final float y, final String buttonId, final String label) {
    super(x, y, TheSimpletonMod.loadTexture(TheSimpletonMod.getResourcePath(getUiPath(buttonId))));
  }

  public CustomButton(final float x, final float y, final Texture img, final String label) {
    super(x, y, img);
  }

  @Override
  public void update() {
    super.update();
    if (this.pressed) {
      handleClick();
    }
  }

  void handleClick() {
    if (!this.isDisabled) {
      this.onClick();
    }
  }

  public abstract void onClick();

  public void enable() {
    if (this.isDisabled) {
      this.isDisabled = false;
    }
  }

  public void disable() {
    if (!this.isDisabled) {
      this.isDisabled = true;
    }
  }

  public void hide() {
    if (!this.isHidden) {
      this.isHidden = true;
    }
  }

  public void show() {
    if (this.isHidden) {
      this.isHidden = false;
    }
  }

  public void render(SpriteBatch sb) {
    super.render(sb);
  }

  private static String getUiPath(String id) {
    return BUTTONS_UI_PATH + id + ".png";
  }
}
