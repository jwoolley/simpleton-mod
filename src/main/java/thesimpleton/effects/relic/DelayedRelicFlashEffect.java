package thesimpleton.effects.relic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import thesimpleton.TheSimpletonMod;

public class DelayedRelicFlashEffect extends AbstractGameEffect {
  private final AbstractRelic relic;
  private final float triggerTime;
  private boolean hasTriggered;

  public DelayedRelicFlashEffect(AbstractRelic relic, float flashDelay) {
    this.relic = relic;
    this.duration = flashDelay;
    this.startingDuration = flashDelay;
    this.triggerTime = 0.0F;
    this.hasTriggered = false;
  }

  @Override
  public void update() {
//    TheSimpletonMod.logger.debug("DelayedRelicFlashAction::update duration: " + this.duration + "; startingDuration: " + this.startingDuration +  "; triggerTime: " + this.triggerTime);

    this.duration -= Gdx.graphics.getDeltaTime();

//    TheSimpletonMod.logger.debug("DelayedRelicFlashAction::update duration after tick: " + this.duration);

    if (this.duration <= triggerTime && !this.hasTriggered) {
      relic.flash();
      this.hasTriggered = true;
      this.isDone = true;
//      TheSimpletonMod.logger.debug("DelayedRelicFlashAction::update triggered and done");

    } else if (duration < 0.0F) {
      this.isDone = true;
//      TheSimpletonMod.logger.debug("DelayedRelicFlashAction::update not triggered but done");
    }
  }

  @Override
  public void render(SpriteBatch spriteBatch) {

  }

  @Override
  public void dispose() {

  }
}