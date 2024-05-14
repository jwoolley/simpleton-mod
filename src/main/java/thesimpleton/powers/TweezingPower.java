package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.ThornsPower;
import thesimpleton.TheSimpletonMod;


public class TweezingPower extends AbstractTheSimpletonPower {
    public static final String POWER_ID = "TheSimpletonMod:TweezingPower";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.DEBUFF;
    public static final String IMG = "tweezers.png";

    public static final int MAX_STACKS = 999;

    public TweezingPower(AbstractCreature owner, int amount) {
        super(IMG);
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = POWER_TYPE;

        if (this.amount >= MAX_STACKS) {
            this.amount = MAX_STACKS;
        }

        updateDescription();
    }

    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (this.amount >= MAX_STACKS) {
            this.amount = MAX_STACKS;
        }
    }

    public void updateDescription()
    {
        this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1]);
    }

    @Override
    public void atStartOfTurn() {
        flashWithoutSound();
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));

        if (this.owner.hasPower(ThornsPower.POWER_ID)
                && this.owner.getPower(ThornsPower.POWER_ID).amount <= this.amount) {
            AbstractDungeon.actionManager.addToBottom(
                    new RemoveSpecificPowerAction(this.owner, this.owner, ThornsPower.POWER_ID));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner,
                    new ThornsPower(this.owner, -this.amount)));
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
