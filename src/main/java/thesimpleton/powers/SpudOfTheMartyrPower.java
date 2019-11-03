package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import thesimpleton.crops.PotatoCrop;
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

    logger.debug("Instantiating SpudOfTheMartyrPower");

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
    this.description = DESCRIPTIONS[0] + this.amount + (this.amount == 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2])
      + DESCRIPTIONS[3];
  }

  @Override
  public void atStartOfTurnPostDraw() {
    final int numSpuds = this.amount;
    this.flash();
    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(PotatoCrop.getSpudCard(), numSpuds));
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}