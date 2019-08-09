package thesimpleton.orbs;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;

public abstract class CustomEmptyOrbSlot extends EmptyOrbSlot {
  public CustomEmptyOrbSlot(float cX, float cY) {
    super(cX, cY);
  }

  public abstract Texture getForegroundImage();
  public abstract Texture getBackgroundImage();
}