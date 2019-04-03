package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;

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

  // draw card and highlight it
  @Override
  public void update() {
    Logger logger = TheSimpletonMod.logger;
    if (!cardDrawn && this.duration != ACTION_DURATION) {
      cardDrawn = true;
      if (!p.drawPile.isEmpty()) {
        AbstractCard topCard = this.p.drawPile.getTopCard();
        AbstractDungeon.actionManager.addToBottom(new MoveCardToHandAction(topCard, p.drawPile));
        topCard.setCostForTurn(this.amount);
        topCard.lighten(false);
        p.drawPile.refreshHandLayout();
        p.hand.refreshHandLayout();
      }
      this.tickDuration();
      this.isDone = true;
    }

    this.tickDuration();
  }
}