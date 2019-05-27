package thesimpleton.effects.orb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class GainCropEffect extends AbstractGameEffect {
  private static final float EFFECT_DURATION = Settings.ACTION_DUR_MED;
  private float x;
  private float y;

  public GainCropEffect() {
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
