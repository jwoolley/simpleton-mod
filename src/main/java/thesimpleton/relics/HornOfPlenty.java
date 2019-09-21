package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.characters.TheSimpletonCharacter;
import thesimpleton.powers.AbundancePower;
import thesimpleton.powers.LoseAbundancePower;

public class HornOfPlenty extends CustomRelic {
  public static final String ID = "TheSimpletonMod:HornOfPlenty";
  public static final String IMG_PATH = "relics/hornofplenty.png";
  public static final String IMG_PATH_LARGE = "relics/hornofplenty_large.png";
  public static final String OUTLINE_IMG_PATH = "relics/hornofplenty_outline.png";

  private static final RelicTier TIER = RelicTier.RARE;
  private static final LandingSound SOUND = LandingSound.MAGICAL;

  private static final int ABUNDANCE_AMOUNT = 1;

  public HornOfPlenty() {
    super(ID, new Texture(TheSimpletonMod.getResourcePath(IMG_PATH)),
        new Texture(TheSimpletonMod.getResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_LARGE));
  }

  @Override
  public void atBattleStart() {
    final AbstractPlayer p = AbstractDungeon.player;
    this.flash();
//    AbstractDungeon.actionManager.addToBottom(
//        new ApplyPowerAction(p, p, new AbundancePower(p, p, ABUNDANCE_AMOUNT), ABUNDANCE_AMOUNT));

    final AbstractCropPowerCard randomCropPowerCard = TheSimpletonCharacter.getCropUtil().getRandomCropCardInSeason();
    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(randomCropPowerCard, 1));

    AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));

    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new AbundancePower(p, p, ABUNDANCE_AMOUNT), ABUNDANCE_AMOUNT));

    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new LoseAbundancePower(p, ABUNDANCE_AMOUNT), ABUNDANCE_AMOUNT));
  }

  @Override
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0] + ABUNDANCE_AMOUNT + this.DESCRIPTIONS[1];
  }

  @Override
  public AbstractRelic makeCopy() {
    return new HornOfPlenty();
  }
}
