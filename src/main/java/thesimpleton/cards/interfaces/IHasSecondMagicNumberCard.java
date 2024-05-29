package thesimpleton.cards.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface IHasSecondMagicNumberCard {
    public AbstractCard getThisCard();

    public void upgradeSecondMagicNumber(int upgradeBonus);

    public boolean isSecondMagicNumberModified();
    public boolean isSecondMagicNumberUpgraded();

    public int getSecondMagicNumberBaseValue();

    public int getSecondMagicNumberCurrentValue();
}
