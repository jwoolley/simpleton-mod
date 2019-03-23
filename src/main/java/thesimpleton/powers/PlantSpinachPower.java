package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thesimpleton.cards.TheSimpletonCardTags;
import thesimpleton.cards.skill.AbstractHarvestCard;

public class PlantSpinachPower extends AbstractCropPower {

  public static final String POWER_ID = "TheSimpletonMod:PlantSpinachPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static AbstractPower.PowerType POWER_TYPE = AbstractPower.PowerType.BUFF;
  public static final String IMG = "plantspinach.png";

  private static int BASE_STRENGTH_PER_STACK = 1;
  private static int strengthPerStack = 1;

  public PlantSpinachPower(AbstractCreature owner, int amount) {
    super(IMG, owner, amount);

    this.name = NAME;
    this.ID = POWER_ID;
    this.type = POWER_TYPE;
    this.strengthPerStack = BASE_STRENGTH_PER_STACK;
    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + this.strengthPerStack + DESCRIPTIONS[1];
  }

  //TODO: AbstractCard should be an AbstractHarvestCard, with harvestAmount, harvestEffect, etc.
  public void onUseCard(AbstractCard card, UseCardAction action) {
    if (card.hasTag(TheSimpletonCardTags.HARVEST) && card instanceof AbstractHarvestCard && ((AbstractHarvestCard)card).autoHarvest) {
      harvest(((AbstractHarvestCard) card).isHarvestAll(), ((AbstractHarvestCard) card).getHavestAmount());
    }
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }

  @Override
  public void harvest(boolean harvestAll, int maxHarvestAmount) {
    if  (amount > 0) {
      final int harvestAmount = Math.min(this.amount, harvestAll ? this.amount : maxHarvestAmount);

      System.out.println("Spinach.harvest this.amount: " + this.amount);
      System.out.println("Spinach.harvest harvestAll: " + harvestAll);
      System.out.println("Spinach.harvest maxHarvestAmount: " + maxHarvestAmount);
      System.out.println("Spinach.harvest harvestAmount: " + harvestAmount);

      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(
              this.owner, this.owner,
              new StrengthPower(this.owner, strengthPerStack), harvestAmount * strengthPerStack));

      amount -= harvestAmount;

      if (amount == 0) {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
      }
    }
  }
}
