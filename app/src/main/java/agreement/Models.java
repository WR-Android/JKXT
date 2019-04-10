package agreement;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

/**
 * Created by Administrator on 2019/4/8.
 */

public class Models extends LitePalSupport {

    private int id;     //设备ID
    private String model_name;
    private String action_name;     //操作名称-->(mode)mode1 mode2 mode3 ...  (input)CV1 CV2 HDMI VGA1 VGA2 ...(check) check_state_1 ...

    private String send_data;       //操作发送协议
    private String return_data;     //操作返回协议

    public static void create_database() {
        LitePal.getDatabase();
        Models data;

        //model 880
        data = new Models();
        data.setModel_name("880");
        data.setAction_name("Device");
        data.setSend_data("E9_01_01_00_00_EB_0D_0A");
        data.setReturn_data("E9_01_01_1B_00_F1_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("CV1");
        data.setSend_data("E9_01_09_01_00_F4_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("CV2");
        data.setSend_data("E9_01_09_01_01_F5_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("HDMI");
        data.setSend_data("E9_01_09_01_02_F6_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("VGA1");
        data.setSend_data("E9_01_09_01_03_F7_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("VGA2");
        data.setSend_data("E9_01_09_01_04_F8_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("DVI");
        data.setSend_data("E9_01_09_01_05_F9_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("check_state_1");
        data.setSend_data("E9_01_92_01_00_7D_0D_0A");
        data.setReturn_data("E9_01_92_01_01_7E_0D_0A");
        data.save();

        //model 600
        data = new Models();
        data.setModel_name("600");
        data.setAction_name("Device");
        data.setSend_data("E9_01_01_00_00_EB_0D_0A");
        data.setReturn_data("E9_01_01_05_06_F6_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("CV1");
        data.setSend_data("E9_01_09_01_00_F4_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F4_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("CV2");
        data.setSend_data("E9_01_09_01_01_F5_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F5_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("VGA");
        data.setSend_data("E9_01_09_01_02_F6_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F6_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("DVI");
        data.setSend_data("E9_01_09_01_03_F7_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F7_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("HDMI");
        data.setSend_data("E9_01_09_01_04_F8_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F8_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("USB");
        data.setSend_data("E9_01_09_01_05_F9_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F9_0D_0A");
        data.save();


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public String getAction_name() {
        return action_name;
    }

    public void setAction_name(String action_name) {
        this.action_name = action_name;
    }

    public String getSend_data() {
        return send_data;
    }

    public void setSend_data(String send_data) {
        this.send_data = send_data;
    }

    public String getReturn_data() {
        return return_data;
    }

    public void setReturn_data(String return_data) {
        this.return_data = return_data;
    }


}
