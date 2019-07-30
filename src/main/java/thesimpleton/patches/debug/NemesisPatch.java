package thesimpleton.patches.debug;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.beyond.Nemesis;
import thesimpleton.TheSimpletonMod;

@SpirePatch(
    clz = Nemesis.class,
    method = "takeTurn"
)

public class NemesisPatch {
//  public static void Postfix (Nemesis __instance) {
//     if (TheSimpletonMod.isPlayingAsSimpleton()) {
//    TheSimpletonMod.logger.info("NemesisPatch::Nemesis::takeTurn after patch called");
// }
//  }
}
