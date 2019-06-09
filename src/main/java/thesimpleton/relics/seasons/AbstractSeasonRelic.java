package thesimpleton.relics.seasons;

import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.seasons.Season;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractSeasonRelic extends CustomRelic implements CustomSavable<List<String>> {
  private static final RelicTier TIER = RelicTier.STARTER;
  private static final LandingSound SOUND = LandingSound.MAGICAL;

  final private List<AbstractCard> cardpool = new ArrayList<>();
  final private Season season;
  final Logger logger = TheSimpletonMod.logger;

  public AbstractSeasonRelic(String id, String imagePath, String largeImagePath, String outlineImagePath, Season season) {
    super(id, new Texture(TheSimpletonMod.getResourcePath(imagePath)),
        new Texture(TheSimpletonMod.getResourcePath(outlineImagePath)), TIER, SOUND);

    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getResourcePath(largeImagePath));
    this.season = season;

    // TODO: make this unremoveable if possible

     logger.debug(this.getClass().getSimpleName() + " intantiated " + this);
  }

  @Override
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0]; // list in-season crops etc.
  }

  @Override
  public void onEquip() {
    logger.debug(this.getClass().getSimpleName() + " onEquip called for " + this);
  }

  @Override
  public List<String> onSave() {

    List<String> idList = new ArrayList<>();

    if (SimpletonUtil.getPlayer().hasRelic(this.relicId) && SimpletonUtil.getPlayer().getRelic(this.relicId) == this) {

      List<AbstractCard> cardPool = new ArrayList<>();
      cardPool.addAll(AbstractDungeon.commonCardPool.group);
      cardPool.addAll(AbstractDungeon.uncommonCardPool.group);
      cardPool.addAll(AbstractDungeon.rareCardPool.group);

      logger.debug(this.getClass().getSimpleName() + ".onSave :: called for " + this);

      idList.addAll(cardPool.stream().map(c -> c.cardID).collect(Collectors.toList()));

      logger.debug(this.getClass().getSimpleName() + ".onSave :: Saving card pool. Cards:");
      int index = 0;
      for (AbstractCard card : cardPool) {
        logger.debug(index++ + ") " + card.name + " [cardId: " + card.cardID + "]");
      }
    }

    return idList;
  }

  @Override
  public void onLoad(List<String> ids) {
    TheSimpletonMod.setSeasonRelic(this);

    if (ids != null && !ids.isEmpty()) {

      logger.debug(this.getClass().getSimpleName() + ".onLoad :: Loading cards into card pool. Card ids:");
      int index = 0;
      for(String id : ids) {
        logger.debug(index++ + ") " + id);
      }

      int cardIndex = 0;
      logger.info(this.getClass().getSimpleName() + ".onLoad :: Loading cards for card pool from save:");
      for (String id : ids) {
        if (!this.cardpool.stream().anyMatch(c -> c.cardID == id)) {
          AbstractCard card = CardLibrary.getCard(id);
          this.cardpool.add(card);
          logger.debug(cardIndex++ + ") " + card.name + " [cardId: " + card.cardID + "]");
        }
      }

      if (!TheSimpletonMod.getSaveCardPool().isEmpty()) {
        logger.info(this.getClass().getSimpleName() + ".onLoad :: Card pool loaded from save with " + ids.size() + " cards. Initializing");
        if (CardCrawlGame.dungeon != null) {
          CardCrawlGame.dungeon.initializeCardPools();
        } else {
          logger.info(this.getClass().getSimpleName() + ".onLoad :: dungeon is not yet initialized. Trusting it will happen eventually.");
        }

//        logger.debug(this.getClass().getSimpleName() + ".onLoad :: disabling season screen");
//        TheSimpletonMod.seasonScreen.dismiss();
      }
    } else {
      logger.info(this.getClass().getSimpleName() + ".onLoad :: no save data found");
    }
  }

  public void setCardPool(List<AbstractCard> cards) {
    logger.info(this.getClass().getSimpleName() + ".setCardPool :: setting card pool for " + this);
    int cardIndex = 0;
    for (AbstractCard card : cards) {
      this.cardpool.add(card);
      logger.debug(cardIndex++ + ") " + card.name + " [cardId: " + card.cardID + "]");
    }
    this.cardpool.clear();
    this.cardpool.addAll(cards);
  }

  public List<AbstractCard> getCardpool() {
    return Collections.unmodifiableList(cardpool);
  }

  public AbstractSeasonRelic makeCopy() {
    return this;
  }

  @Override
  public void atBattleStartPreDraw() {
    super.atBattleStartPreDraw();
    List<AbstractCard> cardPool = new ArrayList<>();
    cardPool.addAll(AbstractDungeon.commonCardPool.group);
    cardPool.addAll(AbstractDungeon.uncommonCardPool.group);
    cardPool.addAll(AbstractDungeon.rareCardPool.group);
    cardPool = cardpool.stream().distinct().collect(Collectors.toList());

    logger.debug(this.getClass().getSimpleName() + ".atBattleStartPreDraw :: LISTING CARD POOL");

    int cardIndex = 0;
    for (AbstractCard card : cardPool) {
      this.cardpool.add(card);
      logger.debug(cardIndex++ + ") " + card.name + " [cardId: " + card.cardID + "]");
    }
  }

  public static AbstractSeasonRelic getSeasonRelic(Season season) {
    switch (season) {
      case FALL:
        return new AutumnSeasonRelic();
      case WINTER:
      case SPRING:
      case SUMMER:
      default:
        return new AutumnSeasonRelic();
    }
  }
}