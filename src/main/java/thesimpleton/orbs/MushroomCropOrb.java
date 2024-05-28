package thesimpleton.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.MushroomCrop;
import thesimpleton.crops.Crop;

public class MushroomCropOrb extends AbstractCropOrb {
  public static final Crop CROP_ENUM = Crop.MUSHROOMS;
  public static final String ORB_ID = "TheSimpletonMod:MushroomCropOrb";
  public static final String IMG_PATH = "plantmushroom";
  public static final String HALO_IMG_PATH = "orbmushroom_halo";
  public static final String TARGET_HALO_IMG_PATH = "orbmushroom_target_halo";
  private static final OrbStrings orbStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  private static final float ORB_IMAGE_HORIZONTAL_MIDPOINT = 40.0F;
  private static final float ORB_IMAGE_BOTTOM_EDGE = 72.0F;

  public MushroomCropOrb() {
    this(0);
  }

  public MushroomCropOrb(int amount) {
    super(CROP_ENUM, ORB_ID, NAME, amount, Crop.MUSHROOMS.getCropInfo().maturityThreshold, DESCRIPTIONS[0],
        IMG_PATH, HALO_IMG_PATH, TARGET_HALO_IMG_PATH, ORB_IMAGE_HORIZONTAL_MIDPOINT, ORB_IMAGE_BOTTOM_EDGE);
  }

  @Override
  public AbstractOrb makeCopy() {
    return new MushroomCropOrb();
  }

  public AbstractCropOrb makeCopy(int amount) {
    return new MushroomCropOrb(amount);
  }

  @Override
  public void playChannelSFX() {
    ORB_LOGGER.trace(" ============================================= ADD playChannelSFX for " + this.name + " =============================================");
  }

  private static String getDescription() {
    return getGenericDescription(Crop.MUSHROOMS.getCropInfo().maturityThreshold)
        + " NL " + DESCRIPTIONS[0];
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
