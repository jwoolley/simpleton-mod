package thesimpleton.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

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
    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card.makeStatEquivalentCopy(),
        Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F * Settings.scale, Settings.HEIGHT / 2.0F));
  }

  public static void gainCards(AbstractCard card1, AbstractCard card2) {
    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card1.makeStatEquivalentCopy(),
        Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F - 20.0F * Settings.scale, Settings.HEIGHT / 2.0F));
    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card2.makeStatEquivalentCopy(),
        Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F + 20.0F * Settings.scale, Settings.HEIGHT / 2.0F));
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
            cards.get(0).makeStatEquivalentCopy()));
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

}
