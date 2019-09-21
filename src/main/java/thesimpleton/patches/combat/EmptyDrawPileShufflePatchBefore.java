package thesimpleton.patches.combat;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.TheSimpletonMod;
import thesimpleton.orbs.AbstractCropOrb;

@SpirePatch(
    clz = EmptyDeckShuffleAction.class,
    method = SpirePatch.CONSTRUCTOR
)
public class EmptyDrawPileShufflePatchBefore {
  public static void Prefix (EmptyDeckShuffleAction __instance) {
    if (!AbstractDungeon.player.orbs.isEmpty() && AbstractCropOrb.playerHasAnyCropOrbs()) {
      TheSimpletonMod.handleOtherShuffleBefore();
      AbstractCropOrb.getActiveCropOrbs().forEach(AbstractCropOrb::onShuffle);
    }
  }
}