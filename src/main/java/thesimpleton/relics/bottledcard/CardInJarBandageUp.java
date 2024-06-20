package thesimpleton.relics.bottledcard;

import com.megacrit.cardcrawl.cards.colorless.BandageUp;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CardInJarBandageUp extends AbstractCardInJarRelic {
    public static final String ID = "TheSimpletonMod:CardInJarBandageUp";
    public static final String IMG_PATH = "relics/cardinjar_bandageup.png";
    public static final String IMG_PATH_LARGE = "relics/cardinjar_bandageup_large.png";

    public CardInJarBandageUp() {
        super(ID, IMG_PATH, IMG_PATH_LARGE, new BandageUp());
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CardInJarBandageUp();
    }
}

