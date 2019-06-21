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
    if (TheSimpletonMod.seasonScreen.isOpen()) {
      __spriteBatch.setColor(Color.WHITE);
      TheSimpletonMod.seasonScreen.render(__spriteBatch);
    }
  }
}