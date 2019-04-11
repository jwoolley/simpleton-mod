package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SwelterAction extends AbstractGameAction {

  private final int[] attackDamage;

  public SwelterAction(AbstractCreature source, DamageType damageType, int[] attackDamage, int burningAmount) {
    this.actionType = ActionType.DEBUFF;
    this.damageType = damageType;
    this.source = source;
    this.attackDamage = attackDamage;
    this.amount = burningAmount;
  }

  @Override
  public void update() {
    Logger logger = TheSimpletonMod.logger;
    List<AbstractMonster> monsters = SimpletonUtil.getMonsters();
    logger.debug("SwelterAction.update :: total monsters: " + monsters.size());

    final List<AbstractMonster> weakMonsters = SimpletonUtil.getMonsters().stream()
        .filter(m -> m.hasPower(WeakPower.POWER_ID))
        .collect(Collectors.toList());

    logger.debug("SwelterAction.update :: weak monsters: " + weakMonsters.size());

    final List<AbstractMonster> damagedMonsters = getDamagedMonsters(weakMonsters);
    logger.debug("SwelterAction.update :: damaged monsters: " + damagedMonsters.size());

    AbstractDungeon.actionManager.addToBottom(
        new DamageAllEnemiesAction(this.source, this.attackDamage, this.damageType,
            AbstractGameAction.AttackEffect.FIRE));

    AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));

    damagedMonsters.stream().forEach(m -> {
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(m, source, new BurningPower(m, this.source, this.amount), this.amount));

      if ((!m.isDying) && (m.currentHealth > 0) && (!m.isEscaping)) {
        AbstractDungeon.effectList.add(new FlashAtkImgEffect(
            m.hb.cX, m.hb.cY, AttackEffect.NONE, true));
      }
    });

    this.isDone = true;
  }

  private List<AbstractMonster> getDamagedMonsters(List<AbstractMonster> monsters) {
    List<AbstractMonster> damagedMonsters = new ArrayList<>();

    Logger logger = TheSimpletonMod.logger;

    for (int i = 0; i < monsters.size(); i++) {
      AbstractMonster m = monsters.get(i);

      logger.debug("SwelterAction.getDamagedMonsters :: currentBlock: " + m.currentBlock + " attackDamage: " + attackDamage[i]);

      if (m.currentBlock < attackDamage[i]) {
        logger.debug("SwelterAction.getDamagedMonsters :: added to damagedMonsters");

        damagedMonsters.add(m);
      }
    }

    return damagedMonsters;
  }
}