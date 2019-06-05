package thesimpleton.powers.utils;

import thesimpleton.crops.*;
import thesimpleton.orbs.*;

import java.util.HashMap;
import java.util.Map;

// TODO: instantiate these reference crops in CropUtil
public enum Crop {
  ARTICHOKES(),
  ASPARAGUS(),
  CORN(),
  CHILIS(),
  SQUASH(),
  MUSHROOMS(),
  ONIONS(),
  POTATOES(),
  SPINACH(),
  TURNIPS();

  private AbstractCrop crop;

  public String getName() {
    return this.getCrop().getName();
  }

  public static void initialize() {
      Map<Crop, AbstractCrop> mappings = new HashMap<>();
      mappings.put(Crop.ARTICHOKES, new ArtichokeCrop());
      mappings.put(Crop.ASPARAGUS, new AsparagusCrop());
      mappings.put(Crop.CORN, new CornCrop());
      mappings.put(Crop.CHILIS, new ChilisCrop());
      mappings.put(Crop.SQUASH, new SquashCrop());
      mappings.put(Crop.MUSHROOMS, new MushroomCrop());
      mappings.put(Crop.ONIONS, new OnionCrop());
      mappings.put(Crop.POTATOES, new PotatoCrop());
      mappings.put(Crop.SPINACH, new SpinachCrop());
      mappings.put(Crop.TURNIPS, new TurnipCrop());

      for (Crop crop : Crop.values()) {
        crop.crop =  mappings.get(crop);
        if (crop.crop == null) {
          throw new RuntimeException("Crop field for Crop enum " + crop + " is not defined");
        }
      }
  }

  public AbstractCrop getCrop() {
    if (this.crop == null) {
      throw new RuntimeException("Crop field for Crop enum " + this + " is not defined");
    }

    return this.crop;
  }

  public static AbstractCropOrb getCropOrb(Crop crop) {
    return getCropOrb(crop, 0);
  }

  public static AbstractCropOrb getCropOrb(Crop crop, int amount) {
    switch(crop) {
      case ARTICHOKES:
        return new ArtichokeCropOrb(amount);
      case ASPARAGUS:
        return new AsparagusCropOrb(amount);
      case CORN:
        return new CornCropOrb(amount);
      case CHILIS:
        return new ChiliCropOrb(amount);
      case MUSHROOMS:
        return new MushroomCropOrb(amount);
      case ONIONS:
        return new OnionCropOrb(amount);
      case POTATOES:
        return new PotatoCropOrb(amount);
      case SPINACH:
        return new SpinachCropOrb(amount);
      case SQUASH:
        return new SquashCropOrb(amount);
      case TURNIPS:
        return new TurnipCropOrb(amount);
      default:
        throw new IllegalArgumentException("Crop cannot be null");
    }
  }
}