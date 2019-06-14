package thesimpleton.savedata;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.seasons.Season;

public class SeasonCustomSavable implements CustomSavable<String> {
  final Logger logger = TheSimpletonMod.logger;
  private Season season;
  public SeasonCustomSavable() {
    logger.debug( this.getClass().getSimpleName() + " instantiated");
  }

  @Override
  public String onSave() {
    reset();
    registerSaveId();
    Season season = TheSimpletonMod.getSeason();
    logger.debug(getLogPrefix("onSave") + " called. season: " + season);
    return season != null ? season.uiName : Season.UNKNOWN.uiName;
  }


  // TODO: make abstract
  public String getCustomSaveKey(){
    return "TheSimpletonMod" + this.getClass().getSimpleName();
  }

  @Override
  public void onLoad(String id) {
    logger.debug( this.getClass().getSimpleName() + "::onLoad called");

    switch(id) {
      case Season.WINTER_UI_NAME:
        this.season = Season.WINTER;
        break;
      case Season.SPRING_UI_NAME:
        this.season = Season.SPRING;
        break;
      case Season.SUMMER_UI_NAME:
        this.season = Season.SUMMER;
        break;
      case Season.AUTUMN_UI_NAME:
        this.season = Season.AUTUMN;
        break;
      case Season.UNKNOWN_UI_NAME:
      default:
        break;
    }

    logger.debug( this.getClass().getSimpleName() + "::onLoad retrieved season from save: " + this.season.name
        + "(saved value: " + id + ")");
  }

  public Season getSeason() {
    return this.season;
  }

  private String getLogPrefix(String methodName) {
    return this.getClass().getSimpleName() + "." + methodName + " ::";
  }

  private void registerSaveId() {
    logger.debug( this.getClass().getSimpleName() + "::registerSaveId");
    logger.debug( this.getClass().getSimpleName() + "::registerSaveId registering customSaveKey: " + getCustomSaveKey());
    BaseMod.addSaveField(this.getCustomSaveKey(), this);
  }

  public void reset() {
    this.season = null;
  }
}