package swipeMenu;

/**
 * Created by Administrator on 2019/5/20.
 */

public class SwipeBean {

    public int id;
    public String timingId;
    public String openTime;
    public String closeTime;
    public char timingWeek;

    public SwipeBean(int sqLiteId, String Id, String open, String close, char Week) {
        this.id = sqLiteId;
        this.timingId = Id;
        this.openTime = open;
        this.closeTime = close;
        this.timingWeek = Week;
    }
}
