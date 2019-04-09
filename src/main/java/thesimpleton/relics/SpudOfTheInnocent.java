package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.powers.PlantPotatoPower;

public class SpudOfTheInnocent extends CustomRelic {
  public static final String ID = "TheSimpletonMod:SpudOfTheInnocent";
  public static final String IMG_PATH = "relics/spudoftheinnocent.png";
  public static final String IMG_PATH_LARGE = "relics/spudoftheinnocent_large.png";
  public static final String OUTLINE_IMG_PATH = "relics/spudoftheinnocent_outline.png";

  private static final RelicTier TIER = RelicTier.STARTER;
  private static final LandingSound SOUND = LandingSound.HEAVY;

  private static final int CROP_AMOUNT = 1;

  public SpudOfTheInnocent() {
    super(ID, new Texture(TheSimpletonMod.getResourcePath(IMG_PATH)),
        new Texture(TheSimpletonMod.getResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_LARGE));
  }

  @Override
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0];
  }

  @Override
  public void atBattleStart() {
    final AbstractPlayer p = AbstractDungeon.player;
    this.flash();
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new PlantPotatoPower(p, CROP_AMOUNT), CROP_AMOUNT));

  }

  @Override
  public AbstractRelic makeCopy() {
    return new SpudOfTheInnocent();
  }
}
