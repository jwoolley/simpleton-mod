package thesimpleton.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import thesimpleton.powers.utils.Crop;

public class ChiliCrop extends AbstractCropOrb {
  public static final Crop enumValue = Crop.CHILIS;
  public static final String ORB_ID = "TheSimpletonMod:ChiliCrop";
  public static final String IMG_PATH = "TheSimpletonMod/img/orbs/plantchili.png";
  public static final int MATURITY_THRESHOLD = 5;
  public static final int DAMAGE_PER_STACK = 3;
  private static final OrbStrings orbStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public ChiliCrop(int amount) {
    super(ORB_ID, NAME, amount, MATURITY_THRESHOLD, DESCRIPTIONS[0], IMG_PATH);
  }

  @Override
  public void onEvoke() {
//    this.
  }

  @Override
  public AbstractOrb makeCopy() {
    return new ChiliCrop(0);
  }

  @Override
  public void playChannelSFX() {

  }

  private static String getDescription() {
    return getGenericDescription(MATURITY_THRESHOLD)
        + " NL " + DESCRIPTIONS[0] + DAMAGE_PER_STACK + DESCRIPTIONS[1] + DAMAGE_PER_STACK + DESCRIPTIONS[2];
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
