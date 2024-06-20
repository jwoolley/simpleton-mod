package thesimpleton.patches.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import thesimpleton.TheSimpletonMod;
import thesimpleton.utilities.ModLogger;

@SpirePatch(
        clz = LargeDialogOptionButton.class,
        method = "renderCardPreview",
        paramtypez = {
                SpriteBatch.class,
        }
)
public class DialogButtonShowPreviewCardAfter {
    public static void Postfix (LargeDialogOptionButton __instance, SpriteBatch __spriteBatch, float ___x, float ___y) {
        try {
            AbstractCard secondPreviewCard = DialogButtonSecondPreviewCardField.secondPreviewCard.get(__instance);
            if(secondPreviewCard != null) {
                if (secondPreviewCard != null && __instance.hb.hovered) {
                    secondPreviewCard.current_x = ___x + __instance.hb.width / 1.75F;
                    if (___y < secondPreviewCard.hb.height / 2.0F + 5.0F)
                        ___y = secondPreviewCard.hb.height / 2.0F + 5.0F;
                    secondPreviewCard.current_y = ___y + AbstractCard.IMG_HEIGHT_S + PREVIEW_CARD_OFFSET_Y * Settings.yScale;
                    secondPreviewCard.render(__spriteBatch);
                }

            }
        } catch (Exception e) {
            TheSimpletonMod.errorLogger.log("Exception adding second preview card: " + e.getMessage());
        }
    }

    private static float PREVIEW_CARD_OFFSET_Y = 10.0f;
}
