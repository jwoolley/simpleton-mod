package thesimpleton.relics.bottledcard;

import com.megacrit.cardcrawl.cards.colorless.JackOfAllTrades;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CardInJarJackOfAllTrades extends AbstractCardInJarRelic {
    public static final String ID = "TheSimpletonMod:CardInJarJackOfAllTrades";
    public static final String IMG_PATH = "relics/cardinjar_jackofalltrades.png";
    public static final String IMG_PATH_LARGE = "relics/cardinjar_jackofalltrades_large.png";

    public CardInJarJackOfAllTrades() {
        super(ID, IMG_PATH, IMG_PATH_LARGE, new JackOfAllTrades());
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CardInJarJackOfAllTrades();
    }
}

