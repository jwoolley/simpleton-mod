package thesimpleton.seasons;

import com.megacrit.cardcrawl.cards.AbstractCard;
import thesimpleton.crops.Crop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

public class SeasonInfo {
  private List<Crop> cropsInSeason = new ArrayList<>();
  private Season season;

  public static  SeasonCropSetDefinition RANDOM_CROP_SET_BY_RARITY;
  public static SeasonCropSetDefinition RANDOM_CROP_SET_BY_RARE_CROP_RARITY;
  public static SeasonCropSetDefinition UNLOCK_CROP_SET_0;
  public static SeasonCropSetDefinition UNLOCK_CROP_SET_1;
  public static SeasonCropSetDefinition UNLOCK_CROP_SET_2;
  public static SeasonCropSetDefinition UNLOCK_CROP_SET_3;

  public SeasonInfo(Season season) {
    this(season, Collections.emptyList());
  }

  public SeasonInfo(SeasonCropSetDefinition seasonCropSetDefinition) {
    this(seasonCropSetDefinition.getSeason(), seasonCropSetDefinition);
  }

  public SeasonInfo(Season season, SeasonCropSetDefinition seasonCropSetDefinition) {
    this(season, seasonCropSetDefinition.getCropsInSeasonStrategy().getCropSet(season));
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

  static  {
    List<BiPredicate<Season,Crop>> randomCropByRarityPredicates = new ArrayList<>();
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

    List<Crop>  unlockedCropsLevel0 = new ArrayList<>();
    unlockedCropsLevel0.add(Crop.POTATOES);
    unlockedCropsLevel0.add(Crop.CORN);
    unlockedCropsLevel0.add(Crop.ASPARAGUS);
    UNLOCK_CROP_SET_0 = new UnlockableSeasonCropSetDefinition(Season.AUTUMN, unlockedCropsLevel0);

    List<Crop>  unlockedCropsLevel1 = new ArrayList<>();
    unlockedCropsLevel1.add(Crop.SQUASH);
    unlockedCropsLevel1.add(Crop.ARTICHOKES);
    unlockedCropsLevel1.add(Crop.SPINACH);
    UNLOCK_CROP_SET_1 = new UnlockableSeasonCropSetDefinition(Season.WINTER, unlockedCropsLevel1);

    List<Crop>  unlockedCropsLevel2 = new ArrayList<>();
    unlockedCropsLevel2.add(Crop.ONIONS);
    unlockedCropsLevel2.add(Crop.COFFEE);
    unlockedCropsLevel2.add(Crop.STRAWBERRIES);
    UNLOCK_CROP_SET_2 = new UnlockableSeasonCropSetDefinition(Season.SPRING, unlockedCropsLevel2);

    List<Crop>  unlockedCropsLevel3 = new ArrayList<>();
    unlockedCropsLevel3.add(Crop.ONIONS);
    unlockedCropsLevel3.add(Crop.TURNIPS);
    unlockedCropsLevel3.add(Crop.CHILIS);
    UNLOCK_CROP_SET_3 = new UnlockableSeasonCropSetDefinition(Season.SUMMER, unlockedCropsLevel3);
  }
}
