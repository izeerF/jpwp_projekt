package jpwp;

import java.util.List;

public class WaveData {

    public static class SpawnStep {
        private final EnemyType enemyType; // null oznacza placeholder X (pusta linia)
        private final double delayBefore;

        public SpawnStep(EnemyType enemyType, double delayBefore) {
            this.enemyType = enemyType;
            this.delayBefore = delayBefore;
        }

        public EnemyType getEnemyType() {
            return enemyType;
        }

        public boolean isPlaceholder() {
            return enemyType == null;
        }

        public double getDelayBefore() {
            return delayBefore;
        }
    }

    private List<SpawnStep> spawnSteps;

    public WaveData(List<SpawnStep> spawnSteps) {
        this.spawnSteps = spawnSteps;
    }

    public List<SpawnStep> getSpawnSteps() {
        return spawnSteps;
    }
}