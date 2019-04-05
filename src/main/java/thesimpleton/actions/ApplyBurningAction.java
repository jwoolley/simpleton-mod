package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.powers.BurningPower;

public class ApplyBurningAction extends AbstractGameAction {
  private static float ACTION_DURATION = Settings.ACTION_DUR_XFAST;

  public ApplyBurningAction(AbstractCreature target, AbstractCreature source, int amount) {
    this.target = target;
    this.source = source;
    this.amount = amount;
    this.duration = ACTION_DURATION;
    this.startDuration = ACTION_DURATION;
  }

  @Override
  public void update() {
    Logger logger = TheSimpletonMod.logger;
    logger.debug("ApplyBurningAction.update called");
    if (this.duration == this.startDuration) {
      logger.debug("ApplyBurningAction.update ticked");

      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(
              this.target, this.source, new BurningPower(this.target, this.source, this.amount), this.amount));
    }
    this.tickDuration();
  }
}
