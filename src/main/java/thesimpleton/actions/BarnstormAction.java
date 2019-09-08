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
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.AbstractCrop;
import thesimpleton.orbs.AbstractCropOrb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BarnstormAction extends AbstractGameAction {
  private static float ACTION_DURATION = Settings.ACTION_DUR_XFAST;
  private final AbstractPlayer player;
  private final int baseDamage;

  private final List<CropCount> cropStacks;
  private final Map<AbstractMonster, Integer> damagedEnemies;
  private final DamageInfo info;

  private Logger logger;

  public BarnstormAction(AbstractPlayer player, AbstractMonster target, int baseDamage, boolean damageAllEnemies) {
    this(player, target, baseDamage, getCropCounts(player), new HashMap<>());
  }

  static List<CropCount> getCropCounts(AbstractPlayer player) {
    return AbstractCrop.getActiveCropOrbs(false).stream()
        .map(cropOrb -> new CropCount(cropOrb, cropOrb.passiveAmount, cropOrb.isMature(true)))
        .collect(Collectors.toList());
  }

  static class CropCount {
    public CropCount(AbstractCropOrb crop, int amount, boolean isMature) {
      this.crop = crop;
      this.amount = amount;
      this.isMature = isMature;
    }

    final AbstractCropOrb crop;
    final boolean isMature;
    int amount;
  }

  public BarnstormAction(AbstractPlayer player, AbstractCreature target, int baseDamage,
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
        CropCount cropCount = cropStacks.get(0);
        this.info.base = cropCount.isMature ? 2 * baseDamage : baseDamage;

        if (cropCount.amount > 1) {
          cropCount.amount--;
        } else {
          cropStacks.remove(cropCount);
        }

        this.info.applyPowers(this.info.owner, this.target);

        // TODO: "flash" the crop orb
        // AbstractDungeon.actionManager.addToBottom(new PowerFlashAction(cropCount.crop));
        AbstractDungeon.actionManager.addToBottom(new SFXAction("THUNDERCLAP", cropCount.isMature ? 0.075f : 0.025f));

        AbstractDungeon.actionManager.addToBottom(
            new VFXAction(player, new LightningEffect(target.hb.cX, target.hb.y), 0.1F));

//        AbstractDungeon.actionManager.addToBottom(
//            new DamageAction(target, new DamageInfo(this.player, this.info.base), AttackEffect.NONE));


        AbstractMonster monsterTarget = (AbstractMonster)this.target;
        monsterTarget.damage(new DamageInfo(this.player, this.info.base));


        if (monsterTarget.lastDamageTaken > 0) {
          logger.info("BarnstormAction::update damaging target: " + target.id);
          if (damagedEnemies.containsKey(monsterTarget)) {
            logger.info("BarnstormAction::update target previously damaged. incrementing damaged count: "
                + damagedEnemies.get(monsterTarget) + 1);
            damagedEnemies.put(monsterTarget, damagedEnemies.get(monsterTarget).intValue() + 1);
          } else {
            logger.info("BarnstormAction::update target not previously damaged. adding entry.");
            damagedEnemies.put(monsterTarget, 1);
          }
        }
      }
      if ((this.cropStacks.size() > 0) && (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())) {
        AbstractDungeon.actionManager.addToBottom(new BarnstormAction(
            this.player,
            AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng),
            this.baseDamage,
            this.cropStacks, this.damagedEnemies));
      } else {
        applyPostDamageStuns();
      }
      this.isDone = true;
    }
  }

  private void applyPostDamageStuns() {
    logger.info("BarnstormAction::applyPostDamageStuns called. Final tally:");

    for (Map.Entry<AbstractMonster, Integer> e : this.damagedEnemies.entrySet()) {
      logger.info("\t" + e.getKey() + ": " + e.getValue());
    }
  }
}
