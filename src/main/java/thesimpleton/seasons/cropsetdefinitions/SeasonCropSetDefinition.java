package thesimpleton.seasons.cropsetdefinitions;

import thesimpleton.seasons.CropsInSeasonStrategy;
import thesimpleton.seasons.Season;

public interface SeasonCropSetDefinition {
  Season getSeason();
  CropsInSeasonStrategy getCropsInSeasonStrategy();
}
