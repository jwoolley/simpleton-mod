package thesimpleton.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import thesimpleton.relics.SpudOfTheMartyr;

public class SpudOfTheMartyrPower extends AbstractTheSimpletonPower {
  public static final String POWER_ID = "TheSimpletonMod:SpudOfTheMartyrPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "spudofthemartyr.png";

  private static SpudOfTheMartyr relic;

  public static final int MAX_STACKS = 999;


  public SpudOfTheMartyrPower(AbstractCreature owner, int amount, SpudOfTheMartyr relic) {
    super(IMG);

    POWER_LOGGER.trace("Instantiating SpudOfTheMartyrPower");

    this.owner = owner;
    this.amount = amount;
    this.relic = relic;

    this.name = NAME;
    this.ID = POWER_ID;
    this.type = POWER_TYPE;

    updateDescription();
  }

  public void stackPower(int stackAmount) {
    super.stackPower(stackAmount);
    if (this.amount >= MAX_STACKS) {
      this.amount = MAX_STACKS;
    }
    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
  }

  @Override
  public void atStartOfTurn() {
    final int strAmount = this.amount;

  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}