package thesimpleton.savedata;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.Crop;
import thesimpleton.enums.TheSimpletonCharEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SeasonCropsCustomSavable implements CustomSavable<List<String>> {
  final Logger logger = TheSimpletonMod.logger;
  private List<Crop> crops = new ArrayList<>();
  public SeasonCropsCustomSavable() {
    logger.debug( this.getClass().getSimpleName() + " instantiated");
  }

  @Override
  public List<String> onSave() {
    if (AbstractDungeon.player.chosenClass == TheSimpletonCharEnum.THE_SIMPLETON) {
      reset();
      registerSaveId();

      List<Crop> crops = TheSimpletonMod.getSeasonInfo().getCropsInSeason();

      logger.debug(this.getClass().getSimpleName() + "::onSave called. Saving Crops:" + crops.stream()
        .map(c -> c.getName()).collect(Collectors.joining(", ")));

      return new ArrayList<String>(crops.stream().map(c -> c.getName()).collect(Collectors.toList()));
    }
    return Collections.emptyList();
  }


  // TODO: make abstract
  public String getCustomSaveKey(){
    return "TheSimpletonMod" + this.getClass().getSimpleName();
  }

  @Override
  public void onLoad(List<String> cropNames) {
    this.reset();
    if (AbstractDungeon.player.chosenClass == TheSimpletonCharEnum.THE_SIMPLETON) {
      logger.debug(this.getClass().getSimpleName() + "::onLoad called");

      if (cropNames != null) {
        logger.debug(this.getClass().getSimpleName() + "::onLoad | Loading Crops save data for: "
            + cropNames.stream().collect(Collectors.joining(", ")));

        List<Crop> loadedCrops = cropNames.stream()
            .map(n -> Crop.getCropFromName(n)).filter(c -> c != null).collect(Collectors.toList());
        this.reset();
        this.crops.addAll(loadedCrops);

        logger.debug(this.getClass().getSimpleName() + "::onLoad | Found Crops: "
            + loadedCrops.stream().map(c -> c.getName()).collect(Collectors.joining(", ")));

        //      List<AbstractCropPowerCard> cropCards = loadedCrops.stream()
        //          .map(c -> c.getCropInfo().powerCard).filter(c -> c != null).collect(Collectors.toList());

        //
        //      logger.debug(this.getClass().getSimpleName() + "::onLoad | Found Crop power cards:"
        //          + cropCards.stream().map(c -> c.name).collect(Collectors.joining(", ")));
        //
        //      TheSimpletonMod.setSeasonalCropCards(cropCards);
      } else {
        logger.debug(this.getClass().getSimpleName() + "::onLoad | no Crops found in save data");
      }
    }
  }

  public List<Crop> getCropsInSeason() {
    return Collections.unmodifiableList(crops);
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
    this.crops.clear();
  }
}