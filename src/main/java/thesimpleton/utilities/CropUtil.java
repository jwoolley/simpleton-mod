package thesimpleton.utilities;

import basemod.interfaces.PostDrawSubscriber;
import basemod.interfaces.PostPowerApplySubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.characters.TheSimpletonCharacter;
import thesimpleton.orbs.AbstractCrop;
import thesimpleton.powers.AbstractCropPower;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CropUtil implements PostUpdateSubscriber, PostDrawSubscriber, PostPowerApplySubscriber {
  private static Logger logger = TheSimpletonMod.logger;

  private final TriggerManager cropTickedTriggerManager;
  private static List<AbstractCropPower> activeCrops = new ArrayList<>();
  private static List<AbstractCropPower> combatStartCrops = new ArrayList<>();
  private boolean wasResetForCombat;

  public CropUtil() {
    this.cropTickedTriggerManager = new TriggerManager("CropUtil");
    wasResetForCombat = true;
    TheSimpletonCharacter.addEndOfCombatTrigger(this::resetForCombatEnd);
//    TheSimpletonCharacter.addPrecombatPredrawTrigger(() -> {  triggerCardUpdates(); /* resetForCombat(); */});
//    TheSimpletonCharacter.addPrecombatPredrawTrigger(() -> { triggerCardUpdates(); });

    TheSimpletonCharacter.addPrecombatTrigger(this::resetForCombatStart);
    TheSimpletonCharacter.addStartOfTurnTrigger(() -> {
      //TODO: consider moving to precombat trigger (and precombat trigger to precombatpredraw trigger_
      wasResetForCombat = false;
    });
    
//    TheSimpletonCharacter.addStartOfTurnTrigger(() -> {
//      logger.debug("CropUtil::startOfTurnTrigger refreshing activeCrops");
//      if (activeCrops.size() == 0) {
//        logger.debug("CropUtil::startOfTurnTrigger activeCrops refreshed");
//        activeCrops.addAll(AbstractCropPower.getActiveCropPowers());
//        triggerCardUpdates();
//        wasResetForCombat = false;
//
//        // enable if works
//        // combatStartCrops.clear();
//      }
//    });
  }


  public void resetForCombatStart() {
    logger.debug("CropUtil::resetForCombatStart Resetting cropTickedTriggerManager");
//    cropTickedTriggerManager.clear();
    logger.debug("CropUtil:resetForCombatStart number combatStartCrops: " + combatStartCrops.size());

    activeCrops.clear();
    activeCrops.addAll(combatStartCrops);
    combatStartCrops.clear();

    logger.debug("CropUtil:resetForCombatStart activeCrops after combatStartCrops added: " + activeCrops.size());

    combatStartCrops.clear();
    wasResetForCombat = true;
  }

  public void resetForCombatEnd() {
    logger.debug("CropUtil::resetForCombat Resetting cropTickedTriggerManager");
//    cropTickedTriggerManager.clear();
    logger.debug("CropUtil:resetForCombat number combatStartCrops: " + combatStartCrops.size());

    activeCrops.clear();

    logger.debug("CropUtil:resetForCombat activeCrops after combatStartCrops added: " + activeCrops.size());

    combatStartCrops.clear();
    wasResetForCombat = true;
  }

  @Override
  public void receivePostUpdate() {
    triggerCardUpdates();

  }

  @Override
  public void receivePostDraw(AbstractCard card) {
    triggerCardUpdates();
  }

  @Override
  public void receivePostPowerApplySubscriber(AbstractPower power, AbstractCreature target, AbstractCreature owner) {
    triggerCardUpdates();
  }


  public void addCropTickedTriggerListener(TriggerListener listener) {
    this.cropTickedTriggerManager.addTriggerListener(listener);
  }

  public void removeCropTickedTriggerListener(TriggerListener listener) {
    this.cropTickedTriggerManager.addTriggerListener(listener);
  }

  private List<AbstractCropPower> getActiveCrops() {
//    return AbstractCropPower.getActiveCropPowers();
    logger.debug("*** CropUtil getActiveCrops (size:" + activeCrops.size() + ")");

    return activeCrops;
  }

  public void addActiveCrop(AbstractCropPower crop) {
    logger.debug("*** CropUtil Adding crop: " + crop.name);
    if (activeCrops.stream().noneMatch(c -> c.name == crop.name)) {
      logger.debug("*** CropUtil crop added: " + crop.name);
      activeCrops.add(crop);
    }

    if (wasResetForCombat) {
      logger.debug("*** CropUtil Adding crop to combatStartCrops: " + crop.name);
      combatStartCrops.add(crop);
    }
  }

  public void removeActiveCrop(AbstractCropPower crop) {
    logger.debug("*** CropUtil Removing crop: " + crop.name);
    activeCrops.remove(crop);
  }

  public boolean playerHasAnyCrops() {
    logger.debug("*** CropUtil playerHasAnyCrops (size:" + activeCrops.size() + ")");
    return getActiveCrops().size() > 0;
  }

  public AbstractCropPower getOldestCrop() {
    return getActiveCrops().get(0);
  }

  public AbstractCropPower getNewestCrop() {
    List<AbstractCropPower> activeCrops = getActiveCrops();
    return activeCrops.get(activeCrops.size() - 1);
  }

  public static void triggerCardUpdates() {
//    AbstractPlayer player = AbstractDungeon.player;
//
//    logger.debug("CropUtil::triggerCardUpdates: updating master deck: " + player.masterDeck.group.stream().filter(c -> c instanceof AbstractCropTriggerCard).count() + " eligible cards, total cards: " + player.masterDeck.size());
//
////    player.masterDeck.group.forEach(card -> {
////        if (card instanceof AbstractCropTriggerCard) {
////            ((AbstractCropTriggerCard) card).getTriggerListener().getTrigger().trigger();
////        }
////    });
//
//    logger.debug("CropUtil::triggerCardUpdates: updating draw pile: " + player.drawPile.group.stream().filter(c -> c instanceof AbstractCropTriggerCard).count() + " eligible cards, total cards: " + player.drawPile.size());
//
//    player.drawPile.group.forEach(card -> {
//      if (card instanceof AbstractCropTriggerCard) {
//        ((AbstractCropTriggerCard) card).getTriggerListener().getTrigger().trigger();
//      }
//    });
//
//    logger.debug("CropUtil::triggerCardUpdates: updating hand: " + player.hand.group.stream().filter(c -> c instanceof AbstractCropTriggerCard).count() + " eligible cards, total cards: " + player.hand.size());
//
//    player.hand.group.forEach(card -> {
//      if (card instanceof AbstractCropTriggerCard) {
//        ((AbstractCropTriggerCard) card).getTriggerListener().getTrigger().trigger();
//      }
//    });
//
//    logger.debug("CropUtil::triggerCardUpdates: updating discard pile: " + player.discardPile.group.stream().filter(c -> c instanceof AbstractCropTriggerCard).count() + " eligible cards, total cards: " + player.discardPile.size());
//
//    player.discardPile.group.forEach(card -> {
//      if (card instanceof AbstractCropTriggerCard) {
//        ((AbstractCropTriggerCard) card).getTriggerListener().getTrigger().trigger();
//      }
//    });
  }

  public void onCropOrbGained(AbstractCrop crop) {
    logger.debug("CropUtil::onCropGained: " + crop.name);
  }

  public void onCropGained(AbstractCropPower crop) {
    logger.debug("CropUtil::onCropGained: " + crop.name);
    addActiveCrop(crop);
//    triggerCardUpdates();
    cropTickedTriggerManager.triggerAll();
  }

  public void onCropLost(AbstractCropPower crop) {
    removeActiveCrop(crop);
//    triggerCardUpdates();
    cropTickedTriggerManager.triggerAll();
  }

  static class CropSeniorityList extends ArrayList<AbstractCropPower> {
    public AbstractCropPower getOldestCrop() {
      AbstractCropPower oldestCrop = (this.size() > 0 ? this.get(0): null);
      logger.debug("CropSeniorityList::getNewestCrop returning oldest crop: " + (oldestCrop != null ? oldestCrop.name : "none"));
      return oldestCrop;
    }

    public AbstractCropPower getNewestCrop() {
      AbstractCropPower newestCrop = (this.size() > 0 ? this.get(this.size() - 1): null);
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
