package thesimpleton.cards.skill.unused;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.unused.ApplyBleedAction;
import thesimpleton.actions.unused.ManuallyActivateBleedAction;
import thesimpleton.enums.AbstractCardEnum;

public class SaltUponWound extends CustomCard {
    public static final String ID = "TheSimpletonMod:SaltUponWound";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/saltuponwound.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 2;
    private static final int BLEED = 6;
    private static final int UPGRADE_BLEED_AMOUNT = 3;

    public SaltUponWound() {
        super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = BLEED;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ManuallyActivateBleedAction(m, 2));
        AbstractDungeon.actionManager.addToBottom(new ApplyBleedAction(m, p, this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new SaltUponWound();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_BLEED_AMOUNT);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
