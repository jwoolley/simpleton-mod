package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.CropReduceAction;
import thesimpleton.actions.CropSpawnAction;
import thesimpleton.cards.HarvestCard;
import thesimpleton.cards.TheSimpletonCardTags;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.characters.TheSimpletonCharacter;
import thesimpleton.orbs.AbstractCropOrb;
import thesimpleton.powers.unused.ToughSkinPower;
import thesimpleton.relics.CashCrop;
import thesimpleton.relics.unused.GrassPellets;
import thesimpleton.utilities.ModLogger;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

abstract public class AbstractCrop {
  protected static ModLogger CROP_LOGGER = TheSimpletonMod.traceLogger;
  public static Map<AbstractCard.CardRarity, Integer> CROP_RARITY_DISTRIBUTION;
  public final Crop enumValue;
  public final  AbstractCard.CardRarity cropRarity;
  private final String cropOrbId;

  private static int numTimesHarvestedThisTurn = 0;
  private static int numStacksHarvestedThisCombat = 0;

  private static final List<AbstractCrop> referenceCrops = new ArrayList<>();;
  private static int STACKS_PER_TRIGGER = 1;

  private final boolean isHarvestAll;
  private final AbstractCropPowerCard powerCard;
  private int maturityThreshold;

  AbstractCrop(Crop crop) {
    this(crop, crop.getCropInfo().orbId, crop.getCropInfo().powerCard, crop.getCropInfo().rarity,
        crop.getCropInfo().maturityThreshold);
  }

  AbstractCrop(Crop enumValue, String cropOrbId, AbstractCropPowerCard powerCard, AbstractCard.CardRarity rarity,
               int autoHarvestThreshold) {
    this(enumValue, cropOrbId, powerCard, rarity, autoHarvestThreshold, false);
  }

  AbstractCrop(Crop enumValue, String cropOrbId, AbstractCropPowerCard powerCard, AbstractCard.CardRarity rarity, int autoHarvestThreshold,
               boolean isHarvestAll) {

    CROP_LOGGER.trace("instantiating AbstractCrop. enumValue: " + enumValue);
    CROP_LOGGER.trace("instantiating AbstractCrop. cropRarity: " + rarity);


    this.enumValue = enumValue;
    this.cropOrbId = cropOrbId;
    this.powerCard = powerCard;
    this.cropRarity = rarity;
    this.maturityThreshold = autoHarvestThreshold;
    this.isHarvestAll = isHarvestAll;
  }

  public String getName() {
    return this.enumValue.name();
  }

  public String getCropOrbId() {
    return this.cropOrbId;
  }


  void onHarvest(int amount) {
    CROP_LOGGER.trace("============> AbstractCrop::onHarvest =====");

    numTimesHarvestedThisTurn++;
    numStacksHarvestedThisCombat += amount;
    CROP_LOGGER.trace("AbstractCrop::" + this.getCropOrb().ID + ": onHarvestCalled.  amount: " + amount + ". Set hasHarvestedThisTurn");

    if (getPlayer().hasPower(ToughSkinPower.POWER_ID)) {
      ((ToughSkinPower) getPlayer().getPower(ToughSkinPower.POWER_ID)).applyPower(getPlayer());
    }

    if (getPlayer().hasRelic(GrassPellets.ID)) {
      ((GrassPellets) getPlayer().getRelic(GrassPellets.ID)).increaseCountAndMaybeActivate();
    }

    if (getPlayer().hasRelic(CashCrop.ID)) {
      ((CashCrop) getPlayer().getRelic(CashCrop.ID)).onHarvest(this.enumValue);
    }

    TheSimpletonMod.updateCardsOnHarvest();
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
    //    final int stacks =  ApplyCropAction.calculateCropStacks(amount, isFromCard);

        CROP_LOGGER.trace("AbstractCrop::stackOrb amount:" + amount + " for " + getClass().getSimpleName());
        CROP_LOGGER.trace("AbstractCrop::stackOrb stacks:" + amount);

        AbstractDungeon.actionManager.addToBottom(new CropSpawnAction((AbstractCropOrb)getCropOrb().makeCopy(), amount, true));
  }

  public static void stackOrb(AbstractCropOrb cropOrb, int amount, boolean isFromCard) {
    // optimize by making one call instead of calling AbstractCropOrb.hasCropOrb
    //    AbstractCropOrb actualCropOrb = AbstractCropOrb.getCropOrb(cropOrb);
    //    logger.trace("AbstractCrop::stackOrb player " + (actualCropOrb != null ? "has " + actualCropOrb.passiveAmount + " stacks of " : "does not have ") + cropOrb.name + " already.");

    AbstractDungeon.actionManager.addToBottom(new CropSpawnAction((AbstractCropOrb) cropOrb.makeCopy(), amount, isFromCard));
  }

//  public boolean isMature() {
//    logger.trace("AbstractCrop::isMature: " + this.getClass().getTypeName());
//       AbstractCropOrb orb = AbstractCropOrb.getCropOrb(getCropOrbId());
//      return orb != null && orb.isMature(true);
////    return AbstractCropOrb.hasCropOrb(getCropOrb()) && AbstractCropOrb.getCropOrb(getCropOrb()).isMature();
//  }

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
    AbstractCropOrb orb = AbstractCropOrb.getCropOrb(getCropOrbId());
    if (orb != null) {
      harvest(harvestAll, maxHarvestAmount, orb);
    }
  }


  public void harvest(boolean harvestAll, int maxHarvestAmount, AbstractCropOrb orb) {
//    TheSimpletonMod.logger.trace("============> AbstractCrop::harvest =====");

//   logger.trace("AbstractCrop::harvest::" + this.getCropOrb().ID + " harvest() called. " + harvestAll + " maxHarvestAmount: " + maxHarvestAmount);

//   return cropOrb != null ? cropOrb.passiveAmount : 0;

//  final int amount = this.getAmount();
    // TODO: should onHarvest get passed harvestAmount instead of orb.passiveAmount?
    onHarvest(orb.passiveAmount);
    final int harvestAmount = calculateHarvestAmount(orb.passiveAmount, maxHarvestAmount, harvestAll);

    // TODO: move this logic down to AbstractCropOrb (e.g. a harvest() call)
    orb.harvestCropEffect();

    if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
      if (harvestAmount > 0) {
//        TheSimpletonMod.logger.trace("============> AbstractCrop::harvest queueing CropReduceAction =====");

        harvestAction(harvestAmount);
        AbstractDungeon.actionManager.addToTop(new CropReduceAction(orb, harvestAmount));
      }
    }
  }

  public void flash() {
    CROP_LOGGER.trace("AbstractCrop.flash REMOVE THIS CALL");
  }

  abstract protected int harvestAction(int harvestAmount);

  public static boolean hasHarvestedThisTurn() {
    return numTimesHarvestedThisTurn > 0;
  }

  public static int getNumTimesHarvestedThisTurn() {
    return numTimesHarvestedThisTurn;
  }
  public static int getNumStacksHarvestedThisCombat() {
    return numStacksHarvestedThisCombat;
  }


  public static void resetHasHarvestedThisTurn() { numTimesHarvestedThisTurn = 0; }
  public static void resetNumCropsHarvestedThisCombat() { numTimesHarvestedThisTurn = 0; }

  public void atStartOfTurn() {
    final int amount = this.getAmount();

    CROP_LOGGER.trace("AbstractCrop: Checking for auto-harvest triggers");
    if (amount >= getMaturityThreshold()) {
      this.flash();
      harvest(isHarvestAll, STACKS_PER_TRIGGER);
    } else { CROP_LOGGER.trace("Not triggered"); }
  }

  public int getAmount() {
    AbstractCropOrb cropOrb = AbstractCropOrb.getCropOrb(getCropOrbId());

    return cropOrb != null ? cropOrb.passiveAmount : 0;
//    return AbstractCropOrb.getCropOrb(getCropOrb()).getAmount();
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

  protected static AbstractPlayer getPlayer() {
    return AbstractDungeon.player;
  }

  public static boolean playerHasAnyActiveCropOrbs() {
    return AbstractCropOrb.playerHasAnyCropOrbs();
  }

  public static AbstractCropOrb getOldestCropOrb() {
    CROP_LOGGER.trace("Oldest crop orb per CropUtil: " +
        (TheSimpletonCharacter.getCropUtil().playerHasAnyCrops() ? AbstractCropOrb.getOldestCropOrb().name : "None"));
    return AbstractCropOrb.getOldestCropOrb();
  }

  public static AbstractCropOrb getNewestCropOrb() {
    CROP_LOGGER.trace("Newest crop orb per CropUtil: "
        + (TheSimpletonCharacter.getCropUtil().playerHasAnyCrops() ? AbstractCropOrb.getNewestCropOrb().name : "None"));
    return AbstractCropOrb.getNewestCropOrb();
  }


  public static List<AbstractCropOrb> getActiveCropOrbs() {
    return getActiveCropOrbs(false);
  }

  public static List<AbstractCropOrb> getActiveCropOrbs(boolean shuffle) {
    List<AbstractCropOrb> activeCropOrbs = AbstractCropOrb.getActiveCropOrbs();

    if (shuffle) {
      Collections.shuffle(activeCropOrbs);
    }

//    logger.trace("AbstractCrop.getActiveCropOrbs: player has " + activeCropOrbs.size() + " active crop orbs");
//    logger.trace("AbstractCrop.getActiveCropOrbs: player has " + SimpletonUtil.getActiveOrbs().size() + " active total orbs");

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
    return getRandomCrops(p, numPowers, numStacks, withRarityDistribution, crop -> true);
  }

  public static List<AbstractCrop>  getRandomCrops(
      AbstractPlayer p, int numCrops, int numStacks, boolean withRarityDistribution,
      Predicate<AbstractCrop> predicate) {
    // TODO: move this logic to a plant power manager class

    final List<AbstractCrop> referenceCrops = getReferenceCrops();

    List<AbstractCrop> filteredCrops = referenceCrops.stream()
        .filter(predicate)
        .collect(Collectors.toList());

    ArrayList<AbstractCrop> resultCrops;

    final int numTotalCrops = filteredCrops.size();
    if (numCrops > numTotalCrops) {
      throw new IndexOutOfBoundsException("Requested " + numCrops + " crop powers but only " + numTotalCrops
          + " crop powers are available");
    } else if (numCrops == numTotalCrops) {
      CROP_LOGGER.trace("Requested crop power cards size equals total crop power cards. Returning all powers.");
      resultCrops = new ArrayList(filteredCrops);
    } else {
      CROP_LOGGER.trace("Choosing crop power cards by probability distribution");

      ArrayList<AbstractCrop> distributedCrops = new ArrayList<>();
      filteredCrops.forEach(crop -> {
        List<AbstractCrop> copies =
            Collections.nCopies(withRarityDistribution
                    ? getCropRarityDistribution().get(crop.cropRarity) : 1,
                crop);
        distributedCrops.addAll(copies);
      });

//      logger.trace(AbstractCropPowerCard.class.getSimpleName()
//          + ".getRandomCropPowers :: distributedPowers: "
//          + distributedCrops.stream().map(dp -> dp.enumValue + "").collect(Collectors.joining(", ")));

      Collections.shuffle(distributedCrops);

      CROP_LOGGER.trace("Choosing " + numCrops + " powers from distributedPowers");

      resultCrops = new ArrayList<>();
      for (int i = 0; i < numCrops; i++) {
        final AbstractCrop crop = distributedCrops.get(0);
        resultCrops.add(crop);
        CROP_LOGGER.trace("Selected " + crop.enumValue);

        distributedCrops.removeIf(dc -> dc == crop);
      }
    }
    return  resultCrops;
  }

  public static List<AbstractCrop> getReferenceCrops() {
    if (referenceCrops.isEmpty()) {
      //TODO: accomplish this dynamically via Crop enum

      final ArtichokeCrop artichokeCrop = new ArtichokeCrop();
      final AsparagusCrop asparagusCrop = new AsparagusCrop();
      final ChilisCrop chilisCrop = new ChilisCrop();
      final CoffeeCrop coffeeCrop= new CoffeeCrop();
      final CornCrop cornCrop = new CornCrop();
      final SquashCrop squashCrop = new SquashCrop();
      final MushroomCrop mushroomCrop = new MushroomCrop();
      final OnionCrop onionCrop = new OnionCrop();
      final PotatoCrop potatoCrop = new PotatoCrop();
      final SpinachCrop spinachCrop = new SpinachCrop();
      final StrawberryCrop strawberryCrop = new StrawberryCrop();
      final TurnipCrop turnipCrop = new TurnipCrop();

    referenceCrops.add(artichokeCrop);
      referenceCrops.add(asparagusCrop);
      referenceCrops.add(chilisCrop);
      referenceCrops.add(coffeeCrop);
      referenceCrops.add(cornCrop);
      referenceCrops.add(mushroomCrop);
      referenceCrops.add(onionCrop);
      referenceCrops.add(potatoCrop);
      referenceCrops.add(turnipCrop);
      referenceCrops.add(squashCrop);
      referenceCrops.add(spinachCrop);
      referenceCrops.add(strawberryCrop);
    }
    return referenceCrops;
  }

  public static int getNumberActiveCropTypes() {
    return getActiveCropOrbs().size();
  }

  private static Map<AbstractCard.CardRarity, Integer> getCropRarityDistribution() {
    if (CROP_RARITY_DISTRIBUTION == null) {
      Map<CardRarity, Integer> rarityDistribution = new HashMap<>();
      rarityDistribution.put(CardRarity.BASIC, 27);
      rarityDistribution.put(CardRarity.COMMON, 18);
      rarityDistribution.put(CardRarity.UNCOMMON, 12);
      rarityDistribution.put(CardRarity.RARE, 8);
      CROP_RARITY_DISTRIBUTION = Collections.unmodifiableMap(rarityDistribution);
    }
    return CROP_RARITY_DISTRIBUTION;
  }

  private static boolean initialized = false;
  public static void initialize() {
    if (!initialized) {
      getCropRarityDistribution();
      initialized = true;
    }
  }
}
