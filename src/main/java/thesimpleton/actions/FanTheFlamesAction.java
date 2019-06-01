package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.powers.BurningPower;

import java.util.*;

public class FanTheFlamesAction extends AbstractGameAction {
  private static final float ACTION_DURATION = Settings.ACTION_DUR_FASTER;
  private static final ActionType ACTION_TYPE = ActionType.DEBUFF;

  private final boolean targetAll;

  private boolean tickedOnce;
  private Map<AbstractCreature, Integer> additionalBurningCounts;
//  private int additionalBurningCounts;

  public FanTheFlamesAction(AbstractCreature source, int amount) {
    this(null, source, amount, true);
  }

  public FanTheFlamesAction(AbstractCreature target, AbstractCreature source, int amount) {
    this(target, source, amount, false);
  }

  private FanTheFlamesAction(AbstractCreature target,  AbstractCreature source, int amount, boolean targetAll) {
    setValues(target,  source, amount);
    this.actionType = ACTION_TYPE;
    this.duration = ACTION_DURATION;
    this.targetAll = targetAll;
    this.tickedOnce = false;
    this.additionalBurningCounts = new HashMap<>();
  }

  public void update() {
    Logger logger = TheSimpletonMod.logger;

    logger.debug("FanTheFlames.update :: ACTION_DURATION: " + ACTION_DURATION);
    logger.debug("FanTheFlames.update :: called with duration: " + this.duration);

     if (tickedOnce && this.duration <= ACTION_DURATION) {
      logger.debug("FanTheFlames.update :: second tick");

      additionalBurningCounts.entrySet()
        .forEach(entry ->  {
          final int additionalBurning = entry.getValue();
          if (additionalBurning > 0) {
           logger.debug("FanTheFlames.update :: target has " + this.additionalBurningCounts + " burning. applying " + this.additionalBurningCounts + " additional burning");
           AbstractDungeon.actionManager.addToBottom(new ApplyBurningAction(entry.getKey(), this.source, additionalBurning));
          } else {
            logger.debug("FanTheFlames.update :: target still does not have burning for some reason");
          }
        });
      this.isDone = true;
     } else if (!this.tickedOnce) {
        logger.debug("FanTheFlames.update :: first tick");

       List<AbstractMonster> targetList = this.targetAll ? AbstractDungeon.getCurrRoom().monsters.monsters : Arrays.asList((AbstractMonster) this.target);

       for (AbstractMonster monster : targetList) {
         int additionalBurning = this.amount;

         if (monster.hasPower(BurningPower.POWER_ID)) {
           additionalBurning += monster.getPower(BurningPower.POWER_ID).amount;
         }

         this.additionalBurningCounts.put(monster, additionalBurning);
         AbstractDungeon.actionManager.addToBottom(new ApplyBurningAction(monster, this.source, this.amount));
         logger.debug("FanTheFlames.update :: will apply " + this.additionalBurningCounts + " additional burning on second tick");
       }
      this.tickedOnce = true;
    }

    this.tickDuration();
  }
}