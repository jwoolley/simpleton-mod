package thesimpleton.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import thesimpleton.cards.SimpletonUtil;

public class DamageAllCharactersAction
    extends AbstractGameAction {
  public int[] damage;
  private boolean firstFrame = true;

  public DamageAllCharactersAction(AbstractCreature source, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect, boolean isFast) {
    setValues(null, source, amount[0]);
    this.damage = amount;
    this.actionType = AbstractGameAction.ActionType.DAMAGE;
    this.damageType = type;
    this.attackEffect = effect;
    if (isFast) {
      this.duration = Settings.ACTION_DUR_XFAST;
    } else {
      this.duration = Settings.ACTION_DUR_FAST;
    }
  }

  public DamageAllCharactersAction(AbstractCreature source, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect) {
    this(source, amount, type, effect, false);
  }

  public void update() {
    boolean playedMusic;
    AbstractPlayer player = SimpletonUtil.getPlayer();
    if (this.firstFrame) {
      playedMusic = false;
      int temp = AbstractDungeon.getCurrRoom().monsters.monsters.size();
      for (int i = 0; i < temp; i++) {
        if ((!(AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).isDying) &&
            ((AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).currentHealth > 0) &&
            (!(AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).isEscaping)) {
          if (playedMusic) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(

                (AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).hb.cX,
                (AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).hb.cY, this.attackEffect, true));
          } else {
            playedMusic = true;
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(
                (AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).hb.cX,
                (AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).hb.cY, this.attackEffect));
          }
        }
      }
      AbstractDungeon.effectList.add(new FlashAtkImgEffect(player.hb.cX, player.hb.cY, this.attackEffect, true));
      this.firstFrame = false;
    }
    tickDuration();
    if (this.isDone) {
      for (AbstractPower p : AbstractDungeon.player.powers) {
        p.onDamageAllEnemies(this.damage);
      }
      int temp = AbstractDungeon.getCurrRoom().monsters.monsters.size();
      for (int i = 0; i < temp; i++) {
        if (!(AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).isDeadOrEscaped()) {
          if (this.attackEffect == AbstractGameAction.AttackEffect.POISON) {
            (AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).tint.color = Color.CHARTREUSE.cpy();
            (AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).tint.changeColor(Color.WHITE.cpy());
          } else if (this.attackEffect == AbstractGameAction.AttackEffect.FIRE) {
            (AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).tint.color = Color.RED.cpy();
            (AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).tint.changeColor(Color.WHITE.cpy());
          }
          (AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).damage(new DamageInfo(this.source, this.damage[i], this.damageType));
        }
      }
      player.damage(new DamageInfo(this.source, this.damage[0], this.damageType));
      if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
        AbstractDungeon.actionManager.clearPostCombatActions();
      }
      if (!Settings.FAST_MODE) {
        AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));
      }
    }
  }
}
