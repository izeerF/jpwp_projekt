package jpwp;

import java.util.List;

public class WaveData {
    private List<EnemyType> enemySequence;

    public WaveData(List<EnemyType> enemySequence) {
        this.enemySequence = enemySequence;
    }

    public List<EnemyType> getEnemySequence() {
        return enemySequence;
    }
}