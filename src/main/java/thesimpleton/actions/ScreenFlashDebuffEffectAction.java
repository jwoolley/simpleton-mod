package thesimpleton.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import thesimpleton.TheSimpletonMod;

public class ScreenFlashDebuffEffectAction extends AbstractGameAction {
  private static final ActionType ACTION_TYPE = ActionType.DEBUFF;
  private static final float DEFAULT_DURATION = Settings.ACTION_DUR_FAST;


  private final Color flashColor;
  private final String sfxKey;

  public ScreenFlashDebuffEffectAction(Color flashColor, String sfxKey) {
    this(flashColor, sfxKey, DEFAULT_DURATION);
  }

  public ScreenFlashDebuffEffectAction(Color flashColor, String sfxKey, float duration) {
    TheSimpletonMod.logger.info("ScreenFlashDebuffEffectAction::constructor");
    this.duration = duration;
    this.actionType = ACTION_TYPE;
    this.flashColor = flashColor.cpy();
    this.sfxKey = sfxKey;
  }

  public void update() {
    TheSimpletonMod.logger.info("ScreenFlashDebuffEffectAction::update duration: " + this.duration);

    if (this.duration < 0.05f) {
      TheSimpletonMod.logger.info("ScreenFlashDebuffEffectAction::update queueing effects");
      AbstractDungeon.actionManager.addToTop(new SFXAction(this.sfxKey));
      AbstractDungeon.actionManager.addToTop(new FastShakeAction(AbstractDungeon.player, 0.2F, 0.1F));
      AbstractDungeon.effectsQueue.add(new BorderFlashEffect(this.flashColor, true));
      this.isDone = true;
      return;
    }
    this.tickDuration();
  }
}