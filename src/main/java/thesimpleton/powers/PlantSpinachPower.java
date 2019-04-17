package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thesimpleton.cards.HarvestCard;
import thesimpleton.cards.TheSimpletonCardTags;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Spinach;
import thesimpleton.powers.utils.Crop;

public class PlantSpinachPower extends AbstractCropPower {
  public static final Crop enumValue = Crop.SPINACH;
  public static final String POWER_ID = "TheSimpletonMod:PlantSpinachPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "plantspinach.png";
  public static final CardRarity cropRarity = CardRarity.RARE;
  private static final AbstractCropPowerCard powerCard = new Spinach();

  private static final int MATURITY_THRESHOLD = 2;

  private static int BASE_STRENGTH_PER_STACK = 1;
  private static int strengthPerStack = 1;

  public PlantSpinachPower(AbstractCreature owner, int amount) {
    super(enumValue, NAME, POWER_ID, POWER_TYPE, DESCRIPTIONS, IMG, owner, cropRarity, powerCard, amount, false, MATURITY_THRESHOLD);
    this.strengthPerStack = BASE_STRENGTH_PER_STACK;
    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = getPassiveDescription() + " NL " + DESCRIPTIONS[0] + this.strengthPerStack + DESCRIPTIONS[1];
  }

  //TODO: AbstractCard should be an HarvestCard, with harvestAmount, harvestEffect, etc.
  public void onUseCard(AbstractCard card, UseCardAction action) {
    if (card.hasTag(TheSimpletonCardTags.HARVEST) && card instanceof HarvestCard && ((HarvestCard)card).isAutoHarvest()) {
      harvest(((HarvestCard) card).isHarvestAll(), ((HarvestCard) card).getHarvestAmount());
    }
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }

  @Override
  public void harvest(boolean harvestAll, int maxHarvestAmount) {
    super.harvest(harvestAll, maxHarvestAmount);

    if  (amount > 0) {
      final int harvestAmount = Math.min(this.amount, harvestAll ? this.amount : maxHarvestAmount);

      AbstractDungeon.actionManager.addToTop(
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
