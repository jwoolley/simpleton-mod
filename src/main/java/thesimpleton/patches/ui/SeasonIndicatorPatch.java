package thesimpleton.patches.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.ui.buttons.CancelButton;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thesimpleton.TheSimpletonMod;
import thesimpleton.ui.seasons.SeasonIndicator;

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
      if (TheSimpletonMod.isPlayingAsSimpleton()) {
        if (SeasonIndicator.shouldRender()) {
          TheSimpletonMod.seasonIndicator.update();
        }
      }
    }
  }

  // for rendering in combat
  @SpirePatch(clz = EnergyPanel.class, method = "renderOrb")
  public static class SeasonIndicatorRenderPatch {
    @SpirePostfixPatch
    public static void Postfix(EnergyPanel __instance, SpriteBatch sb) {
      if (TheSimpletonMod.isPlayingAsSimpleton()) {
        if (SeasonIndicator.shouldRender()) {
          TheSimpletonMod.seasonIndicator.render(sb);
        }
      }
    }
  }

  // for rendering over dungeon map
  @SpirePatch(clz = CancelButton.class, method = "render")
  public static class SeasonIndicatorRenderMapPatch {
    @SpirePostfixPatch
    public static void Postfix(CancelButton __instance, SpriteBatch sb) {
      if (TheSimpletonMod.isPlayingAsSimpleton()) {
        if (SeasonIndicator.shouldRender()) {
          TheSimpletonMod.seasonIndicator.render(sb);
        }
      }
    }
  }
}