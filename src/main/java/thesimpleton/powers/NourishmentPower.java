package thesimpleton.powers;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class NourishmentPower extends AbstractTheSimpletonPower{
    public static final String POWER_ID = "TheSimpletonMod:NourishmentPower";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static AbstractPower.PowerType POWER_TYPE = AbstractPower.PowerType.BUFF;
    public static final String IMG = "nourishment.png";

    public static final int MAX_STACKS = 10;

    public NourishmentPower(int amount) {
        super(IMG);

        POWER_LOGGER.trace("TheSimpletonMod:NourishmentPower: constructor called");

        this.amount = Math.min(amount, MAX_STACKS);
        this.owner = AbstractDungeon.player;
        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        updateDescription();
    }

    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (this.amount >= MAX_STACKS) {
            this.amount = MAX_STACKS;
        }
        updateDescription();
    }

    public void onVictory() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.currentHealth > 0) {
            p.heal(this.amount);
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + MAX_STACKS + DESCRIPTIONS[2];
    }


    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
