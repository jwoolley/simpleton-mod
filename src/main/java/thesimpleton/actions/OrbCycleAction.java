package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.orbs.AbstractCropOrb;


public class OrbCycleAction extends AbstractGameAction {
  private static final ActionType ACTION_TYPE = ActionType.SPECIAL;
  private static final float ACTION_DURATION = Settings.ACTION_DUR_FAST;
  private final boolean isFromCard;
  private final int amount;
  private AbstractOrb orb;

  private boolean secondTick = false;

  // TODO: increment existing cropOrb instead if (?) it
  public OrbCycleAction(AbstractOrb orb, boolean isFromCard) {
    this(orb, -1, isFromCard);
  }

  public OrbCycleAction(AbstractOrb orb, int stacks, boolean isFromCard) {
    TheSimpletonMod.logger.debug("============> CropOrbCycleAction::constructor =====");

    final int rawAmount = stacks >= 0 ? stacks : orb.passiveAmount;

    this.duration = ACTION_DURATION;
    this.actionType = ACTION_TYPE;
    this.isFromCard = isFromCard;
    this.amount = rawAmount;
    this.orb = orb;

    Logger logger = TheSimpletonMod.logger;
    logger.debug("CropOrbCycleAction() constructor: " + orb.getClass().getSimpleName() + "; rawAmount: " + rawAmount + " calculated amount: " + this.amount + " orb.passiveAmount " + orb.passiveAmount);
  }

  public void update() {
    Logger logger = TheSimpletonMod.logger;

    logger.debug("CropOrbCycleAction::update duration: " + this.duration);

    if (AbstractDungeon.player.maxOrbs <= 0) {
      this.isDone = true;
      return;
    }

    if (secondTick) {
      if (this.duration != ACTION_DURATION) {
        logger.debug("CropOrbCycleAction::update spawning orb " + this.orb.name + " for " + this.amount);

        if (orb instanceof AbstractCropOrb) {
          AbstractDungeon.actionManager.addToBottom(new CropSpawnAction((AbstractCropOrb) this.orb, this.amount, this.isFromCard));
        } else {
          AbstractDungeon.actionManager.addToBottom(new thesimpleton.actions.OrbSpawnAction(this.orb, this.amount, this.isFromCard));
        }
      }
      this.isDone = true;
      return;
    }

    if (!AbstractDungeon.player.orbs.isEmpty() &&  AbstractDungeon.player.orbs.get(0).ID != EmptyOrbSlot.ORB_ID) {
//      logger.debug("CropOrbCycleAction::update evoking orb: " + AbstractDungeon.player.orbs.get(0).name);
      AbstractDungeon.actionManager.addToTop(new EvokeOrbAction(1));
    } else {
      logger.debug("CropOrbCycleAction::update no old orb to evoke");
    }
    this.secondTick = true;
    tickDuration();
  }
}



