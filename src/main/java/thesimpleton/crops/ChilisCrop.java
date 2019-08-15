package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.actions.ApplyBurningAction;

public class ChilisCrop extends  AbstractCrop {
  public static final Crop CROP_ENUM = Crop.CHILIS;
  public static int DAMAGE_PER_STACK = 4;

  private int damagePerStack;

  public ChilisCrop() {
    super(CROP_ENUM);
    this.damagePerStack = DAMAGE_PER_STACK;
  }

  protected int harvestAction(int harvestAmount) {
    logger.debug("ChilisCrop::harvestAction");
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