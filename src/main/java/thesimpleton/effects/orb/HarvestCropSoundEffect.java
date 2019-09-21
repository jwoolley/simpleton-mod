package thesimpleton.effects.orb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbActivateEffect;

public class HarvestCropSoundEffect extends PlasmaOrbActivateEffect {
  private static final float EFFECT_DURATION = Settings.ACTION_DUR_MED;
  private float x;
  private float y;

  public HarvestCropSoundEffect(float x, float y) {
    super(x, y);
    this.color = Color.BROWN.cpy();
    this.startingDuration = EFFECT_DURATION;
    this.duration = this.startingDuration;
  }

  public void update() {
    CardCrawlGame.sound.play("SCENE_TORCH_EXTINGUISH");
    this.isDone = true;
  }

  @Override
  public void render(SpriteBatch spriteBatch) { }

  @Override
  public void dispose() {

  }
}

