package thesimpleton.relics.bottledcard;

import com.megacrit.cardcrawl.cards.colorless.FlashOfSteel;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CardInJarFlashOfSteel extends AbstractCardInJarRelic {
    public static final String ID = "TheSimpletonMod:CardInJarFlashOfSteel";
    public static final String IMG_PATH = "relics/cardinjar_flashofsteel.png";
    public static final String IMG_PATH_LARGE = "relics/cardinjar_flashofsteel_large.png";

    public CardInJarFlashOfSteel() {
        super(ID, IMG_PATH, IMG_PATH_LARGE, new FlashOfSteel());
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CardInJarFlashOfSteel();
    }
}

