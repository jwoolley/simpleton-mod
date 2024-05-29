package thesimpleton.customvariables;


import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class SecondMagicNumber extends DynamicVariable {
    private static final String CARD_MAGIC_NUMBER_2_KEY = "magic-num-2";

    private int baseValue;
    private int value;

    private boolean wasUpgraded = false;

    public SecondMagicNumber(int baseValue) {
        super();
        this.baseValue = baseValue;
        this.value = baseValue;
    }

    @Override
    public String key() {
        return CARD_MAGIC_NUMBER_2_KEY;
        // What you put in your localization file between ! to show your value. Eg, !myKey!.
    }

    public void upgradeSecondMagicNumber(int upgradeBonus) {
        this.value += upgradeBonus;
        wasUpgraded = true;
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return value != baseValue;
        // Set to true if the value is modified from the base value.
    }

    @Override
    public void setIsModified(AbstractCard card, boolean v) {
        // Do something such that isModified will return the value v.
        // This method is only necessary if you want smith upgrade previews to function correctly.
    }

    @Override
    public int value(AbstractCard card) {
        return value;
        // What the dynamic variable will be set to on your card. Usually uses some kind of int you store on your card.
    }

    @Override
    public int baseValue(AbstractCard card) {
        return baseValue;
        // Should generally just be the above.
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return wasUpgraded && value > baseValue;
    }
}
