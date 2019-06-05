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
  public static final CropInfo CROP_INFO;

  private static final String ORB_ID = SquashCropOrb.ORB_ID;
  private static final AbstractCropPowerCard POWER_CARD = new Squash();
  private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.BASIC;
  private static AbstractCrop placeholderCrop;

  public static final int MATURITY_THRESHOLD = 5;
  public static final int BLOCK_PER_STACK = 6;

  public SquashCrop() {
    super(CROP_ENUM, ORB_ID, POWER_CARD, RARITY, MATURITY_THRESHOLD);
  }

  protected int harvestAction(int harvestAmount) {
    AbstractPlayer player = SimpletonUtil.getPlayer();
    if (harvestAmount > 0) {
      for(int i = 0; i < harvestAmount; i++) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(player, player, BLOCK_PER_STACK, true));
      }
    }
    return harvestAmount;
  }

  static {
    CROP_INFO = new CropInfo(CROP_ENUM, ORB_ID, POWER_CARD, RARITY, MATURITY_THRESHOLD) {
      @Override
      public AbstractCrop getCrop() {
        if (placeholderCrop == null) {
          placeholderCrop = new SquashCrop();
        }
        return placeholderCrop;
      }
    };
  }
}