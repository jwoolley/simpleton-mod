package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

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
    if (this.duration != ACTION_DURATION) {
      if (this.p.drawPile.isEmpty()) {
        AbstractDungeon.actionManager.addToTop(new EmptyDeckShuffleAction());
      } else {
        AbstractDungeon.actionManager.addToTop(new ShuffleAction(this.p.drawPile, true));
      }
      this.tickDuration();
      this.isDone = true;
      return;
    }
    this.tickDuration();
  }
}
