package thesimpleton.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.curse.Nettles;
import thesimpleton.cards.power.crop.Mushrooms;
import thesimpleton.events.SimpletonEventHelper.EventState;

public class BorealisEvent extends AbstractImageEvent
{
  public static final String ID = TheSimpletonMod.makeID("BorealisEvent");

  private static final String IMG_PATH =  SimpletonEventHelper.getUiPath("borealis1");
  private static final EventStrings eventStrings;
  private static final String NAME;
  private static final String[] DESCRIPTIONS;
  private static final String[] OPTIONS;

  private static final AbstractCard REWARD_CARD = new Mushrooms();
  private static final AbstractCard CURSE_CARD = new Nettles();
  private static final int HP_COST = 10;
  private static final int NUM_CARDS_UPGRADED = 2;

  private final AbstractCard surrenderCard;

  private EventState state;

  public BorealisEvent() {
    super(NAME, DESCRIPTIONS[0],  TheSimpletonMod.getResourcePath(IMG_PATH));
    REWARD_CARD.upgrade();
    surrenderCard = SimpletonEventHelper.getRandomNonCurseCardFromDeck();
    this.imageEventText.setDialogOption(OPTIONS[0] + surrenderCard + OPTIONS[3]);
    this.imageEventText.setDialogOption(OPTIONS[1] + HP_COST + OPTIONS[4] + NUM_CARDS_UPGRADED + OPTIONS[5]);
    this.imageEventText.setDialogOption(OPTIONS[2] + CURSE_CARD.name + OPTIONS[6] + REWARD_CARD.name + OPTIONS[7]);

    this.state = EventState.WAITING;
  }

  @Override
  protected void buttonEffect(int buttonPressed) {
    switch (state) {
      case WAITING:
        switch (buttonPressed) {

          case 0:
            AbstractDungeon.effectList.add(new PurgeCardEffect(this.surrenderCard));
            AbstractDungeon.player.masterDeck.removeCard(this.surrenderCard);
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH * 0.28F, Settings.HEIGHT / 2.0F,
                AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.COMMON));
            break;

          case 1:
            AbstractDungeon.player.damage(new DamageInfo(null, HP_COST, DamageInfo.DamageType.HP_LOSS));
            CardCrawlGame.sound.play("DEBUFF_2");
            SimpletonEventHelper.upgradeCards(SimpletonEventHelper.getRandomUpgradableCards(2));
            break;

          case 2:
            CardCrawlGame.sound.play("EVENT_ANCIENT");
            SimpletonEventHelper.gainCards(CURSE_CARD, REWARD_CARD);
            break;

          default:
            break;
        }
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[8]);
        this.state = EventState.LEAVING;
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        break;
      case LEAVING:
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[8]);
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
