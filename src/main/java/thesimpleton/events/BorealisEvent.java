package thesimpleton.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.Apotheosis;
import com.megacrit.cardcrawl.cards.colorless.HandOfGreed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PrismaticShard;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.curse.Frostbite;
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

  private static final int HP_COST = 10;
  private static final int NUM_CARDS_UPGRADED = 2;

  private final AbstractCard rewardCard1;
  private final AbstractCard rewardCard2;
  private final AbstractCard surrenderCard;
  private final long rumbleSoundId;

  private final AbstractRelic rewardRelic;

  private EventState state;

  public BorealisEvent() {
    super(NAME, DESCRIPTIONS[0],  TheSimpletonMod.getResourcePath(IMG_PATH));
    surrenderCard = SimpletonEventHelper.getRandomNonCurseCardFromDeck();

    rewardCard1 = CardLibrary.getAnyColorCard(AbstractCard.CardRarity.RARE);
    rewardCard2 = CardLibrary.getAnyColorCard(AbstractCard.CardRarity.RARE);
    rewardRelic = new PrismaticShard();

    this.imageEventText.setDialogOption(OPTIONS[0] + surrenderCard + OPTIONS[3]);
    this.imageEventText.setDialogOption(OPTIONS[1] + HP_COST + OPTIONS[4] + NUM_CARDS_UPGRADED + OPTIONS[5]);
    this.imageEventText.setDialogOption(OPTIONS[2], rewardRelic);

    this.state = EventState.WAITING;
    rumbleSoundId = CardCrawlGame.sound.play("GRADUAL_RUMBLE_1");
  }

  @Override
  protected void buttonEffect(int buttonPressed) {
    switch (state) {
      case WAITING:
        switch (buttonPressed) {

          case 0:
            AbstractDungeon.effectList.add(new PurgeCardEffect(this.surrenderCard));
            AbstractDungeon.player.masterDeck.removeCard(this.surrenderCard);
            SimpletonEventHelper.receiveRelic(AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.COMMON));
            break;

          case 1:
            AbstractDungeon.player.damage(new DamageInfo(null, HP_COST, DamageInfo.DamageType.HP_LOSS));
            CardCrawlGame.sound.play("DEBUFF_2");
            SimpletonEventHelper.upgradeCards(SimpletonEventHelper.getRandomUpgradableCards(2));
            break;

          case 2:
            CardCrawlGame.sound.play("EVENT_ANCIENT");
            SimpletonEventHelper.receiveRelic(rewardRelic);
            SimpletonEventHelper.gainCards(rewardCard1, rewardCard2);
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
        CardCrawlGame.sound.stop("GRADUAL_RUMBLE_1");
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
