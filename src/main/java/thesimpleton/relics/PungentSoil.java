package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.DamageImpactCurvyEffect;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.AbstractCrop;
import thesimpleton.effects.relic.DelayedRelicFlashEffect;
import thesimpleton.orbs.AbstractCropOrb;

import java.util.List;
import java.util.stream.Collectors;

public class PungentSoil extends CustomRelic {
  public static final String ID = "TheSimpletonMod:PungentSoil";
  public static final String IMG_PATH = "relics/pungentsoil.png";
  public static final String IMG_PATH_LARGE = "relics/pungentsoil_large.png";
  public static final String OUTLINE_IMG_PATH = "relics/pungentsoil_outline.png";

  private static final RelicTier TIER = RelicTier.STARTER;
  private static final LandingSound SOUND = LandingSound.HEAVY;

  private static final int CROP_AMOUNT = 1;
  private static final float RELIC_FLASH_DELAY = 1.2F;

  public PungentSoil() {
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
    logger.debug("PungentSoil::onShuffle");

      List<AbstractCropOrb> eligibleCropOrbs = AbstractCrop.getActiveCropOrbs().stream()
          .filter(orb -> !orb.isMature())
          .collect(Collectors.toList());

    logger.debug("PungentSoil::onShuffle found " + eligibleCropOrbs.size() + " eligible crops");

    if (!eligibleCropOrbs.isEmpty()) {
      AbstractDungeon.effectList.add(new DelayedRelicFlashEffect(this, RELIC_FLASH_DELAY));
      eligibleCropOrbs.forEach(orb -> AbstractCrop.stackOrb(orb, CROP_AMOUNT, false));
    }
  }

  @Override
  public AbstractRelic makeCopy() {
    return new PungentSoil();
  }
}