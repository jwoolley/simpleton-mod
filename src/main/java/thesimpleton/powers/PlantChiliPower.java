package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import thesimpleton.actions.ApplyBurningAction;
import thesimpleton.cards.TheSimpletonCardTags;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Chilis;
import thesimpleton.cards.skill.AbstractHarvestCard;

public class PlantChiliPower extends AbstractCropPower {
  public static final String POWER_ID = "TheSimpletonMod:PlantChiliPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "plantchili.png";
  private static final CardRarity cropRarity = CardRarity.UNCOMMON;
  private static final AbstractCropPowerCard powerCard = new Chilis();
  private static int BASE_DAMAGE_STACK = 4;
  private static int damagePerStack = 1;

  public PlantChiliPower(AbstractCreature owner, int amount) {
    super(NAME, POWER_ID, POWER_TYPE, DESCRIPTIONS, IMG, owner, cropRarity, powerCard, amount);
    this.damagePerStack = BASE_DAMAGE_STACK;
    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = getPassiveDescription() + " NL " + DESCRIPTIONS[0] + this.damagePerStack + DESCRIPTIONS[1] + this.damagePerStack + DESCRIPTIONS[2];;
  }

  //TODO: AbstractCard should be an AbstractHarvestCard, with harvestAmount, harvestEffect, etc.
  public void onUseCard(AbstractCard card, UseCardAction action) {
    if (card.hasTag(TheSimpletonCardTags.HARVEST) && card instanceof AbstractHarvestCard && ((AbstractHarvestCard)card).autoHarvest) {
      harvest(((AbstractHarvestCard) card).isHarvestAll(), ((AbstractHarvestCard) card).getHarvestAmount());
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

        final int damageAmount = harvestAmount * this.damagePerStack;

        // all monsters version
        for (int i = 0; i < harvestAmount; i++) {
          AbstractDungeon.actionManager.addToTop(
              new DamageAllEnemiesAction(this.owner, DamageInfo.createDamageMatrix(damageAmount, true),
                  DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.FIRE, true));
        }

        AbstractDungeon.getMonsters().monsters.stream()
            .filter(mo -> !mo.isDeadOrEscaped())
            .forEach(mo -> AbstractDungeon.actionManager.addToBottom(new ApplyBurningAction(mo, this.owner, damageAmount)));
      }

      amount -= harvestAmount;

      if (amount == 0) {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
      }
    }
  }
}