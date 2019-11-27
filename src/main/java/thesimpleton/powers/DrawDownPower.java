package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class DrawDownPower extends AbstractTheSimpletonPower
{
  public static final String IMG = "drawdown.png";

  public static final String POWER_ID = "TheSimpletonMod:DrawDownPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;
  public static PowerType POWER_TYPE = PowerType.DEBUFF;

  public static final int MAX_STACKS = 999;

  public DrawDownPower(AbstractCreature owner, int amount)
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
    updateDescription();
  }

  public void updateDescription() {
    this.description = (DESCRIPTIONS[0] + this.amount + ((this.amount == 1) ? DESCRIPTIONS[1] : DESCRIPTIONS[2]));
  }

  @Override
  public void atStartOfTurn() {
    flash();
    AbstractDungeon.player.gameHandSize -= this.amount;
  }

  @Override
  public void atStartOfTurnPostDraw() {
    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    AbstractDungeon.player.gameHandSize += this.amount;
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}