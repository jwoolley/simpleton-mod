package thesimpleton.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import thesimpleton.TheSimpletonMod;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HayseedVictoryVegetablesEffect extends AbstractGameEffect {

    // Settings
    private float dY = -80.0f;  // Gravity (negative is down)
    private float spinPower = 80f;
    private float growthFactor = 5f;
    private float horizontalMovement = 40f;
    private float verticalMovement = 120f;
    private float fadeInSpeed = 4.0f;
    private float minDuration = 6f;
    private float maxDuration = 8f;

    private float x;
    private float y;
    private float vX;
    private float vY;
    private float rotation;
    private float spin;
    private static List<Texture> textures;
    private Texture texture;

    private final List<String> IMG_PATHS = Arrays.asList(
            "orbs/plantartichoke.png",
            "orbs/plantasparagus.png",
            "orbs/plantchili.png",
            "orbs/plantcoffee.png",
            "orbs/plantcorn.png",
            "orbs/plantmushroom.png",
            "orbs/plantonion.png",
            "orbs/plantpotato.png",
            "orbs/plantspinach.png",
            "orbs/plantsquash.png",
            "orbs/plantstrawberry.png",
            "orbs/plantsunflower.png",
            "orbs/plantturnip.png"
    );


    public HayseedVictoryVegetablesEffect() {
        if (textures == null) {
            textures = IMG_PATHS.stream()
                .map(path -> new Texture(TheSimpletonMod.getResourcePath(path)))
                .collect(Collectors.toList());
        }

        // Pick a random texture
        this.texture = textures.get(MathUtils.random(0, textures.size() - 1));

        // Random duration
        this.duration = MathUtils.random(minDuration, maxDuration);
        this.startingDuration = this.duration;

        this.renderBehind = true;

        // Spinny veggies
        this.rotation = MathUtils.random(-spinPower, spinPower);
        this.spin = MathUtils.random(-spinPower, spinPower);

        // Location
        this.x = MathUtils.random(-100.0F, 1870.0F) * Settings.scale - (float) this.texture.getWidth() / 2.0F;
        float h = MathUtils.random(0.15F, 0.9F);
        this.y = (float)Settings.HEIGHT * h;
        this.vX = MathUtils.random(-horizontalMovement, horizontalMovement) * Settings.scale;
        this.vY = MathUtils.random(0, verticalMovement) * Settings.scale;

        // color
        this.color = new Color(1, 1, 1, 1F);

        // scale
        this.scale = h * MathUtils.random(1.5F, growthFactor) * Settings.scale;
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        final int w = texture.getWidth();
        final int h = texture.getHeight();
        // This method has a lot of orb slots
        sb.draw(texture, x, y,
                w/2f, h/2f,
                w, h,
                this.scale * Settings.scale, this.scale * Settings.scale,
                this.rotation,
                0, 0,
                w, h,
                false, false);
    }

    @Override
    public void dispose() {
        // throw away the potato
    }

    @Override
    public void update() {
        final float dt = Gdx.graphics.getDeltaTime();
        this.y += this.vY * dt;
        this.x += this.vX * dt;
        this.vY += this.dY * dt;
        this.rotation += this.spin * dt;
        if (this.duration > this.startingDuration / 2.0F) {
            this.color.a = Interpolation.fade.apply(0.9F, 0.0F, (this.duration - this.startingDuration / 2.0F) / (this.startingDuration / 2.0F));
        } else {
            this.color.a = Interpolation.fade.apply(0.0F, 0.9F, this.duration / (this.startingDuration / fadeInSpeed));
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }
}
