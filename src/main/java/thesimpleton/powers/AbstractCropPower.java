package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thesimpleton.cards.HarvestCard;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.TheSimpletonCardTags;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.characters.TheSimpletonCharacter;
import thesimpleton.powers.utils.Crop;
import thesimpleton.relics.CashCrop;
import thesimpleton.relics.GrassPellets;
import thesimpleton.relics.TheHarvester;
import thesimpleton.utilities.CropUtil;
import thesimpleton.utilities.Trigger;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// TODO: create separate enum for CropRarity
public abstract class AbstractCropPower extends AbstractTheSimpletonPower {

  public static Map<CardRarity, Integer> CROP_RARITY_DISTRIBUTION;

  private static boolean IS_HARVEST_ALL = false;
  private static int AUTO_HARVEST_THRESHOLD = 5;
  private static int CROP_POWER_ID_COUNTER = 0;
  private static int STACKS_PER_TRIGGER = 1;
  private static boolean hasHarvestedThisTurn = false;

  private static final String POWER_DESCRIPTION_ID = "TheSimpletonMod:AbstractCropPower";
  private static final PowerStrings powerStrings;
  public static final String[] PASSIVE_DESCRIPTIONS;
  private static final List<AbstractCropPower> referencePowers = new ArrayList<>();;

  public final Crop enumValue;
  public final String[] descriptions;
  private final int cropPowerId;
  private final boolean isHarvestAll;
  private final AbstractCropPowerCard powerCard;
  private int autoHarvestThreshold;


  public AbstractCard.CardRarity cropRarity;

  AbstractCropPower(Crop enumValue, String name, String id, PowerType powerType, String[] descriptions, String imgName, AbstractCreature owner, AbstractCard.CardRarity rarity,
                    AbstractCropPowerCard powerCard, int amount) {
    this(enumValue, name, id, powerType, descriptions, imgName, owner, rarity, powerCard, amount, IS_HARVEST_ALL, AUTO_HARVEST_THRESHOLD);
    logger.debug("Instantiating CropPower:  enumValue:" + enumValue + ",  name:" + name+ ",  id:" + id + ",  powerType:" + powerType.name()+ ",  owner:" + owner);
  }

  AbstractCropPower(Crop enumValue, String name, String id, PowerType powerType,  String[] descriptions, String imgName, AbstractCreature owner, AbstractCard.CardRarity rarity,
                    AbstractCropPowerCard powerCard, int amount, boolean isHarvestAll) {
    this(enumValue, name, id, powerType, descriptions, imgName, owner, rarity, powerCard, amount, isHarvestAll, AUTO_HARVEST_THRESHOLD);
  }

  AbstractCropPower(Crop enumValue, String name, String id, PowerType powerType, String[] descriptions, String imgName, AbstractCreature owner, AbstractCard.CardRarity rarity,
                            AbstractCropPowerCard powerCard, int amount, boolean isHarvestAll,
                            int autoHarvestThreshold) {
    super(imgName);
    this.enumValue = enumValue;
    this.ID = id;
    this.type = powerType;
    this.name = name;
    this.descriptions = descriptions;
    this.owner = owner;
    this.amount = amount;
    this.cropRarity = rarity;
    this.isHarvestAll = isHarvestAll;
    this.autoHarvestThreshold = autoHarvestThreshold;
    this.cropPowerId = CROP_POWER_ID_COUNTER++;
    this.powerCard = powerCard;

//    triggerCropGained();

    logger.debug(this.name + ": gained " + amount + " stacks. Owner: " + owner.name);
  }

  private void triggerCropGained() {
    logger.debug(this.name + ": gained " + amount + " stacks. Owner: " + owner.name);

    getPlayer().getCropUtil().onCropGained(this);
  }

//  private void triggerCropLost() {
//    getPlayer().getCropUtil().onCropLost(this);
//  }


  protected String getPassiveDescription() {
    return PASSIVE_DESCRIPTIONS[0] + this.autoHarvestThreshold + PASSIVE_DESCRIPTIONS[1];
  }

  void onHarvest(int amount) {
    hasHarvestedThisTurn = true;
    logger.debug(this.name + ": harvested. Set hasHarvestedThisTurn");

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

//  @Override
//  public void onGainCharge(int chargeAmount) {
//    super.onGainCharge(chargeAmount);
//    logger.debug(this.name + ": gained " + chargeAmount + " stacks");
//  }
//
//
//  @Override
//  public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
//    super.onApplyPower(power, target, source);
//
//    if (power instanceof AbstractCropPower && ((AbstractCropPower)power).getInstanceId() == this.getInstanceId()) {
//      logger.debug(this.name + ": power applied");
//    }
//  }

  public int getMaturityThreshold() {
    return this.autoHarvestThreshold;
  }

  public void updateMaturityThreshold(int amount) {
    this.autoHarvestThreshold += amount;
    this.updateDescription();
  }

  public boolean isMature() {
    return this.amount >= this.getMaturityThreshold();
  }

  private static TheSimpletonCharacter getPlayer() {
    return SimpletonUtil.getPlayer();
  }

  public void onUseCard(AbstractCard card, UseCardAction action) {
    if (card.hasTag(TheSimpletonCardTags.HARVEST) && card instanceof HarvestCard && ((HarvestCard) card).isAutoHarvest()) {
      harvest(((HarvestCard) card).isHarvestAll(), ((HarvestCard) card).getHarvestAmount());
    }
  }

  public void harvest(boolean harvestAll, int maxHarvestAmount) {
    onHarvest(amount);
    final int harvestAmount = calculateHarvestAmount(this.amount, maxHarvestAmount, harvestAll);

    if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
      if (harvestAmount > 0) {
        this.flash();
        harvestAction(harvestAmount);
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this.ID, harvestAmount));
      }
    }
  }

  protected int calculateHarvestAmount(int amount, int maxAmount, boolean harvestAll) {
    return Math.min(amount, harvestAll ? amount : maxAmount);
  }

  abstract protected int harvestAction(int harvestAmount);

  public void atStartOfTurn() {
    logger.debug("Checking for auto-harvest triggers (amount: " + this.amount + "; hasHarvester:  " +  getPlayer().hasRelic(TheHarvester.ID)+ ")");
    if (this.amount >= autoHarvestThreshold &&  getPlayer().hasRelic(TheHarvester.ID)) {
      logger.debug("Triggering Harvester");
      getPlayer().getRelic(TheHarvester.ID).flash();
      this.flash();
      harvest(isHarvestAll, STACKS_PER_TRIGGER);
    } else { logger.debug("Not triggered"); }
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

  public static boolean playerHasAnyActiveCropPowers() {
//    return AbstractCropPower.getActiveCropPowers().size() > 0;
    return getPlayer().getCropUtil().playerHasAnyCrops();
  }

  public static AbstractCropPower getOldestCropPower() {
    logger.debug("Newest crop per CropUtil: " + (getPlayer().getCropUtil().playerHasAnyCrops() ? getPlayer().getCropUtil().getOldestCrop().name : "None"));
    return getPlayer().getCropUtil().getOldestCrop();
  }

  public static AbstractCropPower getNewestCropPower() {
    logger.debug("Newest crop per CropUtil: " + (getPlayer().getCropUtil().playerHasAnyCrops() ? getPlayer().getCropUtil().getNewestCrop().name : "None"));
    return getPlayer().getCropUtil().getNewestCrop();
  }

  public static List<AbstractCropPower> getActiveCropPowers() {
    List<AbstractCropPower> cropPowers = new ArrayList<>();
    AbstractDungeon.player.powers.stream()
        .filter(power -> power instanceof AbstractCropPower)
        .sorted((p1, p2) -> ((AbstractCropPower)p2).cropPowerId)
        .forEach(power -> cropPowers.add((AbstractCropPower)power));

    logger.debug("AbstractCropPower.getActiveCropPowers: player has " + cropPowers.size() + " active crop powers");
    logger.debug("AbstractCropPower.getActiveCropPowers: player has " + AbstractDungeon.player.powers.size() + " active total powers");

    return cropPowers;
  }

  public static AbstractCropPower getRandomCropPower(AbstractPlayer p, int numStacks) {
    return getRandomCropPowers(p, 1, numStacks,false).get(0);
  }

  public static AbstractCropPower getRandomCropPower(AbstractPlayer p, int numStacks, boolean withRarityDistribution) {
    return getRandomCropPowers(p, 1, numStacks, withRarityDistribution).get(0);
  }

  public static List<AbstractCropPower> getRandomCropPowers(
      AbstractPlayer p, int numPowers, int numStacks, boolean withRarityDistribution) {
    return getRandomCropPowers(p, numPowers, numStacks, withRarityDistribution, power -> true);
  }

  public static List<AbstractCropPower> getActiveCropPowers(AbstractPlayer player, boolean shuffle) {
    final ArrayList<AbstractPower> activePowers = new ArrayList<>(player.powers);

    if (shuffle) {
      Collections.shuffle(activePowers);
    }

    List<AbstractCropPower> activeCropPowers = activePowers.stream()
        .filter(pow -> pow instanceof AbstractCropPower && pow.amount > 0 && !((AbstractCropPower) pow).finished)
        .map(pow -> (AbstractCropPower) pow)
        .sorted(Comparator.comparing(AbstractCropPower::getInstanceId))
        .collect(Collectors.toList());

    return activeCropPowers;
  }

  public static boolean hasHarvestedThisTurn() {
    return hasHarvestedThisTurn;
  }

  public int getInstanceId() {
    return this.cropPowerId;
  }

  @Override
  public void stackPower(int amount) {
    super.stackPower(amount);
    logger.debug("Called stackPower for " + this.ID + " amount: " + amount + ". Updated amount: " + this.amount);

    logger.debug("Triggering card updated triggers for " + this.name + ".stackPower");
    CropUtil.triggerCardUpdates();

    // TODO: change to triggerCropIncreased / triggerCropChanged
//    triggerCropGained();
    CropUtil.triggerCardUpdates();

    if (this.amount > this.autoHarvestThreshold) {
      this.flash();
      this.harvest(false, this.amount - this.autoHarvestThreshold);
    }
  }

  @Override
  public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
    super.onApplyPower(power, target, source);
    if (power == this && target == getPlayer()) {
      logger.debug("Triggering onApplyPower for " + this.name);
      CropUtil.triggerCardUpdates();
    }
  }


  @Override
  public void onInitialApplication() {
    super.onInitialApplication();
    logger.debug("******************************* onInitialApplication: " + this.name);
    triggerCropGained();
//    getPlayer().getCropUtil().onCropGained(this);
  }


  @Override
  public void onGainCharge(int a) {
    super.onGainCharge(a);
    logger.debug("**************@@@@@@@@@@@@@@@@@ onGainCharge: " + this.name + ", amount:" + a);

  }

  @Override
  public void onRemove() {
    super.onRemove();
    getPlayer().getCropUtil().onCropLost(this);
  }

  public static List<AbstractCropPower> getReferencePowers() {
    if (referencePowers.isEmpty()) {
      AbstractCreature dummy = SimpletonUtil.getDummyCreature();

      //TODO: accomplish this dynamically via Crop enum

      final PlantArtichokePower artichokePower = new PlantArtichokePower(dummy, 0);
      final PlantChiliPower chiliPower = new PlantChiliPower(dummy, 0);
      final PlantCornPower cornPower = new PlantCornPower(dummy, 0);
      final PlantMushroomPower mushroomPower = new PlantMushroomPower(dummy, 0);
      final PlantOnionPower onionPower = new PlantOnionPower(dummy, 0);
      final PlantPotatoPower potatoPower = new PlantPotatoPower(dummy, 0);
      final PlantSpinachPower spinachPower = new PlantSpinachPower(dummy, 0);
      final PlantTurnipPower turnipPower = new PlantTurnipPower(dummy, 0);

      referencePowers.add(artichokePower);
      referencePowers.add(chiliPower);
      referencePowers.add(cornPower);
      referencePowers.add(spinachPower);
      referencePowers.add(mushroomPower);
      referencePowers.add(onionPower);
      referencePowers.add(potatoPower);
      referencePowers.add(turnipPower);

    }
    return referencePowers;
  }

  public static List<AbstractCropPower> getRandomCropPowers(
      AbstractPlayer p, int numPowers, int numStacks, boolean withRarityDistribution,
      Predicate<AbstractCropPower> predicate){
    // TODO: move this logic to a plant power manager class

    final List<AbstractCropPower> referencePowers = getReferencePowers();

    List<AbstractCropPower> filteredPowers = referencePowers.stream()
        .filter(predicate)
        .collect(Collectors.toList());

    logger.debug(AbstractCropPowerCard.class.getSimpleName()
        + ".getRandomCropPowers :: referencePowers: "
        + referencePowers.stream().map(rp -> rp.name).collect(Collectors.joining(", ")));

    logger.debug(AbstractCropPowerCard.class.getSimpleName()
        + ".getRandomCropPowers :: filteredPowers: "
        + filteredPowers.stream().map(fp -> fp.name).collect(Collectors.joining(", ")));

    ArrayList<AbstractCropPower> resultPowers;

    final int numTotalPowers = filteredPowers.size();
    if (numPowers > numTotalPowers) {
      throw new IndexOutOfBoundsException("Requested " + numPowers + " powers but only " + numTotalPowers
          + " powers are available");
    } else if (numPowers == numTotalPowers) {
      logger.debug("Requested powers size equals total powers. Returning all powers.");
      resultPowers = new ArrayList(filteredPowers);
    } else {
      logger.debug("Choosing powers by probability distribution");

      ArrayList<AbstractCropPower> distributedPowers = new ArrayList<>();
      filteredPowers.forEach(power -> {
        List<AbstractCropPower> copies =
            Collections.nCopies(withRarityDistribution
                    ? AbstractCropPower.CROP_RARITY_DISTRIBUTION.get(power.cropRarity) : 1,
                power);
        distributedPowers.addAll(copies);
      });

      logger.debug(AbstractCropPowerCard.class.getSimpleName()
          + ".getRandomCropPowers :: distributedPowers: "
          + distributedPowers.stream().map(dp -> dp.name).collect(Collectors.joining(", ")));

      Collections.shuffle(distributedPowers);

      logger.debug("Choosing " + numPowers + " powers from distributedPowers");

      resultPowers = new ArrayList<>();
      for (int i = 0; i < numPowers; i++) {
        final AbstractCropPower power = distributedPowers.get(0);
        resultPowers.add(power);
        logger.debug("Selected " + power.name);

        distributedPowers.removeIf(pow -> pow == power);
      }
    }

    logger.debug(AbstractCropPowerCard.class.getSimpleName()
        + ".getRandomCropPowers :: resultPowers: "
        + resultPowers.stream().map(rp -> rp.name).collect(Collectors.joining(", ")));

    Collections.shuffle(resultPowers);

    logger.debug(AbstractCropPowerCard.class.getSimpleName()
        + ".getRandomCropPowers :: resultPowers(shuffled): "
        + resultPowers.stream().map(rp -> rp.name).collect(Collectors.joining(", ")));

    //TODO: change to create clones (with a factory model) and instantiate by key/name
    //then set original owner to null and update to be player?
    //then don't trigger on constructor if owner is null
    resultPowers.forEach(pow -> {
      pow.stackPower(numStacks);
    });

    return resultPowers;
  }

  public static int getNumberActiveCropTypes() {
    return getActiveCropPowers().size();
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_DESCRIPTION_ID);
    PASSIVE_DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // define rarity distribution
    Map<CardRarity, Integer> rarityDistribution = new HashMap<>();
    rarityDistribution.put(CardRarity.BASIC, 27);
    rarityDistribution.put(CardRarity.COMMON, 18);
    rarityDistribution.put(CardRarity.UNCOMMON, 12);
    rarityDistribution.put(CardRarity.RARE, 8);
    CROP_RARITY_DISTRIBUTION = Collections.unmodifiableMap(rarityDistribution);

    // TODO: move to CropUtil?
    // reset hasHarvestedThisTurn at start of combat and at end of turn
    Trigger trigger = new Trigger() {
      public void trigger() {
        logger.debug("Resetting hasHarvestedThisTurn");
        hasHarvestedThisTurn = false;
      }
    };
    TheSimpletonCharacter.addPrecombatPredrawTrigger(trigger);
    TheSimpletonCharacter.addStartOfTurnTrigger(trigger);
    TheSimpletonCharacter.addEndOfTurnTrigger(trigger);

//    getPlayer().drawPile.
  }
}