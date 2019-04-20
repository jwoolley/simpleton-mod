package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import thesimpleton.cards.HarvestCard;
import thesimpleton.cards.TheSimpletonCardTags;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Mushrooms;
import thesimpleton.powers.utils.Crop;

public class PlantMushroomPower extends AbstractCropPower {
  public static final Crop enumValue = Crop.MUSHROOMS;

  public static final String POWER_ID = "TheSimpletonMod:PlantMushroomPower";
  private static final PowerStrings powerStrings;

  public static final String NAME;
  public static final String[] DESCRIPTIONS;
  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "plantmushroom.png";
  public static final CardRarity cropRarity = CardRarity.RARE;
  private static final AbstractCropPowerCard powerCard = new Mushrooms();

  private static final int MATURITY_THRESHOLD = 2;

  public PlantMushroomPower(AbstractCreature owner, int amount) {
    super(enumValue, NAME, POWER_ID, POWER_TYPE, DESCRIPTIONS, IMG, owner, cropRarity, powerCard, amount, false, MATURITY_THRESHOLD);
    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = getPassiveDescription() + " NL " + DESCRIPTIONS[0];
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

      this.flash();

      if (harvestAmount > 0) {
        for (int i = 0; i < harvestAmount; i++) {
          AbstractCard card = AbstractDungeon.returnTrulyRandomColorlessCardInCombat().makeCopy();
          AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card, 1));
        }
      }

      amount -= harvestAmount;

      if (amount == 0) {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
      }
    }
  }
}
