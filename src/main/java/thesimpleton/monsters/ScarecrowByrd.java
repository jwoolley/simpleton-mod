package thesimpleton.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.*;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.ScreenFlashDebuffEffectAction;
import thesimpleton.cards.status.Harried;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ScarecrowByrd extends Byrd {

  private static final String FLYING_ATLAS_PATH = TheSimpletonMod.getResourcePath("monsters/crow/flying.atlas");
  private static final String FLYING_JSON_PATH = TheSimpletonMod.getResourcePath("monsters/crow/flying.json");

  private static final String GROUNDED_ATLAS_PATH = TheSimpletonMod.getResourcePath("monsters/crow/grounded.atlas");
  private static final String GROUNDED_JSON_PATH = TheSimpletonMod.getResourcePath("monsters/crow/grounded.json");

  private static final String NAME = "Crow";
  private final int HARRIED_CHANCE_PERCENTAGE = 45;
  private static final int HARRIED_AMOUNT = 2;

  private static final String IDLE_ANIMATION_KEY = "idle_flap";

  private boolean _isFlying = true;

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

    this.name = NAME;

    this.tint.color = Color.NAVY.cpy();
//
//    TheSimpletonMod.customizeAnimation(this, FLYING_ATLAS_PATH, FLYING_JSON_PATH, WIDTH, HEIGHT,
//        IDLE_ANIMATION_KEY);

//    customizeAnimation(FLYING_ATLAS_PATH, FLYING_JSON_PATH, WIDTH, HEIGHT, IDLE_ANIMATION_KEY, 1.0f);
  }

  public void customizeAnimation(String atlasPath, String jsonPath, int imageWidth,
                                 int imageHeight, String idleAnimationKey, float scale) {

    try {
      Method loadAnimationMethod =
          AbstractCreature.class.getDeclaredMethod("loadAnimation", String.class, String.class, float.class);
      loadAnimationMethod.setAccessible(true);
      loadAnimationMethod.invoke(this, atlasPath, jsonPath, 1.0F);
      AnimationState.TrackEntry e = this.state.setAnimation(0, idleAnimationKey, true);
      e.setTime(e.getEndTime() * MathUtils.random());
      e.setTimeScale(MathUtils.random(0.7F, 1.0F));
      float hb_w = imageWidth * Settings.scale;
      float hb_h = imageHeight * Settings.scale;
      this.hb_w = hb_w;
      this.hb_h = hb_h;
      Hitbox hb = this.hb;
      hb.width = hb_w;
      hb.height = hb_h;
      hb.cX = hb.x + hb.width / 2.0F;
      hb.cY = hb.y + hb.height / 2.0F;
      Method refreshHbLoc = AbstractCreature.class.getDeclaredMethod("refreshHitboxLocation");
      refreshHbLoc.setAccessible(true);
      refreshHbLoc.invoke(this);
      this.refreshIntentHbLocation();

      this.atlas = new TextureAtlas(Gdx.files.internal(atlasPath));
      final SkeletonJson json = new SkeletonJson(this.atlas);
      if (CardCrawlGame.dungeon != null && AbstractDungeon.player != null) {
        if (AbstractDungeon.player.hasRelic("PreservedInsect") && !this.isPlayer && AbstractDungeon.getCurrRoom().eliteTrigger) {
          scale += 0.3f;
        }
        if (ModHelper.isModEnabled("MonsterHunter") && !this.isPlayer) {
          scale -= 0.3f;
        }
      }
      json.setScale(Settings.scale / scale);
      final SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(jsonPath));
      (this.skeleton = new Skeleton(skeletonData)).setColor(Color.WHITE);
      this.stateData = new AnimationStateData(skeletonData);
      this.state = new AnimationState(this.stateData);

    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
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
  public void getMove(int num) {
    if (num < HARRIED_CHANCE_PERCENTAGE && this._isFlying && !lastTwoMoves((byte) 7)) {
      setMove((byte)7, Intent.DEBUFF);
    } else {
      super.getMove(num);
    }
  }

  @Override
  public void changeState(String stateName) {
    super.changeState(stateName);
    this._isFlying = "FLYING".equals(stateName);
  }


  @Override
  public void takeTurn() {
    if (this.nextMove == 7) {
      AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Harried(),
          HARRIED_AMOUNT, true, true));
      AbstractDungeon.actionManager.addToBottom(
          new ScreenFlashDebuffEffectAction(Color.VIOLET.cpy(), "POWER_ENTANGLED", Settings.ACTION_DUR_XFAST));
      AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    } else {
      super.takeTurn();
    }
  }

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