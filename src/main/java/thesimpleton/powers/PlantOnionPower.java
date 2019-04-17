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
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import thesimpleton.cards.HarvestCard;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.TheSimpletonCardTags;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Onions;
import thesimpleton.powers.utils.Crop;

public class PlantOnionPower extends AbstractCropPower {
  public static final Crop enumValue = Crop.ONIONS;
  public static final String POWER_ID = "TheSimpletonMod:PlantOnionPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "plantonion.png";
  private static final CardRarity cropRarity = CardRarity.COMMON;
  private static final AbstractCropPowerCard powerCard = new Onions();
  private static int BASE_WEAK_PER_STACK = 1;
  private static int weakPerStack = 1;

  public PlantOnionPower(AbstractCreature owner, int amount) {
    super(enumValue, NAME, POWER_ID, POWER_TYPE, DESCRIPTIONS, IMG, owner, cropRarity, powerCard, amount);
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

      if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
        this.flash();

        for (int i = 0; i < harvestAmount; i++) {
          AbstractMonster randomMonster = SimpletonUtil.getRandomMonster();
          if (randomMonster != null) {
            applyWeakPower(randomMonster, 1);
          }
        }
      }

      amount -= harvestAmount;

      if (amount == 0) {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
      }
    }
  }

  private void applyWeakPower(AbstractMonster m, int numApplications) {
    final int weakAmount =  this.weakPerStack * numApplications;

    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(m, this.owner, new WeakPower(m, weakAmount, false),weakAmount));
  }

}