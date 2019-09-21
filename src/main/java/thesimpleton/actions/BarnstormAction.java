package thesimpleton.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.AbstractCrop;
import thesimpleton.orbs.AbstractCropOrb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BarnstormAction extends AbstractGameAction {
  private static float ACTION_DURATION = Settings.ACTION_DUR_FASTER;

  private final AbstractPlayer player;
  private final int baseDamage;
  private final int stunThreshold;
  private boolean isFirstTick;

  private final List<CropCount> cropStacks;
  private final Map<AbstractMonster, Integer> damagedEnemies;
  private final DamageInfo info;

  private Logger logger;

  public BarnstormAction(AbstractPlayer player, AbstractMonster target, int baseDamage, int stunThreshold) {
    this(player, target, baseDamage, stunThreshold, getCropCounts(player), new HashMap<>());
  }

  static class CropCount {
    public CropCount(AbstractCropOrb crop, int amount) {
      this.crop = crop;
      this.amount = amount;
    }

    final AbstractCropOrb crop;
    int amount;
  }

  public BarnstormAction(AbstractPlayer player, AbstractCreature target, int baseDamage, int stunThreshold,
                         List<CropCount> cropStacks, Map<AbstractMonster, Integer> damagedEnemies) {
    this.logger = TheSimpletonMod.logger;
    this.actionType = ActionType.DAMAGE;
    this.attackEffect = AttackEffect.SLASH_VERTICAL;
    this.damageType = DamageInfo.DamageType.NORMAL;
    this.duration = ACTION_DURATION;

    this.player = player;
    this.baseDamage = baseDamage;
    this.target = target;

    this.cropStacks = cropStacks;

    this.info = new DamageInfo(this.player, this.baseDamage, DamageInfo.DamageType.NORMAL);

    this.damagedEnemies = damagedEnemies;
    this.stunThreshold = stunThreshold;
    this.isFirstTick = true;

    if (target != null && !cropStacks.isEmpty() && this.target.currentHealth > 0) {
      makeLightningEffect();
    }
  }

  @Override
  public void update() {
    if (this.target == null || cropStacks.isEmpty()) {
      this.isDone = true;
      return;
    } else if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
      AbstractDungeon.actionManager.clearPostCombatActions();
      this.isDone = true;
      return;
    }

    AbstractDungeon.effectsQueue.add(
        new RoomTintEffect(Color.BLACK.cpy(), 0.9F, 0.8F, true));

    this.duration -= Gdx.graphics.getDeltaTime();

      AbstractMonster monsterTarget = (AbstractMonster) this.target;

    if (this.duration < Settings.ACTION_DUR_XFAST) {
      if (this.isFirstTick) {
        // logger.info("BarnstormAction::update damage tick. this.duration: " + this.duration);

        if (this.target.currentHealth > 0) {
          this.info.applyPowers(this.info.owner, this.target);
          monsterTarget.damage(new DamageInfo(this.player, this.info.base));
        }
        this.isFirstTick = false;
      } else if (this.duration < 0.0F) {
        // logger.info("BarnstormAction::update retrigger/calulate damage tick. this.duration: " + this.duration);

        CropCount cropCount = cropStacks.get(0);
        this.info.base = baseDamage;

        if (cropCount.amount > 1) {
          cropCount.amount--;
        } else {
          cropStacks.remove(cropCount);
        }

        // logger.info("BarnstormAction::update damaging target: " + target.id + "; lastDamageTaken: " + monsterTarget.lastDamageTaken);

        if (monsterTarget.lastDamageTaken > 0) {
          final int numTimesDamaged = damagedEnemies.containsKey(monsterTarget) ?
              damagedEnemies.get(monsterTarget) + 1 : 1;
            damagedEnemies.put(monsterTarget, numTimesDamaged);
        }
        queueNewBarnstormTick();
        this.isDone = true;
      }
    }
  }

  private void queueNewBarnstormTick() {
    if ((this.cropStacks.size() > 0) && (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())) {
      AbstractDungeon.actionManager.addToBottom(new BarnstormAction(
          this.player,
          AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng),
          this.baseDamage, this.stunThreshold,  this.cropStacks, this.damagedEnemies));
    } else {
      applyPostDamageStuns();
    }
  }

  private void makeLightningEffect() {
    // TODO: "flash" the crop orb
    AbstractDungeon.actionManager.addToBottom(new SFXAction("THUNDERCLAP", 0.025f));

    AbstractDungeon.actionManager.addToBottom(
        new VFXAction(player, new LightningEffect(target.hb.cX, target.hb.y), 0.1F));

  }

  private void applyPostDamageStuns() {
//    logger.info("BarnstormAction::applyPostDamageStuns called. Final tally:");

    for (Map.Entry<AbstractMonster, Integer> e : this.damagedEnemies.entrySet()) {
      logger.info("\t" + e.getKey() + ": " + e.getValue());
    }

    this.damagedEnemies.keySet().stream().filter(m -> damagedEnemies.get(m) >= this.stunThreshold && !m.isDeadOrEscaped())
        .forEach(m ->  AbstractDungeon.actionManager.addToBottom(new StunMonsterAction(m,  this.source)));
  }

  static List<CropCount> getCropCounts(AbstractPlayer player) {
    return AbstractCrop.getActiveCropOrbs(false).stream()
        .map(cropOrb -> new CropCount(cropOrb, cropOrb.passiveAmount))
        .collect(Collectors.toList());
  }
}