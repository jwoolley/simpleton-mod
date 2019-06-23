package thesimpleton.effects.creature;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.vfx.combat.SmokeBlurEffect;

public class DirtBlurEffect extends SmokeBlurEffect {
  public DirtBlurEffect(float x, float y) {
    super(x, y);
    this.color.r = MathUtils.random(0.4F, 0.5F);
    this.color.g = this.color.r - MathUtils.random(0.1F, 0.15F);
    this.color.b = 0.05F;
  }
}
