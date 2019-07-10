package thesimpleton.seasons;

import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.Crop;
import thesimpleton.seasons.cropsetdefinitions.SeasonCropSetDefinition;
import thesimpleton.seasons.cropsetdefinitions.SingleCropSetDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SeasonInfo {
  private List<Crop> cropsInSeason = new ArrayList<>();
  private Season season;

  public SeasonInfo(Season season) {
    this(season, Collections.emptyList());
  }

  public SeasonInfo(SeasonCropSetDefinition seasonCropSetDefinition) {
    this(seasonCropSetDefinition.getSeason(), seasonCropSetDefinition);
  }

  public SeasonInfo(Season season, SeasonCropSetDefinition seasonCropSetDefinition) {
    this(season, seasonCropSetDefinition.getCropsInSeasonStrategy().getCropSet(season));
  }

  public SeasonInfo(Season season, SingleCropSetDefinition singleCropSetDefinition) {
    this(season, singleCropSetDefinition.getCropSet());
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

  public Crop getRandomCropInSeason() {
    List<Crop> crops = new ArrayList<>(this.cropsInSeason);
    Collections.shuffle(crops);
    TheSimpletonMod.logger.info("Returning random in-season crop: " + crops.get(0).getName());
    return crops.get(0);
  }

  public boolean isInSeason(Crop crop) {
    return cropsInSeason.contains(crop);
  }
}
