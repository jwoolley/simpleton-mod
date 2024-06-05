package thesimpleton.patches.actions;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.ending.SpireShield;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import thesimpleton.TheSimpletonMod;
import thesimpleton.characters.TheSimpletonCharacter;
import thesimpleton.utilities.ModLogger;

import java.util.Arrays;
import java.util.stream.Collectors;

@SpirePatch(
        clz = ApplyPowerAction.class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez = {
                AbstractCreature.class,
                AbstractCreature.class,
                AbstractPower.class,
                int.class,
                boolean.class,
                AbstractGameAction.AttackEffect.class
        }
)

public class ApplyPowerActionBeforeSkipFocusDebuff {
    private static ModLogger logger = TheSimpletonMod.traceLogger;

    public static void Postfix(ApplyPowerAction __instance, AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount,
                              boolean isFast, AbstractGameAction.AttackEffect effect) {

        try {

            if (TheSimpletonMod.isPlayingAsSimpleton()
                    && target != null
                    && target.isPlayer && target instanceof TheSimpletonCharacter
                    && source instanceof SpireShield
                    && powerToApply != null && FocusPower.POWER_ID.equals(powerToApply.ID)) {
                __instance.target = null;
            }
        } catch (Exception e) {
            TheSimpletonMod.errorLogger.warn("Exception in ApplyPowerActionBeforeSkipFocusDebuff.Postfix: + "
                    + e.getMessage());
            TheSimpletonMod.errorLogger.warn(Arrays.stream(
                    e.getStackTrace()).map(s -> s.toString()).collect(Collectors.joining("\n\t")));
        }
    }
}
