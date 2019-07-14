package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.powers.AbundancePower;

public class ApplyCropAction extends ApplyPowerAction {

  // TODO: constructor should take Crop enum instead of powerToApply; calculate crop stacks and create new power
  //        before passing to superconstructor (this prevents caller from needing to indicate isFromCard twice)
  public ApplyCropAction(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount) {
    this(target, source, powerToApply, stackAmount, false);
  }

  public ApplyCropAction(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFromCard) {
    super(target, source, powerToApply, calculateCropStacks(stackAmount, isFromCard));
  }

  public static int calculateCropStacks(int amount, boolean isFromCard) {
    AbstractPlayer player =   AbstractDungeon.player;

    Logger logger = TheSimpletonMod.logger;

    logger.info("ApplyCropAction:calculateCropStacks");

    int adjustedAmount = amount;
    if (player.hasPower(AbundancePower.POWER_ID) && isFromCard) {
      AbstractPower power = player.getPower(AbundancePower.POWER_ID);
      if (power.amount > 0) {
        power.flashWithoutSound();
        adjustedAmount += power.amount;
      }
    }
    return adjustedAmount;
  }
}
