package thesimpleton.cards.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface IHasCustomCantPlayEffect {
    public void performCantPlayEffect();
    public boolean canPlayCheck(AbstractCard card);
}
