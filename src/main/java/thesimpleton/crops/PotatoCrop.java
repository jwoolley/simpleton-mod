package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Potatoes;
import thesimpleton.orbs.PotatoCropOrb;
import thesimpleton.powers.utils.Crop;
import thesimpleton.relics.HotPotato;

public class PotatoCrop extends AbstractCrop {
  public static final Crop CROP_ENUM = Crop.POTATOES;
  public static final CropInfo CROP_INFO;

  private static final String ORB_ID = PotatoCropOrb.ORB_ID;
  private static final AbstractCropPowerCard POWER_CARD = new Potatoes();
  private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.BASIC;
  private static AbstractCrop placeholderCrop;

  public static final int MATURITY_THRESHOLD = 5;

  public PotatoCrop() {
    super(CROP_ENUM, ORB_ID, POWER_CARD, RARITY, MATURITY_THRESHOLD);
    logger.debug("MAKIN' POTATOES (instantiating PotatoCrop). CROP_EMUM: " + CROP_ENUM);
  }

  protected int harvestAction(int harvestAmount) {
    logger.debug("PotatoCrop::harvestAction harvestAmount:" + harvestAmount);
    if (harvestAmount > 0) {
      if (SimpletonUtil.getPlayer().hasRelic(HotPotato.ID)) {
        SimpletonUtil.getPlayer().getRelic(HotPotato.ID).flash();
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(SimpletonUtil.FLAMING_SPUD, harvestAmount));
      } else {
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(SimpletonUtil.SPUD_MISSILE, harvestAmount));
      }
    }
    return harvestAmount;
  }

  static {
    CROP_INFO = new CropInfo(CROP_ENUM, ORB_ID, POWER_CARD, RARITY, MATURITY_THRESHOLD) {
      @Override
      public AbstractCrop getCrop() {
        if (placeholderCrop == null) {
          placeholderCrop = new PotatoCrop();
        }
        return placeholderCrop;
      }
    };
  }
}