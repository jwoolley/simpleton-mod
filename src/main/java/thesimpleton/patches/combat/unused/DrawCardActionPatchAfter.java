package thesimpleton.patches.combat.unused;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.powers.NumbPower;

//@SpirePatch(
//    clz = DrawCardAction.class,
//    method = SpirePatch.CONSTRUCTOR,
//    paramtypez = {
//        AbstractCreature.class,
//        int.class,
//        boolean.class
//    }
//)

public class DrawCardActionPatchAfter {

//  @SpirePostfixPatch
//  public static void Postfix (DrawCardAction __instance, AbstractCreature source, int amount, boolean endTurnDraw) {
//      final ModLogger logger = TheSimpletonMod.traceLogger;
//      logger.trace("DrawCardActionPatchAfter Postfix trigger called");
//
//      if (AbstractDungeon.player.hasPower(NumbPower.POWER_ID)) {
//        final NumbPower numbPower =  (NumbPower)AbstractDungeon.player.getPower(NumbPower.POWER_ID);
//
//        logger.trace("DrawCardActionPatchAfter player has " + numbPower.amount + " numb stacks");
//        logger.trace("DrawCardActionPatchAfter decreasing draw amount from"
//            + amount + " to " + numbPower.getUpdatedDrawAmount(amount));
//
//        __instance.amount = numbPower.getUpdatedDrawAmount(amount);
//      }
//  }
}
