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
import thesimpleton.powers.utils.Crop;

public class SpinachCrop extends AbstractCrop {
  public static final Crop CROP_ENUM = Crop.SPINACH;
  private static final String ORB_ID = SpinachCropOrb.ORB_ID;
  private static final AbstractCropPowerCard POWER_CARD = new Spinach();

  private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.UNCOMMON;

  public static final int MATURITY_THRESHOLD = 2;
  public static int STRENGTH_PER_STACK = 1;

  public SpinachCrop() {
    super(CROP_ENUM, ORB_ID, POWER_CARD, RARITY, MATURITY_THRESHOLD);
    logger.debug("MAKIN' Spinach (instantiating Spinach).");
  }

  @Override
  protected int harvestAction(int harvestAmount) {
    AbstractPlayer player = SimpletonUtil.getPlayer();

    if (harvestAmount > 0) {
      AbstractDungeon.actionManager.addToTop(
          new ApplyPowerAction(player, player,
              new StrengthPower(player, harvestAmount * STRENGTH_PER_STACK), harvestAmount * STRENGTH_PER_STACK));
    }
    return harvestAmount;
  }
}