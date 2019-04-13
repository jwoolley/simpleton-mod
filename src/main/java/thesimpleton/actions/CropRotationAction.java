package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class CropRotationAction extends AbstractGameAction {
  private static final ActionType ACTION_TYPE = ActionType.ENERGY;
  private static final float ACTION_DURATION = Settings.ACTION_DUR_XFAST;
  private static final int CARD_DRAW_AMOUNT = 1;

  private AbstractPlayer p;
  private final int numCropsToHarvest;
  private final int numCardsToGain;
  private final boolean reduceCost;

  public CropRotationAction(AbstractPlayer player, int numCropsToHarvest, int numCardsToGain, boolean reduceCost) {
    this.p = player;
    setValues(this.p,  this.p, numCropsToHarvest);
    this.numCropsToHarvest = numCropsToHarvest;
    this.numCardsToGain = numCardsToGain;
    this.amount = numCropsToHarvest;
    this.reduceCost = reduceCost;

    this.actionType = ACTION_TYPE;
    this.duration = ACTION_DURATION;
  }

  public void update() {
    if (this.duration != ACTION_DURATION) {
      AbstractDungeon.actionManager.addToTop(new ChooseAndAddHandCardsToDeckAction(this.numCropsToHarvest));
      AbstractDungeon.actionManager.addToBottom(new ShuffleAction(this.p.drawPile, true));

      if (this.reduceCost) {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.p, CARD_DRAW_AMOUNT));
      }

      AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.amount));

      this.tickDuration();
      this.isDone = true;
    }
    this.tickDuration();
  }
}