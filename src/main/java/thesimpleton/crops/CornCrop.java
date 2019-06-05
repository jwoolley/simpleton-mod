package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Corn;
import thesimpleton.orbs.CornCropOrb;
import thesimpleton.powers.utils.Crop;

public class CornCrop extends AbstractCrop {
  public static final Crop CROP_ENUM = Crop.CORN;
  public static final CropInfo CROP_INFO;

  private static final String ORB_ID = CornCropOrb.ORB_ID;
  private static final AbstractCropPowerCard POWER_CARD = new Corn();
  private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.COMMON;
  private static AbstractCrop placeholderCrop;

  public static final int MATURITY_THRESHOLD = 2;

  public CornCrop() {
    super(CROP_ENUM, ORB_ID, POWER_CARD, RARITY, MATURITY_THRESHOLD);
    logger.debug("MAKIN' CORN (instantiating Corn).");
  }

  protected int harvestAction(int harvestAmount) {
    if (harvestAmount > 0) {
      AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, harvestAmount));
    }
    return harvestAmount;
  }

  static {
    CROP_INFO = new CropInfo(CROP_ENUM, ORB_ID, POWER_CARD, RARITY, MATURITY_THRESHOLD) {
      @Override
      public AbstractCrop getCrop() {
        if (placeholderCrop == null) {
          placeholderCrop = new CornCrop();
        }
        return placeholderCrop;
      }
    };
  }
}