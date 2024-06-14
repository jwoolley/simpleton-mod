package thesimpleton.relics.bottledcard;

import com.megacrit.cardcrawl.cards.colorless.Chrysalis;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CardInJarChrysalis extends AbstractCardInJarRelic {
    public static final String ID = "TheSimpletonMod:CardInJarChrysalis";
    public static final String IMG_PATH = "relics/cardinjar_chrysalis.png";
    public static final String IMG_PATH_LARGE = "relics/cardinjar_chrysalis_large.png";

    public CardInJarChrysalis() {
        super(ID, IMG_PATH, IMG_PATH_LARGE, new Chrysalis());
    }

    @Override
    public AbstractRelic makeCopy() {  return new CardInJarChrysalis(); }
}
