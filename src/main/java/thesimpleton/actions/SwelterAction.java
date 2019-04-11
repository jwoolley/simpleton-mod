package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.skill.Harvest;
import thesimpleton.powers.AbstractCropPower;
import thesimpleton.powers.BurningPower;
import thesimpleton.powers.unused.BleedPower;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SwelterAction extends AbstractGameAction {

  private final int[] attackDamage;

  public SwelterAction(AbstractCreature source, int[] attackDamage, int burningAmount) {
    this.actionType = ActionType.DEBUFF;
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

    for (int i = 0; i < monsters.size(); i++) {
      AbstractMonster m = monsters.get(i);
      if (m.currentBlock < attackDamage[i]) {
        damagedMonsters.add(m);
      }
    }

    return damagedMonsters;
  }

  private void applyBurning(AbstractCreature target) {

  }
}