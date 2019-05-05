package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.WeakPower;

public class NextTurnWeakPower extends AbstractTheSimpletonPower
{
  public static final String IMG = "nextturnweak.png";

  public static final String POWER_ID = "TheSimpletonMod:NextTurnWeak";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;
  public static PowerType POWER_TYPE = PowerType.DEBUFF;

  public static final int MAX_STACKS = 999;

  public NextTurnWeakPower(AbstractCreature owner, int amount)
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


  public void atStartOfTurnPostDraw() {
    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner,
        new WeakPower(this.owner, this.amount, false), this.amount));

    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
  }

  @Override
  public void updateDescription() {
    if (this.amount == 1) {
      this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    } else {
      this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
    }
  }
  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}
