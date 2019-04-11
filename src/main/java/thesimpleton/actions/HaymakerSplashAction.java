package thesimpleton.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.*;
import thesimpleton.cards.SimpletonUtil;

public class HaymakerSplashAction extends AbstractGameAction {
  private final int healAmount;
  private final int vulnerableAmount;
  private final AbstractPlayer p;
  private DamageInfo info;

  public HaymakerSplashAction(AbstractPlayer p, AbstractCreature target, DamageInfo info, int healAmount, int vulnerableAmount) {
    this.setValues(target, info);
    this.actionType = ActionType.DEBUFF;
    this.attackEffect = AttackEffect.BLUNT_HEAVY;
    this.damageType = DamageInfo.DamageType.NORMAL;
    this.duration = 0.1F;
    this.info = info;
    this.p = p;
    this.healAmount = healAmount;
    this.vulnerableAmount = vulnerableAmount;
  }

  @Override
  public void update() {
    if (this.duration == 0.1F && this.target != null) {
      this.target.damage(this.info);

      AbstractDungeon.actionManager.addToBottom(
          new HealAction(p, p, this.healAmount)
      );

      AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
      AbstractDungeon.effectList.add(new DamageImpactCurvyEffect(this.target.hb.cX, this.target.hb.cY, Color.GOLDENROD, false));

      SimpletonUtil.getMonsters().forEach(m -> this.applyVulnerablePower(p, m, this.vulnerableAmount));

      if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
        AbstractDungeon.actionManager.clearPostCombatActions();
      }
    }

    this.tickDuration();
  }

  // TODO: move into separate class
  private void applyVulnerablePower(AbstractPlayer p, AbstractMonster m, int vulnerableAmount) {
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(m, p, new VulnerablePower(m, vulnerableAmount, false),vulnerableAmount));
  }

}