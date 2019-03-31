package thesimpleton.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.*;

public class HaymakerSplashAction extends AbstractGameAction {
  private final int splashDamageAmount;
  private final AbstractPlayer p;
  private DamageInfo info;

  public HaymakerSplashAction(AbstractPlayer p, AbstractCreature target, DamageInfo info, int splashDamageAmount) {
    this.setValues(target, info);
    this.actionType = ActionType.DEBUFF;
    this.attackEffect = AttackEffect.BLUNT_HEAVY;
    this.damageType = DamageInfo.DamageType.NORMAL;
    this.duration = 0.1F;
    this.info = info;
    this.p = p;
    this.splashDamageAmount = splashDamageAmount;
  }

  @Override
  public void update() {

    if (this.duration == 0.1F && this.target != null) {
      this.target.damage(this.info);

      AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
      AbstractDungeon.effectList.add(new DamageImpactCurvyEffect(this.target.hb.cX, this.target.hb.cY, Color.GOLDENROD, false));

      if ((((AbstractMonster) this.target).isDying || this.target.currentHealth <= 0)
          && !this.target.halfDead) {
        AbstractDungeon.actionManager.addToBottom(
            new DamageAllEnemiesAction(this.p, DamageInfo.createDamageMatrix(this.splashDamageAmount, true), this.damageType, this.attackEffect));
      }

      if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
        AbstractDungeon.actionManager.clearPostCombatActions();
      }
    }

    this.tickDuration();
  }

}