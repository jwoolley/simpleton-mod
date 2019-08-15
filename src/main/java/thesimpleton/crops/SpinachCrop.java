package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Spinach;
import thesimpleton.orbs.SpinachCropOrb;

public class SpinachCrop extends AbstractCrop {
  public static final Crop CROP_ENUM = Crop.SPINACH;
  public static int STRENGTH_PER_STACK = 1;

  public SpinachCrop() {
    super(CROP_ENUM);
    logger.debug("MAKIN' Spinach (instantiating Spinach).");
  }

  @Override
  protected int harvestAction(int harvestAmount) {
    AbstractPlayer player = AbstractDungeon.player;

    if (harvestAmount > 0) {
      AbstractDungeon.actionManager.addToTop(
          new ApplyPowerAction(player, player,
              new StrengthPower(player, harvestAmount * STRENGTH_PER_STACK), harvestAmount * STRENGTH_PER_STACK));
    }
    return harvestAmount;
  }
}