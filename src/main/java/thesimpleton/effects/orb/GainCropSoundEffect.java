package thesimpleton.effects.orb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

// TODO: rename this so it's not easily confused with StackCropSoundEffect
public class GainCropSoundEffect extends AbstractGameEffect {
  private static final float EFFECT_DURATION = Settings.ACTION_DUR_FAST;
  private float x;
  private float y;

  public GainCropSoundEffect(float x, float y) {
    this.x = x;
    this.y = y;
    this.color = Color.BROWN.cpy();
    this.startingDuration = EFFECT_DURATION;
    this.duration = this.startingDuration;
  }

  public void update() {
    CardCrawlGame.sound.play("ATTACK_SCYTHE_1");
    this.isDone = true;
  }

  @Override
  public void render(SpriteBatch spriteBatch) { }

  @Override
  public void dispose() {

  }
}
