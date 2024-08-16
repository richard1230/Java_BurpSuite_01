package burp;

import java.awt.*;
import java.io.PrintWriter;
import java.util.List;

//BurpExtender为固定写法，入口类，，这些都是固定套路，包名也必须为burp,
public class BurpExtender implements IBurpExtender, ITab {
    //https://portswigger.net/burp/extender/api/allclasses-noframe.html
    // 固定套路
    private IExtensionHelpers helpers;
    private IBurpExtenderCallbacks callbacks;
    private PrintWriter stdout;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
//        调用回调,固定套路
        this.callbacks = callbacks;
//        helpers需要初始化，获取helpers对象,固定套路
        this.helpers = callbacks.getHelpers();
//        设置插件名称,固定套路
        this.callbacks.setExtensionName("demo-02-burpsuite-plug ");
//        在控制台打印
        this.stdout = new PrintWriter(callbacks.getStdout(), true);
        stdout.println("hello world,this is gui plug!!!");
//      这里一定要注册监听器,不然下面的函数无法运行
//        callbacks.register(this);
    }


    @Override
    public String getTabCaption() {
        return "";
    }

    @Override
    public Component getUiComponent() {
        return null;
    }
}

