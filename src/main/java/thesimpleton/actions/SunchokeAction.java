package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.powers.BurningPower;
import thesimpleton.utilities.ModLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SunchokeAction extends AbstractGameAction {
  private static ModLogger logger = TheSimpletonMod.traceLogger;

  private final int[] attackDamage;

  public SunchokeAction(AbstractCreature source, DamageType damageType, int[] attackDamage, int burningAmount) {
    this.actionType = ActionType.DEBUFF;
    this.damageType = damageType;
    this.source = source;
    this.attackDamage = attackDamage;
    this.amount = burningAmount;
  }

  @Override
  public void update() {
    List<AbstractMonster> monsters = SimpletonUtil.getMonsters();
    logger.trace("SunchokeAction.update :: total monsters: " + monsters.size());

    final List<AbstractMonster> weakMonsters = SimpletonUtil.getMonsters().stream()
        .filter(m -> m.hasPower(WeakPower.POWER_ID))
        .collect(Collectors.toList());

    logger.trace("SunchokeAction.update :: weak monsters: " + weakMonsters.size());

    final List<AbstractMonster> damagedMonsters = getDamagedMonsters(weakMonsters);
    logger.trace("SunchokeAction.update :: damaged monsters: " + damagedMonsters.size());

    AbstractDungeon.actionManager.addToBottom(
        new DamageAllEnemiesAction(this.source, this.attackDamage, this.damageType,
            AbstractGameAction.AttackEffect.FIRE));

    AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));

    damagedMonsters.stream().forEach(m -> {
      AbstractDungeon.actionManager.addToBottom(
          new ApplyBurningAction(m, this.source, this.amount, false, true));


      if ((!m.isDying) && (m.currentHealth > 0) && (!m.isEscaping)) {
        AbstractDungeon.effectList.add(new FlashAtkImgEffect(
            m.hb.cX, m.hb.cY, AttackEffect.NONE, true));
      }
    });

    this.isDone = true;
  }

  private List<AbstractMonster> getDamagedMonsters(List<AbstractMonster> monsters) {
    List<AbstractMonster> damagedMonsters = new ArrayList<>();

    for (int i = 0; i < monsters.size(); i++) {
      AbstractMonster m = monsters.get(i);

      logger.trace("SunchokeAction.getDamagedMonsters :: currentBlock: " + m.currentBlock + " attackDamage: " + attackDamage[i]);

      if (m.currentBlock < attackDamage[i]) {
        logger.trace("SunchokeAction.getDamagedMonsters :: added to damagedMonsters");

        damagedMonsters.add(m);
      }
    }

    return damagedMonsters;
  }
}