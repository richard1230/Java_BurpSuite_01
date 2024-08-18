package burp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;

//BurpExtender为固定写法，入口类，，这些都是固定套路，包名也必须为burp,
public class BurpExtender implements IBurpExtender, ITab {
    //https://portswigger.net/burp/extender/api/allclasses-noframe.html
    // 固定套路
    private IExtensionHelpers helpers;
    private IBurpExtenderCallbacks cbs;
    private PrintWriter stdout;

    public JPanel jPanelMain;


    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
//        调用回调,固定套路
        this.cbs = callbacks;
//        helpers需要初始化，获取helpers对象,固定套路
        this.helpers = callbacks.getHelpers();
//        设置插件名称,固定套路
        this.cbs.setExtensionName("demo-GUI-plug ");
//        在插件控制台打印，固定套路
        this.stdout = new PrintWriter(callbacks.getStdout(), true);
        stdout.println("hello,this is first gui plug!!!");


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                jPanelMain = new JPanel();

                JButton jButton = new JButton("click me,please");

                jButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
//                        super.mouseClicked(e);
                        stdout.println("someone click me");
                    }
                });

                // 将按钮添加到 主面板 jPanelMain 上.
                jPanelMain.add(jButton);

                // 设置自定义组件并添加标签
                cbs.customizeUiComponent(jPanelMain);
                cbs.addSuiteTab(BurpExtender.this);
            }
        });


    }


    @Override
    public String getTabCaption() {
        return "Burp GUI 测试插件";
    }

    @Override
    public Component getUiComponent() {
        return jPanelMain;
    }
}

