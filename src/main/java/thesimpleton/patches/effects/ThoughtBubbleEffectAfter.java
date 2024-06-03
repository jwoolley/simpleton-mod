package thesimpleton.patches.effects;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.interfaces.IHasCustomCantPlayEffect;
import thesimpleton.effects.utils.CustomCantPlayEffectManager;
import thesimpleton.utilities.ModLogger;

@SpirePatch(
        clz = ThoughtBubble.class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez = {
                float.class,
                float.class,
                float.class,
                String.class,
                boolean.class
        }
)
public class ThoughtBubbleEffectAfter {
    private static ModLogger logger = TheSimpletonMod.traceLogger;
    public static void Postfix(ThoughtBubble __instance, float x, float y, float duration, String msg, boolean isPlayer) {
        try {
            if (CustomCantPlayEffectManager.hasEffect()) {
                logger.log("[ThoughtBubbleEffectAfter] performing custom can't  play effect");
                IHasCustomCantPlayEffect effect = CustomCantPlayEffectManager.getEffect();
                CustomCantPlayEffectManager.clearEffect();
                effect.performCantPlayEffect();
            } else {
                logger.log("[ThoughtBubbleEffectAfter] Not performing custom can't play effect: no effect set");
            }
        } catch (Exception e) {
            TheSimpletonMod.errorLogger.log("[ThoughtBubbleEffectAfter]\n" + e.getMessage());
        }
    }
}
