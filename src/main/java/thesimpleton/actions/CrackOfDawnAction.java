package thesimpleton.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import thesimpleton.orbs.AbstractCropOrb;
import thesimpleton.orbs.CornCropOrb;

public class CrackOfDawnAction extends AbstractGameAction {
  private static final Color PALE_YELLOW = new Color(1.0F, 1.0F, .75F, 1.0F);
  private static float ACTION_DURATION = Settings.ACTION_DUR_FAST;
  private final AbstractCreature owner;
  private final AbstractCropOrb cropOrb;
  private final int damageAmount;
  private final int energyAmount;
  private final int plantAmount;
  private int actionCounter;

  public CrackOfDawnAction(AbstractCreature owner, AbstractCropOrb cropOrb, int damageAmount, int plantAmount, int energyAmount) {
    this.actionType = ActionType.DAMAGE;
    this.duration = ACTION_DURATION;

    this.owner = owner;
    this.cropOrb = cropOrb;
    this.damageAmount = damageAmount;
    this.energyAmount = energyAmount;
    this.plantAmount = plantAmount;
    this.actionCounter = 54;
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
        AbstractDungeon.actionManager.addToTop(new SFXAction("ROOSTER_CROW_1"));
      } else if (actionCounter < 20) {
        AbstractDungeon.effectsQueue.add(new RoomTintEffect(PALE_YELLOW, 0.99F));
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.WHITE.cpy(), false));
        AbstractDungeon.actionManager.addToBottom(
            new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(this.damageAmount, true),
            DamageInfo.DamageType.THORNS, AttackEffect.SLASH_VERTICAL, true));
        AbstractDungeon.actionManager.addToBottom(new CropSpawnAction(new CornCropOrb(), this.plantAmount, true));
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.energyAmount));
        this.isDone = true;
      }
    }
  }
}
