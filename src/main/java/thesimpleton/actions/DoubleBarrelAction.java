package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.WeakPower;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;

public class DoubleBarrelAction extends AbstractGameAction {

  private final int damage;
  private final int energyAmount;

  public DoubleBarrelAction(AbstractCreature source, AbstractCreature target, AttackEffect attackEffect, int damage, int energyAmount) {
    this.source = source;
    this.target = target;
    this.damage = damage;
    this.actionType = ActionType.ENERGY;
    this.attackEffect = attackEffect;
    this.energyAmount = energyAmount;
  }

  @Override
  public void update() {
    Logger logger = TheSimpletonMod.logger;

    if (target.hasPower(WeakPower.POWER_ID)) {
      AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));

      AbstractDungeon.actionManager.addToBottom(
          new DamageAction(target, new DamageInfo(source, this.damage, DamageType.NORMAL),
             this.attackEffect));

      AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.energyAmount));
    }

    this.isDone = true;
  }
}