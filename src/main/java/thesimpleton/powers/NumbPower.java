package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thesimpleton.actions.NumbAction;

public class NumbPower extends AbstractTheSimpletonPower
{
  public static final String IMG = "numbed.png";

  public static final String POWER_ID = "TheSimpletonMod:NumbPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;
  public static AbstractPower.PowerType POWER_TYPE = AbstractPower.PowerType.DEBUFF;

  public static final int MAX_STACKS = 999;

  private static final int COST_INCREASE = 1;

  private boolean justApplied;

  public NumbPower(AbstractPlayer owner, int amount)
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

    justApplied = true;

    updateDescription();
  }

  public void stackPower(int stackAmount) {
    super.stackPower(stackAmount);
    if (this.amount >= MAX_STACKS) {
      this.amount = MAX_STACKS;
    }
  }

  @Override
  public void onUseCard(AbstractCard card, UseCardAction action) {
    logger.debug("TheSimpletonMod:NumbPower: onUseCard called");
    AbstractDungeon.actionManager.addToBottom(new NumbAction(COST_INCREASE, this));
  }

  @Override
  public void atEndOfRound() {
    if (justApplied) {
      justApplied = false;
      return;
    }
    if (this.amount <= 1) {
      AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    } else {
      this.amount--;
    }
  }


  @Override
  public void updateDescription() {
      this.description = DESCRIPTIONS[0] + COST_INCREASE + DESCRIPTIONS[1];
  }
  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}