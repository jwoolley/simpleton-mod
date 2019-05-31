package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.crops.AbstractCrop;
import thesimpleton.orbs.AbstractCropOrb;
import thesimpleton.powers.AbundancePower;


public class HarvestCropAction extends AbstractGameAction {
  private static final ActionType ACTION_TYPE = ActionType.SPECIAL;
  private static final float ACTION_DURATION = Settings.ACTION_DUR_XFAST;
  private final boolean isFromCard;
  private final int amount;
  private AbstractCropOrb cropOrb;

  // TODO: increment existing cropOrb instead if it
  public HarvestCropAction(AbstractCropOrb cropOrb, boolean isFromCard) {
    this(cropOrb, -1, isFromCard);
  }

  public HarvestCropAction(AbstractCropOrb cropOrb, int stacks, boolean isFromCard) {
    TheSimpletonMod.logger.debug("============> HarvestCropAction::constructor =====");

    final int rawAmount = stacks >= 0 ? stacks : cropOrb.passiveAmount;

    this.duration = ACTION_DURATION;
    this.actionType = ACTION_TYPE;
    this.isFromCard = isFromCard;
    this.amount = CropSpawnAction.calculateCropStacks(rawAmount, this.isFromCard);
    this.cropOrb = cropOrb;

    Logger logger = TheSimpletonMod.logger;
    logger.debug("HarvestCropAction() constructor: " + cropOrb.getClass().getSimpleName() + "; rawAmount: " + rawAmount + " calculated amount: " + this.amount);
  }

  public void update() {
    TheSimpletonMod.logger.debug("============> HarvestCropAction::update =====");

    if (this.duration == ACTION_DURATION) {
      TheSimpletonMod.logger.debug("============> HarvestCropAction::update duration == ACTION_DURATION =====");

      Logger logger = TheSimpletonMod.logger;

      final int stacks = cropOrb.getAmount() < this.amount ? cropOrb.getAmount() : this.amount;
      logger.debug("HarvestCropAction.update :: player has " + cropOrb.getAmount() + " stacks of " + cropOrb.name);
      logger.debug("HarvestCropAction.update :: numStacksToHarvest: " + this.amount);
      logger.debug("HarvestCropAction.update :: harvesting " + stacks + " stacks of " + cropOrb.name);

      TheSimpletonMod.logger.debug("============> HarvestCropAction::update calling AbstractCrop.harvest() =====");

      cropOrb.getCrop().harvest(false, stacks);
      this.isDone = true;
    }
    TheSimpletonMod.logger.debug("============> HarvestCropAction::update tickDuration =====");

    tickDuration();
  }
}



