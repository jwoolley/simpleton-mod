package thesimpleton.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.ui.SettingsHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SimpletonEventHelper {
  public enum EventState {
    WAITING,
    LEAVING
  }

  public static String getUiPath(String id) {
    return "events/" + id + ".png";
  }

  public static int getIntInRange(int min, int max)  {
    return AbstractDungeon.miscRng.random(min, max);
  }

  public static int getGoldCost(int minCost, int maxCost)  {
    if (AbstractDungeon.player.gold < minCost) {
      return 0;
    } else if (AbstractDungeon.player.gold > maxCost) {
      return AbstractDungeon.miscRng.random(minCost, maxCost);
    }
    return AbstractDungeon.miscRng.random(minCost, AbstractDungeon.player.gold);
  }

  public static AbstractCard  getRandomNonCurseCardFromDeck() {
    return getRandomCardFromDeck(c -> c.type != AbstractCard.CardType.CURSE);
  }

  public static AbstractCard  getRandomNonBasicFromDeck() {
    return getRandomCardFromDeck(c -> c.rarity != AbstractCard.CardRarity.BASIC && c.type != AbstractCard.CardType.CURSE);
  }

  public static AbstractCard getRandomCardFromDeck(Predicate<AbstractCard> predicate) {
    List<AbstractCard> list = AbstractDungeon.player.masterDeck.group.stream()
        .filter(predicate).collect(Collectors.toList());

    if (list.isEmpty()) {
      return null;
    }

    Collections.shuffle(list, new java.util.Random(AbstractDungeon.miscRng.randomLong()));
    return list.get(0);
  }

  public static void gainCard(AbstractCard card) {
    gainCard(card, (Settings.WIDTH) / 2.0F * SettingsHelper.getScaleX(),
        Settings.HEIGHT / 2.0F * SettingsHelper.getScaleY());
  }


  public static void gainCard(AbstractCard card, float cardX, float cardY) {
    AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(card.makeStatEquivalentCopy(), cardX, cardY));
  }

  public static void loseCard(AbstractCard card) {
    AbstractDungeon.effectsQueue.add(new PurgeCardEffect(card));
    AbstractDungeon.player.masterDeck.removeCard(card);
  }

  public static void loseCard(AbstractCard card, float cardX, float cardY) {
    AbstractDungeon.effectsQueue.add(new PurgeCardEffect(card, cardX, cardY));
    AbstractDungeon.player.masterDeck.removeCard(card);
  }

  public static void gainCards(AbstractCard card1, AbstractCard card2) {
    AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(card1.makeStatEquivalentCopy(),
        Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F - 20.0F * Settings.scale, Settings.HEIGHT / 2.0F));
    AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(card2.makeStatEquivalentCopy(),
        Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F + 20.0F * Settings.scale, Settings.HEIGHT / 2.0F));
  }

  public static void gainCards(AbstractCard card1, AbstractCard card2, AbstractCard card3) {
    AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(card1.makeStatEquivalentCopy(),
        Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH - 20.0F * Settings.scale, Settings.HEIGHT / 2.0F));

    AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(card2.makeStatEquivalentCopy(),
        (Settings.WIDTH) / 2.0F * SettingsHelper.getScaleX(),
        Settings.HEIGHT / 2.0F * SettingsHelper.getScaleY()));

    AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(card3.makeStatEquivalentCopy(),
        Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH + 20.0F * Settings.scale, Settings.HEIGHT / 2.0F));
  }


  public static AbstractCard getRandomUpgradeableCard() {
    return getRandomUpgradableCards(1).get(0);
  }

  public static List <AbstractCard> getRandomUpgradableCards(int numCards) {
    return getRandomUpgradableCards(numCards, c -> c.type != AbstractCard.CardType.CURSE);
  }

  public static List <AbstractCard>  getRandomUpgradableCards(int numCards, Predicate<AbstractCard> predicate) {
    List <AbstractCard> upgradeableCards = AbstractDungeon.player.masterDeck.group.stream()
        .filter(c ->  c.canUpgrade() && predicate.test(c)).collect(Collectors.toList());
    Collections.shuffle(upgradeableCards, new java.util.Random(AbstractDungeon.miscRng.randomLong()));
    return upgradeableCards.subList(0, numCards);
  }

  public static AbstractCropPowerCard getSeasonalCropPowerCard(AbstractCard.CardRarity rarity,
                                                               AbstractCropPowerCard defaultCard) {
    return TheSimpletonMod.getSeasonalCropCards()
            .stream().filter(c -> c.rarity == rarity)
            .findFirst()
            .orElse(defaultCard);
  }


  private static AbstractCard getRandomCardFromPool(AbstractCard.CardRarity rarity) {
    final AbstractCard attackCard = AbstractDungeon.getCardFromPool(rarity, AbstractCard.CardType.ATTACK, true);

    final AbstractCard skillCard = AbstractDungeon.getCardFromPool(rarity, AbstractCard.CardType.SKILL, true);

    final AbstractCard powerCard = AbstractDungeon.getCardFromPool(rarity, AbstractCard.CardType.POWER, true);

    final List<AbstractCard> rareCards =  new ArrayList<>();
    if (attackCard != null) {
      rareCards.add(attackCard);
    }
    if (skillCard != null) {
      rareCards.add(skillCard);
    }
    if (powerCard != null) {
      rareCards.add(powerCard);
    }
    Collections.shuffle(rareCards);
    return rareCards.get(0);
  }

  public static AbstractCard getRandomCommonCardFromPool() {
    return getRandomCardFromPool(AbstractCard.CardRarity.COMMON);
  }

  public static AbstractCard getRandomUncommonCardFromPool() {
    return getRandomCardFromPool(AbstractCard.CardRarity.UNCOMMON);
  }

  public static AbstractCard getRandomRareCardFromPool() {
    return getRandomCardFromPool(AbstractCard.CardRarity.RARE);
  }

  // TODO: Handle case where no upgradable cards
  public static void upgradeCard(AbstractCard card) {
    upgradeCards(Arrays.asList(card));
  }

  public static void upgradeCards(List<AbstractCard> cards) {
    if (cards.size() > 2) {
      throw new IllegalArgumentException("Upgrading more than 2 cards isn't currently supported");
    }

    if (!cards.isEmpty()) {
      cards.get(0).upgrade();
      AbstractDungeon.player.bottledCardUpgradeCheck(cards.get(0));

      if (cards.size() == 1) {
        AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(
            cards.get(0).makeStatEquivalentCopy(),
            Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F,
            Settings.HEIGHT / 2.0F));

        AbstractDungeon.topLevelEffects.add(
            new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
      } else {
        cards.get(1).upgrade();
        AbstractDungeon.player.bottledCardUpgradeCheck(cards.get(1));

        AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(
            cards.get(0).makeStatEquivalentCopy(),
            Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F - 20.0F * Settings.scale,
            Settings.HEIGHT / 2.0F));

        AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(
            cards.get(1).makeStatEquivalentCopy(),
            Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F + 20.0F * Settings.scale,
            Settings.HEIGHT / 2.0F));

        AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(
            Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F - 20.0F * Settings.scale,
            Settings.HEIGHT / 2.0F));

        AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(
            Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F + 20.0F * Settings.scale,
            Settings.HEIGHT / 2.0F));

      }
    }
  }

  public static final List<String> SIMPLETON_EVENT_IDS;
  public static final List<String> SIMPLETON_ONLY_EVENT_IDS;

  public static void receiveRelic(AbstractRelic relic) {
    AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH * 0.28F, Settings.HEIGHT / 2.0F, relic);
    SimpletonUtil.removeRelicFromPool(relic);
  }

  static {
    SIMPLETON_EVENT_IDS = Arrays.asList(
        BorealisEvent.ID,
        EarlyThawEvent.ID,
        EquipmentShedEvent.ID,
        GophersEvent.ID,
        HeatWaveEvent.ID,
        FirefliesEvent.ID,
        ReaptideEvent.ID,
        SnowedInEvent.ID
    );

    SIMPLETON_ONLY_EVENT_IDS = Arrays.asList(
        HeatWaveEvent.ID,
        ReaptideEvent.ID,
        SnowedInEvent.ID
    );
  }
}
