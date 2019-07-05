package thesimpleton.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.Crop;

import java.util.Arrays;
import java.util.List;

public class StrawberryCropOrb extends AbstractCropOrb {
  public static final Crop CROP_ENUM = Crop.STRAWBERRIES;
  public static final String ORB_ID = "TheSimpletonMod:StrawberryCropOrb";
  public static final String IMG_PATH = "plantstrawberry";
  public static final String HALO_IMG_PATH = "orbstrawberry_halo";
  public static final List<String> KEYWORD_LIST = Arrays.asList("TheSimpletonMod:GnawberryKeyword");

  private static final OrbStrings orbStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public StrawberryCropOrb() {
    this(0);
  }

  public StrawberryCropOrb(int amount) {
    super(CROP_ENUM, ORB_ID, NAME, amount, Crop.STRAWBERRIES.getCropInfo().maturityThreshold, DESCRIPTIONS[0],
        IMG_PATH, HALO_IMG_PATH);
  }

  @Override
  public AbstractOrb makeCopy() {
    return this.makeCopy(0);
  }

  public AbstractCropOrb makeCopy(int amount) {
    return new StrawberryCropOrb(amount);
  }

  @Override
  public void playChannelSFX() {
    TheSimpletonMod.logger.info(" ============================================= ADD playChannelSFX for " + this.name + " =============================================");
  }

  private static String getDescription() {
    return getGenericDescription(Crop.STRAWBERRIES.getCropInfo().maturityThreshold)
        + " NL " + DESCRIPTIONS[0];
  }

  @Override
  public void updateDescription() {
    this.description = getDescription();
    this.update();
  }

  protected List<String> getCustomKeywords() {
    return KEYWORD_LIST;
  }

  static {
    orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    NAME = orbStrings.NAME;
    DESCRIPTIONS = orbStrings.DESCRIPTION;
  }
}
