package thesimpleton.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import thesimpleton.TheSimpletonMod;
import thesimpleton.effects.utils.SimpletonVfxHelper;

public class BarnstormAnimalChargeEffect extends AbstractGameEffect {
    private static float INITIAL_WAIT_DURATION = Settings.ACTION_DUR_FAST;
    private static float ANIMAL_CHARGE_DURATION =  Settings.ACTION_DUR_MED;
    private static float INITIAL_WAIT_DURATION_FAST = Settings.ACTION_DUR_XFAST;
    private static float ANIMAL_CHARGE_DURATION_FAST =  0.33F;

    private final float totalDuration;

    private final boolean isSheepOrWhatever; // TODO: change to enum

    private float _initialX;
    private float _initialY;

    private float _targetX;
    private float _targetY;

    private AbstractCreature target;

    private float _x;
    private float _y;

    private static final String BARN_UNDERLAY_IMG_PATH =  SimpletonVfxHelper.getImgFilePath("barnstorm-effect", "barn-underlay");

    private static final String BARN_OVERLAY_IMG_PATH =  SimpletonVfxHelper.getImgFilePath("barnstorm-effect", "barn-overlay");

    private static final String PIG_IMG_PATH =  SimpletonVfxHelper.getImgFilePath("barnstorm-effect", "barnanimal-pig");

    private static final String SHEEP_IMG_PATH =  SimpletonVfxHelper.getImgFilePath("barnstorm-effect", "barnanimal-sheep");

    private final  Texture barnUnderlayImage;

    private final Texture barnOverlayImage;

    private final Texture pigImage;
    private final Texture sheepImage;

    private Texture animalImage;


    private final int barnWidth;
    private final int barnHeight;

    private final int animalWidth;
    private final int animalHeight;

    private final float animalOffsetY;

    public static float getFullDuration() {
        return Settings.FAST_MODE
                ? INITIAL_WAIT_DURATION_FAST + ANIMAL_CHARGE_DURATION_FAST
                : INITIAL_WAIT_DURATION + ANIMAL_CHARGE_DURATION;
    }

    public BarnstormAnimalChargeEffect(float initialX, float initialY, AbstractCreature target, float animalOffsetY,
                                       boolean isSheepOrWhatever) {
        this.duration = this.totalDuration = getInitialWaitDuration() + getAnimalChargeDuration();

        this.color = Color.WHITE.cpy();
        this.scale = 1.0F;

        this._x = this._initialX = initialX;
        this._y = this._initialY = initialY;
        this._targetX = target.drawX + target.hb_w / 2.0F;
        this._targetY = initialY; // target.drawY;
        this.target = target;

        this.isSheepOrWhatever = isSheepOrWhatever;

        barnUnderlayImage = TheSimpletonMod.loadTexture(TheSimpletonMod.getImageResourcePath(BARN_UNDERLAY_IMG_PATH));
        barnOverlayImage = TheSimpletonMod.loadTexture(TheSimpletonMod.getImageResourcePath(BARN_OVERLAY_IMG_PATH));

        barnWidth = barnOverlayImage.getWidth();
        barnHeight = barnOverlayImage.getHeight();

        pigImage = TheSimpletonMod.loadTexture(TheSimpletonMod.getImageResourcePath(PIG_IMG_PATH));
        sheepImage = TheSimpletonMod.loadTexture(TheSimpletonMod.getImageResourcePath(SHEEP_IMG_PATH));

        if (isSheepOrWhatever) {
            animalImage = sheepImage;
        } else {
            animalImage = pigImage;
        }
        animalWidth = animalImage.getWidth();
        animalHeight = animalImage.getHeight();
        this.animalOffsetY = animalOffsetY;
    }

    private float getInitialWaitDuration() {
        return Settings.FAST_MODE ? INITIAL_WAIT_DURATION_FAST  : INITIAL_WAIT_DURATION;
    }

    private float getAnimalChargeDuration() {
        return Settings.FAST_MODE ? ANIMAL_CHARGE_DURATION_FAST  : ANIMAL_CHARGE_DURATION;
    }

    private boolean isAnimalCharging = false;
    @Override
    public void update() {

        if (this.duration == this.totalDuration) {
            TheSimpletonMod.traceLogger.log("[BarnstormAnimalChargeEffect] update() called, first tick:"
                    + " (x, y): (" + this._x + ", " + this._y + ")"
                    + "; (startX, startY): " + _initialX + ", " + _initialY + ")"
                    + "; (targetX, targetY): " + _targetX + ", " + _targetY + ")"
            );
        }

        // TODO: play SFX
        if (this.duration < getAnimalChargeDuration()) {
            if (!isAnimalCharging) {
                TheSimpletonMod.traceLogger.log("[BarnstormAnimalChargeEffect] start isAnimalCharging");
                String SFX_KEY = isSheepOrWhatever ? "ANIMAL_SHEEP_BAA_1" : "ANIMAL_PIG_OINK_1";
                AbstractDungeon.actionManager.addToTop(new SFXAction(SFX_KEY));

                isAnimalCharging = true;
            }

            float t = ((this.totalDuration - getInitialWaitDuration()) - this.duration) / this.totalDuration;
//            float tInterpolated = Interpolation.swingIn.apply(t);
            float tInterpolated = Interpolation.swing.apply(t);

            this._x = MathUtils.lerp(_initialX,  _targetX, tInterpolated);
            this._y = MathUtils.lerp(_initialY,  _targetY, tInterpolated);

//            TheSimpletonMod.traceLogger.log("[BarnstormAnimalChargeEffect] update() called."
//                    + " t: " + (this.totalDuration - this.duration) / this.totalDuration
//                    + " x,y: (" + this._x + ", " + this._y + ")"
//                    + " image w/h: (" + this.animalImage.getWidth() + ", " + this.animalImage.getHeight()+ ")"
//            );
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            isAnimalCharging = false;
            AbstractDungeon.actionManager.addToBottom(
                    new VFXAction(new BuzzBombImpactEffect(target.hb.x, target.hb.cY)));
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(barnUnderlayImage, this._initialX - barnWidth/2f, this._initialY,
                barnWidth/2f, barnHeight/2f,
                barnWidth, barnHeight,
                this.scale * Settings.scale, this.scale * Settings.scale,
                this.rotation,
                0, 0,
                barnWidth, barnHeight,
                false, false);

        if (isAnimalCharging) {
//            TheSimpletonMod.traceLogger.log("[BarnstormAnimalChargeEffect] render() called."
//                    + " t: " + (this.totalDuration - this.duration) / this.totalDuration
//                    + " x,y: (" + this._x + ", " + this._y + ")"
//                    + " image w/h: (" + this.animalImage.getWidth() + ", " + this.animalImage.getHeight()+ ")");


            sb.draw(animalImage, this._x, this._y + animalOffsetY - animalHeight,
                    animalWidth/2f, animalHeight/2f,
                    animalWidth, animalHeight,
                    this.scale * Settings.scale, this.scale * Settings.scale,
                    this.rotation,
                    0, 0,
                    animalWidth, animalHeight,
                    false, false);

            sb.draw(barnOverlayImage,this._initialX - barnWidth/2f, this._initialY,
                    barnWidth/2f, barnHeight/2f,
                    barnWidth, barnHeight,
                    this.scale * Settings.scale, this.scale * Settings.scale,
                    this.rotation,
                    0, 0,
                    barnWidth, barnHeight,
                    false, false);
        }
    }

    @Override
    public void dispose() { }
}