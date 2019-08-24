package thesimpleton.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import thesimpleton.TheSimpletonMod;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HayseedVictoryVegetablesEffect extends AbstractGameEffect {

    // Settings
    final private float gravity = -320.0f;  // Gravity (negative is down)
    final private float spinPower = 160f;
    final private float growthFactor = 4f;
    final private float horizontalMovement = 600f;
    final private float verticalMovement = gravity * -2;
    final private float screenBorder = 300;
    private float duration;

    private float x;
    private float y;
    private float vX;
    private float vY;
    private float rotation;
    private float spin;
    private float growth;
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

        // duration
        this.duration = 8f;
        this.startingDuration = this.duration;

        this.renderBehind = true;

        // Spinny veggies
        this.rotation = MathUtils.random(-spinPower, spinPower);
        this.spin = MathUtils.random(-spinPower, spinPower);

        // Location
        boolean lefty = MathUtils.randomBoolean();
        if (lefty) {
            this.x = -screenBorder * Settings.scale - (float) this.texture.getWidth() / 2.0F;
            this.vX = MathUtils.random(horizontalMovement / 3, horizontalMovement) * Settings.scale;
        } else {
            this.x = (1870 + screenBorder) * Settings.scale - (float) this.texture.getWidth() / 2.0F;
            this.vX = MathUtils.random(-horizontalMovement, -horizontalMovement / 3) * Settings.scale;
        }
        float h = MathUtils.random(0.15F, 0.9F);
        this.y = (float)Settings.HEIGHT * h;
        this.vY = MathUtils.random(verticalMovement / 2F, verticalMovement) * Settings.scale;

        // plain old regular colors
        this.color = new Color(1, 1, 1, 1F);

        // how big are the taters
        this.scale = MathUtils.random(0.8f, growthFactor) * Settings.scale;
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
        // Timing
        final float dt = Gdx.graphics.getDeltaTime();
        this.duration -= dt;

        this.y += this.vY * dt;
        this.x += this.vX * dt;
        this.vY += this.gravity * dt;
        this.rotation += this.spin * dt;
    }

    private float clamp(float a, float min, float max) {
        return Math.max(Math.min(a, max), min);
    }
}
