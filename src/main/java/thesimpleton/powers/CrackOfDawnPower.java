package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.WeakPower;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.ApplyBurningAction;
import thesimpleton.actions.CropSpawnAction;
import thesimpleton.crops.Crop;
import thesimpleton.orbs.CornCropOrb;

public class CrackOfDawnPower extends AbstractTheSimpletonPower  {
  public static final String POWER_ID = "TheSimpletonMod:CrackOfDawnPower";
  private static final PowerStrings powerStrings;

  public static final String NAME;
  public static final String[] DESCRIPTIONS;
  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "crackofdawn.png";

  private AbstractCreature source;

  public static final int DAMAGE_AMOUNT_PER_STACK = 16;
  public static final int CROP_AMOUNT_PER_STACK = 3;
  public static final int ENERGY_AMOUNT_PER_STACK = 3;

  private int damageAmount;
  private int plantAmount;
  private int energyAmount;

  public CrackOfDawnPower(AbstractCreature source, int amount) {
    super(IMG);
    this.name = NAME;
    this.ID = POWER_ID;
    this.type = POWER_TYPE;
    this.owner = AbstractDungeon.player;
    this.source = source;
    this.amount = amount;
    this.damageAmount = DAMAGE_AMOUNT_PER_STACK;

    this.damageAmount = DAMAGE_AMOUNT_PER_STACK;
    this.plantAmount = CROP_AMOUNT_PER_STACK;
    this.energyAmount = ENERGY_AMOUNT_PER_STACK;

    updateDescription();
  }

  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + this.amount + (this.amount == 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2]) + DESCRIPTIONS[3] +
        this.damageAmount + DESCRIPTIONS[4] + this.plantAmount + DESCRIPTIONS[5] +
        StringUtils.repeat(DESCRIPTIONS[6], this.energyAmount) + DESCRIPTIONS[7];
  }

  public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
    if (target.hasPower(WeakPower.POWER_ID) && (target != this.owner) && (info.type == DamageInfo.DamageType.NORMAL)) {
      flashWithoutSound();
      AbstractDungeon.actionManager.addToBottom(new ApplyBurningAction(target, this.source, this.amount));
    }
  }

  @Override
  public void atStartOfTurnPostDraw() {
    if (this.amount <= 1) {
      AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
      logger.debug("CrackOfDawnPower::atStartOfTurnPostDraw resolving power effect");
      resolvePowerEffect();
    } else {
      AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
    }
  }

  private void resolvePowerEffect() {
    Logger logger = TheSimpletonMod.logger;

    logger.debug("CrackOfDawnPower::resolvePowerEffect called");

    DamageInfo info =  new DamageInfo(this.owner, this.damageAmount, DamageInfo.DamageType.NORMAL);

    AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(this.owner, DamageInfo.createDamageMatrix(info.base),
        DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE, true));

    AbstractDungeon.actionManager.addToBottom(new CropSpawnAction(new CornCropOrb(this.plantAmount), true));

    AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.energyAmount));

  }

  @Override
  public void stackPower(int amount) {
    this.damageAmount += DAMAGE_AMOUNT_PER_STACK;
    this.plantAmount += CROP_AMOUNT_PER_STACK;
    this.energyAmount += ENERGY_AMOUNT_PER_STACK;
  }


  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}

