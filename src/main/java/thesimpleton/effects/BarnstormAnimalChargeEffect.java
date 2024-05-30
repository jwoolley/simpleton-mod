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
    private static float INITIAL_WAIT_DURATION = 0.005F;
    private static float ANIMAL_CHARGE_DURATION = Settings.ACTION_DUR_MED;


    private final float totalDuration;

    private final boolean isSheepOrWhatever; // TODO: change to enum

    private float _initialX;
    private float _initialY;

    private float _targetX;
    private float _targetY;

    private AbstractCreature target;

    private float _x;
    private float _y;
    private boolean usePowerfulImpact = false;

    private static final String PIG_IMG_PATH =  SimpletonVfxHelper.getImgFilePath("barnstorm-effect", "barnanimal-pig");

    private static final String SHEEP_IMG_PATH =  SimpletonVfxHelper.getImgFilePath("barnstorm-effect", "barnanimal-sheep");
    private static Texture pigImage;
    private static Texture sheepImage;

    private Texture animalImage;

    public static float getFullDuration() {
        return INITIAL_WAIT_DURATION + ANIMAL_CHARGE_DURATION;
    }

    public BarnstormAnimalChargeEffect(float initialX, float initialY, AbstractCreature target, boolean isSheepOrWhatever, boolean usePowerfulImpact) {
        this.duration = this.totalDuration = INITIAL_WAIT_DURATION + ANIMAL_CHARGE_DURATION;
        this.color = Color.WHITE.cpy();
        this.scale = 1.0F;

        this._x = this._initialX = initialX;
        this._y = this._initialY = initialY;
        this._targetX = target.drawX;
        this._targetY = target.drawY;
        this.target = target;

        this.isSheepOrWhatever = isSheepOrWhatever;
        this.usePowerfulImpact = usePowerfulImpact;

        if (pigImage == null) {
            pigImage = TheSimpletonMod.loadTexture(TheSimpletonMod.getImageResourcePath(PIG_IMG_PATH));
        }
        if (sheepImage == null) {
            sheepImage = TheSimpletonMod.loadTexture(TheSimpletonMod.getImageResourcePath(SHEEP_IMG_PATH));
        }

        if (isSheepOrWhatever) {
            animalImage = sheepImage;
        } else {
            animalImage = pigImage;
        }
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

//        TheSimpletonMod.traceLogger.log("[BarnstormAnimalChargeEffect] update() called. t: " + t);


        // TODO: play SFX
        if (this.duration < ANIMAL_CHARGE_DURATION) {
            if (!isAnimalCharging) {
                TheSimpletonMod.traceLogger.log("[BarnstormAnimalChargeEffect] start isAnimalCharging");
                String SFX_KEY = isSheepOrWhatever ? "ANIMAL_SHEEP_BAA_1" : "ANIMAL_PIG_OINK_1";
                AbstractDungeon.actionManager.addToTop(new SFXAction(SFX_KEY));

                isAnimalCharging = true;
            }

            float t = ((this.totalDuration - INITIAL_WAIT_DURATION) - this.duration) / this.totalDuration;
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
                    new VFXAction(new BuzzBombImpactEffect(target.hb.x, target.hb.cY, usePowerfulImpact)));
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (isAnimalCharging) {
//            TheSimpletonMod.traceLogger.log("[BarnstormAnimalChargeEffect] render() called."
//                    + " t: " + (this.totalDuration - this.duration) / this.totalDuration
//                    + " x,y: (" + this._x + ", " + this._y + ")"
//                    + " image w/h: (" + this.animalImage.getWidth() + ", " + this.animalImage.getHeight()+ ")");

            sb.setColor(this.color);

            final int w = animalImage.getWidth();
            final int h = animalImage.getHeight();
            // This method has a lot of orb slots
            sb.draw(animalImage, this._x, this._y,
                    w/2f, h/2f,
                    w, h,
                    this.scale * Settings.scale, this.scale * Settings.scale,
                    this.rotation,
                    0, 0,
                    w, h,
                    false, false);
        }
    }

    @Override
    public void dispose() { }
}