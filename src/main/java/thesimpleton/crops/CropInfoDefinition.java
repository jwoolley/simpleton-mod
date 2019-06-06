package thesimpleton.crops;

import com.megacrit.cardcrawl.cards.AbstractCard;
import thesimpleton.cards.power.crop.*;
import thesimpleton.orbs.*;

abstract class CropInfoDefinition {
  final String orbId;
  final AbstractCropPowerCard powerCard;
  final AbstractCard.CardRarity rarity;
  final int maturityThreshold;

  public CropInfoDefinition(String orbId, AbstractCropPowerCard powerCard, AbstractCard.CardRarity rarity, int maturityThreshold) {
    this.orbId = orbId;
    this.powerCard = powerCard;
    this.rarity = rarity;
    this.maturityThreshold = maturityThreshold;
  }

  public abstract AbstractCrop getCrop();

  public static final CropInfoDefinition Artichokes;
  public static final CropInfoDefinition Asparagus;
  public static final CropInfoDefinition Chilis;
  public static final CropInfoDefinition Corn;
  public static final CropInfoDefinition Mushrooms;
  public static final CropInfoDefinition Onions;
  public static final CropInfoDefinition Potatoes;
  public static final CropInfoDefinition Spinach;
  public static final CropInfoDefinition Squash;
  public static final CropInfoDefinition Turnips;

  static {
    Artichokes = new CropInfoDefinition(ArtichokeCropOrb.ORB_ID, new Artichokes(), AbstractCard.CardRarity.COMMON, 2) {
      public AbstractCrop getCrop() { return new ArtichokeCrop(); }
    };

    Asparagus = new CropInfoDefinition(AsparagusCropOrb.ORB_ID, new Asparagus(), AbstractCard.CardRarity.UNCOMMON, 3) {
      public AbstractCrop getCrop() { return new AsparagusCrop(); }
    };

    Chilis = new CropInfoDefinition(ChiliCropOrb.ORB_ID, new Chilis(), AbstractCard.CardRarity.UNCOMMON, 5) {
      public AbstractCrop getCrop() { return new AsparagusCrop(); }
    };

    Corn = new CropInfoDefinition(CornCropOrb.ORB_ID, new Corn(), AbstractCard.CardRarity.COMMON, 2) {
      public AbstractCrop getCrop() { return new CornCrop(); }
    };

    Mushrooms = new CropInfoDefinition(MushroomCropOrb.ORB_ID, new Mushrooms(), AbstractCard.CardRarity.RARE, 2) {
      public AbstractCrop getCrop() { return new MushroomCrop(); }
    };

    Onions = new CropInfoDefinition(PotatoCropOrb.ORB_ID, new Potatoes(), AbstractCard.CardRarity.BASIC, 5) {
      public AbstractCrop getCrop() { return new PotatoCrop(); }
    };

    Potatoes = new CropInfoDefinition(OnionCropOrb.ORB_ID, new Onions(), AbstractCard.CardRarity.BASIC, 5) {
      public AbstractCrop getCrop() { return new OnionCrop(); }
    };

    Spinach = new CropInfoDefinition(SquashCropOrb.ORB_ID, new Squash(), AbstractCard.CardRarity.BASIC, 5) {
      public AbstractCrop getCrop() { return new SquashCrop(); }
    };

    Squash = new CropInfoDefinition(SpinachCropOrb.ORB_ID, new Spinach(), AbstractCard.CardRarity.UNCOMMON, 2) {
      public AbstractCrop getCrop() { return new SpinachCrop(); }
    };

    Turnips = new CropInfoDefinition(TurnipCropOrb.ORB_ID, new Turnips(), AbstractCard.CardRarity.COMMON, 5) {
      public AbstractCrop getCrop() { return new TurnipCrop(); }
    };
  }
}