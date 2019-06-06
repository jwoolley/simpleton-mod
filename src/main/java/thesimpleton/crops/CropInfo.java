package thesimpleton.crops;

import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;

public abstract class CropInfo {
  public Crop cropEnum;
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

  public CropInfo(Crop cropEnum, CropInfoDefinition cropInfoDefinition) {
    this.cropEnum = cropEnum;
    this.orbId =  cropInfoDefinition.orbId;
    this.powerCard =  cropInfoDefinition.powerCard;
    this.rarity =  cropInfoDefinition.rarity;
    this.maturityThreshold =  cropInfoDefinition.maturityThreshold;
  }

  public abstract AbstractCrop getCrop();
}
