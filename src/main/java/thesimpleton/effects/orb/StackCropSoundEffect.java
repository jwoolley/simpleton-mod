package thesimpleton.effects.orb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbActivateEffect;

public class StackCropSoundEffect extends AbstractGameEffect {
  private static final float EFFECT_DURATION = Settings.ACTION_DUR_FAST;
  private float x;
  private float y;

  public StackCropSoundEffect(float x, float y) {
    this.x = x;
    this.y = y;
    this.color = Color.BROWN;
    this.startingDuration = EFFECT_DURATION;
    this.duration = this.startingDuration;
  }

  public void update() {
    CardCrawlGame.sound.play("SHOVEL");
    this.isDone = true;
  }

  @Override
  public void render(SpriteBatch spriteBatch) { }

  @Override
  public void dispose() {

  }
}
