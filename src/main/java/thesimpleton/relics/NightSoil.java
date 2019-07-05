package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.AbstractCrop;
import thesimpleton.effects.relic.DelayedRelicFlashEffect;
import thesimpleton.orbs.AbstractCropOrb;

import java.util.List;
import java.util.stream.Collectors;

public class NightSoil extends CustomRelic {
  public static final String ID = "TheSimpletonMod:NightSoil";
  public static final String IMG_PATH = "relics/nightsoil.png";
  public static final String IMG_PATH_LARGE = "relics/nightsoillarge.png";
  public static final String OUTLINE_IMG_PATH = "relics/nightsoil_outline.png";

  private static final RelicTier TIER = RelicTier.STARTER;
  private static final LandingSound SOUND = LandingSound.HEAVY;

  private static final int CROP_AMOUNT = 1;
  private static final float RELIC_FLASH_DELAY = 0.8F;

  public NightSoil() {
    super(ID, new Texture(TheSimpletonMod.getResourcePath(IMG_PATH)),
        new Texture(TheSimpletonMod.getResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_LARGE));
  }

  @Override
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0] + CROP_AMOUNT + (CROP_AMOUNT == 1 ? this.DESCRIPTIONS[1] : this.DESCRIPTIONS[2]);
  }

  @Override
  public void onShuffle() {
    Logger logger = TheSimpletonMod.logger;
//    logger.info("NightSoil::onShuffle");

      List<AbstractCropOrb> eligibleCropOrbs = AbstractCrop.getActiveCropOrbs().stream()
          .filter(orb -> !orb.isMature(true))
          .collect(Collectors.toList());

//    logger.info("NightSoil::onShuffle found " + eligibleCropOrbs.size() + " eligible crops");

    if (!eligibleCropOrbs.isEmpty()) {
      AbstractDungeon.effectList.add(new DelayedRelicFlashEffect(this, RELIC_FLASH_DELAY));
      eligibleCropOrbs.forEach(orb -> AbstractCrop.stackOrb(orb, CROP_AMOUNT, false));
    }
  }

  @Override
  public AbstractRelic makeCopy() {
    return new NightSoil();
  }
}