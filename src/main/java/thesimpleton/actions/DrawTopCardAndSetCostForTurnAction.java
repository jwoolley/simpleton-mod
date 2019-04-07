package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.megacrit.cardcrawl.core.Settings.ACTION_DUR_MED;

public class DrawTopCardAndSetCostForTurnAction extends AbstractGameAction {
  private static ActionType ACTION_TYPE = ActionType.CARD_MANIPULATION;
  private static float ACTION_DURATION = ACTION_DUR_MED;

  private AbstractPlayer p;
  private boolean cardDrawn;

  public DrawTopCardAndSetCostForTurnAction(int cost) {
    this.p = AbstractDungeon.player;
    this.amount = cost;
    setValues(this.p, this.p, this.amount);
    this.actionType = ACTION_TYPE;
    this.duration = ACTION_DURATION;
    this.cardDrawn = false;
  }

  //
//  // draw card and highlight it
  @Override
  public void update() {
    if (!cardDrawn && this.duration != ACTION_DURATION) {
      cardDrawn = true;
      List<AbstractCard> handBefore = ((ArrayList<AbstractCard>) p.hand.group.clone());

      AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 1));

      AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
        @Override
        public void update() {
          if (this.duration != ACTION_DURATION) {
            p.hand.refreshHandLayout();
            this.tickDuration();
            this.isDone = true;
            return;
          }
          this.tickDuration();
        }
      });

      AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
        @Override
        public void update() {
          if (this.duration != ACTION_DURATION) {
            final List<AbstractCard> newCards = p.hand.group.stream()
                .filter(c -> !handBefore.stream().anyMatch(hbc -> hbc == c))
                .collect(Collectors.toList());

            if (newCards.size() > 0) {
              AbstractCard newCard = newCards.get(0);
              newCard.setCostForTurn(this.amount);
            }

            this.tickDuration();
            this.isDone = true;
            return;
          }
          this.tickDuration();
        }
      });

      this.tickDuration();
      this.isDone = true;
    }
    this.tickDuration();
  }
}