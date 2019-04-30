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

  public static AbstractCropPower getCrop(Crop crop, AbstractCreature owner, int amount, boolean isFromCard) {
    switch(crop) {
      case ARTICHOKES:
        return new PlantArtichokePower(owner, amount, isFromCard);
      case CORN:
        return new PlantCornPower(owner, amount, isFromCard);
      case CHILIS:
        return new PlantChiliPower(owner, amount, isFromCard);
      case MUSHROOMS:
        return new PlantMushroomPower(owner, amount, isFromCard);
      case ONIONS:
        return new PlantOnionPower(owner, amount, isFromCard);
      case POTATOES:
        return new PlantPotatoPower(owner, amount, isFromCard);
      case SPINACH:
        return new PlantSpinachPower(owner, amount, isFromCard);
      case TURNIPS:
        return new PlantTurnipPower(owner, amount, isFromCard);
      default:
        throw new IllegalArgumentException("Crop cannot be null");
    }
  }
}