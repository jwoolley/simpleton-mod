package thesimpleton.seasons.cropsetdefinitions;

import thesimpleton.crops.Crop;
import thesimpleton.seasons.CropsInSeasonStrategy;
import thesimpleton.seasons.Season;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class UnlockableSeasonCropSetDefinition implements SeasonCropSetDefinition {
  public static UnlockableSeasonCropSetDefinition UNLOCK_CROP_SET_0;
  public static UnlockableSeasonCropSetDefinition UNLOCK_CROP_SET_1;
  public static UnlockableSeasonCropSetDefinition UNLOCK_CROP_SET_2;
  public static UnlockableSeasonCropSetDefinition UNLOCK_CROP_SET_3;

  private final Season season;
  private final List<Crop> crops = new ArrayList<>();

  public UnlockableSeasonCropSetDefinition(Season season, List<Crop> crops) {
    this.season = season;
    this.crops.addAll(crops);
  }

  @Override
  public Season getSeason() {
    return season;
  }

  public CropsInSeasonStrategy getCropsInSeasonStrategy() {
    return new CropsInSeasonStrategy(this.crops.stream()
        .map(c -> (BiPredicate<Season, Crop>) (s, c2) -> c2 == c).collect(Collectors.toList()));
  }

  public List<Crop> getAllCrops() {
    return Collections.unmodifiableList(this.crops);
  }

  static {
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
    unlockedCropsLevel2.add(Crop.TURNIPS);
    unlockedCropsLevel2.add(Crop.STRAWBERRIES);
    UNLOCK_CROP_SET_2 = new UnlockableSeasonCropSetDefinition(Season.SPRING, unlockedCropsLevel2);

    List<Crop>  unlockedCropsLevel3 = new ArrayList<>();
    unlockedCropsLevel3.add(Crop.POTATOES);
    unlockedCropsLevel3.add(Crop.COFFEE);
    unlockedCropsLevel3.add(Crop.CHILIS);
    UNLOCK_CROP_SET_3 = new UnlockableSeasonCropSetDefinition(Season.SUMMER, unlockedCropsLevel3);
  }
}