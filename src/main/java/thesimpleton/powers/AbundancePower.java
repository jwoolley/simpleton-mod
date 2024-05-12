package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;


public class AbundancePower extends AbstractTheSimpletonPower {
  public static final String POWER_ID = "TheSimpletonMod:AbundancePower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;
  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "abundance.png";

  private AbstractCreature source;

  public AbundancePower(AbstractCreature owner, AbstractCreature source, int amount) {
    super(IMG);
    this.owner = owner;
    this.source = source;
    this.amount = amount;

    this.name = NAME;
    this.ID = POWER_ID;
    this.type = POWER_TYPE;

    updateDescription();
  }

  @Override
  public void reducePower(int amount) {
    super.reducePower(amount);

    POWER_LOGGER.trace("AbundancePower::reducePower amount (post superclass call): " + this.amount);

    if (this.amount == 0) {
      AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }
  }

  public void onRemove() {
    POWER_LOGGER.trace("AbundancePower::onRemove called");
  }

  public void stackPower(int amount) {
    super.stackPower(amount);
    POWER_LOGGER.trace("AbundancePower::stackPower called. amount: " + amount);
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}