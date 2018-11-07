package com.link.cloud;

/**
 * Created by lu on 2016/6/16.
 */
public class Constants {

    public static final String TCP_URL = "120.79.244.162";
    public static final String MSG = "MSG_RECIEVE";
    public static final int TCP_PORT = 12125;
//    1003 临时柜
    public static final int REGULAR_CABINET = 1003;
//    1004 月租柜
    public static final int VIP_CABINET = 1004;
//    1005 混合柜
    public static final int VIP_REGULAR_CABINET = 1005;



    public interface ActivityExtra {

        String TYPE="TYPE";
        String ENTITY="ENTITY";
        String UUID="UUID";
        String FINGER="FINGER";
        String XIAOCHENGXU="XIAOCHENGXU";
    }

    public interface FragmentExtra {
        String BEAN="BEAN";
        String PRICELEVELBEAN="PRICELEVELBEAN";

    }


}
