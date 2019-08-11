package thesimpleton.patches.orbs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.vfx.BobEffect;
import thesimpleton.TheSimpletonMod;
import thesimpleton.orbs.CustomEmptyOrbSlot;

@SpirePatch(
    clz = EmptyOrbSlot.class,
    method = "render",
    paramtypez = {
        SpriteBatch.class
    }
)

public class EmptyOrbSlotRender {
  public static SpireReturn Prefix (EmptyOrbSlot __instance, SpriteBatch __spriteBatch) {
    if (__instance instanceof CustomEmptyOrbSlot) {
       __spriteBatch.setColor(Color.WHITE.cpy());
       CustomEmptyOrbSlot orbSlot = (CustomEmptyOrbSlot) __instance;
        BobEffect bobEffect = new BobEffect(3.0F * Settings.scale, 3.0F);
           TheSimpletonMod.seasonScreen.render(__spriteBatch);

         __spriteBatch.draw(orbSlot.getBackgroundImage(), __instance.cX - 48.0F -bobEffect.y / 8.0F, __instance.cY - 48.0F + bobEffect.y / 8.0F, 48.0F, 48.0F, 96.0F, 96.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 96, 96, false, false);
         __spriteBatch.draw(orbSlot.getForegroundImage(), __instance.cX - 48.0F + bobEffect.y / 8.0F, __instance.cY - 48.0F - bobEffect.y / 8.0F, 48.0F, 48.0F, 96.0F, 96.0F, Settings.scale, Settings.scale, 0, 0, 0, 96, 96, false, false);
         __instance.hb.render(__spriteBatch);
         
        return SpireReturn.Return(null);
     }
     return SpireReturn.Continue();
  }
}