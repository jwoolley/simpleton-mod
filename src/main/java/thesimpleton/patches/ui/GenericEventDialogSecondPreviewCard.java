package thesimpleton.patches.ui;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;

public class GenericEventDialogSecondPreviewCard extends GenericEventDialog {
    public static void setSecondPreviewCardOnLastOption(GenericEventDialog dialog, AbstractCard secondPreviewCard) {
        if (dialog.optionList.size() > 0) {
            LargeDialogOptionButton option = dialog.optionList.get(dialog.optionList.size() - 1);
            if (option != null) {
                DialogButtonSecondPreviewCardField.secondPreviewCard.set(option, secondPreviewCard);
            }
        }
    }
}