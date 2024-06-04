package thesimpleton.cards.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface IHasSecondMagicNumberCard {
    public boolean isSecondMagicNumberUpgraded();

    public int getSecondMagicNumberBaseValue();

    public int getSecondMagicNumberCurrentValue();
}
