package thesimpleton.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.PotatoCrop;
import thesimpleton.crops.Crop;

public class PotatoCropOrb extends AbstractCropOrb {
    public static final Crop CROP_ENUM = Crop.POTATOES;
    public static final String ORB_ID = "TheSimpletonMod:PotatoCropOrb";
    public static final String IMG_PATH = "plantpotato";
  public static final String HALO_IMG_PATH = "orbpotato_halo";
    private static final OrbStrings orbStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public PotatoCropOrb() {
        this(0);
    }

    public PotatoCropOrb(int amount) {
        super(CROP_ENUM, ORB_ID, NAME, amount, Crop.POTATOES.getCropInfo().maturityThreshold, DESCRIPTIONS[0],
            IMG_PATH, HALO_IMG_PATH);
    }

    @Override
    public AbstractOrb makeCopy() {
       return this.makeCopy(0);
    }

    public AbstractCropOrb makeCopy(int amount) {
        return new PotatoCropOrb(amount);
    }

    @Override
    public void playChannelSFX() {
        TheSimpletonMod.logger.debug(" ============================================= ADD playChannelSFX for " + this.name + " =============================================");
    }

    private static String getDescription() {
        return getGenericDescription(Crop.POTATOES.getCropInfo().maturityThreshold)
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
