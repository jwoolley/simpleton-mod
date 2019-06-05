package thesimpleton.orbs;

import basemod.abstracts.CustomOrb;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.AbstractCrop;
import thesimpleton.effects.orb.CropAnimationEffect;
import thesimpleton.effects.orb.GainCropSoundEffect;
import thesimpleton.effects.orb.HarvestCropSoundEffect;
import thesimpleton.effects.orb.StackCropSoundEffect;
import thesimpleton.powers.utils.Crop;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractCropOrb extends CustomOrb {
  private static final String ORB_DESCRIPTION_ID = "TheSimpletonMod:AbstractCropOrb";
  private final static String[] GENERIC_DESCRIPTION;
  private final static OrbStrings orbStrings;

  private final Crop crop;

  // TODO: separate CropOrbType (which has e.g. harvest info and description data) from CropOrb (which has stack count)

  public AbstractCropOrb(Crop crop, String ID, String NAME, int amount, int maturityThreshold, String description, String imgPath) {
    super(ID, NAME, amount, maturityThreshold, description, "", imgPath);
    this.crop = crop;
    this.basePassiveAmount = this.passiveAmount = amount;
  }

  abstract public AbstractCropOrb makeCopy(int amount);

  @Override
  public void onEvoke() {
    this.getCrop().harvestAll();
  }

  public AbstractCrop getCrop() { return this.crop.getCropInfo().getCrop(); }

  public int getAmount() {
    AbstractCropOrb orb = getCropOrb();

    return orb != null ? orb.passiveAmount : 0;
//    return hasCropOrbX() ? getCropOrb().passiveAmount : 0;
  }

  public static String getGenericDescription(int maturityThreshold) {
    return GENERIC_DESCRIPTION[0] + maturityThreshold + GENERIC_DESCRIPTION[1];
  }

  public boolean isMature(boolean thisIsActiveOrb) {
//    TheSimpletonMod.logger.debug("AbstractCropOrb::isMature amount: " + this.getAmount() + " maturityThreshold: " + getCrop().getMaturityThreshold());
    if (thisIsActiveOrb) {
      return this.passiveAmount >= this.getCrop().getMaturityThreshold();
    } else {
      return this.getAmount() >= getCrop().getMaturityThreshold();
    }
  }

  public static boolean isMature(Crop crop) {
    AbstractCropOrb orb = AbstractCropOrb.getCropOrb(crop);
    return orb != null && orb.isMature(false);
//    return hasCropOrbX(crop) && getCropOrb(crop).isMature();
  }

  public boolean hasCropOrbX() {
    return hasCropOrb(this.ID);
  }

  public static boolean hasCropOrbX(Crop crop) {
    return hasCropOrb(crop.getCropInfo().orbId);
  }

  public AbstractCropOrb getCropOrb() {
    return getCropOrb(this.ID);
  }

  //TODO: move to util
  public static boolean hasCropOrbX(AbstractCropOrb orbType) {
    return hasCropOrb(orbType.ID);
  }

  //TODO: move to util
  public static List<AbstractCropOrb> getActiveCropOrbs() {
    return AbstractDungeon.player.orbs.stream()
        .filter(orb -> orb instanceof AbstractCropOrb)
        .map(orb -> (AbstractCropOrb)orb)
        .collect((Collectors.toList()));
  }

  public static boolean hasCropOrb(String orbId) {
//    TheSimpletonMod.logger.debug("AbstractCropOrb::hasCropOrb : Player has orbs:"
//        + AbstractDungeon.player.orbs.stream().map(orb -> orb.name).collect(Collectors.joining(", ")));

    Optional<AbstractOrb> cropOrb = AbstractDungeon.player.orbs.stream()
        .filter(orb -> orb.ID == orbId)
        .findFirst();

    return cropOrb.isPresent();
  }

  public static int getNumberActiveCropOrbs() {
    return getActiveCropOrbs().size();
  }

  public void gainCropEffectBefore() {
    AbstractDungeon.effectList.add(new GainCropSoundEffect(-1.0F, -1.0F));
  }

  public void gainCropEffectAfter() {
    AbstractDungeon.effectList.add(new CropAnimationEffect(this.hb.cX, this.hb.y));
  }

  public void stackCropEffect() {
    AbstractDungeon.effectList.add(new StackCropSoundEffect(this.hb.cX, this.hb.y));
    AbstractDungeon.effectList.add(new CropAnimationEffect(this.hb.cX, this.hb.y));
  }

  public void harvestCropEffect() {
    //TODO: use an animation that's visually distinct from gain or stack animations
    AbstractDungeon.effectList.add(new HarvestCropSoundEffect(this.hb.cX, this.hb.y));
    AbstractDungeon.effectList.add(new CropAnimationEffect(this.hb.cX, this.hb.y, Settings.ACTION_DUR_MED));
  }

  public static boolean playerHasAnyCropOrbs() {
    return getActiveCropOrbs().size() > 0;
  }

  public static AbstractCropOrb getOldestCropOrb() {
    List<AbstractCropOrb> activeCropOrbs = getActiveCropOrbs();
    if (!playerHasAnyCropOrbs()) {
      return null;
    }
    return activeCropOrbs.get(0);
  }

  public static AbstractCropOrb getNewestCropOrb() {
    List<AbstractCropOrb> activeCropOrbs = getActiveCropOrbs();
    if (!playerHasAnyCropOrbs()) {
      return null;
    }
    return activeCropOrbs.get(activeCropOrbs.size() - 1);
  }
  //TODO: move to util
  public static AbstractCropOrb getCropOrb(AbstractCropOrb orbType) {
    return getCropOrb(orbType.ID);
  }

  public static AbstractCropOrb getCropOrb(Crop crop) {
    return getCropOrb(crop.getCropInfo().orbId);
  }

  public void stackOrb(int amount) {

  }

  public static AbstractCropOrb getCropOrb(String orbId) {

//    TheSimpletonMod.logger.debug("AbstractCropOrb::getCropOrb : getting crop orb: " + orbId);
//
//    TheSimpletonMod.logger.debug("AbstractCropOrb::getCropOrb : Player has orbs:"
//        + AbstractDungeon.player.orbs.stream().map(orb -> orb.name).collect(Collectors.joining(", ")));
//
//    TheSimpletonMod.logger.debug("AbstractCropOrb::getCropOrb : Player has active orbs:"
//        + AbstractDungeon.player.orbs.stream()
//            .filter(orb -> orb.passiveAmount > 0).map(orb -> orb.name).collect(Collectors.joining(", ")));
//
//    TheSimpletonMod.logger.debug("AbstractCropOrb::getCropOrb : Player active orb counts:"
//        + AbstractDungeon.player.orbs.stream()
//        .filter(orb -> orb.passiveAmount > 0).map(orb -> orb.name + ": " + orb.passiveAmount).collect(Collectors.joining(", ")));

    Optional<AbstractOrb> cropOrb = AbstractDungeon.player.orbs.stream()
        .filter(orb -> orb instanceof AbstractCropOrb && orb.ID == orbId)
        .findFirst();

    if (!cropOrb.isPresent()) {
      return null;
    }

    TheSimpletonMod.logger.debug("AbstractCropOrb::getCropOrb : returning orb " + ((AbstractCropOrb)cropOrb.get()).name  + " with " + cropOrb.get().passiveAmount + " passive stacks");

    return (AbstractCropOrb)cropOrb.get();
  }

  @Override
  public void onStartOfTurn() {
    getCrop().atStartOfTurn();
  }

  static {
    orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_DESCRIPTION_ID);
    GENERIC_DESCRIPTION = orbStrings.DESCRIPTION;
  }
}
