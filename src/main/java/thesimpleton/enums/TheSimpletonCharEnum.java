package thesimpleton.enums;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Finesse;
import com.megacrit.cardcrawl.cards.colorless.FlashOfSteel;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import thesimpleton.cards.attack.*;
import thesimpleton.cards.attack.unused.*;
import thesimpleton.cards.power.unused.*;
import thesimpleton.cards.skill.unused.*;
import thesimpleton.relics.unused.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TheSimpletonCharEnum {
    @SpireEnum
    public static AbstractPlayer.PlayerClass THE_SIMPLETON;

    public enum Theme {
        EXAMPLE_THEME("TheSimpletonMod:ExampleTheme");

        private final String themeId;

        Theme(String themeId) {
            this.themeId = themeId;
        }

        // Construct each cards here instead of having them in each enum's constructor, due to performance.
        public List<AbstractCard> getStartingDeck() throws Exception {
            throw new Exception("Unknown Theme enum");
        }

        public String getStartingRelicId() throws Exception {
            throw new Exception("Unknown Theme enum");
        }

        @Override
        public String toString() {
            return themeId;
        }

        public static Theme fromString(String id) {
            return Arrays.stream(Theme.values())
                    .filter(theme -> theme.themeId == id)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown theme ID: " + id));
        }
    }
}