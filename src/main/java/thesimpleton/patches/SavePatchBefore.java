package thesimpleton.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import thesimpleton.TheSimpletonMod;

@SpirePatch(
  clz = SaveAndContinue.class,
  method = "save"
)
public class SavePatchBefore {
  public static void Prefix (SaveFile save) {
    TheSimpletonMod.handleSaveBefore();
  }
}