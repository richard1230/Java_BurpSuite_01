package burp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;

//BurpExtender为固定写法，入口类，，这些都是固定套路，包名也必须为burp,
public class BurpExtender implements IBurpExtender ,IHttpListener{
    //https://portswigger.net/burp/extender/api/allclasses-noframe.html
    // 固定套路
    private IExtensionHelpers helpers;
    private IBurpExtenderCallbacks cbs;
    private PrintWriter stdout;

    private static final String HOST_FROM = "host1.example.org";
    private static final String HOST_TO = "host2.example.org";




    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
//        调用回调,固定套路
        this.cbs = callbacks;
//        helpers需要初始化，获取helpers对象,固定套路
        this.helpers = callbacks.getHelpers();
//        设置插件名称,固定套路
        this.cbs.setExtensionName("traffic redirector ");
//        在插件控制台打印，固定套路
        this.stdout = new PrintWriter(callbacks.getStdout(), true);
        stdout.println("traffic redirector  plug!!!");

        //register ourselves as an HTTP listener
        cbs.registerHttpListener(this);
    }


    @Override
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {

        if (messageIsRequest){
            //获取当请求的http service
            IHttpService httpService = messageInfo.getHttpService();

            // if the host is HOST_FROM, change it to HOST_TO
            if (HOST_FROM.equalsIgnoreCase(httpService.getHost())){
                messageInfo.setHttpService(helpers.buildHttpService(HOST_TO,httpService.getPort(),httpService.getProtocol()));
            }
        }

    }
}

