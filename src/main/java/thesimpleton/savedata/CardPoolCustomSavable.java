package thesimpleton.savedata;
import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonCardHelper;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.devtools.debugging.DebugLoggers;
import thesimpleton.utilities.ModLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CardPoolCustomSavable implements CustomSavable<List<String>> {
  final private List<AbstractCard> cardpool = new ArrayList<>();
  final ModLogger logger = TheSimpletonMod.debugLogger;

  public CardPoolCustomSavable() {
    logger.debug( this.getClass().getSimpleName() + " instantiated");
  }

  @Override
  public List<String> onSave() {
    this.cardpool.clear();

    if (!TheSimpletonMod.isPlayingAsSimpleton()) {
      return new ArrayList<>();
    }

    registerSaveId();

    logger.trace(getLogPrefix("onSave") + " called");

    List<AbstractCard> currentCardPool = SimpletonCardHelper.getCurrentCardPool();

    final List<String> idList = currentCardPool.stream().map(c -> c.cardID).collect(Collectors.toList());

//    logger.debug(getLogPrefix("onSave") + " Saving card pool. Cards:");
//    int index = 0;
//    for(AbstractCard card : currentCardPool) {
//      logger.debug(index++ + ") " + card.name + " [cardId: " + card.cardID + "]");
//    }

    for (AbstractCropPowerCard c : TheSimpletonMod.getSeasonalCropCards()) {
      if (idList.contains(c.cardID)) {
        idList.add(c.cardID);
      }
    }

    return idList;
  }

  public List<AbstractCard> getCardPool() {
    return Collections.unmodifiableList(this.cardpool);
  }

  // TODO: make abstract
  public String getCustomSaveKey(){
    return "TheSimpletonMod" + this.getClass().getSimpleName();
  }

  @Override
  public void onLoad(List<String> ids) {
    if (TheSimpletonMod.isPlayingAsSimpleton() && ids != null && !ids.isEmpty()) {
      DebugLoggers.LEAKY_CURSES_LOGGER.log(CardPoolCustomSavable.class, "onLoad() has been called");

      int cardIndex = 0;
      logger.trace(getLogPrefix("onLoad") + " Loading cards for card pool from save:");
      for (String id : ids) {
        // if card exists in the CardLibrary (BaseMod.addsCard() has been called, I think)
        if (CardLibrary.isACard(id)) {
          AbstractCard card = CardLibrary.getCard(id);
          this.cardpool.add(card);
          logger.trace(cardIndex++ + ") " + card.name + " [cardId: " + card.cardID + "]");
          DebugLoggers.LEAKY_CURSES_LOGGER.log(CardPoolCustomSavable.class, cardIndex + ")" + " added card to pool: " + card.name  + "(" + card.cardID + ")");

        } else {
          logger.trace(cardIndex++ + ") NOT IN CARD LIBRARY [cardId: " + id + "]");
          DebugLoggers.LEAKY_CURSES_LOGGER.log(CardPoolCustomSavable.class, cardIndex++ + ") NOT IN CARD LIBRARY [cardId: " + id + "]");
        }
      }

      if (!TheSimpletonMod.getSaveCardPool().isEmpty()) {
        logger.trace( getLogPrefix("onLoad") + " Card pool loaded from save with " + ids.size() + " cards. Initializing");
        if (CardCrawlGame.dungeon != null) {
          logger.trace( getLogPrefix("onLoad") + " Force calling initializeCardPools() for loaded save");

          CardCrawlGame.dungeon.initializeCardPools();
        } else {
          logger.trace(getLogPrefix("onLoad") + " dungeon is not yet initialized. Trusting it will happen eventually.");
        }
      } else {
        logger.trace(getLogPrefix("onLoad") + " no save data found");
      }
    }
  }

  private String getLogPrefix(String methodName) {
    return this.getClass().getSimpleName() + "." + methodName + " ::";
  }

  private void registerSaveId() {
    logger.debug( this.getClass().getSimpleName() + "::registerSaveId");
      logger.debug( this.getClass().getSimpleName() + "::registerSaveId registering customSaveKey: " + getCustomSaveKey());
      BaseMod.addSaveField(this.getCustomSaveKey(), this);
  }
}