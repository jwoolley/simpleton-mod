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
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.effects.BuzzBombImpactEffect;
import thesimpleton.orbs.CoffeeCropOrb;

public class BuzzBombAction extends AbstractGameAction {
  private static float ACTION_DURATION = Settings.ACTION_DUR_FAST;

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

    Logger logger = TheSimpletonMod.logger;

    logger.info("BuzzBombAction::update called. duration: " + this.duration + "; numRepetitions: " + numRepetitions);

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
      logger.info("BuzzBombAction::update : times's up!");


      if (this.target.currentHealth > 0) {
        this.info.base = baseDamage;

        logger.info("BuzzBombAction::update : target is alive");


        if (numRepetitions > 0) {
          numRepetitions--;

          logger.info("BuzzBombAction::update : queueing Impact Effect");

          AbstractDungeon.actionManager.addToBottom(
              new VFXAction(new BuzzBombImpactEffect(target.hb.x, target.hb.cY, numRepetitions == 0)));

          if (!Settings.FAST_MODE) {
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.25F));
          }

          logger.info("BuzzBombAction::update : queueing SFX");

          AbstractDungeon.actionManager.addToBottom(
              new SFXAction(numRepetitions > 0 ? "ATTACK_FIRE_IMPACT_1" : "ATTACK_FIRE_IMPACT_2"));

          logger.info("BuzzBombAction::update : applying attack and plant action");

          this.info.applyPowers(this.info.owner, this.target);
          AbstractDungeon.actionManager.addToBottom(
              new BuzzBombDamageAndPlantAction(this.source, this.target, this.info, this.numStacksPerKill));

          if ((this.numRepetitions > 0) && (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())) {
            logger.info("BuzzBombAction::update : queueing subsequent BuzzBombAction with numRepetitions " + numRepetitions);

            AbstractDungeon.actionManager.addToBottom(new BuzzBombAction(this.player, SimpletonUtil.getRandomMonster(),
                this.baseDamage , this.numRepetitions, this.numStacksPerKill));
          }
        }
        this.isDone = true;
      }
    }
  }
}
