package thesimpleton.relics.bottledcard;

import com.megacrit.cardcrawl.cards.colorless.Metamorphosis;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CardInJarMetamorphosis extends AbstractCardInJarRelic {
    public static final String ID = "TheSimpletonMod:CardInJarMetamorphosis";
    public static final String IMG_PATH = "relics/cardinjar_metamorphosis.png";
    public static final String IMG_PATH_LARGE = "relics/cardinjar_metamorphosis_large.png";

    public CardInJarMetamorphosis() {
        super(ID, IMG_PATH, IMG_PATH_LARGE, new Metamorphosis());
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CardInJarMetamorphosis();
    }
}
