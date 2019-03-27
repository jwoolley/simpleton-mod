package thesimpleton.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractCropPower extends AbstractTheSimpletonPower {

  private static boolean IS_HARVEST_ALL = false;
  private static int AUTO_HARVEST_THRESHOLD = 5;
  private static int CROP_POWER_ID_COUNTER = 0;

  private final int cropPowerId;
  private final boolean isHarvestAll;
  private int autoHarvestThreshold;

  AbstractCropPower(String imgName, AbstractCreature owner, int amount) {
    this(imgName, owner, amount, IS_HARVEST_ALL, AUTO_HARVEST_THRESHOLD);
  }

  AbstractCropPower(String imgName, AbstractCreature owner, int amount, boolean isHarvestAll) {
    this(imgName, owner, amount, isHarvestAll, AUTO_HARVEST_THRESHOLD);
  }

  private AbstractCropPower(String imgName, AbstractCreature owner, int amount, boolean isHarvestAll,
                            int autoHarvestThreshold) {
    super(imgName);
    this.owner = owner;
    this.amount = amount;
    this.isHarvestAll = isHarvestAll;
    this.autoHarvestThreshold = autoHarvestThreshold;
    this.cropPowerId = CROP_POWER_ID_COUNTER++;
  }

  abstract public void harvest(boolean all, int amount);

  public void atStartOfTurn() {
    if (this.amount >= autoHarvestThreshold) {
      this.flash();
      harvest(isHarvestAll, 1);
    }
  }

  public static boolean playerHasAnyActiveCropPowers() {
    return AbstractCropPower.getActiveCropPowers().size() > 0;
  }

  public static AbstractCropPower getOldestPower() {
    final Optional<AbstractPower> oldestPower = AbstractDungeon.player.powers.stream()
        .filter(power -> power instanceof AbstractCropPower)
        .reduce((p1, p2) -> ((AbstractCropPower) p2).cropPowerId < ((AbstractCropPower) p1).cropPowerId ? p2 : p1);

    return oldestPower.isPresent() ? (AbstractCropPower)oldestPower.get() : null;
  }

  public static AbstractCropPower getNewestPower() {
    final  Optional<AbstractPower> newestPower = AbstractDungeon.player.powers.stream()
        .filter(power -> power instanceof AbstractCropPower)
        .reduce((p1, p2) -> ((AbstractCropPower) p2).cropPowerId >= ((AbstractCropPower) p1).cropPowerId ? p2 : p1);

    return newestPower.isPresent() ? (AbstractCropPower)newestPower.get() : null;
  }

  public static List<AbstractCropPower> getActiveCropPowers() {
    List<AbstractCropPower> cropPowers = new ArrayList<>();
    AbstractDungeon.player.powers.stream()
        .filter(power -> power instanceof AbstractCropPower)
        .forEach(power -> cropPowers.add((AbstractCropPower)power));
    return cropPowers;
  }

  // TODO: move shared functionality(e.g. harvest logic) to here
}
