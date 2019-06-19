package thesimpleton.seasons;

import com.megacrit.cardcrawl.cards.AbstractCard;
import thesimpleton.crops.Crop;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public class RandomSeasonCropSetDefinition implements SeasonCropSetDefinition  {
  public static  SeasonCropSetDefinition RANDOM_CROP_SET_BY_RARITY;
  public static SeasonCropSetDefinition RANDOM_CROP_SET_BY_RARE_CROP_RARITY;

  private final CropsInSeasonStrategy strategy;
  private final Season season;

  public RandomSeasonCropSetDefinition(CropsInSeasonStrategy strategy) {
    this.strategy = strategy;
    this.season = Season.randomSeason();
  }

  @Override
  public Season getSeason() {
    return season;
  }

  @Override
  public CropsInSeasonStrategy getCropsInSeasonStrategy() {
    return strategy;
  }

  static  {
    List<BiPredicate<Season, Crop>> randomCropByRarityPredicates = new ArrayList<>();
    randomCropByRarityPredicates.add((s, c) -> c.getCropInfo().rarity == AbstractCard.CardRarity.BASIC);
    randomCropByRarityPredicates.add((s, c) -> c.getCropInfo().rarity == AbstractCard.CardRarity.COMMON);
    randomCropByRarityPredicates.add((s, c) -> c.getCropInfo().rarity == AbstractCard.CardRarity.UNCOMMON);
    RANDOM_CROP_SET_BY_RARITY = new RandomSeasonCropSetDefinition(
        new CropsInSeasonStrategy(randomCropByRarityPredicates));

    List<BiPredicate<Season,Crop>>  randomCropRareBySeasonPredicates = new ArrayList<>();
    randomCropRareBySeasonPredicates.add((s, c) -> c.getCropInfo().rarity == AbstractCard.CardRarity.BASIC);
    randomCropRareBySeasonPredicates.add((s, c) -> c.getCropInfo().rarity == AbstractCard.CardRarity.COMMON);
    randomCropRareBySeasonPredicates.add((s, c) -> s.associatedCrop != null && c == s.associatedCrop);
    RANDOM_CROP_SET_BY_RARE_CROP_RARITY = new RandomSeasonCropSetDefinition(
        new CropsInSeasonStrategy(randomCropRareBySeasonPredicates));
  }
}
