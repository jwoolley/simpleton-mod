package thesimpleton.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.TheSimpletonMod;

  @SpirePatch(
      clz = AbstractDungeon.class,
      method = "resetPlayer"
  )
  public class AbstractDungeonResetPlayerPatchAfter {
    public static void Postfix () {
      TheSimpletonMod.logger.debug("AbstractDungeonResetPlayerPatchAfter called");
      TheSimpletonMod.theSimpletonCharacter.getCropUtil().resetForCombatEnd();
    }
  }