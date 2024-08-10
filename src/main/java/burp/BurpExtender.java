package burp;

import java.io.PrintWriter;

//BurpExtender为固定写法，入口类，，这些都是固定套路，包名也必须为burp,
public class BurpExtender implements IBurpExtender, IHttpListener, IProxyListener, IExtensionStateListener {
    //https://portswigger.net/burp/extender/api/allclasses-noframe.html
    // 固定套路
    private IExtensionHelpers helpers;
    private IBurpExtenderCallbacks callbacks;
    private PrintWriter stdout;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
//        在控制台打印插件名称
        this.callbacks = callbacks;
//        设置插件名称
        this.callbacks.setExtensionName("demo-02-burpsuite-plug ");
//        在控制台打印
        this.stdout = new PrintWriter(callbacks.getStdout(), true);
        stdout.println("hello world,this is second plug!!!");
//      这里一定要注册监听器,不然下面的函数无法运行
        callbacks.registerHttpListener(this);
        callbacks.registerExtensionStateListener(this);
        callbacks.registerProxyListener(this);
    }


    @Override
    public void extensionUnloaded() {
        stdout.println("plug have been unloaded");
    }

    @Override
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse iHttpRequestResponse) {
        stdout.println(
                (messageIsRequest ? "HTTP request to " : "HTTP response from ") + iHttpRequestResponse.getHttpService() + "[" + callbacks.getToolName(toolFlag) + "]"
        );
    }

    @Override
    public void processProxyMessage(boolean messageIsRequest, IInterceptedProxyMessage iInterceptedProxyMessage) {
        stdout.println(
                (messageIsRequest ? "Proxy request to " : "Proxy response from ") +
                        iInterceptedProxyMessage.getMessageInfo().getHttpService());
    }
}
