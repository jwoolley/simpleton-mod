package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.actions.ApplyBurningAction;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Chilis;
import thesimpleton.orbs.PotatoCropOrb;
import thesimpleton.powers.utils.Crop;

public class PotatoesCrop extends  AbstractCrop {
  public static final Crop CROP_ENUM = Crop.POTATOES;
  private static final String ORB_ID = PotatoCropOrb.ORB_ID;
  private static final AbstractCropPowerCard POWER_CARD = new Chilis();

  public static final int MATURITY_THRESHOLD = 5;
  private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.UNCOMMON;

  private static int BASE_DAMAGE_STACK = 4;
  private int damagePerStack;

  public PotatoesCrop() {
    super(CROP_ENUM, ORB_ID, POWER_CARD, RARITY, MATURITY_THRESHOLD);
    this.damagePerStack = BASE_DAMAGE_STACK;
    logger.debug("MAKIN' POTATOES (instantiating PotatoesCrop).");

  }

  protected int harvestAction(int harvestAmount) {
    final int damageAmount = harvestAmount * this.damagePerStack;

    if (harvestAmount > 0) {
      // all monsters version
      for (int i = 0; i < harvestAmount; i++) {
        AbstractDungeon.actionManager.addToTop(
            new DamageAllEnemiesAction(getPlayer(), DamageInfo.createDamageMatrix(damageAmount, true),
                DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, true));
      }

      AbstractDungeon.getMonsters().monsters.stream()
          .filter(mo -> !mo.isDeadOrEscaped())
          .forEach(mo -> AbstractDungeon.actionManager.addToBottom(new ApplyBurningAction(mo, getPlayer(), damageAmount)));
    }
    return harvestAmount;
  }

}