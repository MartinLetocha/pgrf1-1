package objects;

public class LeaderboardTime {
    public String name;
    public double time;
    public int shotsFired;
    public int recNum = 0;
    public LeaderboardTime(String name, double time, int shotsFired, int recNum) {
        this.name = name;
        this.time = time;
        this.shotsFired = shotsFired;
        this.recNum = recNum;
    }
    public double getTime() {
        return time;
    }
    public int getShots() {
        return shotsFired;
    }
}
