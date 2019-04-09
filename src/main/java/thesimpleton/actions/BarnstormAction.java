package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.powers.AbstractCropPower;

public class BarnstormAction extends AbstractGameAction {
  private static float ACTION_DURATION = Settings.ACTION_DUR_XFAST;
  private final AbstractPlayer player;
  private final boolean damageAllEnemies;
  private final int baseDamage;

  public BarnstormAction(AbstractPlayer player, int baseDamage, boolean damageAllEnemies) {
    this.actionType = ActionType.DEBUFF;
    this.attackEffect = AttackEffect.BLUNT_HEAVY;
    this.damageType = DamageInfo.DamageType.NORMAL;
    this.duration = ACTION_DURATION;

    this.player = player;
    this.baseDamage = baseDamage;
    this.damageAllEnemies = damageAllEnemies;
  }

  @Override
  public void update() {
    if (this.duration != ACTION_DURATION) {
      for (AbstractCropPower pow :AbstractCropPower.getActiveCropPowers(this.player, true)) {
        final boolean isCropMature =  pow.amount >= pow.getMaturityThreshold();
        final int damagePerStack = isCropMature ? 2 * this.baseDamage : this.baseDamage;

        pow.flash();

        if (this.damageAllEnemies) {
          for (int i = 0; i < pow.amount; i++) {

            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
              AbstractDungeon.actionManager.addToBottom(
                  new VFXAction(player, new LightningEffect(monster.hb.cX, monster.hb.y), 0.1F));
            }

            AbstractDungeon.actionManager.addToBottom(new SFXAction("THUNDERCLAP", 0.075f));
            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(this.player, DamageInfo.createDamageMatrix(damagePerStack),
                DamageInfo.DamageType.NORMAL, AttackEffect.NONE, isCropMature));
          }
        } else {
          for (int i = 0; i < pow.amount; i++) {

            AbstractMonster monster = SimpletonUtil.getRandomMonster();
            AbstractDungeon.actionManager.addToBottom(
                new VFXAction(player, new LightningEffect(monster.hb.cX, monster.hb.y), 0.1F));
            AbstractDungeon.actionManager.addToBottom(new SFXAction("THUNDERCLAP", 0.025f));
            AbstractDungeon.actionManager.addToBottom(
                new DamageAction(monster, new DamageInfo(this.player, damagePerStack), AttackEffect.NONE, isCropMature));
          }
        }
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3F));
      }
      this.tickDuration();
      this.isDone = true;
    }
    this.tickDuration();
  }
}
