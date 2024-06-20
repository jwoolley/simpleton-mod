package thesimpleton.relics.bottledcard;

import com.megacrit.cardcrawl.cards.colorless.Finesse;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CardInJarFinesse extends AbstractCardInJarRelic {
    public static final String ID = "TheSimpletonMod:CardInJarFinesse";
    public static final String IMG_PATH = "relics/cardinjar_finesse.png";
    public static final String IMG_PATH_LARGE = "relics/cardinjar_finesse_large.png";

    public CardInJarFinesse() {
        super(ID, IMG_PATH, IMG_PATH_LARGE, new Finesse());
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CardInJarFinesse();
    }
}

