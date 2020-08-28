package com.kakarote.crm9.utils;



import java.util.ArrayList;

public class XingePushUtil {

    /*private static final String APP_ID_ANDROID = "826206e9c32c1";

    private static final String SECRET_KEY_ANDROID = "ae50d2f59262c56fffd170650e67d766";

    private static final String APP_ID_IOS = "6f055d320de1a";

    private static final String SECRET_KEY_IOS = "c17680b1a4d3fba2e8513fdac6fd9392";*/

    public void sendMessage(String title, String content, Long userId) {
        /*ArrayList<String> accountList = new ArrayList<>();
        accountList.add("@" + userId);

        sendAndroidMessage(title, content, accountList);
        sendIOSMessage(title, content, accountList);*/
    }

    private void sendAndroidMessage(String title, String content, ArrayList<String> accountList) {
       /* XingeApp xingeApp = new XingeApp(APP_ID_ANDROID, SECRET_KEY_ANDROID);
        Message message = new Message();
        message.setTitle(title);
        message.setContent(content);
        PushAppRequest pushAppRequest = new PushAppRequest();
        pushAppRequest.setMessage(message);
        pushAppRequest.setMessage_type(MessageType.notify);
        pushAppRequest.setAudience_type(AudienceType.account);
        pushAppRequest.setPlatform(Platform.android);
        pushAppRequest.setAccount_list(accountList);
        xingeApp.pushApp(pushAppRequest);*/
    }

    private void sendIOSMessage(String title, String content, ArrayList<String> accountList) {
       /* XingeApp xingeApp = new XingeApp(APP_ID_IOS, SECRET_KEY_IOS);
        Message message = new Message();
        message.setTitle(title);
        message.setContent(content);
        Alert alert = new Alert();
        alert.setTitle("subtitle");
        Aps aps = new Aps();
        aps.setAlert(alert);
        MessageIOS ios = new MessageIOS();
        ios.setAps(aps);
        message.setIos(ios);
        PushAppRequest pushAppRequest = new PushAppRequest();
        pushAppRequest.setMessage(message);
        pushAppRequest.setMessage_type(MessageType.notify);
        pushAppRequest.setAudience_type(AudienceType.account);
        pushAppRequest.setPlatform(Platform.ios);
        pushAppRequest.setAccount_list(accountList);
        xingeApp.pushApp(pushAppRequest);*/
    }
}
