package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thesimpleton.TheSimpletonMod;

public class GardenGlove extends CustomRelic {
  public static final String ID = "TheSimpletonMod:GardenGlove";
  public static final String IMG_PATH = "relics/gardenglove.png";
  public static final String IMG_PATH_LARGE = "relics/gardenglove_large.png";
  public static final String OUTLINE_IMG_PATH = "relics/gardenglove_outline.png";

  private static final RelicTier TIER = RelicTier.UNCOMMON;
  private static final LandingSound SOUND = LandingSound.FLAT;

  public GardenGlove() {
    super(ID, new Texture(TheSimpletonMod.getImageResourcePath(IMG_PATH)),
        new Texture(TheSimpletonMod.getImageResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getImageResourcePath(IMG_PATH_LARGE));
  }


  @Override
  public void onPlayerEndTurn() {
    boolean shouldTrigger = AbstractDungeon.player.hand.group.stream()
        .filter(c -> c.type == AbstractCard.CardType.POWER).count() == 1;

    if (shouldTrigger) {
      CardCrawlGame.sound.play("ATTACK_SPLAT_1");
      this.flash();
      AbstractDungeon.player.hand.group.stream()
          .filter(c -> c.type == AbstractCard.CardType.POWER).findFirst().get().retain = true;
    }
  }

  @Override
  public String getUpdatedDescription() { return DESCRIPTIONS[0];  }

  @Override
  public AbstractRelic makeCopy() {
    return new GardenGlove();
  }
}
