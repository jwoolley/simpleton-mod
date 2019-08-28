package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.DiscardGlowEffect;
import thesimpleton.cards.ShuffleTriggeredCard;
import thesimpleton.cards.SimpletonUtil;

public class ChooseAndAddDiscardCardsToDeckAction extends AbstractGameAction {
  private AbstractPlayer p;
  private AbstractGameAction.ActionType ACTION_TYPE = AbstractGameAction.ActionType.CARD_MANIPULATION;
  private static final float ACTION_DURATION = Settings.ACTION_DUR_FASTER;

  private final boolean triggerShuffle;

  public ChooseAndAddDiscardCardsToDeckAction(int numCardsToChoose, boolean triggerShuffle) {
    this.p = AbstractDungeon.player;
    setValues(this.p,  this.p, numCardsToChoose);

    this.actionType = ACTION_TYPE;
    this.amount = numCardsToChoose;
    this.duration = ACTION_DURATION;

    this.triggerShuffle = triggerShuffle;
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
            if (triggerShuffle) {
              if (c instanceof ShuffleTriggeredCard) {
                ((ShuffleTriggeredCard)c).willBeShuffledTrigger();
              }
            }
//            this.p.discardPile.removeCard(c);
          }
          AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        this.tickDuration();
        SimpletonUtil.centerGridSelectScreenFinished();
        this.isDone = true;
        return;
      }

      CardGroup discardPile = this.p.discardPile;
      // TODO: move this text to localized strings
      SimpletonUtil.openCenterGridSelectScreenForUpToNumCards(discardPile, this.amount,
          "Select up to "+ this.amount +" cards.");
      this.tickDuration();
    }
}
