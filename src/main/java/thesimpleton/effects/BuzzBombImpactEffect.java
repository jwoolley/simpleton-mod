package thesimpleton.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.DamageImpactCurvyEffect;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;

public class BuzzBombImpactEffect extends WeightyImpactEffect {
  private static float ACTION_DURATION = Settings.ACTION_DUR_LONG;

  private final boolean isFinalImpact;
  private float _x;
  private float _y;
  private boolean shakeAndFlash = false;
  private static TextureAtlas.AtlasRegion img;

  public BuzzBombImpactEffect(float x, float y) {
    this(x, y, false);
  }

  public BuzzBombImpactEffect(float x, float y, boolean isFinalImpact) {
    super(x, y, Color.TAN.cpy());
    this._x= x;
    this._y = y;

    this.duration = ACTION_DURATION;
    this.isFinalImpact = isFinalImpact;

    if (img == null) {
      img = ImageMaster.vfxAtlas.findRegion("combat/weightyImpact");
    }
  }

  @Override
  public void update() {
    if (this.duration < 0.0F) {

      this._y = Interpolation.fade.apply(Settings.HEIGHT, (this._y - 180.0F * Settings.scale), 1.0F - this.duration / 1.0F);

      this.scale += Gdx.graphics.getDeltaTime();
      this.duration -= Gdx.graphics.getDeltaTime();
      if (this.duration < 0.0F) {
        this.isDone = true;
        //      CardCrawlGame.sound.playA(this.isFinalImpact ? "ATTACK_FIRE_IMPACT_2" : "ATTACK_FIRE_IMPACT_1", 1.0f);
      } else if (this.duration < 0.2F) {
        if (!this.shakeAndFlash) {
          this.shakeAndFlash = true;
          AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.WHITE.cpy()));
          CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.MED, true);
          for (int i = 0; i < 5; i++) {
            AbstractDungeon.effectsQueue.add(new DamageImpactCurvyEffect(this._x + img.packedWidth / 2.0F, this._y + img.packedWidth / 2.0F));
          }
          for (int i = 0; i < 30; i++) {
            AbstractDungeon.effectsQueue.add(new UpgradeShineParticleEffect(this._x +
                MathUtils.random(-100.0F, 100.0F) * Settings.scale + img.packedWidth / 2.0F, this._y +
                MathUtils.random(-50.0F, 120.0F) * Settings.scale + img.packedHeight / 2.0F));
          }
        }
        this.color.a = Interpolation.fade.apply(0.0F, 0.5F, 0.2F / this.duration);
      } else {
        this.color.a = Interpolation.pow2Out.apply(0.6F, 0.0F, this.duration / 1.0F);
      }
      this.isDone = true;
      return;
    }
    this.duration -= Gdx.graphics.getDeltaTime();
  }
}