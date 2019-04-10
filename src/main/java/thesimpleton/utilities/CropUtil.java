package thesimpleton.utilities;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.characters.TheSimpletonCharacter;
import thesimpleton.powers.AbstractCropPower;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class CropUtil {
  private static Logger logger = TheSimpletonMod.logger;

  private final TriggerManager cropTickTriggerManager;
  private final CropSeniorityList cropSeniorityList;

  public CropUtil() {
    this.cropTickTriggerManager = new TriggerManager();
    this.cropSeniorityList = new CropSeniorityList();

    Trigger trigger = () -> {
      logger.debug("CropUtil: Resetting cropSeniorityList");
      resetForCombat();
    };
    ((TheSimpletonCharacter) AbstractDungeon.player).addPrecombatPredrawTrigger(trigger);
  }

  public void resetForCombat() {
    logger.debug("CropUtil: precombatPredrawTrigger");
    this.cropSeniorityList.clear();

    logger.debug("CropUtil: Resetting cropTickTriggerManager");
    cropTickTriggerManager.clear();
  }

  public boolean playerHasAnyCrops() {
    return cropSeniorityList.hasAnyCrops();
  }

  public AbstractCropPower getNewestCrop() {
    return cropSeniorityList.getNewestCrop();
  }

  public void onCropGained(AbstractCropPower crop) {
    cropSeniorityList.moveToEnd(crop);
  }

  public void onCropLost(AbstractCropPower crop) {
    cropSeniorityList.remove(crop);
  }

  static class CropSeniorityList extends ArrayList<AbstractCropPower> {
    public AbstractCropPower getNewestCrop() {
      AbstractCropPower newestCrop = this.size() > 0 ? this.get(this.size() - 1): null;
      logger.debug("CropSeniorityList::getNewestCrop returning newest crop: " + (newestCrop != null ? newestCrop.name : "none"));
      return newestCrop;
    }

    @Override
    public String toString() {
      return this.stream()
        .map(c -> c.name).collect(Collectors.joining(", "));
    }

    public void moveToEnd(AbstractCropPower crop) {
      logger.debug("CropSeniorityList::moveToEnd pre-update crop: " + crop.name);
      logger.debug("CropSeniorityList::moveToEnd pre-update cropSeniorityList: " + this.toString() + " (size:" + this.size()+ ")");

      if (this.stream().anyMatch(c -> c.name == crop.name)) {
        logger.debug("CropSeniorityList::moveToEnd crop exists in list: " + crop.name + ". Removing before re-adding to end: ");
        this.remove(this.stream().filter(c -> c.name == crop.name).findFirst().get());
        logger.debug("CropSeniorityList::moveToEnd post-remove cropSeniorityList: " + this.toString() + " (size:" + this.size()+ ")");
      } else {
        logger.debug("CropSeniorityList::moveToEnd crop doesn't exist in list");
      }

      this.add(crop);

      logger.debug("CropSeniorityList::moveToEnd moved crop to end: " + crop.name);
      logger.debug("CropSeniorityList::moveToEnd post-update cropSeniorityList: " + this.toString() + " (size:" + this.size()+ ")");
    }

    public boolean hasAnyCrops() {
      return this.size() > 0;
    }
  }
}
