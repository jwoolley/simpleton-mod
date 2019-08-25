package thesimpleton.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.skill.ControlledBurn;
import thesimpleton.relics.HeatStroke;

public class HeatWaveEvent extends AbstractImageEvent {
  public static final String ID = TheSimpletonMod.makeID("HeatWaveEvent");

  private static final String IMG_PATH = SimpletonEventHelper.getUiPath("heatwave1");
  private static final EventStrings eventStrings;
  private static final String NAME;
  private static final String[] DESCRIPTIONS;
  private static final String[] OPTIONS;

  private final AbstractCard REWARD_CARD = new ControlledBurn();
  private static final int HEAL_AMOUNT = 10;
  private static final int BURNING_AMOUNT_1 = 10;
  private static final int BURNING_AMOUNT_2 = 20;

  private final AbstractPotion surrenderPotion;
  private final AbstractCard surrenderCard;
  private boolean hasPotion;

  private SimpletonEventHelper.EventState state;

  public HeatWaveEvent() {
    super(NAME, DESCRIPTIONS[0], TheSimpletonMod.getResourcePath(IMG_PATH));

    surrenderCard = SimpletonEventHelper.getRandomUpgradeableCard();
    REWARD_CARD.upgrade();

    if (AbstractDungeon.player.hasAnyPotions()) {
      surrenderPotion = AbstractDungeon.player.getRandomPotion();
      hasPotion = true;
    } else {
      surrenderPotion = null;
      hasPotion = false;
    }

    if (hasPotion) {
      this.imageEventText.setDialogOption(OPTIONS[0] + surrenderPotion.name + OPTIONS[3] + HEAL_AMOUNT + OPTIONS[4]);
    } else {
      this.imageEventText.setDialogOption(OPTIONS[7], true);
    }
    this.imageEventText.setDialogOption(OPTIONS[1] + surrenderCard.name + OPTIONS[5] + BURNING_AMOUNT_1 + OPTIONS[6]);
    this.imageEventText.setDialogOption(OPTIONS[2] + REWARD_CARD.name + OPTIONS[5] + BURNING_AMOUNT_2 + OPTIONS[6],
        REWARD_CARD);

    this.state = SimpletonEventHelper.EventState.WAITING;

    long soundId = CardCrawlGame.sound.play("LOW_RUMBLE_1");
    CardCrawlGame.sound.fadeOut("LOW_RUMBLE_1", soundId);
  }

  @Override
  protected void buttonEffect(int buttonPressed) {
    switch (state) {
      case WAITING:
        switch (buttonPressed) {
          case 0:
            losePotionOption();
            break;

          case 1:
            loseGearOption();
            break;

          case 2:
            cardRewardOption();
            break;

          default:
            break;
        }
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[8]);
        this.state = SimpletonEventHelper.EventState.LEAVING;
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        break;
      case LEAVING:
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[8]);
        openMap();
        break;
    }
  }

  private void losePotionOption() {
    CardCrawlGame.sound.play("POTION_3");
    AbstractDungeon.player.removePotion(this.surrenderPotion);
    AbstractDungeon.player.heal(HEAL_AMOUNT, true);
  }

  private void loseGearOption() {
    CardCrawlGame.sound.play("APPEAR");
    AbstractDungeon.effectList.add(new PurgeCardEffect(this.surrenderCard));
    AbstractDungeon.player.masterDeck.removeCard(this.surrenderCard);
    SimpletonEventHelper.receiveRelic(new HeatStroke(BURNING_AMOUNT_1));
  }

  private void cardRewardOption() {
    CardCrawlGame.sound.play("ATTACK_FIRE");
    SimpletonEventHelper.gainCard(REWARD_CARD);
    SimpletonEventHelper.receiveRelic(new HeatStroke(BURNING_AMOUNT_2));
  }

  static {
    eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    NAME = eventStrings.NAME;
    DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    OPTIONS = eventStrings.OPTIONS;
  }
}