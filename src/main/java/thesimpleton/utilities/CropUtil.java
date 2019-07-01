package thesimpleton.utilities;

import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.orbs.AbstractCropOrb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CropUtil {
  private static Logger logger = TheSimpletonMod.logger;

  public CropUtil() {

  }

  public boolean playerHasAnyCrops() {
    return AbstractCropOrb.getNumberActiveCropOrbs() > 0;
  }

  public AbstractCropPowerCard getRandomCropCardInSeason() {
    List<AbstractCropPowerCard> seasonalCrops = new ArrayList<>(TheSimpletonMod.getSeasonalCropCards());
    Collections.shuffle(seasonalCrops);
    return seasonalCrops.get(0);
  }

}
