package thesimpleton.patches.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thesimpleton.TheSimpletonMod;
import thesimpleton.enums.TheSimpletonCharEnum;

public class SeasonIndicatorPatch {
//  @SpirePatch(clz = AbstractPlayer.class, method = "combatUpdate")
//  public static class SeasonIndicatorUpdatePatch {
//    @SpirePostfixPatch
//    public static void Postfix(AbstractPlayer __instance) {
//      if (shouldRender()) {
//        PokerPlayerMod.pokerScoreViewer.dragUpdate();
//      }
//    }
//  }

  @SpirePatch(clz = EnergyPanel.class, method = "update")
  public static class SeasonIndicatorUpdatePatch {
    @SpirePrefixPatch
    public static void Prefix(EnergyPanel __instance) {
      if (shouldRender()) {
        TheSimpletonMod.seasonIndicator.update();
      }
    }
  }

  @SpirePatch(clz = EnergyPanel.class, method = "renderOrb")
  public static class SeasonIndicatorRenderPatch {
    @SpirePostfixPatch
    public static void Postfix(EnergyPanel __instance, SpriteBatch sb) {
      if (shouldRender()) {
        TheSimpletonMod.seasonIndicator.render(sb);
      }
    }
  }

  private static boolean shouldRender() {
    return AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT &&
        AbstractDungeon.player.chosenClass == TheSimpletonCharEnum.THE_SIMPLETON;
  }
}