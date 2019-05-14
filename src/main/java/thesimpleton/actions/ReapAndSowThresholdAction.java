package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.cards.skill.Cultivate;
import thesimpleton.powers.AbstractCropPower;

public class ReapAndSowThresholdAction extends AbstractGameAction {
  private final boolean upgraded;

  public ReapAndSowThresholdAction(boolean upgraded) {
    this.actionType = ActionType.SPECIAL;
    this.upgraded = upgraded;
  }

  @Override
  public void update() {
    if (AbstractCropPower.getActiveCropPowers().stream().anyMatch(pow -> pow.isMature())) {
      AbstractCard card = new Cultivate();
      if (this.upgraded) {
        card.upgrade();
      }
      AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(card, 1));
    }
    this.isDone = true;
  }
}