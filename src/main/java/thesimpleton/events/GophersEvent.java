package thesimpleton.events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PotionBelt;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.events.SimpletonEventHelper.EventState;

import java.util.ArrayList;
import java.util.List;

public class GophersEvent extends CustomSimpletonEvent {
  public static final String ID = TheSimpletonMod.makeID("GophersEvent");

  private static final String IMG_PATH = SimpletonEventHelper.getUiPath("gophers1");
  private static final String IMG_PATH_STEP_2 = SimpletonEventHelper.getUiPath("gophers1");
  private static final String IMG_PATH_STEP_3 = SimpletonEventHelper.getUiPath("gophers1");
  private static final String IMG_PATH_BITTEN = SimpletonEventHelper.getUiPath("gophers1");

  private static final EventStrings eventStrings;
  private static final String NAME;
  private static final String[] DESCRIPTIONS;
  private static final String[] OPTIONS;
  private static final List<Integer> chosenOptions = new ArrayList();
  private static final List<Integer> disabledOptions = new ArrayList();

  private static AbstractRelic RELIC_REWARD = new PotionBelt();

  private EventState state;

  private final int[] FAIL_CHANCE_PERCENTAGES = { 25, 33, 50 };
  private final float[] HP_PERCENTAGE_MULTIPLIERS = { 1.0f, 1.5f, 2.0f };
  private static final int MAX_HP_PER_TIER = 5;
  private static final int GOLD_PER_TIER = 65;
  private static final int GOLD_MAX_EXTRA = 20;
  private static final int POTIONS_PER_TIER = 2;

  private static final float HP_PERCENT_LOST_PER_TIER = 0.11F;

  private int rewardCounter = 1;
  private final int goldBonus;

  public GophersEvent() {
    super(NAME, DESCRIPTIONS[0],  TheSimpletonMod.getResourcePath(IMG_PATH));

    // TODO: Handle case where no upgradable cards, etc.

    // TODO: UPDATE TEXT/IMAGE AFTER EATEN BY GOPHER (SUCCESS)

    this.goldBonus = GOLD_PER_TIER + AbstractDungeon.eventRng.random(GOLD_MAX_EXTRA + 1);
    updateDialogOptions();
    this.state = EventState.WAITING;
    CardCrawlGame.sound.play("GOPHER_LAUGH");
  }

  @Override
  protected void buttonEffect(int buttonPressed) {
    switch (state) {
      case WAITING:
        switch (buttonPressed) {
          case 0:
            break;

          case 1:
            break;

          case 2:
            break;

          case 3:
            break;

          case 4:
            break;

          case 5:
            break;

          default:
            break;
        }

        // TODO: if not bitten, gopher eats a random remaning options

        boolean done = true;
        if (done) {
          this.imageEventText.clearAllDialogs();
          this.imageEventText.setDialogOption(OPTIONS[25]);
          this.state = EventState.LEAVING;
          AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        }
        break;
      case LEAVING:
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[25]);
        openMap();
        break;
    }
  }

  private void updateDialogOptions() {
    /*
     * OPTIONS
     * 1) Upgrade N random cards
     * 2) Gain N * X Max HP
     * 3) Gain N random (common?) relics
     * 4) Gain N * X Gold
     * 5) Gain N random upgraded Rare cards
     * 6) Gain X random Potions
     */
    final int FAIL_PCT = FAIL_CHANCE_PERCENTAGES[this.rewardCounter - 1];
    final int HP_LOSS =  Math.max ((int)(HP_PERCENT_LOST_PER_TIER * HP_PERCENTAGE_MULTIPLIERS[this.rewardCounter - 1]
        * AbstractDungeon.player.maxHealth), 6);
    final String loseDesc = OPTIONS[2] + FAIL_PCT + OPTIONS[3] + OPTIONS[4] + HP_LOSS + OPTIONS[5];

    final int rewardMultiplier = 1; // rewardMultiplier = this.rewardCounter;


    if (this.rewardCounter == 1) {
      this.imageEventText.setDialogOption(OPTIONS[0] + OPTIONS[6] + OPTIONS[7] + loseDesc);
      this.imageEventText.setDialogOption(OPTIONS[0] + OPTIONS[9] + (rewardMultiplier + MAX_HP_PER_TIER) + OPTIONS[10] + loseDesc);
//      this.imageEventText.setDialogOption(OPTIONS[0] + OPTIONS[11] + OPTIONS[12] + loseDesc);
      this.imageEventText.setDialogOption(OPTIONS[0] + OPTIONS[14] + (rewardMultiplier + goldBonus) + OPTIONS[15] + loseDesc);
      this.imageEventText.setDialogOption(OPTIONS[0] + OPTIONS[16] + OPTIONS[17] + loseDesc);
      this.imageEventText.setDialogOption(OPTIONS[0] + OPTIONS[21] + rewardMultiplier * POTIONS_PER_TIER + OPTIONS[23] + loseDesc);
      this.imageEventText.setDialogOption(OPTIONS[25]);

    } else {
      for (Integer chosenOption: chosenOptions) {
        this.imageEventText.removeDialogOption(chosenOption);
      }
      for (Integer disabledOption: disabledOptions) {
        this.imageEventText.updateDialogOption(disabledOption, OPTIONS[24], true);
      }

//      if (!chosenOptions.contains(0) && !disabledOptions.contains(0)) {
//        this.imageEventText.updateDialogOption(0, OPTIONS[0] + OPTIONS[6] + this.rewardCounter + OPTIONS[8] + loseDesc);
//      }

      if (!chosenOptions.contains(1) && !disabledOptions.contains(1)) {
        if (rewardMultiplier == 1) {
          this.imageEventText.updateDialogOption(1, OPTIONS[0] + OPTIONS[6] + OPTIONS[7] + loseDesc);
        } else {
          this.imageEventText.updateDialogOption(1, OPTIONS[0] + OPTIONS[9] + rewardMultiplier + MAX_HP_PER_TIER + OPTIONS[10] + loseDesc);
        }
      }

      if (!chosenOptions.contains(2) && !disabledOptions.contains(2)) {
        if (rewardMultiplier == 1) {
          this.imageEventText.updateDialogOption(2,OPTIONS[0] + OPTIONS[9] + (rewardMultiplier + MAX_HP_PER_TIER) + OPTIONS[10] + loseDesc);
        } else {
          this.imageEventText.updateDialogOption(2, OPTIONS[0] + OPTIONS[11] + rewardMultiplier + OPTIONS[13] + loseDesc);
        }
      }

      if (!chosenOptions.contains(3) && !disabledOptions.contains(3)) {
        if (rewardMultiplier == 1) {
          this.imageEventText.updateDialogOption(3,OPTIONS[0] + OPTIONS[14] + (rewardMultiplier + goldBonus) + OPTIONS[15] + loseDesc);
        } else {
          this.imageEventText.updateDialogOption(3, OPTIONS[0] + OPTIONS[14] + rewardMultiplier + goldBonus + OPTIONS[15] + loseDesc);
        }
      }

      if (!chosenOptions.contains(4) && !disabledOptions.contains(4)) {
        if (rewardMultiplier == 1) {
          this.imageEventText.updateDialogOption(4,OPTIONS[0] + OPTIONS[16] + OPTIONS[17] + loseDesc);
        } else {
          this.imageEventText.updateDialogOption(4, OPTIONS[0] + OPTIONS[16] + rewardMultiplier + OPTIONS[18] + loseDesc);
        }
      }

      if (!chosenOptions.contains(5) && !disabledOptions.contains(5)) {
        if (POTIONS_PER_TIER * rewardMultiplier == 1) {
          this.imageEventText.updateDialogOption(5,OPTIONS[0] + OPTIONS[21] + OPTIONS[22] + loseDesc);
        } else {
          this.imageEventText.updateDialogOption(5, OPTIONS[0] + OPTIONS[21]
              + POTIONS_PER_TIER * rewardMultiplier + OPTIONS[23] + loseDesc);
        }
      }
    }
  }

  static {
    eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    NAME = eventStrings.NAME;
    DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    OPTIONS = eventStrings.OPTIONS;
  }
}
