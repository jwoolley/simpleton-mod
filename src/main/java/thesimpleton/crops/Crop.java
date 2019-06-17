package thesimpleton.crops;

import thesimpleton.orbs.*;

import java.util.Map;

public enum Crop {
  ARTICHOKES(CropInfoDefinition.Artichokes),
  ASPARAGUS(CropInfoDefinition.Asparagus),
  CHILIS(CropInfoDefinition.Chilis),
  COFFEE(CropInfoDefinition.Coffee),
  CORN(CropInfoDefinition.Corn),
  MUSHROOMS(CropInfoDefinition.Mushrooms),
  ONIONS(CropInfoDefinition.Onions),
  POTATOES(CropInfoDefinition.Potatoes),
  SPINACH(CropInfoDefinition.Spinach),
  SQUASH(CropInfoDefinition.Squash),
  STRAWBERRIES(CropInfoDefinition.Strawberries),
  TURNIPS(CropInfoDefinition.Turnips);

  private CropInfo cropInfo;
  private AbstractCrop placeholderCrop;

  Crop(CropInfoDefinition cropInfoDefinition) {
    this.cropInfo = new CropInfo(this, cropInfoDefinition) {
      public AbstractCrop getCrop() {
        if (placeholderCrop == null) {
          placeholderCrop = cropInfoDefinition.getCrop();
        }
        return placeholderCrop;
      }
    };
  }

  public CropInfo getCropInfo() {
    return cropInfo;
  }

  public String getName() {
    return this.name();
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
      case COFFEE:
        return new CoffeeCropOrb(amount);
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
      case STRAWBERRIES:
        return new MushroomCropOrb(amount); // return new StrawberryCropOrb(amount);
      case TURNIPS:
        return new TurnipCropOrb(amount);
      default:
        throw new IllegalArgumentException("Crop cannot be null");
    }
  }
}