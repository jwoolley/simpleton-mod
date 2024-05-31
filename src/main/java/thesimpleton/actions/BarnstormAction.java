package thesimpleton.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.AbstractCrop;
import thesimpleton.effects.BarnstormAnimalChargeEffect;
import thesimpleton.effects.BarnstormDrawBarnEffect;
import thesimpleton.orbs.AbstractCropOrb;
import thesimpleton.utilities.ModLogger;

import java.util.*;
import java.util.stream.Collectors;

public class BarnstormAction extends AbstractGameAction {
  private static ModLogger logger = TheSimpletonMod.traceLogger;

  private static final AttackEffect ATTACK_EFFECT_HEAVY = AttackEffect.BLUNT_HEAVY;
  private static final AttackEffect ATTACK_EFFECT_LIGHT = AttackEffect.BLUNT_LIGHT;
  private static final AttackEffect ATTACK_EFFECT_SCRATCH = AttackEffect.SLASH_DIAGONAL;
  private static final float LIGHTNING_X_MIN_OFFSET = 20.0F;
  private static final float LIGHTNING_X_RANGE_WIDTH = 300.F ;
  private static final float LIGHTNING_Y_OFFSET = 200.0F;
  private static final float ANIMAL_Y_OFFSET = 10.0F;

  private final AbstractPlayer player;
  private final int baseDamage;
  private final int stunThreshold;
  private boolean didPerformOrSkipDamageTrigger;

  private final List<CropCount> cropStacks;
  private final Map<AbstractMonster, Integer> damagedEnemies;
  private final DamageInfo info;
  private FlashAtkImgEffect flashAttackEffect;
  private final BarnstormAnimalChargeEffect chargeEffect;

  boolean isInitialBarnstormAction;

  public enum BarnstormAnimal {
    CHICKEN,
    COW,
    PIG,
    SHEEP
  }


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
    this(player, target, baseDamage, stunThreshold, cropStacks, damagedEnemies, true);
  }


  public BarnstormAction(AbstractPlayer player, AbstractCreature target, int baseDamage, int stunThreshold,
                         List<CropCount> cropStacks, Map<AbstractMonster, Integer> damagedEnemies, boolean isInitialBarnstormAction) {
    this.actionType = ActionType.DAMAGE;
    this.damageType = DamageInfo.DamageType.NORMAL;

    this.player = player;
    this.baseDamage = baseDamage;
    this.target = target;

    this.cropStacks = cropStacks;

    this.info = new DamageInfo(this.player, this.baseDamage, DamageInfo.DamageType.NORMAL);

    this.damagedEnemies = damagedEnemies;
    this.stunThreshold = stunThreshold;
    this.didPerformOrSkipDamageTrigger = false;

    if (target != null && !cropStacks.isEmpty() && this.target.currentHealth > 0) {
      List<BarnstormAnimal> animals = Arrays.asList(BarnstormAnimal.values());
      Collections.shuffle(animals);
      BarnstormAnimal animal = animals.get(0);

      if (animal == BarnstormAnimal.COW && animal == BarnstormAnimal.PIG) {
        this.attackEffect = ATTACK_EFFECT_HEAVY;
      } else if (animal == BarnstormAnimal.CHICKEN) {
        this.attackEffect = ATTACK_EFFECT_SCRATCH;
      } else {
        this.attackEffect = ATTACK_EFFECT_LIGHT;
      }

      chargeEffect = makeAnimalChargeEffect(animal, 0F, 0F);
      flashAttackEffect = new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect);
      AbstractDungeon.effectList.add(chargeEffect);
      AbstractDungeon.effectList.add(makeLightningEffect());

      // create the flash effect but don't queue it yet
      this.duration = this.startDuration = chargeEffect.startingDuration;

      if (isInitialBarnstormAction) {
        float numTotalCropStacks = cropStacks.stream().map(c -> c.amount).reduce(0, Integer::sum);
        float estimatedTotalDuration = (this.startDuration + 0.1F) * numTotalCropStacks; // 0.1F is a little grace period
        AbstractDungeon.effectList.add(new BarnstormDrawBarnEffect(0F, 0F, estimatedTotalDuration));

//        logger.debug("[BarnstormAction] (initial action, constructor). estimatedTotalDuration: " + estimatedTotalDuration);
//        debug_static_TotalActionDuration = 0.0F;
      }
    } else {
      chargeEffect = null;
    }
  }

  private static float debug_static_TotalActionDuration;
  private float debug_currentActionRunningDuration;

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

    if (this.duration == this.startDuration) {
      AbstractDungeon.effectList.add(
          new RoomTintEffect(Color.BLACK.cpy(), 0.9F, chargeEffect.startingDuration, true));
    } else if (this.duration < this.startDuration
            && !didPerformOrSkipDamageTrigger) {
      if (this.target.currentHealth > 0) {
        AbstractDungeon.effectList.add(flashAttackEffect);
        this.info.applyPowers(this.info.owner, this.target);
        this.target.damage(new DamageInfo(this.player, this.info.base));
      }
      this.didPerformOrSkipDamageTrigger = true;
    }

    this.duration -= Gdx.graphics.getDeltaTime();
    debug_static_TotalActionDuration += Gdx.graphics.getDeltaTime();
    debug_currentActionRunningDuration += Gdx.graphics.getDeltaTime();

    if (this.duration < 0.0F) {
//      logger.debug("[BarnstormAction] [update, finished]."
//              + " elapsed duration: " + debug_currentActionRunningDuration
//              + " total elapsed duration: " + debug_static_TotalActionDuration
//      );

      CropCount cropCount = cropStacks.get(0);
      this.info.base = baseDamage;

      if (cropCount.amount > 1) {
        cropCount.amount--;
      } else {
        cropStacks.remove(cropCount);
      }

      queueNewBarnstormTick();
      this.isDone = true;

    }
  }

  private void queueNewBarnstormTick() {
    if ((this.cropStacks.size() > 0) && (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())) {
      AbstractDungeon.actionManager.addToBottom(new BarnstormAction(
          this.player,
          AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng),
          this.baseDamage, this.stunThreshold,  this.cropStacks, this.damagedEnemies, false));
    } else {
      applyPostDamageStuns();
    }
  }

  private AbstractGameEffect makeLightningEffect() {
    // TODO: "flash" the crop orb
    AbstractDungeon.actionManager.addToBottom(new SFXAction("THUNDERCLAP", 0.025f));

    int numPossibleStrikeLocations = 2;
    int strikeLocationIndex = AbstractDungeon.miscRng.random(numPossibleStrikeLocations);

    float xPos = (LIGHTNING_X_MIN_OFFSET + ((float)strikeLocationIndex) * LIGHTNING_X_RANGE_WIDTH / (numPossibleStrikeLocations + 1.0F))
            * Settings.yScale;
    float yPos = LIGHTNING_Y_OFFSET * Settings.scale;

    return new LightningEffect(xPos, yPos);
  }

  private BarnstormAnimalChargeEffect makeAnimalChargeEffect(BarnstormAnimal animal, float xPos, float yPos) {
    BarnstormAnimalChargeEffect effect =  new BarnstormAnimalChargeEffect(
            xPos,
            yPos,
            target,
            animal,
            ANIMAL_Y_OFFSET);

    return effect;
  }

  private void applyPostDamageStuns() {
    for (Map.Entry<AbstractMonster, Integer> e : this.damagedEnemies.entrySet()) {
      logger.trace("\t" + e.getKey() + ": " + e.getValue());
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