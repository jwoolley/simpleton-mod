package thesimpleton.powers;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.CropSpawnAction;
import thesimpleton.crops.AbstractCrop;
import thesimpleton.crops.Crop;


public class PhotosynthesisPower extends AbstractTheSimpletonPower {
  public static final String POWER_ID = "TheSimpletonMod:PhotosynthesisPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = AbstractPower.PowerType.BUFF;
  public static final String IMG = "photosynthesis.png";

  public PhotosynthesisPower(AbstractCreature owner) {
    super(IMG);
    this.owner = owner;
    this.amount = -1;

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


    // TODO: optimize use of getCropOrb here

    if (energy > 0) {
      this.flash();
      for (int i = 0; i < energy; i++) {
        player.loseEnergy(1);
//        Crop randomCrop = AbstractCrop.getRandomCrop(player, 1).enumValue;
        Crop randomCrop = TheSimpletonMod.getSeasonInfo().getRandomCropInSeason();
        AbstractDungeon.actionManager.addToBottom(
            new CropSpawnAction(Crop.getCropOrb(randomCrop, 1), 1, false));

      }
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