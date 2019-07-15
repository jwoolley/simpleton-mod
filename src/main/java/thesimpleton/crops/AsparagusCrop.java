package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import thesimpleton.cards.SimpletonUtil;

public class AsparagusCrop extends AbstractCrop {
  public static final Crop CROP_ENUM = Crop.ASPARAGUS;

  public static int DEXTERITY_PER_STACK = 1;

  public AsparagusCrop() {
    super(CROP_ENUM);
    logger.info("MAKIN' Asparagus (instantiating Asparagus).");
  }

  protected int harvestAction(int harvestAmount) {
    AbstractPlayer player = AbstractDungeon.player;
    if (harvestAmount > 0) {
      AbstractDungeon.actionManager.addToTop(
          new ApplyPowerAction(player, player,
              new DexterityPower(player, harvestAmount * DEXTERITY_PER_STACK),
              harvestAmount * DEXTERITY_PER_STACK));
    }
    return harvestAmount;
  }
}