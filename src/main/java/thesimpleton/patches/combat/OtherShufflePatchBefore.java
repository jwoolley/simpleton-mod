package thesimpleton.patches.combat;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.TheSimpletonMod;
import thesimpleton.orbs.AbstractCropOrb;

//TODO: second patch for overloaded constructor (w/ no boolean param?)
@SpirePatch(
    clz = ShuffleAction.class,
    method = SpirePatch.CONSTRUCTOR,
    paramtypez = {
        CardGroup.class,
        boolean.class
    }
)
public class OtherShufflePatchBefore {
  public static void Prefix (ShuffleAction __instance, CardGroup cards, boolean trigger) {
    if (trigger && !AbstractDungeon.player.orbs.isEmpty() && AbstractCropOrb.playerHasAnyCropOrbs()) {
      TheSimpletonMod.handleOtherShuffleBefore();
      AbstractCropOrb.getActiveCropOrbs().forEach(AbstractCropOrb::onShuffle);
    }
  }
}