package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import thesimpleton.cards.TheSimpletonCardTags;

public class BiorefinementPower extends AbstractTheSimpletonPower {
  public static final String POWER_ID = "TheSimpletonMod:BiorefinementPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "biorefinement.png";

  private boolean usedForTurn;

  public BiorefinementPower(AbstractCreature owner, int energyAmount) {
    super(IMG);
    this.owner = owner;
    this.amount = energyAmount;

    this.name = NAME;
    this.ID = POWER_ID;
    this.type = POWER_TYPE;

    this.usedForTurn = false;

    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = (DESCRIPTIONS[0]);
  }

  @Override
  public void onUseCard(AbstractCard card, UseCardAction action) {
    if (!usedForTurn && card.hasTag(TheSimpletonCardTags.HARVEST)) {
      this.flash();
      AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.amount));
      usedForTurn = true;
    }
  }

  public void atStartOfTurn() {
    this.usedForTurn = false;
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}
