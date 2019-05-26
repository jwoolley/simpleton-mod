package thesimpleton.powers.utils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import thesimpleton.orbs.AbstractCrop;
import thesimpleton.orbs.PotatoCrop;
import thesimpleton.powers.*;

public enum Crop {
  ARTICHOKES,
  ASPARAGUS,
  CORN,
  CHILIS,
  GOURDS,
  MUSHROOMS,
  ONIONS,
  POTATOES,
  SPINACH,
  TURNIPS;

  public static AbstractCropPower getCrop(Crop crop, AbstractCreature owner, int amount, boolean isFromCard) {
    switch(crop) {
      case ARTICHOKES:
        return new PlantArtichokePower(owner, amount, isFromCard);
      case ASPARAGUS:
        return new PlantAsparagusPower(owner, amount, isFromCard);
      case CORN:
        return new PlantCornPower(owner, amount, isFromCard);
      case CHILIS:
        return new PlantChiliPower(owner, amount, isFromCard);
      case GOURDS:PlantSquashPower:
        return new PlantSquashPower(owner, amount, isFromCard);
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

  public static AbstractCrop getCropOrb(Crop crop, int amount) {
    switch(crop) {
      case ARTICHOKES:
//        return new ArtichokeCrop(amount);
      case CORN:
//        return new CornCrop(amount);
      case CHILIS:
//        return new ChiliCrop(amount);
      case MUSHROOMS:
//        return new MushroomCrop(amount);
      case ONIONS:
//        return new OnionCrop(amount);
      case POTATOES:
        return new PotatoCrop(amount);
      case SPINACH:
//        return new SpinachCrop(amount);
      case TURNIPS:
//        return new TurnipCrop(amount);
      default:
        throw new IllegalArgumentException("Crop cannot be null");
    }
  }
}