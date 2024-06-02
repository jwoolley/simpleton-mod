package thesimpleton.effects.utils;

import thesimpleton.cards.interfaces.IHasCustomCantPlayEffect;

public class CustomCantPlayEffectManager {
    public static void setCantPlayEffect(IHasCustomCantPlayEffect effect) {
        cantPlayEffect = effect;
    }
    public static void clearEffect() {
        cantPlayEffect = null;
    }

    public static IHasCustomCantPlayEffect getEffect() {
        return cantPlayEffect;
    }

    public static boolean hasEffect() {
        return cantPlayEffect != null;
    }

    // this is actually a lambda that can invoke any arbitrary code, maybe rename it?
    private static IHasCustomCantPlayEffect cantPlayEffect;
}
