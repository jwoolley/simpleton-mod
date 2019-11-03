package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.relics.HotPotato;

public class PotatoCrop extends AbstractCrop {
  public static final Crop CROP_ENUM = Crop.POTATOES;


  public PotatoCrop() {
    super(CROP_ENUM);
    logger.debug("MAKIN' POTATOES (instantiating PotatoCrop). CROP_EMUM: " + CROP_ENUM);
  }

  protected int harvestAction(int harvestAmount) {
    logger.debug("PotatoCrop::harvestAction harvestAmount:" + harvestAmount);
    if (harvestAmount > 0) {
      if (AbstractDungeon.player.hasRelic(HotPotato.ID)) {
        AbstractDungeon.player.getRelic(HotPotato.ID).flash();
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(getSpudCard(), harvestAmount));
      } else {
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(getSpudCard(), harvestAmount));
      }
    }
    return harvestAmount;
  }

  public static AbstractCard getSpudCard() {
    if (AbstractDungeon.player.hasRelic(HotPotato.ID)) {
      AbstractDungeon.player.getRelic(HotPotato.ID).flash();
      return SimpletonUtil.FLAMING_SPUD.makeCopy();
    } else {
      return SimpletonUtil.SPUD_MISSILE.makeCopy();
    }
  }
}