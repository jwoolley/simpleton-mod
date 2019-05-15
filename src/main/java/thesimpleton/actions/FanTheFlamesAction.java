package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.powers.BurningPower;

public class FanTheFlamesAction extends AbstractGameAction {
  private static final float ACTION_DURATION = Settings.ACTION_DUR_FASTER;
  private static final ActionType ACTION_TYPE = ActionType.DEBUFF;

  private boolean tickedOnce;
  private int additionalBurning;


  public FanTheFlamesAction(AbstractCreature target, AbstractCreature source, int amount) {
    setValues(this.target,  this.source, this.amount);
    this.target = target;
    this.source = source;
    this.amount = amount;
    this.actionType = ACTION_TYPE;

    this.duration = ACTION_DURATION;
    this.tickedOnce = false;
    this.additionalBurning = 0;
  }

  public void update() {
    Logger logger = TheSimpletonMod.logger;

    logger.debug("FanTheFlames.update :: ACTION_DURATION: " + ACTION_DURATION);
    logger.debug("FanTheFlames.update :: called with duration: " + this.duration);

     if (tickedOnce && this.duration <= ACTION_DURATION) {
      logger.debug("FanTheFlames.update :: second tick");

        if (this.additionalBurning > 0) {
          logger.debug("FanTheFlames.update :: target has " + this.additionalBurning + " burning. applying " + this.additionalBurning + " additional burning");
          AbstractDungeon.actionManager.addToBottom(new ApplyBurningAction(this.target, this.source, this.additionalBurning));
        } else {
          logger.debug("FanTheFlames.update :: target does not have burning for real");
        }
        this.isDone = true;
     } else if (!this.tickedOnce) {
        logger.debug("FanTheFlames.update :: first tick");
        int additionalBurning = 0;

        if (!this.target.hasPower(ArtifactPower.POWER_ID)) {
          additionalBurning += this.amount;
        }

        if (this.target.hasPower(BurningPower.POWER_ID)) {
           additionalBurning += this.target.getPower(BurningPower.POWER_ID).amount;
        }

      this.additionalBurning = additionalBurning;

      AbstractDungeon.actionManager.addToBottom(new ApplyBurningAction(this.target, this.source, this.amount));
      logger.debug("FanTheFlames.update :: will apply " + this.additionalBurning + " additional burning on second tick");
      this.tickedOnce = true;
    }

    this.tickDuration();
  }
}