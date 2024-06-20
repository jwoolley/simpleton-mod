package thesimpleton.relics.bottledcard;

import com.megacrit.cardcrawl.cards.colorless.Transmutation;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CardInJarTransmutation extends AbstractCardInJarRelic {
    public static final String ID = "TheSimpletonMod:CardInJarTransmutation";
    public static final String IMG_PATH = "relics/cardinjar_transmutation.png";
    public static final String IMG_PATH_LARGE = "relics/cardinjar_transmutation_large.png";

    public CardInJarTransmutation() {
        super(ID, IMG_PATH, IMG_PATH_LARGE, new Transmutation());
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CardInJarTransmutation();
    }
}

