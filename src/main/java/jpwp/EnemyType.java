package jpwp;

import javafx.scene.paint.Color;

public enum EnemyType {
    V("Virus", 30, 15, Color.GREEN, Color.DARKGREEN, 1.0, 0.5, false, 1),
    T("Trojan", 45, 25, Color.DARKORANGE, Color.BROWN, 0.6, 2.0, true, 1),
    W("Worm", 40, 20, Color.MAGENTA, Color.DARKMAGENTA, 1.2, 0.25, false, 2);

    private final String displayName;
    private final int maxHp;
    private final int reward;
    private final Color mainColor;
    private final Color strokeColor;
    private final double antivirusDamageMultiplier;
    private final double idsDamageMultiplier;
    private final boolean firewallImmune;
    private final int stepsPerTick;

    EnemyType(String displayName, int maxHp, int reward, Color mainColor, Color strokeColor,
              double antivirusDamageMultiplier, double idsDamageMultiplier,
              boolean firewallImmune, int stepsPerTick) {
        this.displayName = displayName;
        this.maxHp = maxHp;
        this.reward = reward;
        this.mainColor = mainColor;
        this.strokeColor = strokeColor;
        this.antivirusDamageMultiplier = antivirusDamageMultiplier;
        this.idsDamageMultiplier = idsDamageMultiplier;
        this.firewallImmune = firewallImmune;
        this.stepsPerTick = stepsPerTick;
    }

    public String getDisplayName() { return displayName; }
    public int getMaxHp() { return maxHp; }
    public int getReward() { return reward; }
    public Color getMainColor() { return mainColor; }
    public Color getStrokeColor() { return strokeColor; }
    public double getAntivirusDamageMultiplier() { return antivirusDamageMultiplier; }
    public double getIdsDamageMultiplier() { return idsDamageMultiplier; }
    public boolean isFirewallImmune() { return firewallImmune; }
    public int getStepsPerTick() { return stepsPerTick; }

    public static EnemyType fromCode(String code) {
        try {
            return EnemyType.valueOf(code.toUpperCase());
        } catch (IllegalArgumentException e) {
            return V;
        }
    }
}