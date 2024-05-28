package thesimpleton.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.AsparagusCrop;
import thesimpleton.crops.Crop;

import java.util.Arrays;
import java.util.List;

public class AsparagusCropOrb extends AbstractCropOrb {
  public static final Crop CROP_ENUM = Crop.ASPARAGUS;
  public static final String ORB_ID = "TheSimpletonMod:AsparagusCropOrb";
  public static final String IMG_PATH = "plantasparagus";
  public static final String HALO_IMG_PATH = "orbasparagus_halo";
  public static final String TARGET_HALO_IMG_PATH = "orbasparagus_target_halo";

  public static final List<Keyword> BASEGAME_KEYWORDS = Arrays.asList(GameDictionary.DEXTERITY);

  private static final OrbStrings orbStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  private static final float ORB_IMAGE_HORIZONTAL_MIDPOINT = 41.0F;
  private static final float ORB_IMAGE_BOTTOM_EDGE = 87.0F;

  public AsparagusCropOrb() {
    this(0);
  }

  public AsparagusCropOrb(int amount) {
    super(CROP_ENUM, ORB_ID, NAME, amount, Crop.ASPARAGUS.getCropInfo().maturityThreshold, DESCRIPTIONS[0],
        IMG_PATH, HALO_IMG_PATH, TARGET_HALO_IMG_PATH, ORB_IMAGE_HORIZONTAL_MIDPOINT, ORB_IMAGE_BOTTOM_EDGE);
  }

  @Override
  public AbstractOrb makeCopy() {
    return this.makeCopy(0);
  }

  public AbstractCropOrb makeCopy(int amount) {
    return new AsparagusCropOrb(amount);
  }

  @Override
  public void playChannelSFX() {
    ORB_LOGGER.trace(" ============================================= ADD playChannelSFX for " + this.name + " =============================================");
  }

  private static String getDescription() {
    return getGenericDescription(Crop.ASPARAGUS.getCropInfo().maturityThreshold)
        + " NL " + DESCRIPTIONS[0]  + AsparagusCrop.DEXTERITY_PER_STACK+ DESCRIPTIONS[1];
  }

  @Override
  public void updateDescription() {
    this.description = getDescription();
    this.update();
  }

  @Override
  protected List<Keyword> getBaseGameKeywords() {
    return BASEGAME_KEYWORDS;
  }

  static {
    orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    NAME = orbStrings.NAME;
    DESCRIPTIONS = orbStrings.DESCRIPTION;
  }
}