package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;

import java.util.ArrayList;
import java.util.List;

public class ChooseAndAddHandCardsToDeckAction extends AbstractGameAction {
  private static final String UI_NAME = "TheSimpletonMod:ChooseAndAddHandCardsToDeckAction";
  private static final UIStrings uiStrings;
  public static final String[] TEXT;

  private static AbstractGameAction.ActionType ACTION_TYPE = AbstractGameAction.ActionType.CARD_MANIPULATION;
  private static final float ACTION_DURATION = Settings.ACTION_DUR_FASTER;

  private AbstractPlayer p;

  private List<AbstractCard> cardsToMove;
  private boolean cardsWereAddedToDeck;

  public ChooseAndAddHandCardsToDeckAction(int numCardsToChoose) {
    this.p = AbstractDungeon.player;
    setValues(this.p,  this.p, numCardsToChoose);

    this.actionType = ACTION_TYPE;
    this.amount = numCardsToChoose;
    this.duration = ACTION_DURATION;
    this.cardsWereAddedToDeck = false;
    this.cardsToMove = new ArrayList<>();
  }

  @Override
  public void update() {
    if(p.hand.isEmpty()) {
      this.isDone = true;
      this.tickDuration();
      return;
    }

    if(this.duration == ACTION_DURATION) {
      if (p.hand.size() <= this.amount) {
        cardsToMove.addAll(p.hand.group);
      } else {
        AbstractDungeon.handCardSelectScreen.open(TEXT[0],this.amount,false,false);
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.25F));
      }
      this.tickDuration();
    } else {
      // TODO: necessary to add isBattleEnding check? see GathererMod::SaveValuablesAction
      if (!this.cardsWereAddedToDeck) {
        if (cardsToMove.size() == 0) {
          cardsToMove.addAll(AbstractDungeon.handCardSelectScreen.selectedCards.group);
          AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
        }

        cardsToMove.forEach(c -> p.hand.moveToDeck(c, false));
        p.hand.refreshHandLayout();

        this.cardsWereAddedToDeck = true;
        this.isDone = true;
      }

      this.tickDuration();
    }
  }

  static {
    uiStrings = CardCrawlGame.languagePack.getUIString(UI_NAME);
    TEXT = uiStrings.TEXT;
  }
}
