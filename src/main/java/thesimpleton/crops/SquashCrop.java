package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Squash;
import thesimpleton.orbs.SquashCropOrb;
import thesimpleton.powers.utils.Crop;

public class SquashCrop extends AbstractCrop {
  public static final Crop CROP_ENUM = Crop.SQUASH;
  private static final String ORB_ID = SquashCropOrb.ORB_ID;
  private static final AbstractCropPowerCard POWER_CARD = new Squash();

  public static final int MATURITY_THRESHOLD = 5;
  private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.UNCOMMON;

  private static final int BLOCK_PER_STACK = 6;

  public SquashCrop() {
    super(CROP_ENUM, ORB_ID, POWER_CARD, RARITY, MATURITY_THRESHOLD);
  }

  protected int harvestAction(int harvestAmount) {
    AbstractPlayer player = SimpletonUtil.getPlayer();
    if (harvestAmount > 0) {
      AbstractDungeon.actionManager.addToBottom(new GainBlockAction(player, player, harvestAmount * BLOCK_PER_STACK));
    }
    return harvestAmount;
  }
}