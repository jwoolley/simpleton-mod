package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SoilSampleAction extends AbstractGameAction {
  private static final ActionType ACTION_TYPE = ActionType.CARD_MANIPULATION;
  private static final float ACTION_DURATION = Settings.ACTION_DUR_XFAST;
  private static final int COST_FOR_TURN = 0;

  private AbstractPlayer p;
  private final int numCardsToDraw;

  public SoilSampleAction(int numCardsToChoose, int numCardsToDraw) {
    this.p = AbstractDungeon.player;
    setValues(this.p,  this.p, numCardsToChoose);
    this.numCardsToDraw = numCardsToDraw;

    this.amount = numCardsToChoose;

    this.actionType = ACTION_TYPE;
    this.duration = ACTION_DURATION;
  }

  public void update() {
    if (this.duration != ACTION_DURATION) {
      AbstractDungeon.actionManager.addToTop(new ChooseAndAddDiscardCardsToDeckAction(this.amount, true));
      AbstractDungeon.actionManager.addToBottom(new ShuffleIfNeededAction());
      AbstractDungeon.actionManager.addToBottom(new DrawTopCardAndSetCostForTurnAction(COST_FOR_TURN));
      this.tickDuration();
      this.isDone = true;
    }
    this.tickDuration();
  }
}