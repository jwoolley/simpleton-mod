package thesimpleton.patches.debug;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.beyond.Nemesis;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import thesimpleton.TheSimpletonMod;

@SpirePatch(
    clz = IntangiblePower.class,
    method = "atEndOfTurn",
    paramtypez = {
        boolean.class
    }
)

public class IntangiblePowerPatch {
//  public static void Prefix (IntangiblePower __instance, boolean isPlayer) {
//        if (TheSimpletonMod.isPlayingAsSimpleton()) {
//    TheSimpletonMod.logger.info("IntangiblePowerPatch::IntangiblePower::atEndOfTurn before patch called. ");
//      }
//  }
}