package thesimpleton.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.CornCrop;
import thesimpleton.crops.Crop;

public class CornCropOrb extends AbstractCropOrb {
  public static final Crop CROP_ENUM = Crop.CORN;
  public static final String ORB_ID = "TheSimpletonMod:CornCropOrb";
  public static final String IMG_PATH = "plantcorn";
  public static final String HALO_IMG_PATH = "orbcorn_halo";

  private static final OrbStrings orbStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public CornCropOrb() {
    this(0);
  }

  public CornCropOrb(int amount) {
    super(CROP_ENUM, ORB_ID, NAME, amount, Crop.CORN.getCropInfo().maturityThreshold, DESCRIPTIONS[0], IMG_PATH, HALO_IMG_PATH);
  }

  @Override
  public AbstractOrb makeCopy() {
    return new CornCropOrb();
  }

  public AbstractCropOrb makeCopy(int amount) {
    return new CornCropOrb(amount);
  }

  @Override
  public void playChannelSFX() {
    TheSimpletonMod.logger.info(" ============================================= ADD playChannelSFX for " + this.name + " =============================================");
  }

  private static String getDescription() {
    return getGenericDescription(Crop.CORN.getCropInfo().maturityThreshold)
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
