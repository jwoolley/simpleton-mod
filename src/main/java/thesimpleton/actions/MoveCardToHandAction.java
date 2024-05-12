package thesimpleton.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;

public class MoveCardToHandAction extends AbstractGameAction {
  private static ActionType ACTION_TYPE = ActionType.CARD_MANIPULATION;
  private static float ACTION_DURATION = Settings.ACTION_DUR_LONG;

  private AbstractCard card;
  private CardGroup group;


  public MoveCardToHandAction(AbstractCard card, CardGroup group) {
    this.actionType = ACTION_TYPE;
    this.card = card;
    this.group = group;
    this.duration = ACTION_DURATION;
  }

  public void update() {
    if (this.duration != ACTION_DURATION) {

      if (group == AbstractDungeon.player.hand){
        this.isDone = true;
        return;
      }

      if (group.contains(this.card) && AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
        AbstractDungeon.player.hand.addToHand(this.card);
        AbstractDungeon.player.hand.update();
      }

      AbstractDungeon.player.drawPile.updateHoverLogic();
      AbstractDungeon.player.hand.updateHoverLogic();

      AbstractDungeon.player.hand.refreshHandLayout();
      AbstractDungeon.player.hand.glowCheck();

      this.tickDuration();
      this.isDone = true;
      return;
    }
    this.tickDuration();
  }

}