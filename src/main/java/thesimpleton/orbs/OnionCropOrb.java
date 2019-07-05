package thesimpleton.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.OnionCrop;
import thesimpleton.crops.Crop;

import java.util.Arrays;
import java.util.List;

public class OnionCropOrb extends AbstractCropOrb {
  public static final Crop CROP_ENUM = Crop.ONIONS;
  public static final String ORB_ID = "TheSimpletonMod:OnionCropOrb";
  public static final String IMG_PATH = "plantonion";
  public static final String HALO_IMG_PATH = "orbonion_halo";
  public static final List<Keyword> BASEGAME_KEYWORDS = Arrays.asList(GameDictionary.WEAK);

  private static final OrbStrings orbStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public OnionCropOrb() {
    this(0);
  }

  public OnionCropOrb(int amount) {
    super(CROP_ENUM, ORB_ID, NAME, amount, Crop.ONIONS.getCropInfo().maturityThreshold, DESCRIPTIONS[0],
        IMG_PATH, HALO_IMG_PATH);
  }
  
  @Override
  public AbstractOrb makeCopy() {
    return this.makeCopy(0);
  }

  @Override
  public AbstractCropOrb makeCopy(int amount) {
    return new OnionCropOrb(amount);
  }

  @Override
  public void playChannelSFX() {
    TheSimpletonMod.logger.info(" ============================================= ADD playChannelSFX for " + this.name + " =============================================");
  }

  private static String getDescription() {
    return getGenericDescription(Crop.ONIONS.getCropInfo().maturityThreshold)
        + " NL " + DESCRIPTIONS[0] + OnionCrop.WEAK_PER_STACK + DESCRIPTIONS[1];
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
