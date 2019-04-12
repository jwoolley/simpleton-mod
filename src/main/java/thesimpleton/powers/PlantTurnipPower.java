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
import thesimpleton.cards.TheSimpletonCardTags;
import thesimpleton.cards.attack.BabyTurnip;
import thesimpleton.cards.attack.GiantTurnip;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Turnips;
import thesimpleton.cards.skill.AbstractHarvestCard;

public class PlantTurnipPower extends AbstractCropPower {

  public static final String POWER_ID = "TheSimpletonMod:PlantTurnipPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "plantturnip.png";
  private static final CardRarity cropRarity = CardRarity.UNCOMMON;
  private static final AbstractCropPowerCard powerCard = new Turnips();
  private static final boolean IS_HARVEST_ALL = true;
  private static final int MINIMUM_STARTING_STACKS = 2;

  public PlantTurnipPower(AbstractCreature owner, int amount) {
    super(NAME, POWER_ID, POWER_TYPE, DESCRIPTIONS, IMG, owner, cropRarity, powerCard, amount, IS_HARVEST_ALL);
    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = getPassiveDescription() + " NL " + DESCRIPTIONS[0];
  }

  public void onUseCard(AbstractCard card, UseCardAction action) {
    if (card.hasTag(TheSimpletonCardTags.HARVEST) && card instanceof AbstractHarvestCard && ((AbstractHarvestCard)card).autoHarvest) {
      harvest(true, this.amount);
    }
  }

  @Override
  public void harvest(boolean harvestAll, int maxHarvestAmount) {
    super.harvest(harvestAll, maxHarvestAmount);
    if  (amount > 0) {
      final int harvestAmount = this.amount;
      this.flash();

      if (harvestAmount == 1) {
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new BabyTurnip(), 1));
      } else {
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new GiantTurnip(harvestAmount), 1));
      }
      amount -= harvestAmount;

      if (amount == 0) {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
      }
    }
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}
