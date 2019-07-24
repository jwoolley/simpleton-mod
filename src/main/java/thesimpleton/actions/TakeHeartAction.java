package thesimpleton.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.AdrenalineEffect;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.effects.BuzzBombImpactEffect;
import thesimpleton.orbs.ArtichokeCropOrb;
import thesimpleton.orbs.CoffeeCropOrb;

public class TakeHeartAction extends AbstractGameAction {
  private static float ACTION_DURATION = Settings.ACTION_DUR_FAST;
  private final int blockAmount;
  private final int healAmount;
  private final int plantAmount;
  private int numRepetitions;

  private Logger logger;

  public TakeHeartAction(AbstractCreature target, int blockAmount, int healAmount, int plantAmount, int numRepetitions) {
    this.logger = TheSimpletonMod.logger;
    this.actionType = ActionType.HEAL;
    this.duration = ACTION_DURATION;

    this.target = target;
    this.blockAmount = blockAmount;
    this.healAmount = healAmount;
    this.plantAmount = plantAmount;
    this.numRepetitions = numRepetitions;
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
      if (numRepetitions > 0) {
        numRepetitions--;
      }

//      AbstractDungeon.actionManager.addToBottom(new VFXAction(new AdrenalineEffect()));

      if (!Settings.FAST_MODE) {
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.05F));
      }
      AbstractDungeon.actionManager.addToBottom(
          new SFXAction(numRepetitions > 0 ? "HEART_BEAT" : "HEART_SIMPLE"));

      AbstractDungeon.effectsQueue.add(
          new BorderFlashEffect(numRepetitions > 0 ? Color.MAROON.cpy() : Color.MAROON.cpy(), true));

      AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this.target, this.target, blockAmount));
      AbstractDungeon.actionManager.addToBottom(new HealAction(this.target, this.target, healAmount));
      AbstractDungeon.actionManager.addToBottom(new CropSpawnAction(new ArtichokeCropOrb(plantAmount),true));

      if ((this.numRepetitions > 0) && (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())) {
        AbstractDungeon.actionManager.addToBottom(new TakeHeartAction(
            this.target, this.blockAmount, this.healAmount, this.plantAmount, this.numRepetitions));
      }
      this.isDone = true;
    }
  }
}
