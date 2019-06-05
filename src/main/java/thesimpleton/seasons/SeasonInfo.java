package thesimpleton.seasons;

import com.megacrit.cardcrawl.cards.AbstractCard;
import thesimpleton.powers.utils.Crop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SeasonInfo {
  private List<Crop> cropsInSeason = new ArrayList<>();
  private Season season;

  public static CropsInSeasonStrategy RANDOM_CROP_BY_RARITY_STRATEGY;

  public SeasonInfo(Season season) {
    this(season, Collections.emptyList());
  }

  public SeasonInfo(Season season, CropsInSeasonStrategy strategy) {
    this(season, strategy.getCropSet());
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
    List<Predicate<Crop>> predicates = new ArrayList<>();
    public CropsInSeasonStrategy(List<Predicate<Crop>> predicates) {
      this.predicates.addAll(predicates);
    }

    public List<Crop> getCropSet() {
      List<Crop> cropsInSeason = new ArrayList<>();

      // TODO: be smart about this; i.e. attempt to find a matching set that meets the criteria
      for(Predicate predicate: this.predicates) {
        List<Crop> matchingCrops = Arrays.stream(Crop.values())
            .filter(c -> !cropsInSeason.contains(c))
            .filter(c -> predicate.test(c))
            .collect(Collectors.toList());

        // this will throw an error if no matching crops are found
        Collections.shuffle(matchingCrops);
        cropsInSeason.add(matchingCrops.get(0));
      }
      return cropsInSeason;
    }
  }

  static  {
    List<Predicate<Crop>> randomCropByRarityPredicates = new ArrayList<>();
    randomCropByRarityPredicates.add(c -> c.getCropInfo().rarity == AbstractCard.CardRarity.BASIC);
    randomCropByRarityPredicates.add(c -> c.getCropInfo().rarity == AbstractCard.CardRarity.COMMON);
    randomCropByRarityPredicates.add(c -> c.getCropInfo().rarity == AbstractCard.CardRarity.UNCOMMON);

    RANDOM_CROP_BY_RARITY_STRATEGY = new CropsInSeasonStrategy(randomCropByRarityPredicates);
  }
}
