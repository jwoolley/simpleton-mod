package thesimpleton.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.attack.FlamingSpud;
import thesimpleton.cards.attack.Gnawberry;
import thesimpleton.cards.attack.SpudMissile;
import thesimpleton.cards.skill.unused.Husk;
import thesimpleton.cards.skill.unused.RootOut;
import thesimpleton.characters.TheSimpletonCharacter;
import thesimpleton.crops.AbstractCrop;
import thesimpleton.patches.ui.CenterGridCardSelectScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SimpletonUtil {
    public static final AbstractCard SPUD_MISSILE = new SpudMissile();
    public static final AbstractCard FLAMING_SPUD = new FlamingSpud();
    public static final AbstractCard GNAWBERRY = new Gnawberry();

//    public static TheSimpletonCharacter getPlayer() {
//        return (TheSimpletonCharacter) AbstractDungeon.player;
//    }

    public static boolean isPlayerInCombat() {
        return AbstractDungeon.isPlayerInDungeon()
            && AbstractDungeon.currMapNode != null
            && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
    }

    public static AbstractMonster getRandomMonster() {
        List<AbstractMonster> monsters = AbstractDungeon.getMonsters().monsters.stream()
            .filter(m -> !m.isDeadOrEscaped())
            .collect(Collectors.toList());

        Collections.shuffle(monsters);
        if (monsters.size() == 0) {
            return null;
        }

        return monsters.get(0);
    }

    public static List<AbstractMonster> getMonsters() {
        return AbstractDungeon.getCurrRoom().monsters.monsters.stream()
            .filter(m -> !m.isDead && !m.isDying && m.currentHealth > 0)
            .collect(Collectors.toList());
    }

    public static AbstractCreature getDummyCreature() {
        return (new AbstractMonster("Dummy", "Dummy", 1, 0.0F, 0.0F, 1.0F, 1.0F, null) {
            public void damage(DamageInfo di) {
            }

            public void render(com.badlogic.gdx.graphics.g2d.SpriteBatch spriteBatch) {
            }

            protected void getMove(int i) {
            }

            public void takeTurn() {
            }
        });
    }

    // TODO: ensure that this counts Defect orbs etc. (i.e. orb.ID != null)
    public static List<AbstractOrb> getActiveOrbs() {
        Logger logger = TheSimpletonMod.logger;

        logger.debug("SimpletonUtil::getActiveOrbs player has orbs: " + AbstractDungeon.player.orbs.stream().map(orb -> orb.ID).collect(Collectors.joining(", ")));
        return AbstractDungeon.player.orbs.stream().filter(orb -> orb.ID != null && orb.ID != EmptyOrbSlot.ORB_ID).collect(Collectors.toList());
    }

    // TODO: move these harvest functions to CropUtil, move CropUtil to Crop package, and make the called functions
    //       package-scoped
    public static boolean hasHarvestedThisTurn() {
        return AbstractCrop.hasHarvestedThisTurn();
    }

    public static int getNumTimesHarvestedThisTurn() {
        return AbstractCrop.getNumTimesHarvestedThisTurn();
    }

    public static boolean playerHasCard(AbstractCard card) {
        return AbstractDungeon.player.masterDeck.group.stream().anyMatch(c -> c.cardID == card.cardID);
    }

    public static AbstractCard getCardFromPlayerMasterDeck(String cardID) {
        Optional<AbstractCard> card =
            AbstractDungeon.player.masterDeck.group.stream().filter(c -> c.cardID == cardID).findFirst();

        if (!card.isPresent()) {
            return null;
        }

        return card.get();
    }

    public static AbstractCard getRandomCardFromPool(AbstractCard.CardType cardType,
                                                     AbstractCard.CardRarity rarity) {
        List<AbstractCard> cards = getCardsFromPool(cardType, rarity);

        if (cards.size() == 0) {
            return null;
        }
        Collections.shuffle(cards);
        return cards.get(0);
    }

    public static List<AbstractCard> getCardsFromPool(AbstractCard.CardType cardType,
                                                           AbstractCard.CardRarity rarity) {
        ArrayList<AbstractCard> cardsOfRarity = null;
        switch (rarity) {
            case COMMON:
                cardsOfRarity = AbstractDungeon.commonCardPool.group;
                break;
            case UNCOMMON:
                cardsOfRarity = AbstractDungeon.uncommonCardPool.group;
                break;
            case RARE:
                cardsOfRarity = AbstractDungeon.rareCardPool.group;
                break;
            default:
                throw new IllegalArgumentException("Card type not supported: " + cardType);
        }

        return cardsOfRarity.stream().filter(c -> c.type == cardType).collect(Collectors.toList());
    }

    public static void removeRelicFromPool(AbstractRelic relic) {
        final AbstractRelic.RelicTier rarity = relic.tier;

        ArrayList<String> relicPool = new ArrayList<>();

        switch (rarity) {
            case COMMON:
                relicPool = AbstractDungeon.commonRelicPool;
                break;
            case UNCOMMON:
                relicPool = AbstractDungeon.uncommonRelicPool;
                break;
            case RARE:
                relicPool = AbstractDungeon.rareRelicPool;
                break;
            case SHOP:
                relicPool = AbstractDungeon.shopRelicPool;
                break;
            case BOSS:
                relicPool = AbstractDungeon.bossRelicPool;
                break;
            default:
                break;
        }

        if (relicPool.stream().anyMatch(id -> id.equals(relic.relicId))) {
            relicPool.remove(relic.relicId);
        }
    }

    public static void openCenterGridSelectScreenUncancelable(CardGroup cardGroup, int numCards, String tipMessage) {
        CenterGridCardSelectScreen.centerGridSelect = true;
        AbstractDungeon.gridSelectScreen.open(cardGroup, 1, tipMessage,
            false, false, false, false);
    }

    public static void openCenterGridSelectScreenForUpToNumCards(CardGroup cardGroup, int maxCards, String tipMessage) {
        CenterGridCardSelectScreen.centerGridSelect = true;
        AbstractDungeon.gridSelectScreen.open(cardGroup, maxCards, true, tipMessage);
    }

    public static void openCenterGridSelectScreen(CardGroup cardGroup, int numCards, String tipMessage, boolean forUpgrade) {
        CenterGridCardSelectScreen.centerGridSelect = true;
        AbstractDungeon.gridSelectScreen.open(cardGroup,numCards, tipMessage ,forUpgrade);
    }

    public static void centerGridSelectScreenFinished() {
        CenterGridCardSelectScreen.centerGridSelect = false;
        AbstractDungeon.gridSelectScreen.selectedCards.clear();
    }

    public static boolean playerHasStartingOrbs() { return AbstractDungeon.player.masterMaxOrbs > 0; }

    public static String tagString(String stringToTag, String tag) {
        return tag + stringToTag.replaceAll("(\\s)", "$1" + tag);
    }

    public static boolean isCardInHand(AbstractCard card) {
        return isPlayerInCombat() && AbstractDungeon.player.hand.group.contains(card);
    }
}
