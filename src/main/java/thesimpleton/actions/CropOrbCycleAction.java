package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.orbs.AbstractCropOrb;
import thesimpleton.utilities.ModLogger;


public class CropOrbCycleAction extends AbstractGameAction {
  private static ModLogger logger = TheSimpletonMod.traceLogger;

  private static final ActionType ACTION_TYPE = ActionType.SPECIAL;
  private static final float ACTION_DURATION = Settings.ACTION_DUR_FAST;
  private final boolean isFromCard;
  private final int amount;
  private AbstractCropOrb cropOrb;

  private boolean secondTick = false;

  // TODO: increment existing cropOrb instead if (?) it
  public CropOrbCycleAction(AbstractCropOrb cropOrb, boolean isFromCard) {
    this(cropOrb, -1, isFromCard);
  }

  public CropOrbCycleAction(AbstractCropOrb cropOrb, int stacks, boolean isFromCard) {
    logger.trace("============> CropOrbCycleAction::constructor =====");

    final int rawAmount = stacks >= 0 ? stacks : cropOrb.passiveAmount;

    this.duration = ACTION_DURATION;
    this.actionType = ACTION_TYPE;
    this.isFromCard = isFromCard;
    this.amount = rawAmount;
    this.cropOrb = cropOrb;

   logger.trace("CropOrbCycleAction() constructor: " + cropOrb.getClass().getSimpleName() + "; rawAmount: " + rawAmount + " calculated amount: " + this.amount + " cropOrb.amount (current count): " + cropOrb.getAmount() + " cropOrb.passiveAmount " + cropOrb.passiveAmount);
  }

  public void update() {
    logger.trace("CropOrbCycleAction::update duration: " + this.duration);

    if (AbstractDungeon.player.maxOrbs <= 0) {
      this.isDone = true;
      return;
    }

    if (secondTick) {
      if (this.duration != ACTION_DURATION) {
        logger.trace("CropOrbCycleAction::update spawning orb " + this.cropOrb.name + " for " + this.amount);

        AbstractDungeon.actionManager.addToBottom(new CropSpawnAction(this.cropOrb, this.amount, this.isFromCard));
      }
      this.isDone = true;
      return;
    }

    if (!AbstractDungeon.player.orbs.isEmpty() &&  AbstractDungeon.player.orbs.get(0).ID != EmptyOrbSlot.ORB_ID) {
//      logger.trace("CropOrbCycleAction::update evoking orb: " + AbstractDungeon.player.orbs.get(0).name);
      AbstractDungeon.actionManager.addToTop(new EvokeOrbAction(1));
    } else {
      logger.trace("CropOrbCycleAction::update no old orb to evoke");
    }
    this.secondTick = true;
    tickDuration();
  }
}



