package thesimpleton.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.HappyFlower;
import com.megacrit.cardcrawl.relics.PreservedInsect;
import com.megacrit.cardcrawl.relics.Strawberry;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.curse.Nettles;
import thesimpleton.cards.power.crop.Strawberries;
import thesimpleton.events.SimpletonEventHelper.EventState;
import thesimpleton.relics.WoodChipper;

import java.util.*;

public class EarlyThawEvent extends CustomSimpletonEvent
{
  public static final String ID = TheSimpletonMod.makeID("EarlyThawEvent");

  private static final String IMG_PATH = SimpletonEventHelper.getUiPath("earlythaw1");
  private static final EventStrings eventStrings;
  private static final String NAME;
  private static final String[] DESCRIPTIONS;
  private static final String[] OPTIONS;
  private static final int CURSE_CHANCE_PERCENTAGE = 33;

  private static final AbstractCard CURSE_CARD = new Nettles();
  private final AbstractCard REWARD_CARD = new Strawberries();
  private static final int HEAL_AMOUNT = 10;
  private final AbstractCard upgradableCard;

  private final AbstractRelic relicReward;

  private EventState state;

  // CODE ASSUMES THAT THESE ARE ALL COMMON (because selected one is removed from common relic pool)
  private final List<AbstractRelic> RELIC_REWARDS = Arrays.asList(
      new HappyFlower(),
      new PreservedInsect(),
      new Strawberry(),
      new WoodChipper()
  );

  public EarlyThawEvent() {
    super(NAME, DESCRIPTIONS[0],  TheSimpletonMod.getResourcePath(IMG_PATH));
    REWARD_CARD.upgrade();
    upgradableCard = SimpletonEventHelper.getRandomUpgradeableCard();

    final AbstractCard upgradedExampleCard = upgradableCard.makeStatEquivalentCopy();
    upgradedExampleCard.upgrade();

    // TODO: Handle case where no upgradable cards

    if (!TheSimpletonMod.isPlayingAsSimpleton()) {
      final List<AbstractRelic> relics = new ArrayList<>(RELIC_REWARDS);
      Collections.shuffle(relics);

      Optional<AbstractRelic> relicOptional
          = relics.stream().filter(r -> !AbstractDungeon.player.hasRelic(r.relicId)).findFirst();

      relicReward = relicOptional.isPresent() ? relicOptional.get() :
          AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.COMMON);
    } else {
      relicReward = null;
    }

    this.imageEventText.setDialogOption(OPTIONS[0] + upgradableCard + OPTIONS[2] + HEAL_AMOUNT + OPTIONS[3],
        upgradedExampleCard);

    this.imageEventText.setDialogOption(OPTIONS[1] + CURSE_CHANCE_PERCENTAGE + OPTIONS[4] + CURSE_CARD + OPTIONS[5]
        + (relicReward == null ? REWARD_CARD : relicReward) + OPTIONS[6], CURSE_CARD, relicReward);

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
            final boolean receiveCurse = AbstractDungeon.miscRng.randomBoolean(CURSE_CHANCE_PERCENTAGE / 100.0F);

            if (receiveCurse) {
              CardCrawlGame.sound.play("OUCH_1");

              if (relicReward != null) {
                SimpletonEventHelper.receiveRelic(relicReward);
                SimpletonEventHelper.gainCard(CURSE_CARD);
              } else {
                SimpletonEventHelper.gainCards(CURSE_CARD, REWARD_CARD);
              }
            } else {
              if (relicReward != null) {
                SimpletonEventHelper.receiveRelic(relicReward);
              } else {
                SimpletonEventHelper.gainCard(REWARD_CARD);
              }
            }
            break;

          default:
            break;
        }
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[7]);
        this.state = EventState.LEAVING;
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        break;
      case LEAVING:
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[7]);
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
