package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.orbs.AbstractCropOrb;

public class PruningAction extends AbstractGameAction {
  //TODO create "HARVEST/CROP" action type?
  private static final ActionType ACTION_TYPE = ActionType.SPECIAL;
  private static final float ACTION_DURATION = Settings.ACTION_DUR_LONG;

  private AbstractPlayer p;
  private final int numStacksToHarvest;
  private final boolean isFromCard;

  private boolean hasHarvested;
  private boolean firstTickFinished;


  private AbstractCropOrb oldestCropOrb;

  public PruningAction(AbstractPlayer player, int numStacksToHarvest, int numStacksToGain, boolean isFromCard) {
    TheSimpletonMod.logger.debug("============> PruningAction::constructor =====");

    this.p = player;
    setValues(this.p,  this.p, numStacksToGain);
    this.numStacksToHarvest = numStacksToHarvest;
    this.amount = numStacksToGain;
    this.isFromCard = isFromCard;

    this.actionType = ACTION_TYPE;
    this.duration = ACTION_DURATION;
    this.firstTickFinished = false;
    this.hasHarvested = false;

    oldestCropOrb = null;
  }

  public void update() {
    TheSimpletonMod.logger.debug("============> PruningAction::update =====");

    Logger logger = TheSimpletonMod.logger;
    if (this.duration != ACTION_DURATION) {
      AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
      logger.debug("PruningAction.update :: second tick");
      if (this.hasHarvested) {
        logger.debug("PruningAction.update :: stacking " + this.oldestCropOrb.name + " for " + this.amount);

        TheSimpletonMod.logger.debug("============> PruningAction::update =====");

        AbstractDungeon.actionManager.addToBottom(new CropSpawnAction(this.oldestCropOrb, this.amount, true));

      }

      this.tickDuration();
      this.isDone = true;
    }

    if (!this.firstTickFinished) {
      logger.debug("PruningAction.update :: first tick");

      if (AbstractCropOrb.playerHasAnyCropOrbs()) {
        this.oldestCropOrb = AbstractCropOrb.getOldestCropOrb();
        logger.debug("PruningAction.update :: harvesting " + this.oldestCropOrb.name + " for " + this.numStacksToHarvest);

        // this.oldestCropOrb.getCrop().harvest(false, this.numStacksToHarvest);
        TheSimpletonMod.logger.debug("============> PruningAction::update queueing HarvestCropAction =====");

        AbstractDungeon.actionManager.addToTop(new HarvestCropAction(this.oldestCropOrb,  this.numStacksToHarvest,true));

        this.hasHarvested = true;
      } else {
        AbstractDungeon.actionManager.addToBottom(new SFXAction("CARD_SELECT"));
      }
      this.firstTickFinished = true;
    }

    this.tickDuration();
  }
}