package thesimpleton.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.AbstractCrop;
import thesimpleton.orbs.AbstractCropOrb;

import java.util.List;
import java.util.stream.Collectors;

public class SaladShooterAction extends AbstractGameAction {
  private static float ACTION_DURATION = Settings.ACTION_DUR_XFAST;
  private final AbstractPlayer player;
  private final int baseDamage;

  private int numRepetitions;
  private final DamageInfo info;

  private Logger logger;

  public SaladShooterAction(AbstractPlayer player, AbstractCreature target, int baseDamage, int numRepetitions) {
    this.logger = TheSimpletonMod.logger;
    this.actionType = ActionType.DAMAGE;
    this.attackEffect = AttackEffect.SLASH_VERTICAL;
    this.damageType = DamageInfo.DamageType.NORMAL;
    this.duration = ACTION_DURATION;

    this.player = player;
    this.baseDamage = baseDamage;
    this.target = target;
    this.numRepetitions = numRepetitions;

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

    if (this.duration < 0.0F)
    {
      if (this.target.currentHealth > 0)
      {
        this.info.base = baseDamage;

        if (numRepetitions > 0) {
          numRepetitions--;
        }

        this.info.applyPowers(this.info.owner, this.target);

        AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_FIRE", 0.025f));

          AbstractDungeon.actionManager.addToBottom(
              new VFXAction(player, new ThrowDaggerEffect(target.hb.cX, target.hb.y), 0.1F));
          AbstractDungeon.actionManager.addToBottom(
              new DamageAction(target, new DamageInfo(this.player, this.info.base), AttackEffect.NONE));
        }

      if ((this.numRepetitions > 0) && (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())) {
        AbstractDungeon.actionManager.addToBottom(new SaladShooterAction(
            this.player,
            AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng),
            this.baseDamage , this.numRepetitions));
      }
      this.isDone = true;
    }
  }
}
