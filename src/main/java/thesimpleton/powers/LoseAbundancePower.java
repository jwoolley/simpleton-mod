package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import thesimpleton.powers.unused.ToughSkinPower;

public class LoseAbundancePower extends AbstractTheSimpletonPower
{
  public static final String IMG = "abundancedown.png";

  public static final String POWER_ID = "TheSimpletonMod:LoseAbundancePower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;
  public static PowerType POWER_TYPE = PowerType.DEBUFF;

  public static final int MAX_STACKS = 999;

  public LoseAbundancePower(AbstractCreature owner, int amount)
  {
    super(IMG);
    this.name = NAME;
    this.ID = POWER_ID;
    this.owner = owner;
    this.amount = amount;
    this.type = POWER_TYPE;

    if (this.amount >= MAX_STACKS) {
      this.amount = MAX_STACKS;
    }

    updateDescription();
  }

  public void stackPower(int stackAmount) {
    super.stackPower(stackAmount);
    if (this.amount >= MAX_STACKS) {
      this.amount = MAX_STACKS;
    }
  }

  public void updateDescription()
  {
    this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1]);
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    flash();
    AbstractPlayer player = AbstractDungeon.player;

//
//    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner,
//      new AbundancePower(this.owner, this.owner, -this.amount), -this.amount));



    if (this.owner.hasPower(AbundancePower.POWER_ID)
        && this.owner.getPower(AbundancePower.POWER_ID).amount == this.amount) {
      AbstractDungeon.actionManager.addToBottom(
          new RemoveSpecificPowerAction(this.owner, this.owner, AbundancePower.POWER_ID));
    } else {
      AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner,
          new AbundancePower(this.owner, this.owner, -this.amount), -this.amount));
    }

//    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner,
//        new AbundancePower(this.owner, this.owner, -this.amount), -this.amount));

    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}
