package thesimpleton.orbs;

import basemod.abstracts.CustomOrb;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;

public abstract class AbstractCropOrb extends CustomOrb {
  private static final String ORB_DESCRIPTION_ID = "TheSimpletonMod:AbstractCrop";
  private int amount ;

  private static final OrbStrings orbStrings;
  private final static String[] GENERIC_DESCRIPTION;

  // TODO: separate CropOrbType (which has e.g. harvest info and description data) from CropOrb (which has stack count)
  private int maturityThreshold;

  public AbstractCropOrb(String ID, String NAME, int amount, int maturityThreshold, String description, String imgPath) {
    super(ID, NAME, amount, maturityThreshold, description, "", imgPath);
    this.amount = amount;
    this.maturityThreshold = maturityThreshold;
  }

  public int getAmount() {
    return amount;
  }

  public static String getGenericDescription(int maturityThreshold) {
    return GENERIC_DESCRIPTION[0] + maturityThreshold + GENERIC_DESCRIPTION[1];
  }

  static {
    orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_DESCRIPTION_ID);
    GENERIC_DESCRIPTION = orbStrings.DESCRIPTION;
  }
}
