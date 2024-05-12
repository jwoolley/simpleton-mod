package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;

import thesimpleton.TheSimpletonMod;
import thesimpleton.orbs.AbstractCropOrb;
import thesimpleton.utilities.ModLogger;


public class HarvestCropAction extends AbstractGameAction {
  private static final ModLogger logger = TheSimpletonMod.traceLogger;

  private static final ActionType ACTION_TYPE = ActionType.SPECIAL;
  private static final float ACTION_DURATION = Settings.ACTION_DUR_XFAST;
  private final boolean isFromCard;
  private final boolean harvestAll;
  private final int amount;
  private final AbstractCropOrb cropOrb;

  // TODO: increment existing cropOrb instead if it
  public HarvestCropAction(AbstractCropOrb cropOrb, boolean isFromCard) {
    this(cropOrb, -1, isFromCard);
  }

  public HarvestCropAction(AbstractCropOrb cropOrb, int stacks, boolean isFromCard) {
    this(cropOrb, stacks, isFromCard, false);
  }


  public HarvestCropAction(AbstractCropOrb cropOrb, int stacks, boolean isFromCard, boolean harvestAll) {
    logger.trace("============> HarvestCropAction::constructor =====");

    final int rawAmount = stacks >= 0 ? stacks : cropOrb.passiveAmount;

    this.duration = ACTION_DURATION;
    this.actionType = ACTION_TYPE;
    this.isFromCard = isFromCard;
    this.harvestAll = harvestAll;
    this.amount = CropSpawnAction.calculateCropStacks(rawAmount, this.isFromCard, false);
    this.cropOrb = cropOrb;

    logger.trace("HarvestCropAction() constructor: " + cropOrb.getClass().getSimpleName() + "; rawAmount: " + rawAmount + " calculated amount: " + this.amount + "; harvestAll: " + harvestAll);
  }

  public void update() {
//    logger.trace("============> HarvestCropAction::update =====");

    if (this.duration == ACTION_DURATION) {
//      logger.trace("============> HarvestCropAction::update duration == ACTION_DURATION =====");


      final int stacks = cropOrb.passiveAmount < this.amount ? cropOrb.passiveAmount : this.amount;
//      logger.trace("HarvestCropAction.update :: player has " + cropOrb.getAmount() + " stacks of " + cropOrb.name);
//      logger.trace("HarvestCropAction.update :: numStacksToHarvest: " + this.amount);
//      logger.trace("HarvestCropAction.update :: harvesting " + stacks + " stacks of " + cropOrb.name);

//      logger.trace("============> HarvestCropAction::update calling AbstractCrop.harvest() =====");

      cropOrb.getCrop().harvest(this.harvestAll, stacks);
      this.isDone = true;
    }
//    logger.trace("============> HarvestCropAction::update tickDuration =====");

    tickDuration();
  }
}



