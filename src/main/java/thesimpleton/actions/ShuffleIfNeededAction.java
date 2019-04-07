package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;

public class ShuffleIfNeededAction extends AbstractGameAction {
  private static AbstractGameAction.ActionType ACTION_TYPE = AbstractGameAction.ActionType.CARD_MANIPULATION;
  private static float ACTION_DURATION = Settings.ACTION_DUR_FASTER;

  private AbstractPlayer p;

  public ShuffleIfNeededAction() {
    this.p = AbstractDungeon.player;
    setValues(this.p, this.p);
    this.actionType = ACTION_TYPE;
    this.duration = ACTION_DURATION;
  }

  @Override
  public void update() {
    Logger logger = TheSimpletonMod.logger;

    if (this.duration != ACTION_DURATION) {
      if (this.p.drawPile.isEmpty()) {
        logger.debug("ShuffleIfNeededAction:update empty draw pile, queuing EmptyDeckShuffleAction");

        AbstractDungeon.actionManager.addToBottom(new EmptyDeckShuffleAction());
      } else {
        logger.debug("ShuffleIfNeededAction:update non-empty draw pile, queuing ShuffleAction");

        AbstractDungeon.actionManager.addToBottom(new ShuffleAction(this.p.drawPile, true));
      }
      this.tickDuration();
      this.p.drawPile.update();
      this.isDone = true;
      return;
    }

    logger.debug("ShuffleIfNeededAction:update no-op tickDuration");
    this.tickDuration();
  }
}
