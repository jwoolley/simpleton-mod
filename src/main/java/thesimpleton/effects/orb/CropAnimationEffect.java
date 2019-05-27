package thesimpleton.effects.orb;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbActivateEffect;

// TODO: rename this so it's not easily confused with StackCropSoundEffect
public class CropAnimationEffect extends PlasmaOrbActivateEffect {
  private static final float DEFAULT_EFFECT_DURATION = Settings.ACTION_DUR_FAST;

  public CropAnimationEffect(float x, float y) {
    this(x, y, DEFAULT_EFFECT_DURATION);
  }

  public CropAnimationEffect(float x, float y, float effectDuration) {
    super(x, y);
    this.duration = effectDuration;
  }

  @Override
  public void render(SpriteBatch spriteBatch) { }

  @Override
  public void dispose() {

  }
}
