package thesimpleton.seasons;

import thesimpleton.crops.Crop;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class UnlockableSeasonCropSetDefinition implements SeasonCropSetDefinition {
    final Season season;
    final List<Crop> crops = new ArrayList<>();

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
  }