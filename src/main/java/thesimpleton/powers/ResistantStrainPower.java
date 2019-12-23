package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;


public class ResistantStrainPower extends AbstractTheSimpletonPower {
  public static final String POWER_ID = "TheSimpletonMod:ResistantStrainPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "resistantstrain.png";

  private AbstractCreature source;

  public ResistantStrainPower(AbstractCreature owner, AbstractCreature source, int amount) {
    super(IMG);
    this.owner = owner;
    this.source = source;
    this.amount = amount;

    this.name = NAME;
    this.ID = POWER_ID;
    this.type = POWER_TYPE;

    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
  }

  @Override
  public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
    if ((power.type == AbstractPower.PowerType.DEBUFF) && (!power.ID.equals("Shackled")) && (source == this.owner) && (target != this.owner) &&
        (!target.hasPower("Artifact"))) {
      flash();
      AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this.owner, this.owner, this.amount));
    }
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}