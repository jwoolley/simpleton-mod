package thesimpleton.patches.combat;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.TheSimpletonMod;
import thesimpleton.orbs.AbstractCropOrb;

@SpirePatch(
    clz = ShuffleAction.class,
    method = "update"
)
public class OtherShufflePatchAfter {
  public static void Postfix (ShuffleAction __instance) {
    if (!AbstractDungeon.player.orbs.isEmpty() && AbstractCropOrb.playerHasAnyCropOrbs()) {
      TheSimpletonMod.handleOtherShuffleAfter();
    }
  }
}