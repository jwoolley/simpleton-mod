package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Potatoes;
import thesimpleton.characters.TheSimpletonCharacter;
import thesimpleton.orbs.PotatoCropOrb;
import thesimpleton.relics.HotPotato;

public class PotatoCrop extends AbstractCrop {
  public static final Crop CROP_ENUM = Crop.POTATOES;


  public PotatoCrop() {
    super(CROP_ENUM);
    logger.info("MAKIN' POTATOES (instantiating PotatoCrop). CROP_EMUM: " + CROP_ENUM);
  }

  protected int harvestAction(int harvestAmount) {
    logger.info("PotatoCrop::harvestAction harvestAmount:" + harvestAmount);
    if (harvestAmount > 0) {
      if (((TheSimpletonCharacter)AbstractDungeon.player).hasRelic(HotPotato.ID)) {
        ((TheSimpletonCharacter)AbstractDungeon.player).getRelic(HotPotato.ID).flash();
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(SimpletonUtil.FLAMING_SPUD, harvestAmount));
      } else {
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(SimpletonUtil.SPUD_MISSILE, harvestAmount));
      }
    }
    return harvestAmount;
  }
}