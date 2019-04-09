package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;


public class PhotosynthesisPower extends AbstractTheSimpletonPower {
  public static final String POWER_ID = "TheSimpletonMod:PhotosynthesisPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = AbstractPower.PowerType.BUFF;
  public static final String IMG = "photosynthesis.png";

  private static final int CROPS_PER_ENERGY = 1;

  private AbstractCreature source;

  public PhotosynthesisPower(AbstractCreature owner, AbstractCreature source, int amount) {
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
    this.description = DESCRIPTIONS[0];
  }

  void applyPower() {
    final AbstractPlayer player = AbstractDungeon.player;
    final int energy = EnergyPanel.getCurrentEnergy();

    for (int i = 0; i < energy; i++) {
      this.flash();
      player.loseEnergy(1);
      final AbstractCropPower newCrop = AbstractCropPower.getRandomCropPower(player, 1);
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(player, player, newCrop, CROPS_PER_ENERGY));
    }
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    super.atEndOfTurn(isPlayer);
    if (isPlayer) {
      applyPower();
    }
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}