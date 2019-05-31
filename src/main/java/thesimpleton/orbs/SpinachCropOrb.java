package thesimpleton.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.power.crop.Spinach;
import thesimpleton.crops.SpinachCrop;
import thesimpleton.powers.utils.Crop;

public class SpinachCropOrb extends AbstractCropOrb {
  public static final Crop CROP_ENUM = Crop.SPINACH;
  public static final String ORB_ID = "TheSimpletonMod:SpinachCropOrb";
  public static final String IMG_PATH = "TheSimpletonMod/img/orbs/plantspinach.png";
  private static final OrbStrings orbStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public SpinachCropOrb() {
    this(0);
  }

  public SpinachCropOrb(int amount) {
    super(CROP_ENUM, ORB_ID, NAME, amount, SpinachCrop.MATURITY_THRESHOLD, DESCRIPTIONS[0], IMG_PATH);
  }

  @Override
  public void onEvoke() {
//    this.o
  }

  @Override
  public AbstractOrb makeCopy() {
    return new SpinachCropOrb();
  }

  public AbstractCropOrb makeCopy(int amount) {
    return new SpinachCropOrb(amount);
  }

  @Override
  public void playChannelSFX() {
    TheSimpletonMod.logger.debug(" ============================================= ADD playChannelSFX for " + this.name + " =============================================");
  }

  private static String getDescription() {
    return getGenericDescription(SpinachCrop.MATURITY_THRESHOLD)
        + " NL " + DESCRIPTIONS[0]  + SpinachCrop.STRENGTH_PER_STACK + DESCRIPTIONS[1];
  }

  @Override
  public void updateDescription() {
    this.description = getDescription();
    this.update();
  }

  static {
    orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    NAME = orbStrings.NAME;
    DESCRIPTIONS = orbStrings.DESCRIPTION;
  }
}