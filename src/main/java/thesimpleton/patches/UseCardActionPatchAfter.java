package thesimpleton.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import thesimpleton.TheSimpletonMod;

@SpirePatch(
    clz = UseCardAction.class,
    method = SpirePatch.CONSTRUCTOR,
    paramtypez = {
        AbstractCard.class,
        AbstractCreature.class
    }
)

public class UseCardActionPatchAfter {
  public static void Postfix (UseCardAction __instance, AbstractCard card, AbstractCreature target) {
    TheSimpletonMod.handleUseCard(card, __instance);
  }
}