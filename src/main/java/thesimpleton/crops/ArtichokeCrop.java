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
import thesimpleton.powers.utils.Crop;

public class ArtichokeCrop extends AbstractCrop {
  public static final Crop CROP_ENUM = Crop.ARTICHOKES;
  private static final String ORB_ID = ArtichokeCropOrb.ORB_ID;
  private static final AbstractCropPowerCard POWER_CARD = new Artichokes();
  private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.UNCOMMON;

  public static final int MATURITY_THRESHOLD = 2;
  public static int THORNS_PER_STACK = 1;

  public ArtichokeCrop() {
    super(CROP_ENUM, ORB_ID, POWER_CARD, RARITY, MATURITY_THRESHOLD);
    logger.debug("MAKIN' Artichokes (instantiating ArtichokeCrop).");
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