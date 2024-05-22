package thesimpleton.cards.status;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;

public class IceCube extends CustomCard {
    public static final String ID = "TheSimpletonMod:IceCube";
    private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "cards/icecube.png";

    private static final CardType TYPE = CardType.STATUS;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final int COST = -2;

    private static int NUM_TURNS_TO_ETHEREAL = 3;

    public IceCube() {
        super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, CardColor.COLORLESS, RARITY,
                TARGET);
        this.selfRetain = true;
        this.baseMagicNumber = this.magicNumber = NUM_TURNS_TO_ETHEREAL;
        updateDescription();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {

    }

    @Override
    public void atTurnStart() {
        this.flash(Color.SKY.cpy());
        if (this.baseMagicNumber > 0) {
            this.magicNumber = this.baseMagicNumber = this.baseMagicNumber - 1;
            if (this.baseMagicNumber == 0) {
                CardCrawlGame.sound.play("ICE_CLINK_1");
                this.superFlash(Color.SKY.cpy());
                this.isEthereal = true;
                this.selfRetain = false;
            }

            this.isMagicNumberModified = true;
            updateDescription();
        }
    }

    private void updateDescription() {
        this.rawDescription = getDescription(this.baseMagicNumber);
        this.initializeDescription();
    }

    private static String getDescription(int remainingTurns) {
        if (remainingTurns > 0) {
            return DESCRIPTION;
        } else {
            return EXTENDED_DESCRIPTION;
        }
    }


    public AbstractCard makeCopy() {
        return new IceCube();
    }

    public void upgrade() {
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION[0];
    }
}
