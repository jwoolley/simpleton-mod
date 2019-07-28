package thesimpleton.patches.combat;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import thesimpleton.TheSimpletonMod;

@SpirePatch(
    clz = EmptyDeckShuffleAction.class,
    method = "update"
)
public class EmptyDrawPileShufflePatchAfter {
  public static void Postfix (EmptyDeckShuffleAction __instance) {
    if (!TheSimpletonMod.isPlayingAsSimpleton()) {
      TheSimpletonMod.handleEmptyDrawShuffleAfter();
    }
  }
}