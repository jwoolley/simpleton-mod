package thesimpleton.cards.skill.unused;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.ExhaustCurseThenActivateAction;
import thesimpleton.actions.IncreasePlayersMaxHPAction;
import thesimpleton.cards.TheSimpletonCardTags;
import thesimpleton.enums.AbstractCardEnum;

public class AntiAging extends CustomCard {
    public static final String ID = "TheSimpletonMod:AntiAging";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/antiaging.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 2;
    private static final int BLOCK = 5;
    private static final int UPGRADE_BLOCK_AMOUNT = 2;
    private static final int MAX_HP = 2;
    private static final int UPGRADE_MAX_HP_AMOUNT = 1;

    public AntiAging() {
        super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = MAX_HP;
        this.baseBlock = this.block = BLOCK;
        this.exhaust = true;
        this.tags.add(CardTags.HEALING);
        this.tags.add(TheSimpletonCardTags.RITUAL);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));

        AbstractGameAction targetAction = new IncreasePlayersMaxHPAction(this.magicNumber);
        AbstractDungeon.actionManager.addToBottom(new ExhaustCurseThenActivateAction(targetAction, NAME));
    }

    @Override
    public AbstractCard makeCopy() {
        return new AntiAging();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_MAX_HP_AMOUNT);
            this.upgradeBlock(UPGRADE_BLOCK_AMOUNT);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}