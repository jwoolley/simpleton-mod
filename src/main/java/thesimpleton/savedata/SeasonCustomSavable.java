package thesimpleton.savedata;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.enums.TheSimpletonCharEnum;
import thesimpleton.seasons.Season;

public class SeasonCustomSavable implements CustomSavable<String> {
  final Logger logger = TheSimpletonMod.logger;
  private Season season;
  public SeasonCustomSavable() {
    logger.info( this.getClass().getSimpleName() + " instantiated");
  }

  @Override
  public String onSave() {
    if (AbstractDungeon.player.chosenClass == TheSimpletonCharEnum.THE_SIMPLETON) {
      reset();
      registerSaveId();
      Season season = TheSimpletonMod.getSeason();
      logger.info(getLogPrefix("onSave") + " called. season: " + season);
      return season != null ? season.uiName : Season.UNKNOWN.uiName;
    }
    return Season.UNKNOWN.uiName;
  }


  // TODO: make abstract
  public String getCustomSaveKey(){
    return "TheSimpletonMod" + this.getClass().getSimpleName();
  }

  @Override
  public void onLoad(String id) {
    if (AbstractDungeon.player.chosenClass == TheSimpletonCharEnum.THE_SIMPLETON) {

      logger.info(this.getClass().getSimpleName() + "::onLoad called");

      switch (id) {
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

      logger.info(this.getClass().getSimpleName() + "::onLoad retrieved season from save: " + (this.season == null ? Season.UNKNOWN : this.season.name)
          + "(saved value: " + id + ")");
    }
  }

  public Season getSeason() {
    return this.season;
  }

  private String getLogPrefix(String methodName) {
    return this.getClass().getSimpleName() + "." + methodName + " ::";
  }

  private void registerSaveId() {
    logger.info( this.getClass().getSimpleName() + "::registerSaveId");
    logger.info( this.getClass().getSimpleName() + "::registerSaveId registering customSaveKey: " + getCustomSaveKey());
    BaseMod.addSaveField(this.getCustomSaveKey(), this);
  }

  public void reset() {
    this.season = null;
  }
}