package thesimpleton.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.core.AbstractCreature;
import thesimpleton.actions.ApplyCropAction;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.powers.utils.Crop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// TODO: create separate enum for CropRarity
public abstract class AbstractCropPowder extends AbstractTheSimpletonPower {

  public static Map<CardRarity, Integer> CROP_RARITY_DISTRIBUTION;

  private static boolean IS_HARVEST_ALL = false;
  private static int AUTO_HARVEST_THRESHOLD = 5;
  private static int CROP_POWER_ID_COUNTER = 0;
  private static int STACKS_PER_TRIGGER = 1;
  private static boolean hasHarvestedThisTurn = false;

  private static final String POWER_DESCRIPTION_ID = "TheSimpletonMod:AbstractCropPowder";
  private static final List<AbstractCropPowder> referencePowers = new ArrayList<>();;

  public final Crop enumValue;
  public final String[] descriptions;
  private final int cropPowerId;
  private final boolean isHarvestAll;
  private final AbstractCropPowerCard powerCard;
  private int autoHarvestThreshold;


  public AbstractCard.CardRarity cropRarity;
//
AbstractCropPowder(Crop enumValue, String name, String id, PowerType powerType, String[] descriptions, String imgName, AbstractCreature owner, AbstractCard.CardRarity rarity,
                    AbstractCropPowerCard powerCard, int amount, boolean isFromCard) {
    this(enumValue, name, id, powerType, descriptions, imgName, owner, rarity, powerCard, amount, isFromCard, IS_HARVEST_ALL, AUTO_HARVEST_THRESHOLD);
    logger.debug("Instantiating CropPower:  enumValue:" + enumValue + ",  name:" + name+ ",  id:" + id + ",  powerType:" + powerType.name()+ ",  owner:" + owner);
  }

  AbstractCropPowder(Crop enumValue, String name, String id, PowerType powerType,  String[] descriptions, String imgName, AbstractCreature owner, AbstractCard.CardRarity rarity,
                    AbstractCropPowerCard powerCard, int amount, boolean isFromCard, boolean isHarvestAll) {
    this(enumValue, name, id, powerType, descriptions, imgName, owner, rarity, powerCard, amount, isFromCard, isHarvestAll, AUTO_HARVEST_THRESHOLD);
  }

  AbstractCropPowder(Crop enumValue, String name, String id, PowerType powerType, String[] descriptions, String imgName, AbstractCreature owner, AbstractCard.CardRarity rarity,
                            AbstractCropPowerCard powerCard, int amount,  boolean isFromCard, boolean isHarvestAll,
                            int autoHarvestThreshold) {
    super(imgName);
    this.enumValue = enumValue;
    this.ID = id;
    this.type = powerType;
    this.name = name;
    this.descriptions = descriptions;
    this.owner = owner;
    this.amount = ApplyCropAction.calculateCropStacks(amount, isFromCard);
    this.cropRarity = rarity;
    this.isHarvestAll = isHarvestAll;
    this.autoHarvestThreshold = autoHarvestThreshold;
    this.cropPowerId = CROP_POWER_ID_COUNTER++;
    this.powerCard = powerCard;

//    triggerCropGained();

    logger.debug(this.name + ": gained " + amount + " stacks. Owner: " + owner.name);
  }
}