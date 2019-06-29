package thesimpleton.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.curse.Nettles;
import thesimpleton.cards.power.crop.Mushrooms;
import thesimpleton.events.SimpletonEventHelper;
import thesimpleton.events.SimpletonEventHelper.EventState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BorealisEvent extends AbstractImageEvent
{
  public static final String ID = TheSimpletonMod.makeID("EarlyThaw");

  private static final String IMG_PATH = getUiPath("earlythaw1");
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

    AbstractCard attackCard = CardHelper.hasCardType(CardType.ATTACK)
        ? CardHelper.returnCardOfType(CardType.ATTACK, AbstractDungeon.miscRng) : null;

    AbstractCard skillCard  = CardHelper.hasCardType(CardType.SKILL)
        ? CardHelper.returnCardOfType(AbstractCard.CardType.SKILL, AbstractDungeon.miscRng) : null;

    AbstractCard powerCard = CardHelper.hasCardType(CardType.POWER)
        ?  CardHelper.returnCardOfType(AbstractCard.CardType.POWER, AbstractDungeon.miscRng) : null;

    List<AbstractCard> candidateCards = Arrays.asList(attackCard, skillCard, powerCard).stream()
        .filter(c -> c != null).collect(Collectors.toList());

    surrenderCard = candidateCards.remove(AbstractDungeon.miscRng.random(candidateCards.size() - 1));

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
            upgradeRandomCardsAction();
            break;

          case 2:
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(CURSE_CARD.makeStatEquivalentCopy(),
                Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F - 20.0F * Settings.scale, Settings.HEIGHT / 2.0F));
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(REWARD_CARD.makeStatEquivalentCopy(),
                Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F + 20.0F * Settings.scale, Settings.HEIGHT / 2.0F));
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

  private void upgradeRandomCardsAction() {
    ArrayList<AbstractCard> upgradableCards = new ArrayList();

    for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
      if ((c.canUpgrade()) && (c.type == AbstractCard.CardType.ATTACK)) {
        upgradableCards.add(c);
      }
    }

    Collections.shuffle(upgradableCards, new java.util.Random(AbstractDungeon.miscRng.randomLong()));

    if (!upgradableCards.isEmpty()) {
      if (upgradableCards.size() == 1) {
        AbstractDungeon.effectList.add(new PurgeCardEffect(surrenderCard));
        AbstractDungeon.player.masterDeck.removeCard(surrenderCard);
        upgradableCards.get(0).upgrade();
        AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(0));
        AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(
            upgradableCards.get(0).makeStatEquivalentCopy()));
        AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
      } else {
        upgradableCards.get(0).upgrade();
        upgradableCards.get(1).upgrade();
        AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(0));
        AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(1));

        AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(
            upgradableCards.get(0).makeStatEquivalentCopy(),
            Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F - 20.0F * Settings.scale, Settings.HEIGHT / 2.0F));
        AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(
            upgradableCards.get(1).makeStatEquivalentCopy(),
            Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F + 20.0F * Settings.scale, Settings.HEIGHT / 2.0F));
        AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
      }
    }
  }

  private static String getUiPath(String id) {
    return "events/" + id + ".png";
  }

  static {
    eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    NAME = eventStrings.NAME;
    DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    OPTIONS = eventStrings.OPTIONS;
  }
}
