package thesimpleton.cards.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface IHasSecondMagicNumberCard {
    public AbstractCard getThisCard();

    public void upgradeSecondMagicNumber();

    public boolean isSecondMagicNumberModified();
    public boolean isSecondMagicNumberUpgraded();

    public int getSecondMagicNumberBaseValue();

    public int getSecondMagicNumberCurrentValue();
}
