package burp;

import java.io.PrintWriter;

//BurpExtender为固定写法，入口类，，这些都是固定套路，包名也必须为burp,
public class BurpExtender implements IBurpExtender{
    //https://portswigger.net/burp/extender/api/allclasses-noframe.html
    // 固定套路
    private IExtensionHelpers helpers;
    private IBurpExtenderCallbacks callbacks;
    private PrintWriter stdout;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks iBurpExtenderCallbacks) {
//        在控制台打印插件名称
        this.callbacks = iBurpExtenderCallbacks;
//        设置插件名称
        this.callbacks.setExtensionName("demo-01-burpsuite-plug ");
//        在控制台打印
        this.stdout = new PrintWriter(callbacks.getStdout(), true);
        stdout.println("hello world,this is first plug!!!");
    }
}
