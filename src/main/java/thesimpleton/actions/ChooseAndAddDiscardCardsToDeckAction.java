package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.DiscardGlowEffect;

public class ChooseAndAddDiscardCardsToDeckAction extends AbstractGameAction {
  private AbstractPlayer p;
  private AbstractGameAction.ActionType ACTION_TYPE = AbstractGameAction.ActionType.CARD_MANIPULATION;
  private static final float ACTION_DURATION = Settings.ACTION_DUR_FASTER;

  public ChooseAndAddDiscardCardsToDeckAction(int numCardsToChoose) {
    this.p = AbstractDungeon.player;
    setValues(this.p,  this.p, numCardsToChoose);

    this.actionType = ACTION_TYPE;
    this.amount = numCardsToChoose;
    this.duration = ACTION_DURATION;
  }

    @Override
    public void update() {
      if(p.discardPile.isEmpty()) {
        AbstractDungeon.effectList.add(new DiscardGlowEffect(true));
        this.isDone = true;
        this.tickDuration();
        return;
      }

      if(this.duration != ACTION_DURATION) {
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
          // TODO: necessary to add isBattleEnding check? see GathererMod::SaveValuablesAction

          //TODO: clean up: find the one (first) selected card and add it
          for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
            this.p.discardPile.moveToDeck(c, true);
            this.p.discardPile.removeCard(c);

            //TODO: remove this spaghetti fom the wall
            this.p.discardPile.refreshHandLayout();
            this.p.drawPile.refreshHandLayout();

          }
          AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        this.tickDuration();
        this.isDone = true;
        return;
      }

      CardGroup discardPile = this.p.discardPile;
      // TODO: move this text to localized strings
      AbstractDungeon.gridSelectScreen.open(discardPile,this.amount,true,"Select up to "+ this.amount +" cards.");
      this.tickDuration();
    }
}
