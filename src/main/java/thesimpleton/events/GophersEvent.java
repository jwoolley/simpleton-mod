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
import java.util.stream.Stream;

public class GophersEvent extends CustomSimpletonEvent {
  public static final String ID = TheSimpletonMod.makeID("GophersEvent");

  private static final String IMG_PATH_STEP_1 = SimpletonEventHelper.getUiPath("gophers1");
  private static final String IMG_PATH_STEP_2 = SimpletonEventHelper.getUiPath("gophers2");
  private static final String IMG_PATH_STEP_3 = SimpletonEventHelper.getUiPath("gophers3");
  private static final String IMG_PATH_BITTEN = SimpletonEventHelper.getUiPath("gophers4");
  private static final String IMG_PATH_FINISHED = SimpletonEventHelper.getUiPath("gophers3");

  private static final EventStrings eventStrings;
  private static final String NAME;
  private static final String[] DESCRIPTIONS;
  private static final String[] OPTIONS;
  private final List<Integer> chosenOptions = new ArrayList<>();
  private final List<Integer> disabledOptions = new ArrayList<>();

  private EventState state;

  private final int[] FAIL_CHANCE_PERCENTAGES = { 25, 33, 50 };
  private final float[] HP_PERCENTAGE_MULTIPLIERS = { 1.0f, 1.5f, 2.0f };
  private static final int MAX_HP_PER_TIER = 5;
  private static final int GOLD_PER_TIER = 65;
  private static final int GOLD_MAX_EXTRA = 20;

  private static final int NUM_UPGRADE_CARDS = 2;
  private static final int NUM_POTIONS = 2;

  private static final float HP_PERCENT_LOST_PER_TIER = 0.11F;

  private int rewardCounter = 1;
  private final int goldBonus;

  List<String> rewardTexts = new ArrayList<>();

  public GophersEvent() {
    super(NAME, DESCRIPTIONS[0],  TheSimpletonMod.getResourcePath(IMG_PATH_STEP_1));
    // TODO: Handle case where no upgradable cards, etc.

    /*
     * OPTIONS
     * 1) Upgrade N random cards
     * 2) Gain N * X Max HP
     * 3) Gain N * X Gold
     * 4) Gain N random upgraded Rare cards
     * 5) Gain X random Potions
     */

    this.goldBonus = GOLD_PER_TIER + AbstractDungeon.eventRng.random(GOLD_MAX_EXTRA + 1);
    rewardTexts.addAll(Arrays.asList(
        OPTIONS[4] + NUM_UPGRADE_CARDS + OPTIONS[5],
        OPTIONS[6] + MAX_HP_PER_TIER + OPTIONS[7],
        OPTIONS[8] + goldBonus + OPTIONS[9],
        OPTIONS[10],
        OPTIONS[13] + NUM_POTIONS + OPTIONS[14]
    ));

    initializeOptions();
    this.state = EventState.WAITING;
    this.hasDialog = true;
    this.hasFocus = true;

    for (int i = 0; i < 50; i++) {
      TheSimpletonMod.logger.info("GophersEvent::constructor testing eventRng.random with limit 5: " +  AbstractDungeon.eventRng.random(5));
    }

    CardCrawlGame.sound.play("GOPHER_LAUGH_1");
  }

  private void initializeOptions() {
    final String penaltyText = getPenaltyText(0);
    this.imageEventText.setDialogOption(rewardTexts.get(0) + penaltyText);
    this.imageEventText.setDialogOption(rewardTexts.get(1) + penaltyText);
    this.imageEventText.setDialogOption(rewardTexts.get(2) + penaltyText);
    this.imageEventText.setDialogOption(rewardTexts.get(3) + penaltyText);
    this.imageEventText.setDialogOption(rewardTexts.get(4) + penaltyText);
    this.imageEventText.setDialogOption(OPTIONS[17]);
  }

  @Override
  protected void buttonEffect(int buttonPressed) {
    switch (state) {
      case WAITING:
        TheSimpletonMod.logger.info(">>> GophersEvent::buttonEffect selected option: " + buttonPressed);

        TheSimpletonMod.logger.info("GophersEvent::buttonEffect remaining availableOptions before: "
            + getAvailableOptions().stream().map(i -> i.toString()).collect(Collectors.joining(", ")));

        chosenOptions.add(buttonPressed);

        boolean bitten = AbstractDungeon.miscRng.randomBoolean(FAIL_CHANCE_PERCENTAGES[this.rewardCounter - 1] / 100.f);

        if (bitten || getAvailableOptions().isEmpty()) {
          this.state = EventState.LEAVING;
          AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
          this.imageEventText.clearAllDialogs();
          this.imageEventText.setDialogOption(OPTIONS[17]);
          if (bitten) {
            TheSimpletonMod.logger.info("GophersEvent::buttonEffect BITTEN");
            this.imageEventText.updateBodyText(OPTIONS[20]);
            this.imageEventText.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_BITTEN));
            CardCrawlGame.sound.play("OUCH_1");
          } else {
            TheSimpletonMod.logger.info("GophersEvent::buttonEffect FINISHED");
            this.imageEventText.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_FINISHED));
            this.imageEventText.updateBodyText(OPTIONS[21]);
          }
        } else if (buttonPressed != 5) {
          CardCrawlGame.sound.play("POP_SHORT_1");
          TheSimpletonMod.logger.info("GophersEvent::buttonEffect NOT_BITTEN. UPDATING DIALOG OPTIONS");
          final List<Integer> availableOptions = getAvailableOptions();
          final int disabledIndex = AbstractDungeon.eventRng.random(availableOptions.size() - 1);
          final int disabledOption = availableOptions.get(disabledIndex);

          TheSimpletonMod.logger.info("GophersEvent::buttonEffect disabling option" + disabledOption);
          disabledOptions.add(disabledOption);
          updateDialogOptions(rewardCounter);
          rewardCounter++;
          if (this.rewardCounter == 2) {
            this.imageEventText.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_STEP_2));
            this.imageEventText.updateBodyText(OPTIONS[18]);
          } else {
            this.imageEventText.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_STEP_3));
            this.imageEventText.updateBodyText(OPTIONS[19]);
          }
        }

        TheSimpletonMod.logger.info("GophersEvent::buttonEffect remaining chosenOptions: "
            + chosenOptions.stream().map(Object::toString).collect(Collectors.joining(", ")));
        TheSimpletonMod.logger.info("GophersEvent::buttonEffect remaining disabledOptions: "
            + disabledOptions.stream().map(Object::toString).collect(Collectors.joining(", ")));
        TheSimpletonMod.logger.info("GophersEvent::buttonEffect remaining availableOptions: "
            + getAvailableOptions().stream().map(Object::toString).collect(Collectors.joining(", ")));

        // TODO: keep rewards in an array (using lambdas?) and trigger the nth reward
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
            // TODO: change to choose upgraded rare card?
            break;

          case 5:
            executeLeaveOption();
            break;

          default:
            break;
        }
        break;
      case LEAVING:
        executeLeaveOption();
        break;
    }
  }

  private void executeLeaveOption() {
    this.state = EventState.LEAVING;
    this.imageEventText.clearAllDialogs();
    this.imageEventText.setDialogOption(OPTIONS[17]);
    openMap();
  }

  private String getOptionString(int optionIndex, String rewardText, int optionTier) {
    String optionPrefix = isAvailable(optionIndex) ? "" : (!isAlreadyChosen(optionIndex) ?  OPTIONS[15] : OPTIONS[16]);
    return optionPrefix + rewardText + (isAvailable(optionIndex) ? getPenaltyText(optionTier) : "");
  }

  private List<Integer> getAvailableOptions() {
    return Stream.of(0, 1, 2, 3, 4)
        .filter(o -> !chosenOptions.contains(o) && !disabledOptions.contains(o))
        .collect(Collectors.toList());
  }

  private boolean isAvailable(int optionIndex) {
    return getAvailableOptions().contains(optionIndex);
  }

  private boolean isAlreadyChosen(int optionIndex) {
    return chosenOptions.contains(optionIndex);
  }

  private String getPenaltyText(int optionTier) {
    final int FAIL_PCT = FAIL_CHANCE_PERCENTAGES[optionTier];

    final int HP_LOSS = Math.max ((int)(HP_PERCENT_LOST_PER_TIER * HP_PERCENTAGE_MULTIPLIERS[optionTier]
        * AbstractDungeon.player.maxHealth), 6);

    return OPTIONS[1] + FAIL_PCT + OPTIONS[2] + HP_LOSS + OPTIONS[3];
  }
  private void updateDialogOptions(int optionTier) {
    TheSimpletonMod.logger.info("GophersEvent::updateDialogOptions called");

    TheSimpletonMod.logger.info("GophersEvent::updateDialogOptions availableOptions: "
        + getAvailableOptions().stream().map(Object::toString).collect(Collectors.joining(", ")));

    TheSimpletonMod.logger.info("GophersEvent::updateDialogOptions chosenOptions: "
        + chosenOptions.stream().map(Object::toString).collect(Collectors.joining(", ")));

    TheSimpletonMod.logger.info("GophersEvent::updateDialogOptions disabledOptions: "
        + disabledOptions.stream().map(Object::toString).collect(Collectors.joining(", ")));

    for (int i = 0; i < 5; i++) {
      this.imageEventText.updateDialogOption(i, getOptionString(i,rewardTexts.get(i), optionTier), !isAvailable(i));
    }
  }

  static {
    eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    NAME = eventStrings.NAME;
    DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    OPTIONS = eventStrings.OPTIONS;
  }
}
