package thesimpleton.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.omg.PortableInterceptor.ACTIVE;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.curse.SimpletonCurse;
import thesimpleton.cards.skill.Cultivate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SimpletonCardHelper {
  public static boolean isCardInHand(AbstractCard card) {
    return (AbstractDungeon.player != null) && (AbstractDungeon.player.hand.contains(card));
  }

  public static List<AbstractCard> getCurrentCardPool() {
    List<AbstractCard> cardPool = new ArrayList<>();
    cardPool.addAll(AbstractDungeon.commonCardPool.group);
    cardPool.addAll(AbstractDungeon.uncommonCardPool.group);
    cardPool.addAll(AbstractDungeon.rareCardPool.group);

    if (TheSimpletonMod.isPlayingAsSimpleton() || TheSimpletonMod.ConfigData.enableCursesForAllCharacters) {
      cardPool.addAll(AbstractDungeon.curseCardPool.group.stream()
          .filter(c -> c instanceof SimpletonCurse).collect(Collectors.toList()));
    }

    return Collections.unmodifiableList(cardPool);
  }

  private static List<AbstractCard> harvestCards;
  public static List<AbstractCard> getHarvestCards() {
    if (harvestCards == null) {
      harvestCards = new ArrayList<>(getCurrentCardPool().stream()
          .filter(c -> c.hasTag(TheSimpletonCardTags.HARVEST))
          .map(c -> c.makeCopy())
          .collect(Collectors.toList()));
      harvestCards.add(new Cultivate());
    }

    return new ArrayList<>(harvestCards);
  }
}