package thesimpleton.patches.ui.unused;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
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
public class MathHelperFadeLerpSnapReplace {
//  public static void Postfix (float startX, float targetX) {
    //     if (TheSimpletonMod.isPlayingAsSimpleton()) {

//    if (TheSimpletonMod.seasonScreen.isOpen()) {
//       TheSimpletonMod.traceLogger.trace("MathHelperFadeLerpSnapReplace season screen is open. skipping Lerp.");
//    }
//
//    if (startX != targetX) {
//      startX = MathUtils.lerp(startX, targetX, Gdx.graphics.getDeltaTime() * 12.0F);
//      if (Math.abs(startX - targetX) < 0.01F) {
//        startX = targetX;
//      }
//    }
////    return startX;
//
////    if ( && startX != targetX) {
////      TheSimpletonMod.traceLogger.trace("MathHelperFadeLerpSnapBefore triggered for map. returning");
////      return;
////    }
    // }
//  }
}