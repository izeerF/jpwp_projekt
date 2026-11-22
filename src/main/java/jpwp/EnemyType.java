package jpwp;

import javafx.scene.paint.Color;

public enum EnemyType {
    V("Virus", 30, 15, Color.GREEN, Color.DARKGREEN, 1.0, false),
    T("Trojan", 60, 25, Color.DARKORANGE, Color.BROWN, 0.5, true);

    private final String displayName;
    private final int maxHp;
    private final int reward;
    private final Color mainColor;
    private final Color strokeColor;
    private final double antivirusDamageMultiplier;
    private final boolean firewallImmune;

    EnemyType(String displayName, int maxHp, int reward, Color mainColor, Color strokeColor, double antivirusDamageMultiplier, boolean firewallImmune) {
        this.displayName = displayName;
        this.maxHp = maxHp;
        this.reward = reward;
        this.mainColor = mainColor;
        this.strokeColor = strokeColor;
        this.antivirusDamageMultiplier = antivirusDamageMultiplier;
        this.firewallImmune = firewallImmune;
    }

    public String getDisplayName() { return displayName; }
    public int getMaxHp() { return maxHp; }
    public int getReward() { return reward; }
    public Color getMainColor() { return mainColor; }
    public Color getStrokeColor() { return strokeColor; }
    public double getAntivirusDamageMultiplier() { return antivirusDamageMultiplier; }
    public boolean isFirewallImmune() { return firewallImmune; }

    public static EnemyType fromCode(String code) {
        try {
            return EnemyType.valueOf(code.toUpperCase());
        } catch (IllegalArgumentException e) {
            return V;
        }
    }
}