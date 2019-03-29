package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.CurseUtil;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.powers.AbstractCropPower;

import java.util.List;
import java.util.stream.Collectors;

public class TillingAction extends AbstractGameAction {
  private AbstractPlayer p;

  private AbstractGameAction.ActionType ACTION_TYPE = ActionType.CARD_MANIPULATION;

  public TillingAction(int numPowers) {
    this.p = AbstractDungeon.player;
    setValues(this.p, AbstractDungeon.player, numPowers);
    this.actionType = ACTION_TYPE;
    this.duration = Settings.ACTION_DUR_MED;
  }

  public void update() {
    if (this.duration != Settings.ACTION_DUR_MED) {
      TheSimpletonMod.logger.debug(this.getClass().getSimpleName() + ".update " +
          "this.duration != Settings.ACTION_DUR_MED");

      if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
        TheSimpletonMod.logger.debug(this.getClass().getSimpleName() + ".update found one or more selected cards");
        //TODO: clean up: find the one (first) selected card and add it
        for (final AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
          c.unhover();
          TheSimpletonMod.logger.debug("Selected card: " + c.name + ". Returning");

          AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(c, 1));
        }
        this.p.hand.refreshHandLayout();
        this.tickDuration();
        this.isDone = true;
        return;
      }
    }

    final CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    List<AbstractCropPowerCard> cards = AbstractCropPowerCard.getRandomCropPowerCards(this.amount);

    TheSimpletonMod.logger.debug("Randomly selected " + cards.size() + " crop power cards: ", cards.stream().map(c -> c.name)
        .collect(Collectors.joining(", ")));

    for (final AbstractCropPowerCard c2 : cards) {
      cardGroup.addToRandomSpot(c2);
    }

    if (cardGroup.size() == 0) {
      TheSimpletonMod.logger.debug("No card to select. Returning.");
      this.isDone = true;
      return;
    }
    if (cardGroup.size() == 1) {
      final AbstractCard c3 = cardGroup.getTopCard();
      TheSimpletonMod.logger.debug("Only one card to select: " + c3.name + ". Returning");
      c3.unhover();
      AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(c3, 1));
      this.isDone = true;
      return;
    }
    // TODO: move to localized strings
    AbstractDungeon.gridSelectScreen.open(cardGroup, 1, "Select a power card.", false);
    this.tickDuration();
  }
}