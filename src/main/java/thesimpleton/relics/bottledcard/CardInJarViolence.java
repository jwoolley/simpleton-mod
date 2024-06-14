package thesimpleton.relics.bottledcard;

import com.megacrit.cardcrawl.cards.colorless.Violence;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CardInJarViolence extends AbstractCardInJarRelic {
    public static final String ID = "TheSimpletonMod:CardInJarViolence";
    public static final String IMG_PATH = "relics/cardinjar_violence.png";
    public static final String IMG_PATH_LARGE = "relics/cardinjar_violence_large.png";

    public CardInJarViolence() {
        super(ID, IMG_PATH, IMG_PATH_LARGE, new Violence());
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CardInJarViolence();
    }
}

