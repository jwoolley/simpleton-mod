package thesimpleton.patches.combat;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.orbs.AbstractCropOrb;

//@SpirePatch(
//    clz = ShuffleAction.class,
//    method = "update"
//)
//public class ShuffleActionOnShuffleBefore {
//  public static void Prefix (ShuffleAction __instance) {
//
//    if (!AbstractDungeon.player.orbs.isEmpty() && AbstractCropOrb.playerHasAnyCropOrbs()) {
//      AbstractCropOrb.getActiveCropOrbs().forEach(AbstractCropOrb::onShuffle);
//    }
//
//  }
//}