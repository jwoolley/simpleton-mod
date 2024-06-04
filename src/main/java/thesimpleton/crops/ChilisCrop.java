package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.ApplyBurningAction;
import thesimpleton.actions.BurnAllEnemiesAction;

public class ChilisCrop extends  AbstractCrop {
  public static final Crop CROP_ENUM = Crop.CHILIS;
  public static int DAMAGE_PER_STACK = 4;

  private int damagePerStack;

  public ChilisCrop() {
    super(CROP_ENUM);
    this.damagePerStack = DAMAGE_PER_STACK;
  }

  protected int harvestAction(int harvestAmount) {
    TheSimpletonMod.traceLogger.trace("ChilisCrop::harvestAction");
    final int damageAmount = harvestAmount * this.damagePerStack;

    if (harvestAmount > 0) {
      AbstractDungeon.actionManager.addToTop(
            new DamageAllEnemiesAction(getPlayer(),
                DamageInfo.createDamageMatrix(damageAmount, true,false),
                DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, true));

      AbstractDungeon.actionManager.addToTop(new BurnAllEnemiesAction(damageAmount));
    }
    return harvestAmount;
  }
}