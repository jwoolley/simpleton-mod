package thesimpleton.patches.input;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import thesimpleton.TheSimpletonMod;
import thesimpleton.effects.utils.CustomCantPlayEffectManager;
import thesimpleton.utilities.ModLogger;

@SpirePatch(
        clz = InputHelper.class,
        method = "getCardSelectedByHotkey"
)

public class InputHelperGetCardSelectedFromHotKeyAfter {
    private static ModLogger logger = TheSimpletonMod.traceLogger;

    public static void Postfix(CardGroup cards) {
        if (CustomCantPlayEffectManager.hasEffect()) {
            CustomCantPlayEffectManager.clearEffect();
        }
    }
}
