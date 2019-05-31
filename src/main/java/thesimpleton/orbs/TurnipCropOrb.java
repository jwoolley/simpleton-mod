package thesimpleton.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.TurnipCrop;
import thesimpleton.powers.utils.Crop;

public class TurnipCropOrb extends AbstractCropOrb {
  public static final Crop CROP_ENUM = Crop.TURNIPS;
  public static final String ORB_ID = "TheSimpletonMod:TurnipCropOrb";
  public static final String IMG_PATH = "TheSimpletonMod/img/orbs/plantturnip.png";
  private static final OrbStrings orbStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public TurnipCropOrb() {
    this(0);
  }

  public TurnipCropOrb(int amount) {
    super(CROP_ENUM, ORB_ID, NAME, amount, TurnipCrop.MATURITY_THRESHOLD, DESCRIPTIONS[0], IMG_PATH);
  }

  @Override
  public AbstractOrb makeCopy() {
    return new TurnipCropOrb();
  }

  public AbstractCropOrb makeCopy(int amount) {
    return new TurnipCropOrb(amount);
  }

  @Override
  public void playChannelSFX() {
    TheSimpletonMod.logger.debug(" ============================================= ADD playChannelSFX for " + this.name + " =============================================");
  }

  private static String getDescription() {
    return getGenericDescription(TurnipCrop.MATURITY_THRESHOLD)
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