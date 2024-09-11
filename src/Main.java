import java.util.Random;

public class Main {
    public static int bossHealth = 850;
    public static int bossDamage = 50;
    public static String bossDefence;
    public static int[] heroesHealth = {270, 280, 250, 300, 500, 200, 150, 250};
    public static int[] heroesDamage = {20, 15, 10, 0, 5, 10, 0, 25};
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Medic", "Golem", "Lucky", "Witcher", "Thor"};
    public static int medicHeal = 25;
    public static int roundNumber = 0;
    public static boolean bossStunned = false;

    public static void main(String[] args) {
        printStatistics();
        while (!isGameOver()) {
            playRound();
        }
    }

    public static boolean isGameOver() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }
        boolean allHeroesDead = true;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("Boss won!!!");
            return true;
        }
        return false;
    }

    public static void playRound() {
        roundNumber++;
        if (!bossStunned) {
            chooseBossDefence();
            bossAttack();
        } else {
            System.out.println("Boss is stunned and skips this round!");
            bossStunned = false;
        }
        heroesAttack();
        medicHeal();
        printStatistics();
    }

    public static void chooseBossDefence() {
        Random random = new Random();
        int randomIndex = random.nextInt(heroesAttackType.length - 1);
        bossDefence = heroesAttackType[randomIndex];
    }

    public static void bossAttack() {
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                int damage = bossDamage;
                if (i != 4 && heroesHealth[4] > 0) { // Голем принимает 1/5 урона
                    int golemDamage = bossDamage / 5;
                    damage -= golemDamage;
                    heroesHealth[4] -= golemDamage;
                    if (heroesHealth[4] < 0) heroesHealth[4] = 0;
                    System.out.println("Golem took " + golemDamage + " damage from Boss for " + heroesAttackType[i]);
                }

                if (heroesHealth[i] - damage < 0) {
                    heroesHealth[i] = 0;
                } else {
                    heroesHealth[i] -= damage;
                }
            }
        }
    }

    public static void heroesAttack() {
        Random random = new Random();
        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0) {
                if (i == 5 && random.nextBoolean()) { // Шанс Лаки уклониться
                    System.out.println("Lucky dodged the Boss's attack!");
                    continue;
                }

                if (i == 7 && random.nextBoolean()) { // Шанс Тора оглушить босса
                    System.out.println("Thor stunned the Boss!");
                    bossStunned = true;
                }

                int damage = heroesDamage[i];
                if (bossDefence.equals(heroesAttackType[i])) {
                    int coeff = random.nextInt(9) + 2; // 2,3,4,5,6,7,8,9,10
                    damage = heroesDamage[i] * coeff;
                    System.out.println("Critical Damage: " + damage);
                }
                if (bossHealth - damage < 0) {
                    bossHealth = 0;
                } else {
                    bossHealth -= damage;
                }
            }
        }
    }

    public static void medicHeal() {
        for (int i = 0; i < heroesHealth.length; i++) {
            if (i != 3 && heroesHealth[i] > 0 && heroesHealth[i] < 100 && heroesHealth[3] > 0) {
                heroesHealth[i] += medicHeal;
                System.out.println("Medic healed " + heroesAttackType[i] + " for " + medicHeal + " health.");
                break;
            }
        }
    }

    public static void printStatistics() {
        System.out.println("ROUND: " + roundNumber + " ------------------");
        System.out.println("Boss health: " + bossHealth + " damage: " + bossDamage + " defence: " + (bossDefence == null ? "No defence" : bossDefence));
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i] + " damage: " + heroesDamage[i]);
        }
    }
}
