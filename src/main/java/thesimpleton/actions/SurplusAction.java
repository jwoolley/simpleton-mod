package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;

import java.util.List;

public class SurplusAction extends AbstractGameAction {
  private static final ActionType ACTION_TYPE = ActionType.SPECIAL;
  private static final float ACTION_DURATION = Settings.ACTION_DUR_FASTER;

  private AbstractPlayer p;

  private boolean secondTick;

  public SurplusAction(AbstractPlayer player, int numCardsToGain) {
    this.p = player;
    this.amount = numCardsToGain;
    setValues(this.p,  this.p, numCardsToGain);

    this.actionType = ACTION_TYPE;
    this.duration = ACTION_DURATION;
    this.secondTick = false;
  }

  public void update() {
    if (this.duration != ACTION_DURATION) {
      for (int i = 0; i < this.amount; i++) {
        AbstractCropPowerCard commonCropCard
            = AbstractCropPowerCard.getRandomCropPowerCard(false, c -> c.cropRarity == AbstractCard.CardRarity.BASIC);
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(commonCropCard));
      }
      this.isDone = true;
    }
    this.tickDuration();
  }
}