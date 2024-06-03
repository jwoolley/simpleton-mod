package thesimpleton.patches.cards;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.interfaces.IHasCustomCantPlayEffect;
import thesimpleton.effects.utils.CustomCantPlayEffectManager;
import thesimpleton.utilities.ModLogger;

import java.util.Arrays;
import java.util.stream.Collectors;

@SpirePatch(
    clz = AbstractCard.class,
    method = "canPlay",
    paramtypez = {
            AbstractCard.class
    }
)

public class AbstractCardCanPlayAfter {
    private static ModLogger logger = TheSimpletonMod.traceLogger;

    public static boolean Postfix(boolean __result, AbstractCard __instance, AbstractCard potentiallyPlayableCard) {
        boolean canPlay = __result;

        if (!CustomCantPlayEffectManager.hasEffect() && __instance instanceof  IHasCustomCantPlayEffect) {
            try {
                canPlay = ((IHasCustomCantPlayEffect)__instance).canPlayCheck(potentiallyPlayableCard);
                if (!canPlay && __instance instanceof IHasCustomCantPlayEffect) {
                    // unset after every input update via InputHelperGetCardSelectedFromHotKeyAfter.Postfix
                    CustomCantPlayEffectManager.setCantPlayEffect(((IHasCustomCantPlayEffect) __instance));
                }
            } catch (Exception e) {
                TheSimpletonMod.errorLogger.log("[AbstractCardCanPlayAfter]\n" + e.getMessage()
                        + " at " + Arrays.stream(Thread.currentThread().getStackTrace()).map(s -> s.toString()).collect(Collectors.joining("\n\t")));
            }
        }

        return canPlay;
    }
}