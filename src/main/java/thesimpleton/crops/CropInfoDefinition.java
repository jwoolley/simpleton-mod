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

  static final CropInfoDefinition Artichokes;
  static final CropInfoDefinition Asparagus;
  static final CropInfoDefinition Chilis;
  static final CropInfoDefinition Corn;
  static final CropInfoDefinition Mushrooms;
  static final CropInfoDefinition Onions;
  static final CropInfoDefinition Potatoes;
  static final CropInfoDefinition Spinach;
  static final CropInfoDefinition Squash;
  static final CropInfoDefinition Turnips;

  // TODO: make a separate CropRarity enum

  static {
    Artichokes = new CropInfoDefinition(
        ArtichokeCropOrb.ORB_ID,
        new Artichokes(),
        AbstractCard.CardRarity.COMMON,
        2) {
      public AbstractCrop getCrop() { return new ArtichokeCrop(); }
    };

    Asparagus = new CropInfoDefinition(
        AsparagusCropOrb.ORB_ID,
        new Asparagus(),
        AbstractCard.CardRarity.UNCOMMON,
        3) {
      public AbstractCrop getCrop() { return new AsparagusCrop(); }
    };

    Chilis = new CropInfoDefinition(
        ChiliCropOrb.ORB_ID,
        new Chilis(), AbstractCard.CardRarity.UNCOMMON,
        5) {
      public AbstractCrop getCrop() { return new ChilisCrop(); }
    };

    Corn = new CropInfoDefinition(
        CornCropOrb.ORB_ID,
        new Corn(),
        AbstractCard.CardRarity.COMMON,
        2) {
      public AbstractCrop getCrop() { return new CornCrop(); }
    };

    Mushrooms = new CropInfoDefinition(
        MushroomCropOrb.ORB_ID,
        new Mushrooms(),
        AbstractCard.CardRarity.RARE,
        2) {
      public AbstractCrop getCrop() { return new MushroomCrop(); }
    };

    Onions= new CropInfoDefinition(
        OnionCropOrb.ORB_ID,
        new Onions(),
        AbstractCard.CardRarity.BASIC,
        5) {
      public AbstractCrop getCrop() { return new OnionCrop(); }
    };

    Potatoes = new CropInfoDefinition(
        PotatoCropOrb.ORB_ID,
        new Potatoes(),
        AbstractCard.CardRarity.BASIC,
        5) {
      public AbstractCrop getCrop() { return new PotatoCrop(); }
    };

    Spinach = new CropInfoDefinition(
        SquashCropOrb.ORB_ID,
        new Squash(),
        AbstractCard.CardRarity.BASIC,
        5) {
      public AbstractCrop getCrop() { return new SquashCrop(); }
    };

    Squash = new CropInfoDefinition(
        SquashCropOrb.ORB_ID,
        new Spinach(),
        AbstractCard.CardRarity.UNCOMMON,
        5) {
      public AbstractCrop getCrop() { return new SquashCrop(); }
    };

    Turnips = new CropInfoDefinition(
        TurnipCropOrb.ORB_ID,
        new Turnips(),
        AbstractCard.CardRarity.COMMON,
        5) {
      public AbstractCrop getCrop() { return new TurnipCrop(); }
    };
  }
}