package thesimpleton.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import thesimpleton.TheSimpletonMod;

public class ScarecrowByrd extends Byrd {

  private static final String FLYING_ATLAS_PATH = TheSimpletonMod.getResourcePath("monsters/crow/flying.atlas");
  private static final String FLYING_JSON_PATH = TheSimpletonMod.getResourcePath("monsters/crow/flying.json");

  private static final String GROUNDED_ATLAS_PATH = TheSimpletonMod.getResourcePath("monsters/crow/grounded.atlas");
  private static final String GROUNDED_JSON_PATH = TheSimpletonMod.getResourcePath("monsters/crow/grounded.json");


  private static final String IDLE_ANIMATION_KEY = "idle_flap";

  // TODO: use private field access from MTS instead
  // https://cdn.discordapp.com/attachments/398373038732738570/625057908563902464/unknown.png
  private static final int WIDTH = 240;
  private static final int HEIGHT = 180;


  private static final int INCREASED_HP = 16;
  private static final int A_18_INCREASED_HP = 32;

  public ScarecrowByrd(float x, float y) {
    super(x, y);

    if (AbstractDungeon.ascensionLevel >= 8) {
      setHp(this.maxHealth + A_18_INCREASED_HP, this.maxHealth + A_18_INCREASED_HP);
    } else {
      setHp(this.maxHealth + INCREASED_HP, this.maxHealth + INCREASED_HP);
    }

    this.tint.color = Color.NAVY.cpy();

    TheSimpletonMod.customizeAnimation(this, FLYING_ATLAS_PATH, FLYING_JSON_PATH, WIDTH, HEIGHT,
        IDLE_ANIMATION_KEY);
  }


  public void render(SpriteBatch sb) {
    super.render(sb);

  }


//  @Override
//  public void loadAnimation(String atlasPath, String jsonPath, float scale) {
//    TheSimpletonMod.logger.info("ScarecrowByrd::loadAnimation called");
//    if (atlasPath.contains("flying.atlas")) {
//      TheSimpletonMod.logger.info("ScarecrowByrd::loadAnimation applying flying atlas");
//      super.loadAnimation(FLYING_ATLAS_PATH, jsonPath, scale);
//    } else if (atlasPath.contains("grounded.atlas")) {
//      TheSimpletonMod.logger.info("ScarecrowByrd::loadAnimation applying grounded atlas");
//      super.loadAnimation(GROUNDED_ATLAS_PATH, jsonPath, scale);
//    }
//    else {
//      super.loadAnimation(atlasPath, jsonPath, scale);
//    }
//  }

  @Override
  public void die() {
    super.die();
    for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
      if ((!m.isDead) && (!m.isDying)) {
        m.deathReact();
      }
    }
  }
}