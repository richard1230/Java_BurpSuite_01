package burp;

import java.io.PrintWriter;
import java.util.List;

//BurpExtender为固定写法，入口类，，这些都是固定套路，包名也必须为burp,
public class BurpExtender implements IBurpExtender, IHttpListener {
    //https://portswigger.net/burp/extender/api/allclasses-noframe.html
    // 固定套路
    private IExtensionHelpers helpers;
    private IBurpExtenderCallbacks callbacks;
    private PrintWriter stdout;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
//        在控制台打印插件名称
        this.callbacks = callbacks;
//        helpers需要初始化
        this.helpers = callbacks.getHelpers();
//        设置插件名称
        this.callbacks.setExtensionName("demo-02-burpsuite-plug ");
//        在控制台打印
        this.stdout = new PrintWriter(callbacks.getStdout(), true);
        stdout.println("hello world,this is second plug!!!");
//      这里一定要注册监听器,不然下面的函数无法运行
        callbacks.registerHttpListener(this);
    }


    @Override
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {
        //切换http监听模块为Burpsuiteproxy模块
        if (toolFlag == IBurpExtenderCallbacks.TOOL_PROXY) {
            //对请求包进行处理
            if (messageIsRequest) {
                //对消息体进行解析,messageInfo是整个HTTP请求和响应消息体的总和，各种HTTP相关信息的获取都来自于它，HTTP流量的修改都是围绕它进行的。
                IRequestInfo analyzeRequest = helpers.analyzeRequest(messageInfo);

                /*****************获取参数**********************/
                List<IParameter> parameList = analyzeRequest.getParameters();
                //获取参数的方法
                //遍历参数
                //如果List对象直接打印，那么他的输出就是一个对象。
                //遍历，解析对象中的数据
                for (IParameter para : parameList) {
                    //获取参数
                    String key = para.getName();
                    //获取参数值(value)
                    String value = para.getValue();
                    //获取参数类型是cookie/表单/文件上传/xml等等
                    int type = para.getType();
                    stdout.println("参数key value type:" + key + " " + value + " " + type);
                }

                /*****************获取参数**
                 *
                 参数key value type:clientId docs 0
                 参数key value type:identityId 2ALQ0eG43md 0
                 参数key value type:_did web_22615907944DE773 2
                 参数key value type:K-Uims-Token 449162796d822dce1488e2cec3019f44 2
                 参数key value type:K-Uims-Token-More 449162796d822dce1488e2cec3019f44 2
                 参数key value type:K-Csrf-Token c4a5b6b7a642e01ad9e07a8dbc36d418 2
                 参数key value type:Recent-Identity-Id 2ALQ0eG43md 2
                 参数key value type:enabledapps.uploader 0 2
                 参数key value type:lbcookie 1 2

                 对应源代码中的:
                 byte PARAM_URL = 0;
                 byte PARAM_BODY = 1;
                 byte PARAM_COOKIE = 2;
                 byte PARAM_XML = 3;
                 byte PARAM_XML_ATTR = 4;
                 byte PARAM_MULTIPART_ATTR = 5;
                 byte PARAM_JSON = 6;
                 *
                 * ********************/

                /*****************修改并更新参数**********************/

                IParameter newPara = helpers.buildParameter("testkey", "this is testValue", IParameter.PARAM_BODY);//构造新的参数
                byte[] new_Request = messageInfo.getRequest();
                new_Request = helpers.updateParameter(new_Request, newPara); //更新(构造)请求
                messageInfo.setRequest(new_Request); //设置最终新的请求包


                /*****************删除参数**********************/
                for (IParameter para : parameList) {// 循环获取参数，判断类型，进行加密处理后，再构造新的参数，合并到新的请求包中。
                    String key = para.getName();//获取参数的名称
                    if (key.equals("abc")) {
                        new_Request = helpers.removeParameter(new_Request, para);//删除请求
                    }
                }


                /*****************获取header**********************/
                List<String> headers = analyzeRequest.getHeaders();
                //新增header
                //对所有的http请求消息新增一个http请求头
                //浏览器xff头插件,新增header
                headers.add("myheader:hello world");
                for (String header : headers) {
                    /*****************遍历header**********************/
                    stdout.println("request header: " + header);
                    if (header.startsWith("referer"))
                    /*****************删除header**********************/
                        headers.remove(header);
                    stdout.println("删除了header中的referer ");

                }

                /*****************获取请求体***固定套路*******************/

                String req = new String(messageInfo.getRequest());
                int bodyoffset = analyzeRequest.getBodyOffset();
                String body = req.substring(bodyoffset);
                byte[] byte_body = body.getBytes();  //String to byte[]
                stdout.println("获取网页的请求内容resquest body: " + body);

            }


            //获取协议 端口 和主机名
            IHttpService service = messageInfo.getHttpService();
            stdout.println("协议 主机 端口 " + service.getProtocol() + " " + service.getHost() + " " + service.getPort());


        } else {//这个逻辑是处理响应包
            IResponseInfo analyzeResponse = helpers.analyzeResponse(messageInfo.getResponse());
            //如果没有这个条件，会报错:没有定义数据内容
            if (analyzeResponse != null) {
                //获取响应码信息
                //int float short
                short statusCode = analyzeResponse.getStatusCode();
                stdout.println("响应状态码status= " + statusCode);
                //获取响应头信息
                List<String> headers = analyzeResponse.getHeaders();
                for (String header : headers) {
                    stdout.println("响应头response header:" + header);
                }
                // 获取响应body信息 打印网页信息
                //固定格式
                String resp = new String(messageInfo.getResponse());
                int bodyOffset = analyzeResponse.getBodyOffset();
                String body = resp.substring(bodyOffset);
                stdout.println("获取网页内容response body=" + body);
            }
        }
    }
}

