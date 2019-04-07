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
  private static float ACTION_DURATION = Settings.ACTION_DUR_MED;

  private AbstractPlayer p;
  private boolean cardDrawn;

  public DrawTopCardAndSetCostForTurnAction(int cost) {
    this.p = AbstractDungeon.player;
    this.amount = cost;
    setValues(this.p,  this.p, this.amount);
    this.actionType = ACTION_TYPE;
    this.duration = ACTION_DURATION;
    this.cardDrawn = false;
  }
//
//  // draw card and highlight it
  @Override
  public void update() {
    Logger logger = TheSimpletonMod.logger;
    if (!cardDrawn && this.duration != ACTION_DURATION) {
      cardDrawn = true;
      if (!p.drawPile.isEmpty()) {
        AbstractCard topCard = this.p.drawPile.getTopCard();
        List<AbstractCard> handBefore = ((ArrayList<AbstractCard>)p.hand.group.clone());

        logger.debug("DrawTopCardAndSetCostForTurnAction:update adding to hand, topCard: " + topCard.name + "(cost: " + topCard.costForTurn + ") 1");
        logger.debug("DrawTopCardAndSetCostForTurnAction:update adding to hand, handBefore: " + TheSimpletonMod.cardListToString(handBefore) + " 1");

//        AbstractDungeon.actionManager.addToBottom(new MoveCardToHandAction(topCard, p.drawPile));

        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
          @Override
          public void update() {

            if (this.duration != ACTION_DURATION) {
              final List<AbstractCard> handAfter = ((ArrayList<AbstractCard>)p.hand.group.clone());
              final List<AbstractCard> newCards = handAfter.stream()
                  .filter(c -> !handBefore.stream().anyMatch(hbc -> hbc == c))
                  .collect(Collectors.toList());

              final boolean topCardIsInNewCards = newCards.stream().anyMatch(c -> c == topCard);

              if (topCardIsInNewCards) {
                Optional<AbstractCard> cardHandle = p.hand.group.stream()
                    .filter(c -> c == topCard)
                    .findFirst();

                if (cardHandle.isPresent()) {
                  cardHandle.get().setCostForTurn(this.amount);
                }
              }

              logger.debug("DrawTopCardAndSetCostForTurnAction:update added to hand, topCard: " + topCard.name + "(cost: " + topCard.costForTurn + ") 2");
              logger.debug("DrawTopCardAndSetCostForTurnAction:update added to hand, handAfter: " + TheSimpletonMod.cardListToString(handAfter) + " 2");
              logger.debug("DrawTopCardAndSetCostForTurnAction:update added to hand, newCards: " + TheSimpletonMod.cardListToString(newCards) + " 2");
              logger.debug("DrawTopCardAndSetCostForTurnAction:update added to hand, topCardIsInNewCards: " + topCardIsInNewCards + " 2");

              topCard.setCostForTurn(this.amount);
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
              topCard.lighten(false);
              p.hand.refreshHandLayout();
              topCard.setCostForTurn(this.amount);
              logger.debug("DrawTopCardAndSetCostForTurnAction:update added to hand, topCard: " + topCard.name + "(cost: " + topCard.costForTurn + ") 3");
              this.tickDuration();
              topCard.setCostForTurn(this.amount);
              this.isDone = true;
              return;
            }
            this.tickDuration();
          }
        });
      }
      this.tickDuration();
      this.isDone = true;
    }

    this.tickDuration();
  }


  // draw card and highlight it
//  public void updateNew() {
//    Logger logger = TheSimpletonMod.logger;
//    if (!cardDrawn && this.duration != ACTION_DURATION) {
//      cardDrawn = true;
//      if (!p.drawPile.isEmpty()) {
//        AbstractCard topCard = this.p.drawPile.getTopCard();
//        List<AbstractCard> handBefore = ((ArrayList<AbstractCard>)p.hand.group.clone());
//
//        logger.debug("DrawTopCardAndSetCostForTurnAction:update adding to hand, topCard: " + topCard.name + "(cost: " + topCard.costForTurn + ") 1");
//        logger.debug("DrawTopCardAndSetCostForTurnAction:update adding to hand, handBefore: " + TheSimpletonMod.cardListToString(handBefore) + " 1");
//
//        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 1));
//
//        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
//          @Override
//          public void update() {
//
//
//            if (this.duration != ACTION_DURATION) {
//              topCard.setCostForTurn(this.amount);
//              this.tickDuration();
//              this.isDone = true;
//              return;
//            }
//            this.tickDuration();
//          }
//        });
//
//        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
//          @Override
//          public void update() {
//            if (this.duration != ACTION_DURATION) {
//              topCard.lighten(false);
//              p.hand.refreshHandLayout();
//              //             topCard.setCostForTurn(this.amount);
//              logger.debug("DrawTopCardAndSetCostForTurnAction:update added to hand, topCard: " + topCard.name + "(cost: " + topCard.costForTurn + ") 3");
//              this.tickDuration();
//              topCard.setCostForTurn(this.amount);
//              this.isDone = true;
//              return;
//            }
//            this.tickDuration();
//          }
//        });
//      }
//      this.tickDuration();
//      this.isDone = true;
//    }
//
//    this.tickDuration();
//  }
}