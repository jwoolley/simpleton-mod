package thesimpleton.patches.ui.unused;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.helpers.MathHelper;
import thesimpleton.TheSimpletonMod;

@SpirePatch(
    clz = MathHelper.class,
    method = "fadeLerpSnap",
    paramtypez = {
        float.class,
        float.class
    }
)

public class MathHelperFadeLerpSnapBefore {
//  public static void Prefix (float startX, float targetX) {
    //     if (TheSimpletonMod.isPlayingAsSimpleton()) {
//    if (TheSimpletonMod.seasonScreen.isOpen()) {
//      TheSimpletonMod.logger.debug("MathHelperFadeLerpSnapReplace season screen is open. skipping Lerp.");
//      SpireReturn.Return(targetX);
//    }
//   }
// }
}