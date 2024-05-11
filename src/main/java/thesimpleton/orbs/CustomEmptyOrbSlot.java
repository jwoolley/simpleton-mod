package thesimpleton.orbs;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;

public abstract class CustomEmptyOrbSlot extends EmptyOrbSlot {
  private static final OrbStrings orbStrings;
  public static final String NAME;
  public static final String[] CUSTOM_DESCRIPTIONS;

  public static final String ORB_ID = "TheSimpletonMod:EmptyCropOrb";
  public CustomEmptyOrbSlot(float cX, float cY) {

    super(cX, cY);
    this.name = NAME;
    this.evokeAmount = 0;
  }

  public abstract Texture getForegroundImage();
  public abstract Texture getBackgroundImage();

  public void updateDescription() {
    this.description = CUSTOM_DESCRIPTIONS[0];
  }

  static {
    orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    NAME = orbStrings.NAME;
    CUSTOM_DESCRIPTIONS = orbStrings.DESCRIPTION;
  }
}