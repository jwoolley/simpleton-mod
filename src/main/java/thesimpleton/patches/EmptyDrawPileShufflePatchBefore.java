package thesimpleton.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import thesimpleton.TheSimpletonMod;

@SpirePatch(
    clz = EmptyDeckShuffleAction.class,
    method = SpirePatch.CONSTRUCTOR
)
public class EmptyDrawPileShufflePatchBefore {
  public static void Prefix (EmptyDeckShuffleAction __instance) {
    TheSimpletonMod.handleEmptyDrawShuffleBefore();
  }
}