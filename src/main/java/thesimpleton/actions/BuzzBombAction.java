package thesimpleton.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.effects.BuzzBombImpactEffect;
import thesimpleton.orbs.CoffeeCropOrb;

public class BuzzBombAction extends AbstractGameAction {
  private static float ACTION_DURATION = Settings.ACTION_DUR_XFAST;

  private final AbstractPlayer player;
  private final int baseDamage;
  private final int numStacksPerKill;
  private final DamageInfo info;

  private int numRepetitions;

  private Logger logger;

  public BuzzBombAction(AbstractPlayer player, AbstractCreature target, int baseDamage, int numRepetitions, int numStacksPerKill) {
    this.logger = TheSimpletonMod.logger;
    this.actionType = AbstractGameAction.ActionType.DAMAGE;
    this.attackEffect = AbstractGameAction.AttackEffect.SLASH_VERTICAL;
    this.damageType = DamageInfo.DamageType.NORMAL;
    this.duration = ACTION_DURATION;

    this.player = player;
    this.baseDamage = baseDamage;
    this.target = target;
    this.numRepetitions = numRepetitions;
    this.numStacksPerKill = numStacksPerKill;

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

    this.duration -= Gdx.graphics.getDeltaTime();

    if (this.duration < 0.0F) {
      if (this.target.currentHealth > 0) {
        this.info.base = baseDamage;

        if (numRepetitions > 0) {
          numRepetitions--;
//          AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_FIRE_IMPACT_1"));
        } else {
//          AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_FIRE_IMPACT_2"));
        }


        AbstractDungeon.actionManager.addToBottom(
            new VFXAction(new BuzzBombImpactEffect(target.hb.x, target.hb.cY, numRepetitions == 0)));

        if (!Settings.FAST_MODE) {
          AbstractDungeon.actionManager.addToBottom(new WaitAction(0.25F));
        }
        AbstractDungeon.actionManager.addToBottom(
            new SFXAction(numRepetitions > 0 ? "ATTACK_FIRE_IMPACT_1" : "ATTACK_FIRE_IMPACT_2"));

        this.info.applyPowers(this.info.owner, this.target);
        AbstractDungeon.actionManager.addToBottom(
            new BuzzBombDamageAndPlantAction(this.source, this.target, this.info, this.numStacksPerKill));
//
//        this.target.damage(this.info);
//
////        AbstractDungeon.actionManager.addToBottom(
////            new DamageAction(target, new DamageInfo(this.player, this.info.base), AbstractGameAction.AttackEffect.NONE));
//
//        if ((((AbstractMonster)this.target).isDying) || (this.target.currentHealth <= 0)) {
//          AbstractDungeon.actionManager.addToBottom(new CropSpawnAction(new CoffeeCropOrb(1),true));
//        }
      }

      if ((this.numRepetitions > 0) && (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())) {
//        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.5F));

        AbstractDungeon.actionManager.addToBottom(new BuzzBombAction(
            this.player,
            AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng),
            this.baseDamage , this.numRepetitions, this.numStacksPerKill));
      }
      this.isDone = true;
    }
  }
}
