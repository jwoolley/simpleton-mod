package thesimpleton.seasons;

import com.megacrit.cardcrawl.cards.AbstractCard;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.Crop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SeasonInfo {
  private List<Crop> cropsInSeason = new ArrayList<>();
  private Season season;

  public static CropsInSeasonStrategy RANDOM_CROP_BY_RARITY_STRATEGY;
  public static CropsInSeasonStrategy RANDOM_SEASON_BY_RARE_CROP_STRATEGY;

  public SeasonInfo(Season season) {
    this(season, Collections.emptyList());
  }

  public SeasonInfo(Season season, CropsInSeasonStrategy strategy) {
    this(season, strategy.getCropSet(season));
  }

  public SeasonInfo(Season season, List<Crop> cropsInSeason) {
    this.season = season;
    this.cropsInSeason.addAll(cropsInSeason);
  }

  public Season getSeason() {
    return this.season;
  }

  public List<Crop> getCropsInSeason() {
    return new ArrayList<>(this.cropsInSeason);
  }

  static class CropsInSeasonStrategy {
    List<BiPredicate<Season,Crop>> predicates = new ArrayList<>();
    public CropsInSeasonStrategy(List<BiPredicate<Season,Crop>> predicates) {
      this.predicates.addAll(predicates);
    }

    public List<Crop> getCropSet(Season season) {
      List<Crop> cropsInSeason = new ArrayList<>();

      // TODO: be smart about this; i.e. attempt to find a matching set that meets the criteria
      for(BiPredicate predicate: this.predicates) {
        List<Crop> matchingCrops = Arrays.stream(Crop.values())
            .filter(c -> !cropsInSeason.contains(c))
            .filter(c -> predicate.test(season,c))
            .collect(Collectors.toList());

        Collections.shuffle(matchingCrops);
        cropsInSeason.add(matchingCrops.get(0));
      }
      return cropsInSeason;
    }
  }

  static  {
    List<BiPredicate<Season,Crop>> randomCropByRarityPredicates = new ArrayList<>();
    randomCropByRarityPredicates.add((s, c) -> c.getCropInfo().rarity == AbstractCard.CardRarity.BASIC);
    randomCropByRarityPredicates.add((s, c) -> c.getCropInfo().rarity == AbstractCard.CardRarity.COMMON);
    randomCropByRarityPredicates.add((s, c) -> c.getCropInfo().rarity == AbstractCard.CardRarity.UNCOMMON);
    RANDOM_CROP_BY_RARITY_STRATEGY = new CropsInSeasonStrategy(randomCropByRarityPredicates);

    List<BiPredicate<Season,Crop>>  randomCropRareBySeasonPredicates = new ArrayList<>();
    randomCropRareBySeasonPredicates.add((s, c) -> c.getCropInfo().rarity == AbstractCard.CardRarity.BASIC);
    randomCropRareBySeasonPredicates.add((s, c) -> c.getCropInfo().rarity == AbstractCard.CardRarity.COMMON);
    randomCropRareBySeasonPredicates.add((s, c) -> s.associatedCrop != null && c == s.associatedCrop);
    RANDOM_SEASON_BY_RARE_CROP_STRATEGY = new CropsInSeasonStrategy(randomCropRareBySeasonPredicates);
  }
}
