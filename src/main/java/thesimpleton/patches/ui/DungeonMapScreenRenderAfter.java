package thesimpleton.patches.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import thesimpleton.TheSimpletonMod;

@SpirePatch(
    clz = DungeonMapScreen.class,
    method = "render",
    paramtypez = {
        SpriteBatch.class,
    }
)
public class DungeonMapScreenRenderAfter {
  public static void Postfix (DungeonMapScreen __instance, SpriteBatch __spriteBatch) {
//    TheSimpletonMod.logger.debug("DungeonMapScreenRenderAfter invoked post trigger");
    if (TheSimpletonMod.seasonScreen.isOpen()) {
//      TheSimpletonMod.logger.debug("DungeonMapScreenRenderAfter season screen open; rendering");
      __spriteBatch.setColor(Color.WHITE);
      TheSimpletonMod.seasonScreen.render(__spriteBatch);
    }
  }
}