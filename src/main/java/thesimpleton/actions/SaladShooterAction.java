package thesimpleton.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.effects.utils.VFXActionTemplate;
import thesimpleton.utilities.ModLogger;

public class SaladShooterAction extends AbstractGameAction {
  private static ModLogger logger =TheSimpletonMod.traceLogger;
  private static float ACTION_DURATION = Settings.ACTION_DUR_XFAST;
  private final AbstractPlayer player;
  private final int baseDamage;

  private int numRepetitions;
  private final DamageInfo info;
  private final VFXActionTemplate vfxTemplate;
  private final SFXAction sfxAction;

  public SaladShooterAction(AbstractPlayer player, AbstractCreature target, int baseDamage, int numRepetitions,
                            VFXActionTemplate vfxTemplate, SFXAction sfxAction) {
    this.logger = TheSimpletonMod.traceLogger;
    this.actionType = ActionType.DAMAGE;
    this.damageType = DamageInfo.DamageType.NORMAL;
    this.duration = ACTION_DURATION;

    this.player = player;
    this.baseDamage = baseDamage;
    this.target = target;
    this.numRepetitions = numRepetitions;

    this.vfxTemplate = vfxTemplate;
    this.sfxAction = sfxAction;

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

        AbstractDungeon.actionManager.addToBottom(this.sfxAction);

          AbstractDungeon.actionManager.addToBottom(this.vfxTemplate.getAction(target.hb.x, target.hb.y));
          AbstractDungeon.actionManager.addToBottom(
              new DamageAction(target, new DamageInfo(this.player, this.info.base), AttackEffect.NONE));
        }

      if ((this.numRepetitions > 0) && (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())) {
        AbstractDungeon.actionManager.addToBottom(new SaladShooterAction(
            this.player,
            AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng),
            this.baseDamage , this.numRepetitions, this.vfxTemplate, this.sfxAction));
      }
      this.isDone = true;
    }
  }
}
