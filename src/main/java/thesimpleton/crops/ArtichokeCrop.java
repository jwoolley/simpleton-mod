package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ThornsPower;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Artichokes;
import thesimpleton.orbs.ArtichokeCropOrb;

public class ArtichokeCrop extends AbstractCrop {
  public static final Crop CROP_ENUM = Crop.ARTICHOKES;

  public static int THORNS_PER_STACK = 1;

  public ArtichokeCrop() {
    super(CROP_ENUM);
    logger.info("MAKIN' Artichokes (instantiating ArtichokeCrop).");
  }


  protected int harvestAction(int harvestAmount) {
    AbstractPlayer player = SimpletonUtil.getPlayer();

    if (harvestAmount > 0) {
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(
              player, player,
              new ThornsPower(player,harvestAmount * THORNS_PER_STACK), harvestAmount * THORNS_PER_STACK));
    }
    return harvestAmount;
  }
}