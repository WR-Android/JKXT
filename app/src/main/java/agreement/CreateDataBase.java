package agreement;

import org.litepal.LitePal;

/**
 * Created by Administrator on 2019/4/17.
 */

public class CreateDataBase {

    public static void create_database() {
        LitePal.getDatabase();
        create_880();
        create_600();
        create_910();
        create_800();
    }

    private static void create_910() {
        Models data;

        data = new Models();
        data.setModel_name("910");
        data.setAction_name("Device");
        data.setAction_type("CheckConnect");
        data.setSend_data("E9_01_01_00_00_EB_0D_0A");
        data.setReturn_data("E9_01_01_17_00_02_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("910");
        data.setAction_name("CV1");
        data.setAction_type("input");
        data.setSend_data("E9_01_40_00_00_2A_0D_0A");
        data.setReturn_data("E9_01_40_00_FF_29_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("910");
        data.setAction_name("CV2");
        data.setAction_type("input");
        data.setSend_data("E9_01_40_00_10_3A_0D_0A");
        data.setReturn_data("E9_01_40_00_FF_29_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("910");
        data.setAction_name("VGA1");
        data.setAction_type("input");
        data.setSend_data("E9_01_40_00_20_4A_0D_0A");
        data.setReturn_data("E9_01_40_00_FF_29_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("910");
        data.setAction_name("DVI");
        data.setAction_type("input");
        data.setSend_data("E9_01_40_00_30_5A_0D_0A");
        data.setReturn_data("E9_01_40_00_FF_29_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("910");
        data.setAction_name("VGA2");
        data.setAction_type("input");
        data.setSend_data("E9_01_40_00_22_4C_0D_0A");
        data.setReturn_data("E9_01_40_00_FF_29_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("910");
        data.setAction_name("HDMI");
        data.setAction_type("input");
        data.setSend_data("E9_01_40_00_32_5C_0D_0A");
        data.setReturn_data("E9_01_40_00_FF_29_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("910");
        data.setAction_name("mode1");
        data.setAction_type("mode");
        data.setSend_data("E9_01_11_01_00_FC_0D_0A");
        data.setReturn_data("E9_01_11_05_FF_FF_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("910");
        data.setAction_name("mode2");
        data.setAction_type("mode");
        data.setSend_data("E9_01_11_02_00_FD_0D_0A");
        data.setReturn_data("E9_01_11_05_FF_FF_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("910");
        data.setAction_name("mode3");
        data.setAction_type("mode");
        data.setSend_data("E9_01_11_03_00_FE_0D_0A");
        data.setReturn_data("E9_01_11_05_FF_FF_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("910");
        data.setAction_name("mode4");
        data.setAction_type("mode");
        data.setSend_data("E9_01_11_04_00_FF_0D_0A");
        data.setReturn_data("E9_01_11_05_FF_FF_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("910");
        data.setAction_name("mode5");
        data.setAction_type("mode");
        data.setSend_data("E9_01_11_05_00_00_0D_0A");
        data.setReturn_data("E9_01_11_05_FF_FF_0D_0A");
        data.save();
    }

    private static void create_600() {
        Models data;

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("Device");
        data.setAction_type("CheckConnect");
        data.setSend_data("E9_01_01_00_00_EB_0D_0A");
        data.setReturn_data("E9_01_01_05_06_F6_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("CV2");
        data.setAction_type("input");
        data.setSend_data("E9_01_09_01_00_F4_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("CV1");
        data.setAction_type("input");
        data.setSend_data("E9_01_09_01_01_F5_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("VGA");
        data.setAction_type("input");
        data.setSend_data("E9_01_09_01_02_F6_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("DVI");
        data.setAction_type("input");
        data.setSend_data("E9_01_09_01_03_F7_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("HDMI");
        data.setAction_type("input");
        data.setSend_data("E9_01_09_01_04_F8_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("USB");
        data.setAction_type("input");
        data.setSend_data("E9_01_09_01_05_F9_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("CV2");
        data.setAction_type("check_signal");
        data.setSend_data("E9_01_0B_01_00_F6_0D_0A");
        data.setReturn_data("E9_01_0B_01_00_F6_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("CV1");
        data.setAction_type("check_signal");
        data.setSend_data("E9_01_0B_01_00_F6_0D_0A");
        data.setReturn_data("E9_01_0B_01_01_F7_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("VGA");
        data.setAction_type("check_signal");
        data.setSend_data("E9_01_0B_01_00_F6_0D_0A");
        data.setReturn_data("E9_01_0B_01_02_F8_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("DVI");
        data.setAction_type("check_signal");
        data.setSend_data("E9_01_0B_01_00_F6_0D_0A");
        data.setReturn_data("E9_01_0B_01_03_F9_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("HDMI");
        data.setAction_type("check_signal");
        data.setSend_data("E9_01_0B_01_00_F6_0D_0A");
        data.setReturn_data("E9_01_0B_01_04_FA_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("USB");
        data.setAction_type("check_signal");
        data.setSend_data("E9_01_0B_01_00_F6_0D_0A");
        data.setReturn_data("E9_01_0B_01_05_FB_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("mode1");
        data.setAction_type("mode");
        data.setSend_data("E9_01_11_01_00_FC_0D_0A");
        data.setReturn_data("E9_01_11_05_FF_FF_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("mode2");
        data.setAction_type("mode");
        data.setSend_data("E9_01_11_02_00_FD_0D_0A");
        data.setReturn_data("E9_01_11_05_FF_FF_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("mode3");
        data.setAction_type("mode");
        data.setSend_data("E9_01_11_03_00_FE_0D_0A");
        data.setReturn_data("E9_01_11_05_FF_FF_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("mode4");
        data.setAction_type("mode");
        data.setSend_data("E9_01_11_04_00_FF_0D_0A");
        data.setReturn_data("E9_01_11_05_FF_FF_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("600");
        data.setAction_name("mode5");
        data.setAction_type("mode");
        data.setSend_data("E9_01_11_05_00_00_0D_0A");
        data.setReturn_data("E9_01_11_05_FF_FF_0D_0A");
        data.save();
    }

    private static void create_880() {
        Models data;

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("Device");
        data.setAction_type("CheckConnect");
        data.setSend_data("E9_01_01_00_00_EB_0D_0A");
        data.setReturn_data("E9_01_01_1B_00_06_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("CV1");
        data.setAction_type("input");
        data.setSend_data("E9_01_09_01_00_F4_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("CV2");
        data.setAction_type("input");
        data.setSend_data("E9_01_09_01_01_F5_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("HDMI");
        data.setAction_type("input");
        data.setSend_data("E9_01_09_01_02_F6_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("VGA1");
        data.setAction_type("input");
        data.setSend_data("E9_01_09_01_03_F7_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("VGA2");
        data.setAction_type("input");
        data.setSend_data("E9_01_09_01_04_F8_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("DVI");
        data.setAction_type("input");
        data.setSend_data("E9_01_09_01_05_F9_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("CV1");
        data.setAction_type("check_signal");
        data.setSend_data("E9_01_0B_01_00_F6_0D_0A");
        data.setReturn_data("E9_01_0B_01_00_F6_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("CV2");
        data.setAction_type("check_signal");
        data.setSend_data("E9_01_0B_01_00_F6_0D_0A");
        data.setReturn_data("E9_01_0B_01_01_F7_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("HDMI");
        data.setAction_type("check_signal");
        data.setSend_data("E9_01_0B_01_00_F6_0D_0A");
        data.setReturn_data("E9_01_0B_01_02_F8_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("VGA1");
        data.setAction_type("check_signal");
        data.setSend_data("E9_01_0B_01_00_F6_0D_0A");
        data.setReturn_data("E9_01_0B_01_03_F9_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("VGA2");
        data.setAction_type("check_signal");
        data.setSend_data("E9_01_0B_01_00_F6_0D_0A");
        data.setReturn_data("E9_01_0B_01_04_FA_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("DVI");
        data.setAction_type("check_signal");
        data.setSend_data("E9_01_0B_01_00_F6_0D_0A");
        data.setReturn_data("E9_01_0B_01_05_FB_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("check_state_1");
        data.setAction_type("CheckState");
        data.setSend_data("E9_01_92_01_00_7D_0D_0A");
        data.setReturn_data("E9_01_92_01_01_7E_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("mode1");
        data.setAction_type("mode");
        data.setSend_data("E9_01_11_01_00_FC_0D_0A");
        data.setReturn_data("E9_01_11_05_FF_FF_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("mode2");
        data.setAction_type("mode");
        data.setSend_data("E9_01_11_02_00_FD_0D_0A");
        data.setReturn_data("E9_01_11_05_FF_FF_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("mode3");
        data.setAction_type("mode");
        data.setSend_data("E9_01_11_03_00_FE_0D_0A");
        data.setReturn_data("E9_01_11_05_FF_FF_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("mode4");
        data.setAction_type("mode");
        data.setSend_data("E9_01_11_04_00_FF_0D_0A");
        data.setReturn_data("E9_01_11_05_FF_FF_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("880");
        data.setAction_name("mode5");
        data.setAction_type("mode");
        data.setSend_data("E9_01_11_05_00_00_0D_0A");
        data.setReturn_data("E9_01_11_05_FF_FF_0D_0A");
        data.save();
    }

    private static void create_800() {
        Models data;

        data = new Models();
        data.setModel_name("800");
        data.setAction_name("Device");
        data.setAction_type("CheckConnect");
        data.setSend_data("E9_01_01_00_00_EB_0D_0A");
        data.setReturn_data("E9_01_01_04_02_F1_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("800");
        data.setAction_name("CV1");
        data.setAction_type("input");
        data.setSend_data("E9_01_09_01_00_F4_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("800");
        data.setAction_name("CV2");
        data.setAction_type("input");
        data.setSend_data("E9_01_09_01_01_F5_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("800");
        data.setAction_name("HDMI");
        data.setAction_type("input");
        data.setSend_data("E9_01_09_01_03_F7_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("800");
        data.setAction_name("VGA1");
        data.setAction_type("input");
        data.setSend_data("E9_01_09_01_04_F8_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("800");
        data.setAction_name("VGA2");
        data.setAction_type("input");
        data.setSend_data("E9_01_09_01_05_F9_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("800");
        data.setAction_name("DVI");
        data.setAction_type("input");
        data.setSend_data("E9_01_09_01_06_FA_0D_0A");
        data.setReturn_data("E9_01_09_01_FF_F3_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("800");
        data.setAction_name("CV1");
        data.setAction_type("check_signal");
        data.setSend_data("E9_01_0B_01_00_F6_0D_0A");
        data.setReturn_data("E9_01_0B_01_00_F6_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("800");
        data.setAction_name("CV2");
        data.setAction_type("check_signal");
        data.setSend_data("E9_01_0B_01_00_F6_0D_0A");
        data.setReturn_data("E9_01_0B_01_01_F7_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("800");
        data.setAction_name("HDMI");
        data.setAction_type("check_signal");
        data.setSend_data("E9_01_0B_01_00_F6_0D_0A");
        data.setReturn_data("E9_01_0B_01_03_F9_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("800");
        data.setAction_name("VGA1");
        data.setAction_type("check_signal");
        data.setSend_data("E9_01_0B_01_00_F6_0D_0A");
        data.setReturn_data("E9_01_0B_01_04_FA_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("800");
        data.setAction_name("VGA2");
        data.setAction_type("check_signal");
        data.setSend_data("E9_01_0B_01_00_F6_0D_0A");
        data.setReturn_data("E9_01_0B_01_05_FB_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("800");
        data.setAction_name("DVI");
        data.setAction_type("check_signal");
        data.setSend_data("E9_01_0B_01_00_F6_0D_0A");
        data.setReturn_data("E9_01_0B_01_06_FC_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("800");
        data.setAction_name("check_state_1");
        data.setAction_type("CheckState");
        data.setSend_data("E9_01_92_01_00_7D_0D_0A");
        data.setReturn_data("E9_01_92_01_01_7E_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("800");
        data.setAction_name("mode1");
        data.setAction_type("mode");
        data.setSend_data("E9_01_11_01_00_FC_0D_0A");
        data.setReturn_data("E9_01_11_05_FF_FF_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("800");
        data.setAction_name("mode2");
        data.setAction_type("mode");
        data.setSend_data("E9_01_11_02_00_FD_0D_0A");
        data.setReturn_data("E9_01_11_05_FF_FF_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("800");
        data.setAction_name("mode3");
        data.setAction_type("mode");
        data.setSend_data("E9_01_11_03_00_FE_0D_0A");
        data.setReturn_data("E9_01_11_05_FF_FF_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("800");
        data.setAction_name("mode4");
        data.setAction_type("mode");
        data.setSend_data("E9_01_11_04_00_FF_0D_0A");
        data.setReturn_data("E9_01_11_05_FF_FF_0D_0A");
        data.save();

        data = new Models();
        data.setModel_name("800");
        data.setAction_name("mode5");
        data.setAction_type("mode");
        data.setSend_data("E9_01_11_05_00_00_0D_0A");
        data.setReturn_data("E9_01_11_05_FF_FF_0D_0A");
        data.save();
    }

}
