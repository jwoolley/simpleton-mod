package thesimpleton.patches.combat;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import thesimpleton.TheSimpletonMod;

//TODO: second patch for overloaded constructor (w/ no boolean param?)
@SpirePatch(
    clz = ShuffleAction.class,
    method = SpirePatch.CONSTRUCTOR,
    paramtypez = {
        CardGroup.class,
        boolean.class
    }
)
public class OtherShufflePatchBefore {
  public static void Prefix (ShuffleAction __instance) {
    if (TheSimpletonMod.isPlayingAsSimpleton()) {
      TheSimpletonMod.handleOtherShuffleBefore();
    }
  }
}