package thesimpleton.utilities;

import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.orbs.AbstractCropOrb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CropUtil {
  private static ModLogger logger = TheSimpletonMod.debugLogger;

  public CropUtil() {

  }

  public boolean playerHasAnyCrops() {
    return AbstractCropOrb.getNumberActiveCropOrbs() > 0;
  }

  public AbstractCropPowerCard getRandomCropCardInSeason() {
    List<AbstractCropPowerCard> seasonalCrops = new ArrayList<>(TheSimpletonMod.getSeasonalCropCards());

    if (seasonalCrops.isEmpty()) {
      return AbstractCropPowerCard.getRandomCropPowerCard(false);
    }
    Collections.shuffle(seasonalCrops);
    return seasonalCrops.get(0);
  }

}
