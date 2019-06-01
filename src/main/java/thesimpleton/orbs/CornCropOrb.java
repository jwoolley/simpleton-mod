package thesimpleton.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.CornCrop;
import thesimpleton.powers.utils.Crop;

public class CornCropOrb extends AbstractCropOrb {
  public static final Crop CROP_ENUM = Crop.CORN;
  public static final String ORB_ID = "TheSimpletonMod:CornCropOrb";
  public static final String IMG_PATH = "TheSimpletonMod/img/orbs/plantcorn.png";
  private static final OrbStrings orbStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public CornCropOrb() {
    this(0);
  }

  public CornCropOrb(int amount) {
    super(CROP_ENUM, ORB_ID, NAME, amount, CornCrop.MATURITY_THRESHOLD, DESCRIPTIONS[0], IMG_PATH);
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
    TheSimpletonMod.logger.debug(" ============================================= ADD playChannelSFX for " + this.name + " =============================================");
  }

  private static String getDescription() {
    return getGenericDescription(CornCrop.MATURITY_THRESHOLD)
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
