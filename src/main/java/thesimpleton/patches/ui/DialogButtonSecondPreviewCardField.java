package thesimpleton.patches.ui;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;

@SpirePatch(
        clz= LargeDialogOptionButton.class,
        method=SpirePatch.CLASS
)

public class DialogButtonSecondPreviewCardField {
    public static SpireField<AbstractCard> secondPreviewCard = new SpireField<>(() -> null);
}
