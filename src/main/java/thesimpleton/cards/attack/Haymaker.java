package thesimpleton.cards.attack;

import java.util.Optional;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.actions.HaymakerSplashAction;
    ;
public class Haymaker extends CustomCard {
    public static final String ID = "TheSimpletonMod:Haymaker";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/haymaker.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 2;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_DAMAGE_AMOUNT = 4;
    private static final int VULNERABLE_AMOUNT = 1;
    private static final int UPGRADE_VULNERABLE_AMOUNT = 1;

    public Haymaker() {
        super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
        this.baseDamage = this.damage = DAMAGE;
        this.baseMagicNumber = this.magicNumber = VULNERABLE_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
//        AbstractDungeon.actionManager.addToBottom(
//            new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
//                AbstractGameAction.AttackEffect.BLUNT_HEAVY));

        AbstractDungeon.actionManager.addToBottom(
            new HaymakerSplashAction(p, m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                this.magicNumber));
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
            this.upgradeMagicNumber(UPGRADE_VULNERABLE_AMOUNT);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
