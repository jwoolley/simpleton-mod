package thesimpleton.patches.combat;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import thesimpleton.TheSimpletonMod;

@SpirePatch(
    clz = ShuffleAction.class,
    method = "update"
)
public class OtherShufflePatchAfter {
  public static void Postfix (ShuffleAction __instance) {
    if (!TheSimpletonMod.isPlayingAsSimpleton()) {
      TheSimpletonMod.handleOtherShuffleAfter();
    }
  }
}