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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SoilSampleAction extends AbstractGameAction {
  private static int COST_FOR_TURN = 0;

  private AbstractPlayer p;
  private AbstractGameAction.ActionType ACTION_TYPE = ActionType.CARD_MANIPULATION;
  private final int numCardsToDraw;

  public SoilSampleAction(int numCardsToChoose, int numCardsToDraw) {
    this.p = AbstractDungeon.player;
    setValues(this.p,  this.p, numCardsToChoose);
    this.numCardsToDraw = numCardsToDraw;

    this.actionType = ACTION_TYPE;
    this.amount = numCardsToChoose;
    this.duration = Settings.ACTION_DUR_FASTER;
  }

  public void update() {
    // TODO: necessary to add isBattleEnding check? see GathererMod::SaveValuablesAction
    if (this.duration != Settings.ACTION_DUR_FASTER) {
      if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
        //TODO: clean up: find the one (first) selected card and add it

        for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
          this.p.discardPile.removeCard(c);
          this.p.hand.moveToDeck(c, true);
        }

        AbstractDungeon.gridSelectScreen.selectedCards.clear();

        this.tickDuration();
        this.isDone = true;
        return;
      }
    }

    CardGroup discardPile = this.p.discardPile;

    TheSimpletonMod.logger.debug(discardPile + TheSimpletonMod.cardListToString(discardPile.group));

    if (discardPile.isEmpty()) {
      this.isDone = true;
      return;
    }

    // TODO: move this text to localized strings
    AbstractDungeon.gridSelectScreen.open(discardPile, this.amount, "Select " + this.amount + " cards.", false);
    this.tickDuration();
  }
}