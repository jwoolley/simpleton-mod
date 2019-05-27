package thesimpleton.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import thesimpleton.TheSimpletonMod;
import thesimpleton.powers.utils.Crop;

public class PotatoCropOrb extends AbstractCropOrb {
    public static final Crop CROP_ENUM = Crop.POTATOES;
    public static final String ORB_ID = "TheSimpletonMod:PotatoCropOrb";
    public static final String IMG_PATH = "TheSimpletonMod/img/orbs/plantpotato.png";
    public static final int MATURITY_THRESHOLD = 2;
    private static final OrbStrings orbStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public PotatoCropOrb() {
        this(0);
    }

    public PotatoCropOrb(int amount) {
        super(CROP_ENUM, ORB_ID, NAME, amount, MATURITY_THRESHOLD, DESCRIPTIONS[0], IMG_PATH);
    }

    @Override
    public void onEvoke() {
//    this.
    }

    @Override
    public AbstractOrb makeCopy() {
        return new PotatoCropOrb();
    }

    @Override
    public void playChannelSFX() {
        TheSimpletonMod.logger.debug(" ============================================= ADD playChannelSFX for " + this.name + " =============================================");
    }

    private static String getDescription() {
        return getGenericDescription(MATURITY_THRESHOLD)
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
