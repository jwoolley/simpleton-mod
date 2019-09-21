package thesimpleton.patches.monsters;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.monsters.exordium.LouseNormal;
import thesimpleton.TheSimpletonMod;

@SpirePatch(clz = Byrd.class, method = SpirePatch.CONSTRUCTOR)
public class ByrdPatch
{

  private static final String FLYING_ATLAS_PATH = TheSimpletonMod.getResourcePath("monsters/crow/flying.atlas");
  private static final String FLYING_JSON_PATH = TheSimpletonMod.getResourcePath("monsters/crow/flying.json");
  private static final String IDLE_ANIMATION_KEY = "idle_flap";

  // TODO: use private field access from MTS instead
  // https://cdn.discordapp.com/attachments/398373038732738570/625057908563902464/unknown.png
  private static final int WIDTH = 240;
  private static final int HEIGHT = 180;


  public static void Postfix(Byrd __instance, float x, float y) {
    TheSimpletonMod.logger.info("ByrdPatch: Postfix called");
    TheSimpletonMod.customizeAnimation(__instance, FLYING_ATLAS_PATH, FLYING_JSON_PATH, WIDTH, HEIGHT,
        IDLE_ANIMATION_KEY);  }
}