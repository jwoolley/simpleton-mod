package thesimpleton.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.orbs.AbstractCropOrb;
import thesimpleton.orbs.CornCropOrb;
import thesimpleton.relics.Raygun;

public class RaygunAction extends AbstractGameAction {
  private static final Color PALE_GREEN = new Color(.75F, 1.0F, .75F, 1.0F);
  private static float ACTION_DURATION = Settings.ACTION_DUR_FAST;
  private final AbstractCreature owner;
  private final Raygun raygunRelic;
  private final DamageInfo info;
  private final int damageAmount;
  private int actionCounter;
  private Logger logger;

  public RaygunAction(Raygun raygunRelic, AbstractCreature owner, AbstractMonster target, int damageAmount) {
    this.logger = TheSimpletonMod.logger;
    this.actionType = ActionType.DAMAGE;
    this.duration = ACTION_DURATION;

    this.owner = owner;
    this.damageAmount = damageAmount;
    this.actionCounter = 54;
    this.raygunRelic = raygunRelic;
    this.info = new DamageInfo(this.source, this.damageAmount, DamageInfo.DamageType.THORNS);
  }

  @Override
  public void update() {
    if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
      AbstractDungeon.actionManager.clearPostCombatActions();
      this.isDone = true;
      return;
    }

    this.duration -= Gdx.graphics.getDeltaTime();

    if (this.duration < 0.0F) {
      if (actionCounter > 0) {
        actionCounter--;
      } else {
        this.isDone = true;
        return;
      }

      if (Settings.FAST_MODE) {
        // do all the stuff quickly
      }

      if (actionCounter == 53) {
        AbstractDungeon.effectsQueue.add(new RoomTintEffect(Color.BLACK.cpy(), 0.9F));
      } else if (actionCounter == 50) {
        AbstractDungeon.actionManager.addToTop(new SFXAction("LASER_BEAM_1"));
      } else if (actionCounter < 40) {
        AbstractDungeon.effectsQueue.add(new RoomTintEffect(PALE_GREEN, 0.99F));
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.WHITE.cpy(), false));

        AbstractDungeon.actionManager.addToBottom(
            new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(this.damageAmount, true),
                DamageInfo.DamageType.THORNS, AttackEffect.SLASH_HORIZONTAL, true));

        this.raygunRelic.clearCharges();
        this.isDone = true;
      }
    }
  }
}
