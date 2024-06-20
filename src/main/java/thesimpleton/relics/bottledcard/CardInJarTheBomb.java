package thesimpleton.relics.bottledcard;

import com.megacrit.cardcrawl.cards.colorless.TheBomb;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CardInJarTheBomb extends AbstractCardInJarRelic {
    public static final String ID = "TheSimpletonMod:CardInJarTheBomb";
    public static final String IMG_PATH = "relics/cardinjar_thebomb.png";
    public static final String IMG_PATH_LARGE = "relics/cardinjar_thebomb_large.png";

    public CardInJarTheBomb() {
        super(ID, IMG_PATH, IMG_PATH_LARGE, new TheBomb());
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CardInJarTheBomb();
    }
}

