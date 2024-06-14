package thesimpleton.relics.bottledcard;

import com.megacrit.cardcrawl.cards.colorless.Discovery;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CardInJarDiscovery extends AbstractCardInJarRelic {
    public static final String ID = "TheSimpletonMod:CardInJarDiscovery";
    public static final String IMG_PATH = "relics/cardinjar_discovery.png";
    public static final String IMG_PATH_LARGE = "relics/cardinjar_discovery_large.png";

    public CardInJarDiscovery() {
        super(ID, IMG_PATH, IMG_PATH_LARGE, new Discovery());
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CardInJarDiscovery();
    }
}
