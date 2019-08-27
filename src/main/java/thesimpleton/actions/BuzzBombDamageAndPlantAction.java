package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.orbs.CoffeeCropOrb;

public class BuzzBombDamageAndPlantAction extends AbstractGameAction {
  private static final float ACTION_DURATION = Settings.ACTION_DUR_FAST;
  private final DamageInfo damageInfo;

  public BuzzBombDamageAndPlantAction(AbstractCreature source, AbstractCreature target, DamageInfo damageInfo,
                                      int numStacksPerKill) {
    this.source = source;
    this.target = target;
    this.damageInfo = damageInfo;
    this.amount = numStacksPerKill;
  }

  @Override
  public void update() {
    if (this.duration != ACTION_DURATION) {
      this.target.damage(this.damageInfo);

      if ((((AbstractMonster) this.target).isDying) || (this.target.currentHealth <= 0)) {
        AbstractDungeon.actionManager.addToBottom(
            new CropSpawnAction(new CoffeeCropOrb(), this.amount,true));
      }

      this.isDone = true;
    }
    this.tickDuration();
  }
}