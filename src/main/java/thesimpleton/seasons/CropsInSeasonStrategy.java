package thesimpleton.seasons;

import thesimpleton.crops.Crop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class CropsInSeasonStrategy {
  List<BiPredicate<Season, Crop>> predicates = new ArrayList<>();
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