package thesimpleton.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.curse.Gnats;
import thesimpleton.patches.ui.GenericEventDialogSecondPreviewCard;
import thesimpleton.relics.bottledcard.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FirefliesEvent extends CustomSimpletonEvent {
  public static final String ID = TheSimpletonMod.makeID("FirefliesEvent");
  private static final String IMG_PATH = SimpletonEventHelper.getUiPath("fireflies1");
  private static final EventStrings eventStrings;
  private static final String NAME;
  private static final String[] DESCRIPTIONS;
  private static final String[] OPTIONS;
  private static final AbstractCard CURSE_GNATS = new Gnats();

  private final AbstractCardInJarRelic relicReward;
  private final AbstractPotion potionReward;
  private final AbstractCard curseCard;

  private SimpletonEventHelper.EventState state;

  public FirefliesEvent() {
    super(NAME, DESCRIPTIONS[0],  TheSimpletonMod.getImageResourcePath(IMG_PATH));

    final AbstractPlayer player = AbstractDungeon.player;

    potionReward = PotionHelper.getRandomPotion();
    curseCard = CURSE_GNATS;

    List<AbstractCardInJarRelic> possibleJarRelics
            = getPossibleJarRelics().stream().filter(r ->  !player.hasRelic(r.relicId)).collect(Collectors.toList());

    boolean canPlayerGainBottleRelic = possibleJarRelics.size() > 0;

    Collections.shuffle(possibleJarRelics);
    relicReward = canPlayerGainBottleRelic ? possibleJarRelics.get(0) : null;

    this.imageEventText.setDialogOption(OPTIONS[0] + potionReward.name + OPTIONS[2]);

    if (canPlayerGainBottleRelic) {
      this.imageEventText.setDialogOption(OPTIONS[1] + relicReward.name + OPTIONS[4] + curseCard.name + OPTIONS[2],
              curseCard, relicReward);

      GenericEventDialogSecondPreviewCard.setSecondPreviewCardOnLastOption( this.imageEventText, relicReward.getCardInJar());
    }

    this.state = SimpletonEventHelper.EventState.WAITING;
    CardCrawlGame.sound.play("MAGIC_CHIMES_1");
  }

  @Override
  protected void buttonEffect(int buttonPressed) {
    switch (state) {
      case WAITING:
        switch (buttonPressed) {

          case 0:
            CardCrawlGame.sound.play("POTION_3");
            AbstractDungeon.player.obtainPotion(this.potionReward);
            break;

          case 1:
            CardCrawlGame.sound.playA("ATTACK_BEE_BUZZ_1", 1.5F);
            TheSimpletonMod.traceLogger.trace("TheSimpletonMod::FirefliesEvent receiving curse");
            SimpletonEventHelper.gainCard(curseCard);
            SimpletonEventHelper.receiveRelic(relicReward);
            break;

          default:
            break;
        }
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[5]);
        this.state = SimpletonEventHelper.EventState.LEAVING;
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        break;
      case LEAVING:
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[5]);
        openMap();
        break;
    }
  }

  private List<AbstractCardInJarRelic> getPossibleJarRelics() {
    return Arrays.asList(
      new CardInJarApotheosis(),
      new CardInJarChrysalis(),
      new CardInJarDiscovery(),
      new CardInJarEnlightenment(),
      new CardInJarMadness(),
      new CardInJarMayhem(),
      new CardInJarMetamorphosis(),
      new CardInJarTheBomb(),
      new CardInJarTransmutation(),
      new CardInJarViolence()
    );
  }

  static {
    eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    NAME = eventStrings.NAME;
    DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    OPTIONS = eventStrings.OPTIONS;
  }
}