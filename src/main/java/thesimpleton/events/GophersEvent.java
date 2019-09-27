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
  private static final EventStrings eventStrings;
  private static final String NAME;
  private static final String[] DESCRIPTIONS;
  private static final String[] OPTIONS;
  private static final List<Integer> chosenOptions = new ArrayList();
  private static final List<Integer> disabledOptions = new ArrayList();

  private static AbstractRelic RELIC_REWARD = new PotionBelt();

  private EventState state;

  private final int[] FAIL_CHANCE_PERCENTAGES = { 25, 33, 50 };
  private static final int MAX_HP_PER_TIER = 5;
  private static final int GOLD_PER_TIER = 65;
  private static final int GOLD_MAX_EXTRA = 20;

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
     * 6) Gain X (potion slots and) random Potions
     */
    final int FAIL_PCT = FAIL_CHANCE_PERCENTAGES[this.rewardCounter - 1];
    final int HP_LOSS =  Math.max ((int)(HP_PERCENT_LOST_PER_TIER * this.rewardCounter * AbstractDungeon.player.maxHealth), 6);
    final String loseDesc = OPTIONS[2] + FAIL_PCT + OPTIONS[3] + OPTIONS[4] + HP_LOSS + OPTIONS[5];

    if (this.rewardCounter == 1) {
      this.imageEventText.setDialogOption(OPTIONS[0] + OPTIONS[6] + OPTIONS[7] + loseDesc);
      this.imageEventText.setDialogOption(OPTIONS[0] + OPTIONS[9] + (this.rewardCounter + MAX_HP_PER_TIER) + OPTIONS[10] + loseDesc);
//      this.imageEventText.setDialogOption(OPTIONS[0] + OPTIONS[11] + OPTIONS[12] + loseDesc);
      this.imageEventText.setDialogOption(OPTIONS[0] + OPTIONS[14] + (this.rewardCounter + goldBonus) + OPTIONS[15] + loseDesc);
      this.imageEventText.setDialogOption(OPTIONS[0] + OPTIONS[16] + OPTIONS[17] + loseDesc);
      this.imageEventText.setDialogOption(OPTIONS[0] + (!AbstractDungeon.player.hasRelic(RELIC_REWARD.relicId)
            ? OPTIONS[19] + SimpletonUtil.tagString(RELIC_REWARD.name, "#g") + OPTIONS[20] : "")
            + OPTIONS[21] + OPTIONS[22] + loseDesc);
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
        this.imageEventText.updateDialogOption(1,OPTIONS[0] + OPTIONS[9] + this.rewardCounter + MAX_HP_PER_TIER + OPTIONS[10] + loseDesc);
      }

      if (!chosenOptions.contains(2) && !disabledOptions.contains(2)) {
        this.imageEventText.updateDialogOption(2,OPTIONS[0] + OPTIONS[11] + this.rewardCounter + OPTIONS[13] + loseDesc);
      }

      if (!chosenOptions.contains(3) && !disabledOptions.contains(3)) {
        this.imageEventText.updateDialogOption(3,OPTIONS[0] + OPTIONS[14] + this.rewardCounter + goldBonus + OPTIONS[15] + loseDesc);
      }

      if (!chosenOptions.contains(4) && !disabledOptions.contains(4)) {
        this.imageEventText.updateDialogOption(4,OPTIONS[0] + OPTIONS[16] + this.rewardCounter + OPTIONS[18] + loseDesc);
      }

      if (!chosenOptions.contains(5) && !disabledOptions.contains(5)) {
        this.imageEventText.updateDialogOption(5, OPTIONS[0]
            + (!AbstractDungeon.player.hasRelic(RELIC_REWARD.relicId)
            ? OPTIONS[19] + SimpletonUtil.tagString(RELIC_REWARD.name, "#g") + OPTIONS[20] : "")
            + OPTIONS[21] + this.rewardCounter + OPTIONS[23] + loseDesc);
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
