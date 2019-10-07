package thesimpleton.patches.player;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import thesimpleton.orbs.UnevokableOrb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EvokeOrbPatch {
  @SpirePatch(
      clz = AbstractPlayer.class,
      method = "evokeOrb"
  )

  public static class EvokeOrbPatchBefore {

    public static SpireReturn Prefix(AbstractPlayer __instance) {

      ArrayList<AbstractOrb> orbs = __instance.orbs;

      if (orbs.stream().anyMatch(o -> o instanceof UnevokableOrb)) {
        if ((!(orbs.get(0) instanceof EmptyOrbSlot))) {

          List<Integer> evokableOrbIndxes = getEvokableOrbsIndexes();

          if (!evokableOrbIndxes.isEmpty()) {
            (orbs.get(evokableOrbIndxes.get(0))).onEvoke();

            AbstractOrb orbSlot = new EmptyOrbSlot();

            for (int i = 1; i < evokableOrbIndxes.size(); i++) {
              Collections.swap(orbs, evokableOrbIndxes.get(i), evokableOrbIndxes.get(i - 1));
            }
            orbs.set(evokableOrbIndxes.get(evokableOrbIndxes.size() - 1), orbSlot);

            for (int i = 0; i < evokableOrbIndxes.size(); i++) {
              (orbs.get(evokableOrbIndxes.get(i))).setSlot(evokableOrbIndxes.get(i), __instance.maxOrbs);
            }
            return SpireReturn.Return(null);
          }
        }
      }
      return SpireReturn.Continue();
    }
  }

  @SpirePatch(
      clz = AbstractPlayer.class,
      method = "evokeNewestOrb"
  )

  public static class EvokeNewestOrbPatchBefore {
    public static SpireReturn Prefix(AbstractPlayer __instance) {
      ArrayList<AbstractOrb> orbs = __instance.orbs;

      if(orbs.stream().anyMatch(o -> o instanceof UnevokableOrb)) {

        List<Integer> evokableOrbIndxes = getEvokableOrbsIndexes();

        int firstEvokableIndex = evokableOrbIndxes.get(0);

        if (orbs.get(firstEvokableIndex) instanceof EmptyOrbSlot) {
          AbstractOrb orbSlot = new EmptyOrbSlot((orbs.get(firstEvokableIndex)).cX, (orbs.get(firstEvokableIndex)).cY);
          for (int i = 1; i < evokableOrbIndxes.size(); i++) {
            Collections.swap(orbs, evokableOrbIndxes.get(i), evokableOrbIndxes.get(i - 1));
          }
          orbs.set(evokableOrbIndxes.get(evokableOrbIndxes.size() - 1), orbSlot);
          for (int i = 0; i < evokableOrbIndxes.size(); i++) {
            (orbs.get(evokableOrbIndxes.get(i))).setSlot(i, evokableOrbIndxes.get(evokableOrbIndxes.size() - 1));
          }
          return SpireReturn.Return(null);
        }
      }
      return SpireReturn.Continue();
    }
  }

  static List<Integer> getEvokableOrbsIndexes() {
    List<AbstractOrb> orbs = AbstractDungeon.player.orbs;
    ArrayList<Integer> evokableOrbIndxes = new ArrayList<>();

    for (int i = 0; i < orbs.size(); i++) {
      if (!(orbs.get(i) instanceof UnevokableOrb)) {
        evokableOrbIndxes.add(i);
      }
    }

    return evokableOrbIndxes;
  }
}