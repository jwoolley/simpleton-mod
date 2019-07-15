package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.cards.SimpletonUtil;

public class SquashCrop extends AbstractCrop {
  public static final Crop CROP_ENUM = Crop.SQUASH;
  public static final int BLOCK_PER_STACK = 6;

  public SquashCrop() {
    super(CROP_ENUM);
  }

  protected int harvestAction(int harvestAmount) {
    AbstractPlayer player = AbstractDungeon.player;
    if (harvestAmount > 0) {
      for(int i = 0; i < harvestAmount; i++) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(player, player, BLOCK_PER_STACK, true));
      }
    }
    return harvestAmount;
  }
}