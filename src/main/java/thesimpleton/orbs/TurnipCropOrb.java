package thesimpleton.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.TurnipCrop;
import thesimpleton.crops.Crop;

public class TurnipCropOrb extends AbstractCropOrb {
  public static final Crop CROP_ENUM = Crop.TURNIPS;
  public static final String ORB_ID = "TheSimpletonMod:TurnipCropOrb";
  public static final String IMG_PATH = "plantturnip";
  public static final String HALO_IMG_PATH = "orbturnip_halo";
  public static final String TARGET_HALO_IMG_PATH = "orbturnip_target_halo";

  private static final OrbStrings orbStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public TurnipCropOrb() {
    this(0);
  }

  public TurnipCropOrb(int amount) {
    super(CROP_ENUM, ORB_ID, NAME, amount, Crop.TURNIPS.getCropInfo().maturityThreshold, DESCRIPTIONS[0],
        IMG_PATH, HALO_IMG_PATH, TARGET_HALO_IMG_PATH);
  }

  @Override
  public AbstractOrb makeCopy() {
    return this.makeCopy(0);
  }

  public AbstractCropOrb makeCopy(int amount) {
    return new TurnipCropOrb(amount);
  }

  @Override
  public void playChannelSFX() {
    TheSimpletonMod.logger.info(" ============================================= ADD playChannelSFX for " + this.name + " =============================================");
  }

  private static String getDescription() {
    return getGenericDescription(Crop.TURNIPS.getCropInfo().maturityThreshold)
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