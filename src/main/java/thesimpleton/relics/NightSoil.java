package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
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

  private static final float RELIC_FLASH_DELAY = 0.2F;

  public NightSoil() {
    super(ID, new Texture(TheSimpletonMod.getImageResourcePath(IMG_PATH)),
        new Texture(TheSimpletonMod.getImageResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getImageResourcePath(IMG_PATH_LARGE));
  }

  @Override
  public String getUpdatedDescription() {
    final int cropAmount = AbstractCropOrb.STACK_AMOUNT_ON_SHUFFLE;
    return this.DESCRIPTIONS[0] + cropAmount + (cropAmount == 1 ? this.DESCRIPTIONS[1] : this.DESCRIPTIONS[2]);
  }

  @Override
  public void onShuffle() {
      List<AbstractCropOrb> eligibleCropOrbs = AbstractCrop.getActiveCropOrbs().stream()
          .filter(orb -> !orb.isMature(true))
          .collect(Collectors.toList());

    if (!eligibleCropOrbs.isEmpty()) {
      AbstractDungeon.effectList.add(new DelayedRelicFlashEffect(this, RELIC_FLASH_DELAY));
      // moved into AbstractCrop
      // eligibleCropOrbs.forEach(orb -> AbstractCrop.stackOrb(orb, CROP_AMOUNT, false));
    }
  }

  @Override
  public AbstractRelic makeCopy() {
    return new NightSoil();
  }
}