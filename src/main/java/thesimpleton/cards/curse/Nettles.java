package thesimpleton.cards.curse;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;
import thesimpleton.TheSimpletonMod;

import java.util.List;
import java.util.stream.Collectors;

// Nettles adds a thorns to a single random enemy when drawn
public class Nettles extends CustomCard implements SeasonalCurse {
    public static final String ID = "TheSimpletonMod:Nettles";
    private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/nettles.png";

    private static final CardType TYPE = CardType.CURSE;
    private static final CardRarity RARITY = CardRarity.CURSE;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int THORNS_DAMAGE = 1;

    private static final int COST = -2;

    public Nettles() {
        super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, CardColor.CURSE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = THORNS_DAMAGE;
    }

    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        final List<AbstractCreature> monsters = AbstractDungeon.getMonsters().monsters.stream()
                .filter(mo -> !mo.isDeadOrEscaped())
                .collect(Collectors.toList());

        final AbstractCreature monster = monsters.get(AbstractDungeon.miscRng.random(0, monsters.size() - 1));

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, monster, new ThornsPower(monster, magicNumber), magicNumber));

        // No idea if every curse needs this
        if ((AbstractDungeon.player.hasPower("Evolve")) && (!AbstractDungeon.player.hasPower("No Draw")))
        {
            AbstractDungeon.player.getPower("Evolve").flash();
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player,
                    AbstractDungeon.player.getPower("Evolve").amount));
        }
    }

    // I guess this is how you do it
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasRelic("Blue Candle")) {
            this.useBlueCandle(p);
        } else {
            AbstractDungeon.actionManager.addToBottom(new UseCardAction(this));
        }

    }

    public AbstractCard makeCopy() {
        return new Nettles();
    }

    public void upgrade() {
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
