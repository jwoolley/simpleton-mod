package thesimpleton.patches.player;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import thesimpleton.TheSimpletonMod;
import thesimpleton.orbs.SimpletonEmptyOrbSlot;

import java.util.ArrayList;
import java.util.List;

@SpirePatch(
    clz = AbstractPlayer.class,
    method = "increaseMaxOrbSlots",
    paramtypez = {
        int.class,
        boolean.class
    }
)



public class IncreaseMaxOrbSlotsAfter {
  public static void Postfix(AbstractPlayer __instance, int __amount, boolean __playSfx) {
    if (TheSimpletonMod.isPlayingAsSimpleton()) {

      for (int i = 0; i < __instance.orbs.size(); i++) {
        AbstractOrb orbSlot = __instance.orbs.get(i);

        if (orbSlot instanceof EmptyOrbSlot && !(orbSlot instanceof SimpletonEmptyOrbSlot)) {
          __instance.orbs.set(i, new SimpletonEmptyOrbSlot(0.0F, 0.0F));
        }
      }

      for (int i = 0; i < __instance.orbs.size(); i++) {
        (__instance.orbs.get(i)).setSlot(i, __instance.maxOrbs);
      }
    }
  }
}
