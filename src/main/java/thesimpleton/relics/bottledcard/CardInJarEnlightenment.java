package thesimpleton.relics.bottledcard;

import com.megacrit.cardcrawl.cards.colorless.Enlightenment;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CardInJarEnlightenment extends AbstractCardInJarRelic {
    public static final String ID = "TheSimpletonMod:CardInJarEnlightenment";
    public static final String IMG_PATH = "relics/cardinjar_enlightenment.png";
    public static final String IMG_PATH_LARGE = "relics/cardinjar_enlightenment_large.png";

    public CardInJarEnlightenment() {
        super(ID, IMG_PATH, IMG_PATH_LARGE, new Enlightenment());
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CardInJarEnlightenment();
    }
}

