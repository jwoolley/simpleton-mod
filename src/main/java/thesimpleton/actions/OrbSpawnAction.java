package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.powers.AbundancePower;

public class OrbSpawnAction extends AbstractGameAction {
  private static final ActionType ACTION_TYPE = ActionType.SPECIAL;
  private static final float ACTION_DURATION = Settings.ACTION_DUR_XFAST;
  private final boolean isFromCard;
  private final int amount;
  private AbstractOrb orb;

  private boolean secondTick = false;

  public OrbSpawnAction(AbstractOrb orb, int stacks, boolean isFromCard) {
    TheSimpletonMod.logger.info("OrbSpawnAction::constructor called");

    final int rawAmount = stacks >= 0 ? stacks : orb.passiveAmount;
    this.duration = ACTION_DURATION;
    this.actionType = ACTION_TYPE;
    this.isFromCard = isFromCard;
    this.amount = rawAmount;
    this.orb = orb;

    Logger logger = TheSimpletonMod.logger;
  }

  public void update() {
    TheSimpletonMod.logger.info("OrbSpawnAction::update duration: " + this.duration);

    Logger logger = TheSimpletonMod.logger;

    if (AbstractDungeon.player.maxOrbs <= 0 && !SimpletonUtil.playerHasStartingOrbs()) {
      AbstractDungeon.player.increaseMaxOrbSlots(1, true);
    }

    if (SimpletonUtil.getActiveOrbs().size() >= AbstractDungeon.player.maxOrbs) {
      logger.info("OrbSpawnAction::update player has no free orb slots. Queueing CropOrbCycleAction with " + this.orb.name + " for " + this.orb.passiveAmount + " stacks");
      AbstractDungeon.actionManager.addToBottom(new OrbCycleAction(this.orb, this.amount, this.isFromCard));
    }  else {
      this.orb.passiveAmount = this.amount;

      logger.info("OrbSpawnAction::update # of " + this.orb.name + " passiveAmount after (1): " + this.orb.passiveAmount + "; this.amount: " + this.amount);

      AbstractOrb newOrb = this.orb.makeCopy();
      AbstractDungeon.player.channelOrb(newOrb);
      newOrb.update();
      this.isDone = true;
    }

    tickDuration();
  }
}



