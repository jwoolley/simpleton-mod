package thesimpleton.relics.bottledcard;

import com.megacrit.cardcrawl.cards.colorless.Apotheosis;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CardInJarApotheosis extends AbstractCardInJarRelic {
    public static final String ID = "TheSimpletonMod:CardInJarApotheosis";
    public static final String IMG_PATH = "relics/cardinjar_apotheosis.png";
    public static final String IMG_PATH_LARGE = "relics/cardinjar_apotheosis_large.png";
    public static final String OUTLINE_IMG_PATH = "relics/cardinjar_outline.png";

    public CardInJarApotheosis() {
        super(ID, IMG_PATH, OUTLINE_IMG_PATH, new Apotheosis());
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CardInJarApotheosis();
    }
}
