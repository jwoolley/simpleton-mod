package thesimpleton.cards.interfaces;


public interface IHasSecondMagicNumberCard {

    public boolean isSecondMagicNumberUpgraded();

    public int getSecondMagicNumberBaseValue();

    public int getSecondMagicNumberCurrentValue();

    public void upgradeSecondMagicNumber();

    // this should be overridden to call super.upgradeMagicNumber() and upgradeSecondMagicNumber();
    public void upgradeMagicNumber(int bonusAmount);
}
