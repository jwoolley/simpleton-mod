package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class DeenergizedPower extends AbstractTheSimpletonPower
{
  public static final String IMG = "deenergized.png";

  public static final String POWER_ID = "TheSimpletonMod:DeenergizedPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;
  public static PowerType POWER_TYPE = PowerType.DEBUFF;

  public static final int MAX_STACKS = 999;

  public DeenergizedPower(AbstractCreature owner, int energyAmt)
  {
    super(IMG);
    this.name = NAME;
    this.ID = POWER_ID;
    this.owner = owner;
    this.amount = energyAmt;
    this.type = POWER_TYPE;

    if (this.amount >= MAX_STACKS) {
      this.amount = MAX_STACKS;
    }

    updateDescription();
  }

  public void stackPower(int stackAmount)
  {
    super.stackPower(stackAmount);
    if (this.amount >= MAX_STACKS) {
      this.amount = MAX_STACKS;
    }
  }

  public void updateDescription()
  {
    this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1]);
  }

  public void onEnergyRecharge()
  {
    flash();
    AbstractDungeon.player.loseEnergy(this.amount);
    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}
