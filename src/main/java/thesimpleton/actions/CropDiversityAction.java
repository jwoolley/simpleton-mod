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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CropDiversityAction extends AbstractGameAction {
  private static int COST_FOR_TURN = 0;

  private AbstractPlayer p;
  private AbstractGameAction.ActionType ACTION_TYPE = ActionType.CARD_MANIPULATION;

  public CropDiversityAction(int numPowers) {
    TheSimpletonMod.logger.info("CropDiversityAction: constructing with numPowers: " + numPowers);

    this.p = AbstractDungeon.player;
    setValues(this.p,  this.p, numPowers);

    TheSimpletonMod.logger.info("CropDiversityAction: constructor setValues. this.amount: " + this.amount);

    this.actionType = ACTION_TYPE;
    this.amount = numPowers;
    this.duration = Settings.ACTION_DUR_MED;

    TheSimpletonMod.logger.info("CropDiversityAction: constructed. this.amount: " + this.amount);

  }

  public void update() {
    List<AbstractCard> allCardPoolDebug = new ArrayList<>();

    if (this.duration != Settings.ACTION_DUR_MED) {
      if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
        //TODO: clean up: find the one (first) selected card and add it
        for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
          c.unhover();
          c.setCostForTurn(COST_FOR_TURN);
          AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(c, 1));
        }

        TheSimpletonMod.logger.info("CropDiversityAction::update [4] CardPool | commons:"
            + AbstractDungeon.commonCardPool.size() + "; uncommon: "
            + AbstractDungeon.uncommonCardPool.size() +  "; rare: "
            + AbstractDungeon.rareCardPool.size());
        allCardPoolDebug.clear();
        allCardPoolDebug.addAll(AbstractDungeon.commonCardPool.group);
        allCardPoolDebug.addAll(AbstractDungeon.uncommonCardPool.group);
        allCardPoolDebug.addAll(AbstractDungeon.rareCardPool.group);
        TheSimpletonMod.logger.info("CropDiversityAction::update [4] CardPool | CROPS:"
            + allCardPoolDebug.stream().filter(c -> c instanceof  AbstractCropPowerCard).map(c -> c.name).collect(Collectors.joining(", ")));



        AbstractDungeon.gridSelectScreen.selectedCards.clear();
        this.p.hand.refreshHandLayout();

        TheSimpletonMod.logger.info("CropDiversityAction::update [5] CardPool | commons:"
            + AbstractDungeon.commonCardPool.size() + "; uncommon: "
            + AbstractDungeon.uncommonCardPool.size() + "; rare:"
            + AbstractDungeon.rareCardPool.size());
        allCardPoolDebug.clear();
        allCardPoolDebug.addAll(AbstractDungeon.commonCardPool.group);
        allCardPoolDebug.addAll(AbstractDungeon.uncommonCardPool.group);
        allCardPoolDebug.addAll(AbstractDungeon.rareCardPool.group);
        TheSimpletonMod.logger.info("CropDiversityAction::update [5] CardPool | CROPS:"
            + allCardPoolDebug.stream().filter(c -> c instanceof  AbstractCropPowerCard).map(c -> c.name).collect(Collectors.joining(", ")));




        this.tickDuration();
        this.isDone = true;
        return;
      }
    }



    TheSimpletonMod.logger.info("CropDiversityAction::update [0] CardPool | commons:"
        + AbstractDungeon.commonCardPool.size() + "; uncommon: "
        + AbstractDungeon.uncommonCardPool.size() + "; rare: "
        + AbstractDungeon.rareCardPool.size());
    allCardPoolDebug.clear();
    allCardPoolDebug.addAll(AbstractDungeon.commonCardPool.group);
    allCardPoolDebug.addAll(AbstractDungeon.uncommonCardPool.group);
    allCardPoolDebug.addAll(AbstractDungeon.rareCardPool.group);
    TheSimpletonMod.logger.info("CropDiversityAction::update [0] CardPool | CROPS:"
            + allCardPoolDebug.stream().filter(c -> c instanceof  AbstractCropPowerCard).map(c -> c.name).collect(Collectors.joining(", ")));


    final CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    List<AbstractCropPowerCard> cards = AbstractCropPowerCard.getRandomCropPowerCards(this.amount, true);

    TheSimpletonMod.logger.info("CropDiversityAction::update [1] CardPool | commons:"
        + AbstractDungeon.commonCardPool.size() + "; uncommon: "
        + AbstractDungeon.uncommonCardPool.size() + "; rare: "
        + AbstractDungeon.rareCardPool.size());
    allCardPoolDebug.clear();
    allCardPoolDebug.addAll(AbstractDungeon.commonCardPool.group);
    allCardPoolDebug.addAll(AbstractDungeon.uncommonCardPool.group);
    allCardPoolDebug.addAll(AbstractDungeon.rareCardPool.group);
    TheSimpletonMod.logger.info("CropDiversityAction::update [1] CardPool | CROPS:"
        + allCardPoolDebug.stream().filter(c -> c instanceof  AbstractCropPowerCard).map(c -> c.name).collect(Collectors.joining(", ")));


    for (final AbstractCropPowerCard c2 : cards) {
      cardGroup.addToRandomSpot(c2);
    }

    TheSimpletonMod.logger.info("CropDiversityAction::update [2] CardPool | commons:"
        + AbstractDungeon.commonCardPool.size() + "; uncommon: "
        + AbstractDungeon.uncommonCardPool.size() +  "; rare: "
        + AbstractDungeon.rareCardPool.size());
    allCardPoolDebug.clear();
    allCardPoolDebug.addAll(AbstractDungeon.commonCardPool.group);
    allCardPoolDebug.addAll(AbstractDungeon.uncommonCardPool.group);
    allCardPoolDebug.addAll(AbstractDungeon.rareCardPool.group);
    TheSimpletonMod.logger.info("CropDiversityAction::update [2] CardPool | CROPS:"
        + allCardPoolDebug.stream().filter(c -> c instanceof  AbstractCropPowerCard).map(c -> c.name).collect(Collectors.joining(", ")));




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

    TheSimpletonMod.logger.info("CropDiversityAction::update [3] CardPool | commons:"
        + AbstractDungeon.commonCardPool.size() + "; uncommon: "
        + AbstractDungeon.uncommonCardPool.size() +  "; rare: "
        + AbstractDungeon.rareCardPool.size());
    allCardPoolDebug.clear();
    allCardPoolDebug.addAll(AbstractDungeon.commonCardPool.group);
    allCardPoolDebug.addAll(AbstractDungeon.uncommonCardPool.group);
    allCardPoolDebug.addAll(AbstractDungeon.rareCardPool.group);
    TheSimpletonMod.logger.info("CropDiversityAction::update [3] CardPool | CROPS:"
        + allCardPoolDebug.stream().filter(c -> c instanceof  AbstractCropPowerCard).map(c -> c.name).collect(Collectors.joining(", ")));



    this.tickDuration();
  }
}