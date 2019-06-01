package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;

import java.util.List;

public class CropDiversityAction extends AbstractGameAction {
  private static int COST_FOR_TURN = 0;

  private AbstractPlayer p;
  private AbstractGameAction.ActionType ACTION_TYPE = ActionType.CARD_MANIPULATION;

  public CropDiversityAction(int numPowers) {
    TheSimpletonMod.logger.debug("CropDiversityAction: constructing with numPowers: " + numPowers);

    this.p = AbstractDungeon.player;
    setValues(this.p,  this.p, numPowers);

    TheSimpletonMod.logger.debug("CropDiversityAction: constructor setValues. this.amount: " + this.amount);

    this.actionType = ACTION_TYPE;
    this.amount = numPowers;
    this.duration = Settings.ACTION_DUR_MED;

    TheSimpletonMod.logger.debug("CropDiversityAction: constructed. this.amount: " + this.amount);

  }

  public void update() {
    if (this.duration != Settings.ACTION_DUR_MED) {
      if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
        //TODO: clean up: find the one (first) selected card and add it
        for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
          c.unhover();
          c.setCostForTurn(COST_FOR_TURN);
          AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(c, 1));
        }
        AbstractDungeon.gridSelectScreen.selectedCards.clear();
        this.p.hand.refreshHandLayout();

        this.tickDuration();
        this.isDone = true;
        return;
      }
    }

    final CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    List<AbstractCropPowerCard> cards = AbstractCropPowerCard.getRandomCropPowerCards(this.amount, true);

    for (final AbstractCropPowerCard c2 : cards) {
      cardGroup.addToRandomSpot(c2);
    }

    if (cardGroup.size() == 0) {
      this.isDone = true;
      return;
    }
    if (cardGroup.size() == 1) {
      final AbstractCard c3 = cardGroup.getTopCard();
      c3.unhover();
      AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(c3, 1));
      this.isDone = true;
      return;
    }
    // TODO: move to localized strings
    AbstractDungeon.gridSelectScreen.open(cardGroup, 1, "Select a card.", false);
    this.tickDuration();
  }
}