package thesimpleton.powers.unused;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import thesimpleton.cards.attack.unused.*;
import thesimpleton.cards.skill.unused.CursedAmulet;
import thesimpleton.cards.skill.unused.CursedArmor;
import thesimpleton.cards.skill.unused.CursedSpellbook;
import thesimpleton.powers.AbstractTheSimpletonPower;

import java.util.ArrayList;

public class HellsGateOpenPower extends AbstractTheSimpletonPower {
    public static final String POWER_ID = "TheSimpletonMod:HellsGateOpenPower";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;
    public static final String IMG = "hellsgateopen.png";

    private ArrayList<AbstractCard> simpletonCardPool;

    public HellsGateOpenPower(AbstractCreature owner, int amount) {
        super(IMG);
        this.owner = owner;
        this.amount = amount;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        this.simpletonCardPool = new ArrayList<>();
        this.simpletonCardPool.add(new CursedBlade());
        this.simpletonCardPool.add(new CursedBoomerang());
        this.simpletonCardPool.add(new CursedMace());
        this.simpletonCardPool.add(new CursedRelics());
        this.simpletonCardPool.add(new CursedShiv());
        this.simpletonCardPool.add(new CursedStaff());
        this.simpletonCardPool.add(new CursedAmulet());
        this.simpletonCardPool.add(new CursedArmor());
        this.simpletonCardPool.add(new CursedSpellbook());

        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
    }

    public void atStartOfTurn() {
        this.flash();
        for (int i = 0; i < this.amount; ++i) {
            AbstractCard card =
                    this.simpletonCardPool
                            .get(AbstractDungeon.cardRandomRng.random(this.simpletonCardPool.size() - 1))
                            .makeCopy();
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card));
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
