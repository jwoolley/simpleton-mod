package thesimpleton.savedata;
import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.StartGameSubscriber;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CardPoolCustomSavable implements CustomSavable<List<String>> {
  final private List<AbstractCard> cardpool = new ArrayList<>();
  final Logger logger = TheSimpletonMod.logger;

  public CardPoolCustomSavable() {
    logger.debug( this.getClass().getSimpleName() + " instantiated");
  }

  @Override
  public List<String> onSave() {
    this.cardpool.clear();

    registerSaveId();

    logger.debug(getLogPrefix("onSave") + " called");

    List<AbstractCard> currentCardPool = TheSimpletonMod.getCurrentCardPool();

    final List<String> idList = currentCardPool.stream().map(c -> c.cardID).collect(Collectors.toList());

    logger.debug(getLogPrefix("onSave") + " Saving card pool. Cards:");
    int index = 0;
    for(AbstractCard card : currentCardPool) {
      logger.debug(index++ + ") " + card.name + " [cardId: " + card.cardID + "]");
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
    if (ids != null && !ids.isEmpty()) {

      int cardIndex = 0;
      logger.info(getLogPrefix("onLoad") + " Loading cards for card pool from save:");
      for (String id : ids) {
        AbstractCard card = CardLibrary.getCard(id);
        this.cardpool.add(card);
        logger.debug(cardIndex++ + ") " + card.name + " [cardId: " + card.cardID + "]");
      }

      if (!TheSimpletonMod.getSaveCardPool().isEmpty()) {
        logger.info( getLogPrefix("onLoad") + " Card pool loaded from save with " + ids.size() + " cards. Initializing");
        if (CardCrawlGame.dungeon != null) {
          CardCrawlGame.dungeon.initializeCardPools();
        } else {
          logger.info(getLogPrefix("onLoad") + " dungeon is not yet initialized. Trusting it will happen eventually.");
        }
      } else {
        logger.info(getLogPrefix("onLoad") + " no save data found");
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