package thesimpleton.monsters;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MonsterUtil {
  public static boolean otherMonstersAreAllDead(AbstractMonster monster) {
    return allOtherMonsters(monster).stream().filter(m -> !m.isDead && !m.isDying).count() == 0;
  }

  public static List<AbstractMonster> allOtherMonsters(AbstractMonster exceptMonster) {
    return AbstractDungeon.getMonsters().monsters.stream().filter(m -> m != exceptMonster).collect(Collectors.toList());
  }

  public static AbstractMonster randomMonster() {
    return randomMonster(AbstractDungeon.getMonsters().monsters);
  }

  public static AbstractMonster randomMonster(List<AbstractMonster> monsters) {
    List<AbstractMonster> _monsters = monsters
        .stream().filter(m -> !m.isDead && !m.isDying).collect(Collectors.toList());

    Collections.shuffle(_monsters);
    return _monsters.get(0);
  }


  public static List<AbstractMonster> getDamagedMonsters() {
    return AbstractDungeon.getMonsters().monsters.stream()
        .filter(m -> !m.isDying && !m.isEscaping && m.currentHealth < m.maxHealth).collect(Collectors.toList());
  }

}