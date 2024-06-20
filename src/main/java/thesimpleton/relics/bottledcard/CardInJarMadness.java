package thesimpleton.relics.bottledcard;

import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CardInJarMadness extends AbstractCardInJarRelic {
    public static final String ID = "TheSimpletonMod:CardInJarMadness";
    public static final String IMG_PATH = "relics/cardinjar_madness.png";
    public static final String IMG_PATH_LARGE = "relics/cardinjar_madness_large.png";

    public CardInJarMadness() {
        super(ID, IMG_PATH, IMG_PATH_LARGE, new Madness());
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CardInJarMadness();
    }
}

