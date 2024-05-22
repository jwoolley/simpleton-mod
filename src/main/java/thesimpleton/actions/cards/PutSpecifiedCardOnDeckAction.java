package thesimpleton.actions.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class PutSpecifiedCardOnDeckAction extends AbstractGameAction {
    private AbstractCard targetCard;

    public PutSpecifiedCardOnDeckAction(AbstractCard card) {
        this.targetCard = card;
        this.startDuration = Settings.ACTION_DUR_XFAST;
        this.duration = this.startDuration;
    }

    public void update() {
        if (this.duration == this.startDuration && AbstractDungeon.player.hand.contains(this.targetCard)) {
            AbstractDungeon.player.hand.moveToDeck(this.targetCard, false);
        }

        this.tickDuration();
        if (Settings.FAST_MODE) {
            this.isDone = true;
        }

    }
}