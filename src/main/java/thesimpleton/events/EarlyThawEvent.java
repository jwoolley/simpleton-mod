package thesimpleton.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.curse.Nettles;
import thesimpleton.cards.power.crop.Strawberries;
import thesimpleton.events.SimpletonEventHelper.EventState;

public class EarlyThawEvent extends CustomSimpletonEvent implements CustomSimpletonOnlyEvent
{
  public static final String ID = TheSimpletonMod.makeID("EarlyThawEvent");

  private static final String IMG_PATH = SimpletonEventHelper.getUiPath("earlythaw1");
  private static final EventStrings eventStrings;
  private static final String NAME;
  private static final String[] DESCRIPTIONS;
  private static final String[] OPTIONS;

  private static final AbstractCard CURSE_CARD = new Nettles();
  private final AbstractCard REWARD_CARD = new Strawberries();
  private static final int HEAL_AMOUNT = 10;
  private final AbstractCard upgradableCard;

  private EventState state;

  public EarlyThawEvent() {
    super(NAME, DESCRIPTIONS[0],  TheSimpletonMod.getResourcePath(IMG_PATH));

    upgradableCard = SimpletonEventHelper.getRandomUpgradeableCard();
    // TODO: Handle case where no upgradable cards

    this.imageEventText.setDialogOption(OPTIONS[0] + upgradableCard + OPTIONS[2] + HEAL_AMOUNT + OPTIONS[3]);
    this.imageEventText.setDialogOption(OPTIONS[1] + CURSE_CARD + OPTIONS[4] + REWARD_CARD + OPTIONS[5], CURSE_CARD);

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
            SimpletonEventHelper.gainCards(CURSE_CARD, REWARD_CARD);
            break;

          default:
            break;
        }
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[6]);
        this.state = EventState.LEAVING;
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        break;
      case LEAVING:
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[6]);
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
