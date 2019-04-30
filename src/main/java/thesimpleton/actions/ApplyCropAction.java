package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.powers.FecundityPower;

public class ApplyCropAction extends ApplyPowerAction {

  public ApplyCropAction(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount) {
    this(target, source, powerToApply, stackAmount, false);
  }

  public ApplyCropAction(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFromCard) {
    super(target, source, powerToApply, calculateCropStacks(stackAmount, isFromCard));
  }

  private static int calculateCropStacks(int amount, boolean isFromCard) {
    AbstractPlayer player = SimpletonUtil.getPlayer();

    int adjustedAmount = amount;
    if (player.hasPower(FecundityPower.POWER_ID) && isFromCard) {
      AbstractPower power = player.getPower(FecundityPower.POWER_ID);
      if (power.amount > 0) {
        adjustedAmount += power.amount;
      }
    }
    return adjustedAmount;
  }
}
