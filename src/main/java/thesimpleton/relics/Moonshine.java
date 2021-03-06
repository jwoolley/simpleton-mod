package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thesimpleton.TheSimpletonMod;
import thesimpleton.powers.IntoxicatedPower;
import thesimpleton.powers.SoberUpPower;

public class Moonshine extends CustomRelic {
  public static final String ID = "TheSimpletonMod:Moonshine";
  public static final String IMG_PATH = "relics/moonshine.png";
  public static final String IMG_PATH_LARGE = "relics/moonshine_large.png";
  public static final String OUTLINE_IMG_PATH = "relics/moonshine_outline.png";

  private static final RelicTier TIER = RelicTier.UNCOMMON;
  private static final LandingSound SOUND = LandingSound.CLINK;

  private static final int TRIGGER_TURN = 3;
  private static final int STRENGTH_AMOUNT = 5;
  private static final int DRAW_AMOUNT = 1;

  public Moonshine() {
    super(ID, new Texture(TheSimpletonMod.getImageResourcePath(IMG_PATH)),
        new Texture(TheSimpletonMod.getImageResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getImageResourcePath(IMG_PATH_LARGE));
  }

  @Override
  public void atBattleStart() {
    this.counter = 0;
  }

  @Override
  public void atTurnStart() {
    AbstractPlayer player = AbstractDungeon.player;
    this.counter += 1;
    if (this.counter == TRIGGER_TURN) {
      stopPulse();
      this.flash();
      AbstractDungeon.actionManager.addToBottom(new SFXAction("DRINK_BOTTLE_1"));
      AbstractDungeon.actionManager.addToBottom(new SFXAction("GIBBERISH_ANGRY_1"));
      AbstractDungeon.actionManager.addToBottom(
          new TalkAction(true, DESCRIPTIONS[8], 1.0F, 2.0F));

      AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player,
          new StrengthPower(player, STRENGTH_AMOUNT), STRENGTH_AMOUNT));
      AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player,
          new LoseStrengthPower(player, STRENGTH_AMOUNT), STRENGTH_AMOUNT));

      final boolean wasConfused = player.hasPower(ConfusionPower.POWER_ID);

      AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new IntoxicatedPower(player)));
      if (!wasConfused) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new SoberUpPower(player)));
      }

      AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));

      AbstractDungeon.actionManager.addToBottom(new DrawCardAction(player, DRAW_AMOUNT));
    }
  }

  @Override
  public void onPlayerEndTurn() {
    if (this.counter == TRIGGER_TURN - 1) {
      beginLongPulse();
    }
  }

  @Override
  public void onVictory() {
    this.counter = -1;
    stopPulse();
  }

  @Override
  public String getUpdatedDescription() {
    return DESCRIPTIONS[0] + TRIGGER_TURN + DESCRIPTIONS[1] + STRENGTH_AMOUNT
        + (DRAW_AMOUNT == 0 ? DESCRIPTIONS[2]
          : DESCRIPTIONS[3] + (DRAW_AMOUNT == 1 ? DESCRIPTIONS[4]
            : DESCRIPTIONS[5] + DRAW_AMOUNT + DESCRIPTIONS[6]))
        + DESCRIPTIONS[7];
  }

  @Override
  public AbstractRelic makeCopy() {
    return new Moonshine();
  }
}
