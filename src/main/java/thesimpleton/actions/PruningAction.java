package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.powers.AbstractCropPower;
import thesimpleton.powers.utils.Crop;

public class PruningAction extends AbstractGameAction {
  //TODO create "HARVEST/CROP" action type?
  private static final ActionType ACTION_TYPE = ActionType.SPECIAL;
  private static final float ACTION_DURATION = Settings.ACTION_DUR_LONG;

  private AbstractPlayer p;
  private final int numStacksToHarvest;

  private boolean hasHarvested;
  private boolean firstTickFinished;

  private AbstractCropPower oldestPower;

  public PruningAction(AbstractPlayer player, int numStacksToHarvest, int numStacksToGain) {
    this.p = player;
    setValues(this.p,  this.p, numStacksToGain);
    this.numStacksToHarvest = numStacksToHarvest;
    this.amount = numStacksToGain;

    this.actionType = ACTION_TYPE;
    this.duration = ACTION_DURATION;
    this.firstTickFinished = false;
    this.hasHarvested = false;

    oldestPower = null;
  }

  public void update() {
    Logger logger = TheSimpletonMod.logger;
    if (this.duration != ACTION_DURATION) {
      AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
      logger.debug("PruningAction.update :: second tick");
      if (this.hasHarvested) {
        logger.debug("PruningAction.update :: stacking " + this.oldestPower.name + " for " + this.amount);
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, Crop.getCrop(oldestPower.enumValue, p, this.amount), this.amount));
//        this.oldestPower.stackPower(this.amount);
      }

      this.tickDuration();
      this.isDone = true;
    }

    if (!this.firstTickFinished) {
      logger.debug("PruningAction.update :: first tick");

      if (AbstractCropPower.playerHasAnyActiveCropPowers()) {
        this.oldestPower = AbstractCropPower.getOldestCropPower();
        logger.debug("PruningAction.update :: harvesting " + this.oldestPower.name + " for " + this.numStacksToHarvest);

        this.oldestPower.harvest(false, this.numStacksToHarvest);
        this.hasHarvested = true;
      } else {
        AbstractDungeon.actionManager.addToBottom(new SFXAction("CARD_SELECT"));
      }
      this.firstTickFinished = true;
    }

    this.tickDuration();
  }
}