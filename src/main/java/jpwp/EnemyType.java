package jpwp;

import javafx.scene.paint.Color;

public enum EnemyType {
    V("Virus", 30, 15, Color.GREEN, Color.DARKGREEN, 1.0, 0.5, 0.4, false, 55.0),
    T("Trojan", 45, 25, Color.DARKORANGE, Color.BROWN, 0.6, 2.0, 0.5, true, 45.0),
    W("Worm", 40, 20, Color.MAGENTA, Color.DARKMAGENTA, 1.2, 0.25, 0.3, false, 105.0),
    R("Ransomware", 85, 40, Color.CRIMSON, Color.BLACK, 0.2, 0.2, 2.5, true, 35.0);

    private final String displayName;
    private final int maxHp;
    private final int reward;
    private final Color mainColor;
    private final Color strokeColor;
    private final double antivirusDamageMultiplier;
    private final double idsDamageMultiplier;
    private final double decryptorDamageMultiplier;
    private final boolean firewallImmune;
    private final double speedPxPerSec;

    EnemyType(String displayName, int maxHp, int reward, Color mainColor, Color strokeColor,
              double antivirusDamageMultiplier, double idsDamageMultiplier, double decryptorDamageMultiplier,
              boolean firewallImmune, double speedPxPerSec) {
        this.displayName = displayName;
        this.maxHp = maxHp;
        this.reward = reward;
        this.mainColor = mainColor;
        this.strokeColor = strokeColor;
        this.antivirusDamageMultiplier = antivirusDamageMultiplier;
        this.idsDamageMultiplier = idsDamageMultiplier;
        this.decryptorDamageMultiplier = decryptorDamageMultiplier;
        this.firewallImmune = firewallImmune;
        this.speedPxPerSec = speedPxPerSec;
    }

    public String getDisplayName() { return displayName; }
    public int getMaxHp() { return maxHp; }
    public int getReward() { return reward; }
    public Color getMainColor() { return mainColor; }
    public Color getStrokeColor() { return strokeColor; }
    public double getAntivirusDamageMultiplier() { return antivirusDamageMultiplier; }
    public double getIdsDamageMultiplier() { return idsDamageMultiplier; }
    public double getDecryptorDamageMultiplier() { return decryptorDamageMultiplier; }
    public boolean isFirewallImmune() { return firewallImmune; }
    public double getSpeedPxPerSec() { return speedPxPerSec; }

    public static EnemyType fromCode(String code) {
        try {
            return EnemyType.valueOf(code.toUpperCase());
        } catch (IllegalArgumentException e) {
            return V;
        }
    }
}