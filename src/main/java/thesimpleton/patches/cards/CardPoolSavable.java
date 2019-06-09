package thesimpleton.patches.cards;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CardPoolSavable implements CustomSavable<List<String>> {
  final private List<AbstractCard> cardpool = new ArrayList<>();
  final Logger logger = TheSimpletonMod.logger;

  public CardPoolSavable() { }

  public CardPoolSavable(List<AbstractCard> cardpool) {
    logger.debug("CardPoolSavable :: instantiated with card pool. size:" + cardpool.size());
    this.cardpool.addAll(cardpool);
  }

  @Override
  public List<String> onSave() {
    logger.debug("CardPoolSavable.onSave :: called");

    final List<String> idList = cardpool.stream().map(c -> c.cardID).collect(Collectors.toList());

    logger.debug("CardPoolSavable.onSave :: Saving card pool. Cards:");
    int index = 0;
    for(AbstractCard card : cardpool) {
      logger.debug(index++ + ") " + card.name + " [cardId: " + card.cardID + "]");
    }

    return idList;
  }

  @Override
  public void onLoad(List<String> ids) {
    logger.debug("CardPoolSavable.onLoad :: Loading cards into card pool. Card ids:");
    int index = 0;
    for(String id : ids) {
      logger.debug(index++ + ") " + id);
    }

    if (ids == null && !ids.isEmpty()) {
//
//      for (String id : ids) {
//        TheSimpletonMod.addCardToLoadedCardPool(CardLibrary.getCard(id));
//      }

      int cardIndex = 0;
      logger.info("CardPoolSavable.onLoad :: Loading cards for card pool from save:");
      for (String id : ids) {
        AbstractCard card = CardLibrary.getCard(id);
        this.cardpool.add(card);
        logger.debug(cardIndex++ + ") " + card.name + " [cardId: " + card.cardID + "]");
      }

      if (!TheSimpletonMod.getSaveCardPool().isEmpty()) {
        logger.info("CardPoolSavable.onLoad :: Card pool loaded from save with " + ids.size() + " cards. Initializing");
        CardCrawlGame.dungeon.initializeCardPools();
      }
    } else {
      logger.info("CardPoolSavable.onLoad :: no save data found");
    }
  }

  public List<AbstractCard> getCardpool() {
    return Collections.unmodifiableList(cardpool);
  }
}
