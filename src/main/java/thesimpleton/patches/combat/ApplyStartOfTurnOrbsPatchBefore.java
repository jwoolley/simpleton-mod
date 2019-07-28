package thesimpleton.patches.combat;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import thesimpleton.TheSimpletonMod;

@SpirePatch(
    clz = AbstractPlayer.class,
    method = "applyStartOfTurnOrbs"
)

public class ApplyStartOfTurnOrbsPatchBefore {
  public static void Prefix (AbstractPlayer __instance) {
    if (!TheSimpletonMod.isPlayingAsSimpleton()) {
      TheSimpletonMod.onBeforeStartOfTurnOrbs();
    }
  }
}