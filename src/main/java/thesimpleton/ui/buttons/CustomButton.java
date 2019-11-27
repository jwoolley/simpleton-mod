package thesimpleton.ui.buttons;

import com.badlogic.gdx.graphics.Color;
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
  private float scale;
  private Texture imgForScale;


  // hack in this temporary reference for the img texture to capture it before it's passed to super and saved as private
  private static Texture tempImg = null;

  // TODO: load UI text and Texture by ID
  private static Texture getImg(String path) {
    tempImg = TheSimpletonMod.loadTexture(path);
    return tempImg;
  }

  public CustomButton(final float x, final float y, final String buttonId, final String label) {
    this(x, y, 1.0F, buttonId, label);
  }

  public CustomButton(final float x, final float y, final float scale, final String buttonId, final String label) {
    super(x, y, getImg(TheSimpletonMod.getImageResourcePath(getUiPath(buttonId))));
    this.scale = scale;
    this.imgForScale = tempImg;
    tempImg = null;
  }

  public CustomButton(final float x, final float y, final Texture img, final String label) {
    super(x, y, img);
  }

  @Override
  public void update() {
    super.update();
    if (this.pressed) {
      logger.debug("CustomButton::update: calling handleClick: button was pressed");
      handleClick();
    }
  }

  void handleClick() {
    if (!this.isDisabled) {
      logger.debug("CustomButton::handleClick: Handling click: button is enabled");
      this.onClick();
      this.pressed = false;
    } else {
      logger.debug("CustomButton::handleClick: Not handling click: button is disabled");
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

  public void reset() {
    this.pressed = false;
    this.hide();
    this.disable();
  }

  public boolean induceHover = false;

  public void render(SpriteBatch sb) {
    if (this.hb.hovered || this.induceHover) {
      sb.setColor(this.activeColor);
    } else {
      sb.setColor(this.inactiveColor);
    }
//    sb.draw(this.imgForScale, this.x, this.y);
    sb.draw(this.imgForScale,
        this.x, this.y,
        this.scale * this.width, this.scale * this.height,
        0, 0,
        width, height,
        false, false);
    sb.setColor(Color.WHITE.cpy());
    this.hb.render(sb);
  }

  private static String getUiPath(String id) {
    return BUTTONS_UI_PATH + id + ".png";
  }
}
