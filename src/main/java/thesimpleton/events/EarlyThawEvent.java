package thesimpleton.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.curse.Nettles;
import thesimpleton.cards.power.crop.Strawberries;
import thesimpleton.events.SimpletonEventHelper.EventState;
import thesimpleton.relics.WoodChipper;

import java.util.*;
import java.util.stream.Collectors;

public class EarlyThawEvent extends CustomSimpletonEvent
{
  public static final String ID = TheSimpletonMod.makeID("EarlyThawEvent");

  private static final String IMG_PATH = SimpletonEventHelper.getUiPath("earlythaw1");
  private static final EventStrings eventStrings;
  private static final String NAME;
  private static final String[] DESCRIPTIONS;
  private static final String[] OPTIONS;
  private static final int CURSE_CHANCE_PERCENTAGE = 50;

  private static final AbstractCard CURSE_CARD = new Nettles();
  private final AbstractCard REWARD_CARD = new Strawberries();
  private static final int HEAL_AMOUNT = 15;
  private final AbstractCard upgradableCard;

  private final AbstractRelic relicReward;

  private EventState state;

  // CODE ASSUMES THAT THESE ARE ALL COMMON (because the selected one is removed from the common relic pool)
  private final List<AbstractRelic> COMMON_RELIC_REWARDS = Arrays.asList(
      new HappyFlower(),
      new PreservedInsect(),
      new Strawberry(),
      new WoodChipper()
  );

  // CODE ASSUMES THAT THESE ARE ALL RARE (because the selected one is removed from the rare relic pool)
  private final List<AbstractRelic> RARE_RELIC_REWARDS = Arrays.asList(
      new Shovel(),
      new Turnip()
  );

  public EarlyThawEvent() {
    super(NAME, DESCRIPTIONS[0],  TheSimpletonMod.getImageResourcePath(IMG_PATH));
    REWARD_CARD.upgrade();
    upgradableCard = SimpletonEventHelper.getRandomUpgradeableCard();

    AbstractCard upgradedExampleCard = null;
    if (upgradableCard != null) {
      upgradedExampleCard = upgradableCard.makeStatEquivalentCopy();
      upgradedExampleCard.upgrade();
    }

    // TODO: Handle case where no upgradable cards
    List<AbstractRelic> relics = new ArrayList<>();
    relics.addAll(COMMON_RELIC_REWARDS.stream().filter(r ->  !AbstractDungeon.player.hasRelic(r.relicId)).collect(Collectors.toList()));
    relics.addAll(RARE_RELIC_REWARDS.stream().filter(r ->  !AbstractDungeon.player.hasRelic(r.relicId)).collect(Collectors.toList()));
    Collections.shuffle(relics);

    relicReward = relics.size() > 0
            ? relics.get(0)
            : AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.COMMON);

    if (upgradedExampleCard != null) {
      this.imageEventText.setDialogOption(OPTIONS[0] + upgradableCard + OPTIONS[2] + HEAL_AMOUNT + OPTIONS[3],
          upgradedExampleCard);
    } else {
      this.imageEventText.setDialogOption(OPTIONS[9], true);
    }

    if (relicReward.tier == AbstractRelic.RelicTier.RARE) {
      this.imageEventText.setDialogOption(OPTIONS[1]
              + OPTIONS[4] + CURSE_CHANCE_PERCENTAGE + OPTIONS[5] + CURSE_CARD
              + OPTIONS[6] + OPTIONS[7] + relicReward + OPTIONS[8],
              relicReward);
    } else {
      this.imageEventText.setDialogOption(OPTIONS[1] + OPTIONS[7] + relicReward + OPTIONS[8],
              relicReward);
    }

    this.state = EventState.WAITING;
    CardCrawlGame.sound.play("BIRD_TWEET_1");
  }

  @Override
  protected void buttonEffect(int buttonPressed) {
    switch (state) {
      case WAITING:
        switch (buttonPressed) {
          case 0:
            SimpletonEventHelper.upgradeCard(upgradableCard);
            AbstractDungeon.player.heal(HEAL_AMOUNT, true);
            break;

          case 1:
            final boolean receiveCurse = relicReward.tier == AbstractRelic.RelicTier.RARE
                    && AbstractDungeon.miscRng.randomBoolean(CURSE_CHANCE_PERCENTAGE / 100.0F);

            if (receiveCurse) {
              CardCrawlGame.sound.playA("OUCH_1", -0.1F);
              SimpletonEventHelper.gainCard(CURSE_CARD);
            }
            SimpletonEventHelper.receiveRelic(relicReward);
            break;

          default:
            break;
        }
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[11]);
        this.state = EventState.LEAVING;
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        break;
      case LEAVING:
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[11]);
        openMap();
        break;
    }
  }

  static {
    eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    NAME = eventStrings.NAME;
    DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    OPTIONS = eventStrings.OPTIONS;
  }
}
