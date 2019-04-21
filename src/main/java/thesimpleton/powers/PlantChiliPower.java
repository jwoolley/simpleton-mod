package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import thesimpleton.actions.ApplyBurningAction;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Chilis;
import thesimpleton.powers.utils.Crop;

public class PlantChiliPower extends AbstractCropPower {
  public static final Crop enumValue = Crop.CHILIS;
  public static final String POWER_ID = "TheSimpletonMod:PlantChiliPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "plantchili.png";
  private static final CardRarity cropRarity = CardRarity.UNCOMMON;
  private static final AbstractCropPowerCard powerCard = new Chilis();
  private static int BASE_DAMAGE_STACK = 4;
  private int damagePerStack;

  public PlantChiliPower(AbstractCreature owner, int amount) {
    super(enumValue, NAME, POWER_ID, POWER_TYPE, DESCRIPTIONS, IMG, owner, cropRarity, powerCard, amount);
    this.damagePerStack = BASE_DAMAGE_STACK;
    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = getPassiveDescription() + " NL " + DESCRIPTIONS[0] + this.damagePerStack + DESCRIPTIONS[1] + this.damagePerStack + DESCRIPTIONS[2];;
  }

  protected int harvestAction(int harvestAmount) {
    final int damageAmount = harvestAmount * this.damagePerStack;

    if (harvestAmount > 0) {
      // all monsters version
      for (int i = 0; i < harvestAmount; i++) {
        AbstractDungeon.actionManager.addToTop(
            new DamageAllEnemiesAction(this.owner, DamageInfo.createDamageMatrix(damageAmount, true),
                DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, true));
      }

      AbstractDungeon.getMonsters().monsters.stream()
          .filter(mo -> !mo.isDeadOrEscaped())
          .forEach(mo -> AbstractDungeon.actionManager.addToBottom(new ApplyBurningAction(mo, this.owner, damageAmount)));
    }
    return harvestAmount;
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}