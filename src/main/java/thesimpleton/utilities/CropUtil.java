package thesimpleton.utilities;

import basemod.interfaces.PostDrawSubscriber;
import basemod.interfaces.PostPowerApplySubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.StartGameSubscriber;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.characters.TheSimpletonCharacter;
import thesimpleton.orbs.AbstractCropOrb;

public class CropUtil {
  private static Logger logger = TheSimpletonMod.logger;

  public CropUtil() {

  }

  public boolean playerHasAnyCrops() {
    return AbstractCropOrb.getNumberActiveCropOrbs() > 0;
  }

}
