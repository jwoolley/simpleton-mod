package thesimpleton.crops;

import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.powers.utils.Crop;

public abstract class CropInfo {
  public final Crop cropEnum;
  public final String orbId;
  public final AbstractCropPowerCard powerCard;
  public final CardRarity rarity;
  public final int maturityThreshold;

  public CropInfo(Crop cropEnum, String orbId, AbstractCropPowerCard powerCard, CardRarity rarity, int maturityThreshold) {
    this.cropEnum = cropEnum;
    this.orbId = orbId;
    this.powerCard = powerCard;
    this.rarity = rarity;
    this.maturityThreshold = maturityThreshold;
  }

  public abstract AbstractCrop getCrop();
}
