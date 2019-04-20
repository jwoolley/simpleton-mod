package thesimpleton.powers.utils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import thesimpleton.powers.*;

public enum Crop {
  ARTICHOKES,
  CORN,
  CHILIS,
  MUSHROOMS,
  ONIONS,
  POTATOES,
  SPINACH,
  TURNIPS;

  public static AbstractCropPower getCrop(Crop crop, AbstractCreature owner, int amount) {
    switch(crop) {
      case ARTICHOKES:
        return new PlantArtichokePower(owner, amount);
      case CORN:
        return new PlantCornPower(owner, amount);
      case CHILIS:
        return new PlantChiliPower(owner, amount);
      case MUSHROOMS:
        return new PlantMushroomPower(owner, amount);
      case ONIONS:
        return new PlantOnionPower(owner, amount);
      case POTATOES:
        return new PlantPotatoPower(owner, amount);
      case SPINACH:
        return new PlantSpinachPower(owner, amount);
      case TURNIPS:
        return new PlantTurnipPower(owner, amount);
      default:
        throw new IllegalArgumentException("Crop cannot be null");
    }
  }
}