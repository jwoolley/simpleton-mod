package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Asparagus;
import thesimpleton.orbs.AsparagusCropOrb;
import thesimpleton.powers.utils.Crop;

public class AsparagusCrop extends AbstractCrop {
  public static final Crop CROP_ENUM = Crop.ASPARAGUS;
  private static final String ORB_ID = AsparagusCropOrb.ORB_ID;
  private static final AbstractCropPowerCard POWER_CARD = new Asparagus();

  public static final int MATURITY_THRESHOLD = 2;
  private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.UNCOMMON;

  private static int DEXTERITY_PER_STACK = 1;

  public AsparagusCrop() {
    super(CROP_ENUM, ORB_ID, POWER_CARD, RARITY, MATURITY_THRESHOLD);
    logger.debug("MAKIN' Asparagus (instantiating Asparagus).");
  }

  protected int harvestAction(int harvestAmount) {
    AbstractPlayer player = SimpletonUtil.getPlayer();
    if (harvestAmount > 0) {
      AbstractDungeon.actionManager.addToTop(
          new ApplyPowerAction(player, player,
              new DexterityPower(player, DEXTERITY_PER_STACK), harvestAmount * DEXTERITY_PER_STACK));
    }
    return harvestAmount;
  }
}