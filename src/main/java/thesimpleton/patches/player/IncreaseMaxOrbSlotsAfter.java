package thesimpleton.patches.player;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
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
      List<AbstractOrb> newOrbs = new ArrayList<>();

      for (AbstractOrb orbSlot : __instance.orbs) {
        newOrbs.add(new SimpletonEmptyOrbSlot(orbSlot.cX, orbSlot.cY));
      }

      __instance.orbs.clear();
      __instance.orbs.addAll(newOrbs);

      for (int i = 0; i < __instance.orbs.size(); i++) {
        (__instance.orbs.get(i)).setSlot(i, __instance.maxOrbs);
      }
    }
  }
}
