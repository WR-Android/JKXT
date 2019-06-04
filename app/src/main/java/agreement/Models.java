package agreement;

import org.litepal.crud.LitePalSupport;

/**
 * Created by Administrator on 2019/4/8.
 */

public class Models extends LitePalSupport {

    private int id;     //设备ID
    private String model_name;
    private String action_name;     //操作名称-->(mode)mode1 mode2 mode3 ...  (input)CV1 cv2 hdmi VGA1 VGA2 ...(check) check_state_1 ...
    private String action_type;
    private String send_data;       //操作发送协议
    private String return_data;     //操作返回协议

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

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
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
