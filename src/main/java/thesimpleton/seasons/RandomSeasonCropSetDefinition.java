package thesimpleton.seasons;

public class RandomSeasonCropSetDefinition implements SeasonCropSetDefinition  {
  private CropsInSeasonStrategy strategy;
  private Season season;

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
}
