package thesimpleton.orbs;

import basemod.BaseMod;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.KeywordStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.ChilisCrop;
import thesimpleton.crops.SpinachCrop;
import thesimpleton.crops.Crop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChiliCropOrb extends AbstractCropOrb {
  public static final Crop CROP_ENUM = Crop.CHILIS;
  public static final String ORB_ID = "TheSimpletonMod:ChiliCropOrb";
  public static final String IMG_PATH = "plantchili";
  public static final String HALO_IMG_PATH = "orbchili_halo";
  public static final List<String> KEYWORD_LIST = Arrays.asList("TheSimpletonMod:BurningKeyword");


  private static final OrbStrings orbStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public ChiliCropOrb() {
    this(0);
  }

  public ChiliCropOrb(int amount) {
    super(CROP_ENUM, ORB_ID, NAME, amount, Crop.CHILIS.getCropInfo().maturityThreshold, DESCRIPTIONS[0],
        IMG_PATH, HALO_IMG_PATH);
  }

  @Override
  public AbstractOrb makeCopy() {
    return new ChiliCropOrb();
  }

  public AbstractCropOrb makeCopy(int amount) {
    return new ChiliCropOrb(amount);
  }


  @Override
  public void playChannelSFX() {
    TheSimpletonMod.logger.debug(" ============================================= ADD playChannelSFX for " + this.name + " =============================================");
  }

  private static String getDescription() {
    return getGenericDescription(Crop.CHILIS.getCropInfo().maturityThreshold)
        + " NL " + DESCRIPTIONS[0] + ChilisCrop.DAMAGE_PER_STACK + DESCRIPTIONS[1] + ChilisCrop.DAMAGE_PER_STACK
        + DESCRIPTIONS[2];
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
