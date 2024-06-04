package thesimpleton.customvariables;


import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import thesimpleton.cards.interfaces.IHasSecondMagicNumberCard;

public class SecondMagicNumber extends DynamicVariable {
    private static final String CARD_MAGIC_NUMBER_2_KEY = "TheSimpleton:magic-num-2";
    private static final int DEFAULT_VALUE = -1;

    public SecondMagicNumber() {
        this(DEFAULT_VALUE);
    }

    public SecondMagicNumber(int baseValue) {
        super();
    }

    @Override
    public String key() {
        return CARD_MAGIC_NUMBER_2_KEY;
        // What you put in your localization file between ! to show your value. Eg, !myKey!.
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof IHasSecondMagicNumberCard) {
            return ((IHasSecondMagicNumberCard)card).getSecondMagicNumberCurrentValue()
                   != ((IHasSecondMagicNumberCard)card).getSecondMagicNumberBaseValue();
        } else {
            return false;
        }
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        if (card instanceof IHasSecondMagicNumberCard) {
            return ((IHasSecondMagicNumberCard) card).isSecondMagicNumberUpgraded();
        } else {
            return false;
        }
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof IHasSecondMagicNumberCard) {
            return ((IHasSecondMagicNumberCard)card).getSecondMagicNumberCurrentValue();
        } else {
            return DEFAULT_VALUE;
        }
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof IHasSecondMagicNumberCard) {
            return ((IHasSecondMagicNumberCard)card).getSecondMagicNumberBaseValue();
        } else {
            return DEFAULT_VALUE;
        }
    }
}
