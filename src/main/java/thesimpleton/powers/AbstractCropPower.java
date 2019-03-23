package thesimpleton.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

public abstract class AbstractCropPower extends AbstractTheSimpletonPower {

  public AbstractCropPower(String imgName, AbstractCreature owner, int amount) {
    super(imgName);
    this.owner = owner;
    this.amount = amount;
  }

  abstract public void harvest(boolean all, int amount);

  // TODO: move shared functionality(e.g. harvest logic) to here
}
