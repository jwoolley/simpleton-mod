package thesimpleton.patches.cards;

import basemod.abstracts.CustomPlayer;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.interfaces.IHasCustomGlowCondition;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.devtools.debugging.DebugLoggers;
import thesimpleton.utilities.ModLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpirePatch(
        clz = CardGroup.class,
        method = "glowCheck"
)

public class CardGroupGlowCheckPatchBefore {
    private static ModLogger logger = TheSimpletonMod.traceLogger;
    @SpirePostfixPatch
    public static void Postfix(CardGroup __instance) {
        try {
            for(AbstractCard card : __instance.group) {
                if (card instanceof IHasCustomGlowCondition
                        && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.HAND_SELECT
                        && ((IHasCustomGlowCondition) card).shouldGlow()
                        && !card.isGlowing) {
                    card.beginGlowing();
                    card.triggerOnGlowCheck();
                }
            }
        } catch (Exception e) {
            TheSimpletonMod.errorLogger.log(e);
        }
    }
}