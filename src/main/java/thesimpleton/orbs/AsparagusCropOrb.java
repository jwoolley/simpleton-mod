package thesimpleton.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.AbstractCrop;
import thesimpleton.crops.AsparagusCrop;
import thesimpleton.crops.SpinachCrop;
import thesimpleton.powers.utils.Crop;

public class AsparagusCropOrb extends AbstractCropOrb {
  public static final Crop CROP_ENUM = Crop.ASPARAGUS;
  public static final String ORB_ID = "TheSimpletonMod:AsparagusCropOrb";
  public static final String IMG_PATH = "TheSimpletonMod/img/orbs/plantasparagus.png";
  private static final OrbStrings orbStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public AsparagusCropOrb() {
    this(0);
  }

  public AsparagusCropOrb(int amount) {
    super(CROP_ENUM, ORB_ID, NAME, amount, AsparagusCrop.MATURITY_THRESHOLD, DESCRIPTIONS[0], IMG_PATH);
  }

  @Override
  public AbstractOrb makeCopy() {
    return new AsparagusCropOrb();
  }

  public AbstractCropOrb makeCopy(int amount) {
    return new AsparagusCropOrb(amount);
  }

  @Override
  public void playChannelSFX() {
    TheSimpletonMod.logger.debug(" ============================================= ADD playChannelSFX for " + this.name + " =============================================");
  }

  private static String getDescription() {
    return getGenericDescription(AsparagusCrop.MATURITY_THRESHOLD)
        + " NL " + DESCRIPTIONS[0]  + AsparagusCrop.DEXTERITY_PER_STACK+ DESCRIPTIONS[1];
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