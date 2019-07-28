package thesimpleton.patches.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.map.DungeonMap;
import thesimpleton.TheSimpletonMod;

@SpirePatch(
    clz = DungeonMap.class,
    method = "render",
    paramtypez = {
        SpriteBatch.class
    }
)

public class DungeonMapRenderAfter {
  public static void Postfix (DungeonMap __instance, SpriteBatch __spriteBatch) {
//     if (!TheSimpletonMod.isPlayingAsSimpleton()) {

//    if (TheSimpletonMod.seasonScreen.isOpen()) {
//      TheSimpletonMod.seasonScreen.render(__spriteBatch);
//
//    }
//   }
  }
}