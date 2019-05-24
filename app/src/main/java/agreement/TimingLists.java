package agreement;

import org.litepal.crud.LitePalSupport;

/**
 * Created by Administrator on 2019/5/23.
 */

public class TimingLists extends LitePalSupport {

    private int id;
    private String opentime;
    private String closetime;
    private char week;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getClosetime() {
        return closetime;
    }

    public void setClosetime(String closetime) {
        this.closetime = closetime;
    }

    public char getWeek() {
        return week;
    }

    public void setWeek(char week) {
        this.week = week;
    }
}
