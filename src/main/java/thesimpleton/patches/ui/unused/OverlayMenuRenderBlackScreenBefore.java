package thesimpleton.patches.ui.unused;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.OverlayMenu;

@SpirePatch(
    clz = OverlayMenu.class,
    method = "render",
    paramtypez = {
        SpriteBatch.class,
    }
)

public class OverlayMenuRenderBlackScreenBefore {
//  public static void Prefix (OverlayMenu __instance, SpriteBatch __spriteBatch) {
//     if (TheSimpletonMod.isPlayingAsSimpleton()) {

//    if (TheSimpletonMod.seasonScreen.isOpen()) {
//      TheSimpletonMod.traceLogger.trace("OverlayMenuRenderBlackScreenBefore:: Hiding black screen");
//      __instance.hideBlackScreen();
//    }
// }
//  }
}