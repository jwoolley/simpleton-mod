package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.OrbSpawnAction;
import thesimpleton.orbs.ParasiteFruitOrb;

public class AlienArtifact extends CustomRelic {
  public static final String ID = "TheSimpletonMod:AlienArtifact";
  public static final String IMG_PATH = "relics/alienartifact.png";
  public static final String IMG_PATH_LARGE = "relics/alienartifact_large.png";
  public static final String OUTLINE_IMG_PATH = "relics/alienartifact_outline.png";

  private static final RelicTier TIER = RelicTier.SPECIAL;
  private static final LandingSound SOUND = LandingSound.SOLID;

  private boolean firstTurn = true;


  public AlienArtifact() {
    super(ID, new Texture(TheSimpletonMod.getResourcePath(IMG_PATH)),
        new Texture(TheSimpletonMod.getResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_LARGE));
  }

  @Override
  public void  atBattleStartPreDraw() {
    this.flash();
    CardCrawlGame.sound.play("SWOOSH_SCIFI_1");
    AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    ParasiteFruitOrb.plantFruitAndAddSlot(1);
}

  @Override
  public void atTurnStart() {
//    if (this.firstTurn) {
//      this.flash();
//
//      this.firstTurn = false;
//    }
  }


  @Override
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0];
  }

  public void activate(AbstractCreature source, AbstractCreature target, int amount) {
    flash();
  }

  @Override
  public AbstractRelic makeCopy() {
    return new AlienArtifact();
  }
}
