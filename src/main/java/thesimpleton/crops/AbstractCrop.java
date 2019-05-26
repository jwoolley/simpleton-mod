package thesimpleton.crops;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.characters.TheSimpletonCharacter;
import thesimpleton.orbs.AbstractCropOrb;
import thesimpleton.powers.AbstractCropPower;
import thesimpleton.powers.utils.Crop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AbstractCrop {
  public static Map<AbstractCard.CardRarity, Integer> CROP_RARITY_DISTRIBUTION;
  public final Crop enumValue;
  public final  AbstractCard.CardRarity cropRarity;
  public final AbstractCropOrb cropOrb;

  protected static final Logger logger = TheSimpletonMod.logger;

  private static final List<AbstractCrop> referenceCrops = new ArrayList<>();;
  private static boolean IS_HARVEST_ALL = false;
  private static int AUTO_HARVEST_THRESHOLD = 5;
  private static int CROP_POWER_ID_COUNTER = 0;
  private static int STACKS_PER_TRIGGER = 1;
  private static boolean hasHarvestedThisTurn = false;

  private final boolean isHarvestAll;
  private final AbstractCropPowerCard powerCard;
  private int autoHarvestThreshold;

  AbstractCrop(Crop enumValue, AbstractCropOrb cropOrb, AbstractCard.CardRarity rarity, AbstractCropPowerCard powerCard,
               int autoHarvestThreshold) {
    this(enumValue, cropOrb, rarity, powerCard, autoHarvestThreshold, false);
  }

  AbstractCrop(Crop enumValue, AbstractCropOrb cropOrb, AbstractCard.CardRarity rarity, AbstractCropPowerCard powerCard, int autoHarvestThreshold,
    boolean isHarvestAll) {
    this.enumValue = enumValue;
    this.cropOrb = (AbstractCropOrb)cropOrb.makeCopy();
    this.powerCard = powerCard;
    this.cropRarity = rarity;
    this.autoHarvestThreshold = autoHarvestThreshold;
    this.isHarvestAll = isHarvestAll;
  }

  private void triggerCropGained() {
//    getPlayer().getCropUtil().onCropGained(this);
  }

  private static TheSimpletonCharacter getPlayer() {
    return SimpletonUtil.getPlayer();
  }
}
