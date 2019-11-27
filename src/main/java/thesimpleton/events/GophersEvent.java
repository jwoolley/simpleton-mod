package thesimpleton.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
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
  private static final String IMG_PATH_FINISHED = SimpletonEventHelper.getUiPath("gophers5");

  private static final EventStrings eventStrings;
  private static final String NAME;
  private static final String[] DESCRIPTIONS;
  private static final String[] OPTIONS;
  private final List<Integer> chosenOptions = new ArrayList<>();
  private final List<Integer> disabledOptions = new ArrayList<>();
  private final List<String> rewardTexts = new ArrayList<>();

  private EventState state;

  private final int[] FAIL_CHANCE_PERCENTAGES = { 33, 50, 66 };
  private final float[] HP_PERCENTAGE_MULTIPLIERS = { 1.0f, 1.5f, 2.0f };

  private static final int MAX_HP_INCREASE = 5;
  private static final int GOLD_PER_TIER = 65;
  private static final int GOLD_MAX_EXTRA = 20;
  private static final int NUM_UPGRADE_CARDS = 2;
  private static final int NUM_POTIONS = 2;
  private static final float HP_PERCENT_LOST_PER_TIER = 0.11F;
  private static final int MIN_HP_LOSS = 6;

  private final int goldBonus;
  private final AbstractCard rareCardReward;

  private int rewardCounter = 1;

  public GophersEvent() {
    super(NAME, DESCRIPTIONS[0],  TheSimpletonMod.getImageResourcePath(IMG_PATH_STEP_1));
    // TODO: Handle case where no upgradable cards, etc.

    this.goldBonus = GOLD_PER_TIER + AbstractDungeon.eventRng.random(GOLD_MAX_EXTRA + 1);
    rewardTexts.addAll(Arrays.asList(
        OPTIONS[4] + NUM_UPGRADE_CARDS + OPTIONS[5],
        OPTIONS[6] + MAX_HP_INCREASE + OPTIONS[7],
        OPTIONS[8] + goldBonus + OPTIONS[9],
        OPTIONS[10],
        OPTIONS[13] + NUM_POTIONS + OPTIONS[14]
    ));

    initializeOptions();
    this.state = EventState.WAITING;
    this.hasDialog = true;
    this.hasFocus = true;

    rareCardReward = SimpletonEventHelper.getRandomRareCardFromPool();
    rareCardReward.upgrade();

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
    this.noCardsInRewards = false;
    switch (state) {
      case WAITING:
        chosenOptions.add(buttonPressed);

        boolean bitten = AbstractDungeon.miscRng.randomBoolean(FAIL_CHANCE_PERCENTAGES[this.rewardCounter - 1] / 100.f);

        if (bitten || getAvailableOptions().isEmpty()) {
          this.state = EventState.LEAVING;
          AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
          this.imageEventText.clearAllDialogs();
          this.imageEventText.setDialogOption(OPTIONS[17]);
          if (bitten) {
            this.imageEventText.updateBodyText(OPTIONS[20]);
            this.imageEventText.loadImage(TheSimpletonMod.getImageResourcePath(IMG_PATH_BITTEN));
            CardCrawlGame.sound.play("CHOMP_SHORT_1");
            CardCrawlGame.sound.play("OUCH_1");

            AbstractDungeon.player.damage(
                new DamageInfo(AbstractDungeon.player, getBiteDamage(rewardCounter - 1)));
          } else {
            this.imageEventText.loadImage(TheSimpletonMod.getImageResourcePath(IMG_PATH_FINISHED));
            this.imageEventText.updateBodyText(OPTIONS[21]);
          }
        } else if (buttonPressed != 5) {
          CardCrawlGame.sound.play("POP_SHORT_1");
          final List<Integer> availableOptions = getAvailableOptions();
          final int disabledIndex = AbstractDungeon.eventRng.random(availableOptions.size() - 1);
          final int disabledOption = availableOptions.get(disabledIndex);

          disabledOptions.add(disabledOption);
          updateDialogOptions(rewardCounter);
          rewardCounter++;
          if (this.rewardCounter == 2) {
            this.imageEventText.loadImage(TheSimpletonMod.getImageResourcePath(IMG_PATH_STEP_2));
            this.imageEventText.updateBodyText(OPTIONS[18]);
          } else {
            this.imageEventText.loadImage(TheSimpletonMod.getImageResourcePath(IMG_PATH_STEP_3));
            this.imageEventText.updateBodyText(OPTIONS[19]);
          }
        }

        /*
         * REWARDS
         * 1) Upgrade N random cards
         * 2) Gain N Max HP
         * 3) Gain N Gold
         * 4) Gain a random upgraded Rare card
         * 5) Gain N random Potions
         */
        switch (buttonPressed) {
          case 0:
            SimpletonEventHelper.upgradeCards(SimpletonEventHelper.getRandomUpgradableCards(2));
            break;
          case 1:
            AbstractDungeon.player.increaseMaxHp(MAX_HP_INCREASE, true);
            break;
          case 2:
            CardCrawlGame.sound.play("GOLD_GAIN");
            AbstractDungeon.player.gainGold(this.goldBonus);
            break;
          case 3:
            SimpletonEventHelper.gainCard(rareCardReward);
            break;
          case 4:
            this.noCardsInRewards = true;
            SimpletonEventHelper.receivePotionRewards(NUM_POTIONS);
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

  private int getBiteDamage(int optionTier) {
    return Math.max ((int)(HP_PERCENT_LOST_PER_TIER * HP_PERCENTAGE_MULTIPLIERS[optionTier]
        * AbstractDungeon.player.maxHealth), MIN_HP_LOSS);
  }

  private String getPenaltyText(int optionTier) {
    final int FAIL_PCT = FAIL_CHANCE_PERCENTAGES[optionTier];
    return OPTIONS[1] + FAIL_PCT + OPTIONS[2] + getBiteDamage(optionTier) + OPTIONS[3];
  }

  private void updateDialogOptions(int optionTier) {
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
