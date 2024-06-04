package thesimpleton.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.effects.BuzzBombImpactEffect;
import thesimpleton.effects.BuzzBombPlaneSilhouetteEffect;

public class BuzzBombAction extends AbstractGameAction {
  private static float ACTION_DURATION = Settings.ACTION_DUR_FAST;

  private final AbstractPlayer player;
  private final int baseDamage;
  private final int numStacksPerKill;
  private final DamageInfo info;

  private int numRepetitions;

  private boolean isFirstTick;

  public BuzzBombAction(AbstractPlayer player, AbstractCreature target, int baseDamage, int numRepetitions, int numStacksPerKill) {
    this(player, target, baseDamage, numRepetitions, numStacksPerKill, true);
  }

  private BuzzBombAction(AbstractPlayer player, AbstractCreature target, int baseDamage, int numRepetitions, int numStacksPerKill,
    boolean isFirstTick) {
    this.actionType = AbstractGameAction.ActionType.DAMAGE;
    this.attackEffect = AbstractGameAction.AttackEffect.SLASH_VERTICAL;
    this.damageType = DamageInfo.DamageType.NORMAL;
    this.duration = this.startDuration = ACTION_DURATION;

    this.player = player;
    this.baseDamage = baseDamage;
    this.target = target;
    this.numRepetitions = numRepetitions;
    this.numStacksPerKill = numStacksPerKill;
    this.isFirstTick = isFirstTick;

    this.info = new DamageInfo(this.player, this.baseDamage, DamageInfo.DamageType.NORMAL);
  }

  @Override
  public void update() {
    if (this.target == null) {
      this.isDone = true;
      return;
    } else if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
      AbstractDungeon.actionManager.clearPostCombatActions();
      this.isDone = true;
      return;
    }

    if (this.duration == this.startDuration && this.isFirstTick) {
      float PLANE_START_X = 0.0F;
      float PLANE_START_Y = Settings.HEIGHT / 2.0F;
      float PLANE_END_X = Settings.WIDTH * Settings.xScale;
      float PLANE_END_Y =  PLANE_START_Y;
      float PLANE_ACTION_DURATION = 1.75F;

      AbstractDungeon.effectList.add(new BuzzBombPlaneSilhouetteEffect(PLANE_START_X, PLANE_START_Y, PLANE_END_X, PLANE_END_Y, PLANE_ACTION_DURATION));
    }

    this.duration -= Gdx.graphics.getDeltaTime();

    if (this.duration < 0.0F) {
      if (this.target.currentHealth > 0) {
        this.info.base = baseDamage;

        if (numRepetitions > 0) {
          numRepetitions--;

          AbstractDungeon.actionManager.addToBottom(
              new VFXAction(new BuzzBombImpactEffect(target.hb.x, target.hb.cY, numRepetitions == 0)));

//          if (!Settings.FAST_MODE) {
//            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.25F));
//          }

          AbstractDungeon.actionManager.addToBottom(
              new SFXAction(numRepetitions > 0 ? "ATTACK_FIRE_IMPACT_1" : "ATTACK_FIRE_IMPACT_2"));

//          this.info.applyPowers(this.info.owner, this.target);
          AbstractDungeon.actionManager.addToBottom(
              new BuzzBombDamageAndPlantAction(this.source, this.target, this.info, this.numStacksPerKill));
        }
      }
      if ((this.numRepetitions > 0) && (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())) {
        AbstractDungeon.actionManager.addToBottom(new BuzzBombAction(this.player, SimpletonUtil.getRandomMonster(),
            this.baseDamage , this.numRepetitions, this.numStacksPerKill, false));
      }
      this.isDone = true;
    }
  }
}
