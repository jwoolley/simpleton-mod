package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thesimpleton.cards.CurseUtil;
import thesimpleton.cards.TheSimpletonCardTags;

public class PlantPotatoPower extends AbstractTheSimpletonPower {

  public static final String POWER_ID = "TheSimpletonMod:PlantPotatoPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static AbstractPower.PowerType POWER_TYPE = AbstractPower.PowerType.BUFF;
  public static final String IMG = "plantpotato.png";

  public PlantPotatoPower(AbstractCreature owner, int amount) {
    super(IMG);
    this.owner = owner;
    this.amount = amount;

    this.name = NAME;
    this.ID = POWER_ID;
    this.type = POWER_TYPE;

    updateDescription();
  }

  @Override
  public void updateDescription() {
    final boolean singular = this.amount == 1;
    this.description = DESCRIPTIONS[0]
        + (singular ? "a " : "") + DESCRIPTIONS[1] + (singular ? "" : "#ys")
        + DESCRIPTIONS[2];
  }

  //TODO: AbstractCard should be an AbstractHarvestCard, with harvestAmount, harvestEffect, etc.
  public void onUseCard(AbstractCard card, UseCardAction action) {
    final boolean harvestAll = true;
    final int harvestAmount = harvestAll == true ? this.amount : 1; // card.harvestAmount;
    if (this.amount > 0) {
      if (card.hasTag(TheSimpletonCardTags.HARVEST) || card.cardID == CurseUtil.SHIV.cardID) {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(CurseUtil.SPUD_MISSILE, harvestAmount));
        this.amount -= harvestAmount;
        if (this.amount == 0) {
          AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
      }
    }
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}
