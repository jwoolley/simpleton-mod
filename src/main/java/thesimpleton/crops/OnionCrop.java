package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Onions;
import thesimpleton.orbs.OnionCropOrb;

public class OnionCrop extends AbstractCrop {
  public static final Crop CROP_ENUM = Crop.ONIONS;
  public static final int WEAK_PER_STACK = 1;

  public OnionCrop() {
    super(CROP_ENUM);
  }

  protected int harvestAction(int harvestAmount) {
    AbstractPlayer player = AbstractDungeon.player;
    if (harvestAmount > 0) {
      for (int i = 0; i < harvestAmount; i++) {
        AbstractMonster m = SimpletonUtil.getRandomMonster();
        if (m != null) {
          AbstractDungeon.actionManager.addToBottom(
              new ApplyPowerAction(m, player, new WeakPower(m, WEAK_PER_STACK, false), WEAK_PER_STACK));
        }
      }
    }
    return harvestAmount;
  }
}