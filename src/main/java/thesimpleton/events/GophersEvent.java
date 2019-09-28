package thesimpleton.events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PotionBelt;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thesimpleton.TheSimpletonMod;
import thesimpleton.events.SimpletonEventHelper.EventState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GophersEvent extends CustomSimpletonEvent {
  public static final String ID = TheSimpletonMod.makeID("GophersEvent");

  private static final String IMG_PATH = SimpletonEventHelper.getUiPath("gophers1");
  private static final String IMG_PATH_STEP_2 = SimpletonEventHelper.getUiPath("gophers2");
  private static final String IMG_PATH_STEP_3 = SimpletonEventHelper.getUiPath("gophers3");
  private static final String IMG_PATH_BITTEN = SimpletonEventHelper.getUiPath("gophers4");

  private static final EventStrings eventStrings;
  private static final String NAME;
  private static final String[] DESCRIPTIONS;
  private static final String[] OPTIONS;
  private final List<Integer> availableOptions = new ArrayList<>();
  private final List<Integer> chosenOptions = new ArrayList<>();
  private final List<Integer> disabledOptions = new ArrayList<>();

  private static AbstractRelic RELIC_REWARD = new PotionBelt();

  public static enum CurScreen {
    INTRO,  CHOSE_1,  CHOSE_2, BITTEN, LEAVE;
    private CurScreen() {}
  }
  private CurScreen screen;
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

    this.hasDialog = true;
    this.hasFocus = true;
    this.screen = CurScreen.INTRO;
  }

  @Override
  protected void buttonEffect(int buttonPressed) {
    switch (state) {
      case WAITING:
        TheSimpletonMod.logger.info("GophersEvent::buttonEffect removing option: " + buttonPressed);

        TheSimpletonMod.logger.info("GophersEvent::buttonEffect remaining availableOptions before: "
            + availableOptions.stream().map(i -> i.toString()).collect(Collectors.joining(", ")));


        availableOptions.remove(buttonPressed);
        chosenOptions.add(buttonPressed);

        boolean bitten = AbstractDungeon.miscRng.randomBoolean(FAIL_CHANCE_PERCENTAGES[this.rewardCounter - 1] / 100.f);

        // TODO: update event text
        this.imageEventText.clearAllDialogs();

        if (bitten || availableOptions.isEmpty()) {
          this.imageEventText.setDialogOption(OPTIONS[25]);
          this.state = EventState.LEAVING;
          AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
          if (bitten) {
            TheSimpletonMod.logger.info("GophersEvent::buttonEffect BITTEN");

            this.imageEventText.updateBodyText(OPTIONS[28]);
            this.imageEventText.loadImage(IMG_PATH_BITTEN);
            CardCrawlGame.sound.play("OUCH_1");
            this.screen = CurScreen.BITTEN;
          } else {
            TheSimpletonMod.logger.info("GophersEvent::buttonEffect FINISHED");
            this.imageEventText.updateBodyText(OPTIONS[29]);
            this.screen = CurScreen.LEAVE;
          }
        } else {
          CardCrawlGame.sound.play("POP_SHORT_1");
          TheSimpletonMod.logger.info("GophersEvent::buttonEffect NOT_BITTEN. UPDATING DIALOG OPTIONS");
          rewardCounter++;
          updateDialogOptions();
          if (this.rewardCounter == 1) {
            this.imageEventText.loadImage(IMG_PATH_STEP_2);
            this.imageEventText.updateBodyText(OPTIONS[26]);
            this.screen = CurScreen.CHOSE_1;
          } else {
            this.imageEventText.loadImage(IMG_PATH_STEP_3);
            this.imageEventText.updateBodyText(OPTIONS[27]);
            this.screen = CurScreen.CHOSE_2;
          }
          final int removedOption = AbstractDungeon.eventRng.random(availableOptions.size() - 1);
          availableOptions.remove(removedOption);
          disabledOptions.add(removedOption);
          TheSimpletonMod.logger.info("GophersEvent::buttonEffect removing option" + removedOption);
        }

        TheSimpletonMod.logger.info("GophersEvent::buttonEffect remaining chosenOptions: "
            + chosenOptions.stream().map(i -> i.toString()).collect(Collectors.joining(", ")));
        TheSimpletonMod.logger.info("GophersEvent::buttonEffect remaining disabledOptions: "
            + disabledOptions.stream().map(i -> i.toString()).collect(Collectors.joining(", ")));
        TheSimpletonMod.logger.info("GophersEvent::buttonEffect remaining availableOptions: "
            + availableOptions.stream().map(i -> i.toString()).collect(Collectors.joining(", ")));

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

    TheSimpletonMod.logger.info("GophersEvent::updateDialogOptions called");

    final int FAIL_PCT = FAIL_CHANCE_PERCENTAGES[this.rewardCounter - 1];
    final int HP_LOSS =  Math.max ((int)(HP_PERCENT_LOST_PER_TIER * HP_PERCENTAGE_MULTIPLIERS[this.rewardCounter - 1]
        * AbstractDungeon.player.maxHealth), 6);
    final String loseDesc = OPTIONS[2] + FAIL_PCT + OPTIONS[3] + OPTIONS[4] + HP_LOSS + OPTIONS[5];

    final int rewardMultiplier = 1; // rewardMultiplier = this.rewardCounter;

    TheSimpletonMod.logger.info("GophersEvent::updateDialogOptions availableOptions: "
        + availableOptions.stream().map(i -> i.toString()).collect(Collectors.joining(", ")));

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

      if (availableOptions.contains(1)) {
        if (rewardMultiplier == 1) {
          this.imageEventText.updateDialogOption(1, OPTIONS[0] + OPTIONS[6] + OPTIONS[7] + loseDesc);
        } else {
          this.imageEventText.updateDialogOption(1, OPTIONS[0] + OPTIONS[9] + rewardMultiplier + MAX_HP_PER_TIER + OPTIONS[10] + loseDesc);
        }
      }

      if (availableOptions.contains(2)) {
        if (rewardMultiplier == 1) {
          this.imageEventText.updateDialogOption(2,OPTIONS[0] + OPTIONS[9] + (rewardMultiplier + MAX_HP_PER_TIER) + OPTIONS[10] + loseDesc);
        } else {
          this.imageEventText.updateDialogOption(2, OPTIONS[0] + OPTIONS[11] + rewardMultiplier + OPTIONS[13] + loseDesc);
        }
      }

      if (availableOptions.contains(3)) {
        if (rewardMultiplier == 1) {
          this.imageEventText.updateDialogOption(3,OPTIONS[0] + OPTIONS[14] + (rewardMultiplier + goldBonus) + OPTIONS[15] + loseDesc);
        } else {
          this.imageEventText.updateDialogOption(3, OPTIONS[0] + OPTIONS[14] + rewardMultiplier + goldBonus + OPTIONS[15] + loseDesc);
        }
      }

      if (availableOptions.contains(4)) {
        if (rewardMultiplier == 1) {
          this.imageEventText.updateDialogOption(4,OPTIONS[0] + OPTIONS[16] + OPTIONS[17] + loseDesc);
        } else {
          this.imageEventText.updateDialogOption(4, OPTIONS[0] + OPTIONS[16] + rewardMultiplier + OPTIONS[18] + loseDesc);
        }
      }

      if (availableOptions.contains(5)) {
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
