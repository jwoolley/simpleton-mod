package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.ApplyCropAction;
import thesimpleton.actions.CropReduceAction;
import thesimpleton.actions.CropSpawnAction;
import thesimpleton.cards.HarvestCard;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.TheSimpletonCardTags;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.characters.TheSimpletonCharacter;
import thesimpleton.orbs.AbstractCropOrb;
import thesimpleton.powers.ToughSkinPower;
import thesimpleton.powers.utils.Crop;
import thesimpleton.relics.CashCrop;
import thesimpleton.relics.GrassPellets;
import thesimpleton.utilities.CropUtil;
import thesimpleton.utilities.Trigger;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

abstract public class AbstractCrop {
  public static Map<AbstractCard.CardRarity, Integer> CROP_RARITY_DISTRIBUTION;
  public final Crop enumValue;
  public final  AbstractCard.CardRarity cropRarity;
  private final String cropOrbId;

  protected static final Logger logger = TheSimpletonMod.logger;

  private static final List<AbstractCrop> referenceCrops = new ArrayList<>();;

  private static int STACKS_PER_TRIGGER = 1;
  private static boolean hasHarvestedThisTurn = false;

  private final boolean isHarvestAll;
  private final AbstractCropPowerCard powerCard;
  private int maturityThreshold;

  AbstractCrop(Crop enumValue, String cropOrbId, AbstractCropPowerCard powerCard, AbstractCard.CardRarity rarity,
               int autoHarvestThreshold) {
    this(enumValue, cropOrbId, powerCard, rarity, autoHarvestThreshold, false);
  }

  AbstractCrop(Crop enumValue, String cropOrbId, AbstractCropPowerCard powerCard, AbstractCard.CardRarity rarity, int autoHarvestThreshold,
               boolean isHarvestAll) {
    this.enumValue = enumValue;
    this.cropOrbId = cropOrbId;
    this.powerCard = powerCard;
    this.cropRarity = rarity;
    this.maturityThreshold = autoHarvestThreshold;
    this.isHarvestAll = isHarvestAll;
  }

  public String getName() {
    return this.getCropOrb().getClass().getSimpleName();
  }

  public String getCropOrbId() {
    return this.cropOrbId;
  }

  private void triggerCropGained() {
    //  getPlayer().getCropUtil().onCropGained(this);
  }

  void onHarvest(int amount) {
    TheSimpletonMod.logger.debug("============> AbstractCrop::onHarvest =====");

    hasHarvestedThisTurn = true;
    logger.debug("AbstractCrop::" + this.getCropOrb().ID + ": onHarvestCalled.  amount: " + amount + ". Set hasHarvestedThisTurn");

    if (getPlayer().hasPower(ToughSkinPower.POWER_ID)) {
      ((ToughSkinPower) getPlayer().getPower(ToughSkinPower.POWER_ID)).applyPower(getPlayer());
    }

    if (getPlayer().hasRelic(GrassPellets.ID)) {
      ((GrassPellets) getPlayer().getRelic(GrassPellets.ID)).increaseCountAndMaybeActivate();
    }

    if (getPlayer().hasRelic(CashCrop.ID)) {
      ((CashCrop) getPlayer().getRelic(CashCrop.ID)).onHarvest(this.enumValue);
    }
  }

  public int getMaturityThreshold() {
    return this.maturityThreshold;
  }

  public void updateMaturityThreshold(int amount) {
    this.maturityThreshold += amount;
    // TODO: update orb if present
  }

  public AbstractCropOrb getCropOrb() {
    return AbstractCropOrb.getCropOrb(getCropOrbId());
  }

  public void stackOrb(int amount, boolean isFromCard) {
    final int stacks =  ApplyCropAction.calculateCropStacks(amount, isFromCard);

    logger.debug("AbstractCrop::stackOrb amount:" + amount + " for " + getClass().getSimpleName());
    logger.debug("AbstractCrop::stackOrb stacks:" + stacks);

    if (AbstractCropOrb.hasCropOrb(getCropOrbId())) {
      logger.debug("AbstractCrop::stackOrb player has " +getClass().getSimpleName() + " already. Num stacks: " + getCropOrb().getAmount());
    } else {
      logger.debug("AbstractCrop::stackOrb doesn't have " + getClass().getSimpleName() + " already.");
    }


    AbstractDungeon.actionManager.addToBottom(new CropSpawnAction((AbstractCropOrb)getCropOrb().makeCopy(), stacks, true));
    CropUtil.triggerCardUpdates();
  }

  public void reduceOrb(int amount) {
    AbstractDungeon.actionManager.addToBottom(new CropReduceAction((AbstractCropOrb)getCropOrb().makeCopy(), amount));
    CropUtil.triggerCardUpdates();
  }

  public boolean isMature() {
    logger.debug("AbstractCrop::isMature: " + this.getClass().getTypeName());

    return AbstractCropOrb.hasCropOrb(getCropOrb()) && AbstractCropOrb.getCropOrb(getCropOrb()).isMature();
  }

  public void onUseCard(AbstractCard card, UseCardAction action) {
    if (card.hasTag(TheSimpletonCardTags.HARVEST) && card instanceof HarvestCard && ((HarvestCard) card).isAutoHarvest()) {
      harvest(((HarvestCard) card).isHarvestAll(), ((HarvestCard) card).getHarvestAmount());
    }
  }

  protected int calculateHarvestAmount(int amount, int maxAmount, boolean harvestAll) {
    return Math.min(amount, harvestAll ? amount : maxAmount);
  }

  public void harvestAll() {
    this.harvest(true, this.getAmount());
  }

  public void harvest(boolean harvestAll, int maxHarvestAmount) {
    TheSimpletonMod.logger.debug("============> AbstractCrop::harvest =====");

    logger.debug("AbstractCrop::harvest::" + this.getCropOrb().ID + " harvest() called. " + harvestAll + " maxHarvestAmount: " + maxHarvestAmount);

    final int amount = this.getAmount();
    onHarvest(amount);
    final int harvestAmount = calculateHarvestAmount(amount, maxHarvestAmount, harvestAll);

    // TODO: move this logic down to AbstractCropOrb (e.g. a harvest() call)
    this.getCropOrb().harvestCropEffect();

    if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
      if (harvestAmount > 0) {
        TheSimpletonMod.logger.debug("============> AbstractCrop::harvest queueing CropReduceAction =====");

        harvestAction(harvestAmount);
        AbstractDungeon.actionManager.addToTop(new CropReduceAction(getCropOrb(), harvestAmount));
      }
    }
  }

  public void flash() {
    logger.debug("AbstractCrop.flash REMOVE THIS CALL");
  }

  abstract protected int harvestAction(int harvestAmount);

  public static boolean hasHarvestedThisTurn() {
    return hasHarvestedThisTurn;
  }


  public static void resetHasHarvestedThisTurn() { hasHarvestedThisTurn = false; }

  public void atStartOfTurn() {
    final int amount = this.getAmount();

    logger.debug("AbstractCrop: Checking for auto-harvest triggers");
    if (amount >= getMaturityThreshold()) {
      this.flash();
      harvest(isHarvestAll, STACKS_PER_TRIGGER);
    } else { logger.debug("Not triggered"); }
  }

  public int getAmount() {
    if (!AbstractCropOrb.hasCropOrb(getCropOrb())) {
      return 0;
    }
    return AbstractCropOrb.getCropOrb(getCropOrb()).getAmount();
  }

  public AbstractCropPowerCard getPowerCard() throws UnsupportedOperationException {
    if (!this.hasPowerCard()) {
      throw new UnsupportedOperationException("No crop power card defined for " + this.getClass().getSimpleName());
    }
    return (AbstractCropPowerCard)this.powerCard.makeCopy();
  }

  public boolean hasPowerCard() {
    return this.powerCard != null;
  }

  protected static TheSimpletonCharacter getPlayer() {
    return SimpletonUtil.getPlayer();
  }

  public static boolean playerHasAnyActiveCropOrbs() {
    return AbstractCropOrb.playerHasAnyCropOrbs();
  }

  public static AbstractCropOrb getOldestCropOrb() {
    logger.debug("Oldest crop orb per CropUtil: " + (getPlayer().getCropUtil().playerHasAnyCrops() ? getPlayer().getCropUtil().getOldestCrop().name : "None"));
    return AbstractCropOrb.getOldestCropOrb();
  }

  public static AbstractCropOrb getNewestCropOrb() {
    logger.debug("Newest crop orb per CropUtil: " + (getPlayer().getCropUtil().playerHasAnyCrops() ? getPlayer().getCropUtil().getNewestCrop().name : "None"));
    return AbstractCropOrb.getNewestCropOrb();
  }


  public static List<AbstractCropOrb> getActiveCropOrbs() {
    List<AbstractCropOrb> activeCropOrbs = AbstractCropOrb.getActiveCropOrbs();

    logger.debug("AbstractCrop.getActiveCropOrbs: player has " + activeCropOrbs.size() + " active crop orbs");
    logger.debug("AbstractCrop.getActiveCropOrbs: player has " + SimpletonUtil.getActiveOrbs().size() + " active total orbs");

    return activeCropOrbs;
  }

  public static List<AbstractCrop> getActiveCrops() {
    return getActiveCropOrbs().stream().map(AbstractCropOrb::getCrop).collect(Collectors.toList());
  }

  public static AbstractCrop getRandomCrop(AbstractPlayer p, int numStacks) {
    return getRandomCrops(p, 1, numStacks,false).get(0);
  }

  public static AbstractCrop getRandomCrop(AbstractPlayer p, int numStacks, boolean withRarityDistribution) {
    return getRandomCrops(p, 1, numStacks, withRarityDistribution).get(0);
  }

  public static List<AbstractCrop> getRandomCrops(
      AbstractPlayer p, int numPowers, int numStacks, boolean withRarityDistribution) {
    return getRandomCrops(p, numPowers, numStacks, withRarityDistribution, power -> true);
  }

  public static List<AbstractCrop> getRandomCrops(
      AbstractPlayer p, int numPowers, int numStacks, boolean withRarityDistribution,
      Predicate<AbstractCrop> predicate) {
    // TODO: move this logic to a plant power manager class

    final List<AbstractCrop> referenceCrops = getReferenceCrops();

    List<AbstractCrop> filteredCrops = referenceCrops.stream()
        .filter(predicate)
        .collect(Collectors.toList());

    logger.debug(AbstractCropPowerCard.class.getSimpleName()
        + ".getRandomCropPowers :: referencePowers: "
        + referenceCrops.stream().map(rp -> rp.getName()).collect(Collectors.joining(", ")));

    logger.debug(AbstractCropPowerCard.class.getSimpleName()
        + ".getRandomCropPowers :: filteredPowers: "
        + filteredCrops.stream().map(fp -> fp.getName()).collect(Collectors.joining(", ")));

    ArrayList<AbstractCrop> resultCrops;

    final int numTotalPowers = filteredCrops.size();
    if (numPowers > numTotalPowers) {
      throw new IndexOutOfBoundsException("Requested " + numPowers + " powers but only " + numTotalPowers
          + " powers are available");
    } else if (numPowers == numTotalPowers) {
      logger.debug("Requested powers size equals total powers. Returning all powers.");
      resultCrops = new ArrayList(filteredCrops);
    } else {
      logger.debug("Choosing powers by probability distribution");

      ArrayList<AbstractCrop> distributedCrops = new ArrayList<>();
      filteredCrops.forEach(power -> {
        List<AbstractCrop> copies =
            Collections.nCopies(withRarityDistribution
                    ? CROP_RARITY_DISTRIBUTION.get(power.cropRarity) : 1,
                power);
        distributedCrops.addAll(copies);
      });

      logger.debug(AbstractCropPowerCard.class.getSimpleName()
          + ".getRandomCropPowers :: distributedPowers: "
          + distributedCrops.stream().map(dp -> dp.getName()).collect(Collectors.joining(", ")));

      Collections.shuffle(distributedCrops);

      logger.debug("Choosing " + numPowers + " powers from distributedPowers");

      resultCrops = new ArrayList<>();
      for (int i = 0; i < numPowers; i++) {
        final AbstractCrop crop = distributedCrops.get(0);
        resultCrops.add(crop);
        logger.debug("Selected " + crop.getName());

        distributedCrops.removeIf(dc -> dc == crop);
      }
    }
    return  resultCrops;
  }

  public static List<AbstractCrop> getReferenceCrops() {
    if (referenceCrops.isEmpty()) {
      //TODO: accomplish this dynamically via Crop enum

//      final ArtichokeCrop artichokeCrop = new ArtichokeCrop();
      final AsparagusCrop asparagusCrop = new AsparagusCrop();
      final ChilisCrop chilisCrop = new ChilisCrop();
      final CornCrop cornCrop = new CornCrop();
      final SquashCrop squashCrop = new SquashCrop();
      final MushroomCrop mushroomCrop = new MushroomCrop();
      final OnionCrop onionCrop = new OnionCrop();
      final PotatoCrop potatoCrop = new PotatoCrop();
      final SpinachCrop spinachCrop = new SpinachCrop();
      final TurnipCrop turnipCrop = new TurnipCrop();

//      referenceCrops.add(artichokeCrop);
      referenceCrops.add(asparagusCrop);
      referenceCrops.add(chilisCrop);
      referenceCrops.add(potatoCrop);
      referenceCrops.add(cornCrop);
      referenceCrops.add(squashCrop);
      referenceCrops.add(spinachCrop);
      referenceCrops.add(mushroomCrop);
      referenceCrops.add(onionCrop);
      referenceCrops.add(turnipCrop);

    }
    return referenceCrops;
  }

  public static int getNumberActiveCropTypes() {
    return getActiveCropOrbs().size();
  }

  static {
    Map<CardRarity, Integer> rarityDistribution = new HashMap<>();
    rarityDistribution.put(CardRarity.BASIC, 27);
    rarityDistribution.put(CardRarity.COMMON, 18);
    rarityDistribution.put(CardRarity.UNCOMMON, 12);
    rarityDistribution.put(CardRarity.RARE, 8);


    // TODO: move to CropUtil?
    // reset hasHarvestedThisTurn at start of combat and at end of turn
    Trigger trigger = new Trigger() {
      public void trigger() {
        logger.debug("AbstractCrop: Managed trigger: Resetting hasHarvestedThisTurn ");
        hasHarvestedThisTurn = false;
      }
    };

    TheSimpletonCharacter.addPrecombatPredrawTrigger(trigger);
    TheSimpletonCharacter.addStartOfTurnTrigger(trigger);
    TheSimpletonCharacter.addEndOfTurnTrigger(trigger);
  }
}
