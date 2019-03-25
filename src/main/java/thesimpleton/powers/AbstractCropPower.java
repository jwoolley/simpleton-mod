package thesimpleton.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

public abstract class AbstractCropPower extends AbstractTheSimpletonPower {

  private static boolean IS_HARVEST_ALL = false;
  private static int AUTO_HARVEST_THRESHOLD = 5;

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
  }

  abstract public void harvest(boolean all, int amount);

  public void atStartOfTurn() {
    if (this.amount >= autoHarvestThreshold) {
      this.flash();
      harvest(isHarvestAll, 1);
    }
  }

  // TODO: move shared functionality(e.g. harvest logic) to here
}
