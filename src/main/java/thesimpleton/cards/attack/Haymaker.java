package thesimpleton.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.HaymakerSplashAction;
import thesimpleton.cards.interfaces.IHasSecondMagicNumberCard;
import thesimpleton.customvariables.SecondMagicNumber;
import thesimpleton.enums.AbstractCardEnum;

public class Haymaker extends CustomCard implements IHasSecondMagicNumberCard {
    public static final String ID = "TheSimpletonMod:Haymaker";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "cards/haymaker.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 2;
    private static final int DAMAGE = 9;
    private static final int UPGRADE_DAMAGE_AMOUNT = 1;
    private static final int VULNERABLE_AMOUNT = 1;
    private static final int UPGRADE_VULNERABLE_AMOUNT = 1;
    private static final int HEAL_AMOUNT = 4;
    private static final int UPGRADE_HEAL_AMOUNT = 1;

    private int baseSecondMagicNumber;

    private int secondMagicNumber;

    public Haymaker() {
        super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, getDescription(), TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
        this.baseDamage = this.damage = DAMAGE;
        this.baseMagicNumber = this.magicNumber = HEAL_AMOUNT;
        this.baseSecondMagicNumber = secondMagicNumber = VULNERABLE_AMOUNT;
        this.exhaust = true;
        this.tags.add(AbstractCard.CardTags.HEALING);

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
            new HaymakerSplashAction(p, m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                this.magicNumber, this.secondMagicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Haymaker();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMOUNT);
            this.upgradeMagicNumber(UPGRADE_HEAL_AMOUNT);
            this.upgradeSecondMagicNumber();
            this.rawDescription = getDescription();
            initializeDescription();
        }
    }

    public static String getDescription() {
        return DESCRIPTION;
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    }

    @Override
    public AbstractCard getThisCard() {
        return null;
    }

    @Override
    public void upgradeSecondMagicNumber() {
        this.secondMagicNumber += UPGRADE_VULNERABLE_AMOUNT;
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();

        ((Haymaker)card).secondMagicNumber = this.secondMagicNumber;

        return card;
    }

    @Override
    public boolean isSecondMagicNumberModified() {
        return secondMagicNumber != baseSecondMagicNumber;
    }

    @Override
    public boolean isSecondMagicNumberUpgraded() {
        return  upgraded && isSecondMagicNumberModified();
    }

    @Override
    public int getSecondMagicNumberBaseValue() {
        return baseSecondMagicNumber;
    }

    @Override
    public int getSecondMagicNumberCurrentValue() {
        return secondMagicNumber;
    }
}
