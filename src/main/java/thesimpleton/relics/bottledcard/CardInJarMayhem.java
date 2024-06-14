package thesimpleton.relics.bottledcard;

import com.megacrit.cardcrawl.cards.colorless.Mayhem;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CardInJarMayhem extends AbstractCardInJarRelic {
    public static final String ID = "TheSimpletonMod:CardInJarMayhem";
    public static final String IMG_PATH = "relics/cardinjar_mayhem.png";
    public static final String IMG_PATH_LARGE = "relics/cardinjar_mayhem_large.png";

    public CardInJarMayhem() {
        super(ID, IMG_PATH, IMG_PATH_LARGE, new Mayhem());
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CardInJarMayhem();
    }
}
