package com.cyh.spring.formework.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * View 就是自定义模板解析引擎，核心方法render().在render()方法中完成对模板的渲染，最终返回浏览器能识别的字符串，通过response输出
 */
public class GPView {
    private static final String DEFAULT_CONTENT_TYPE = "text/html;charset=utf-8";
    private File viewFile;

    public GPView(File viewFile) {
        this.viewFile = viewFile;
    }

    public static String getDefaultContentType() {
        return DEFAULT_CONTENT_TYPE;
    }

    public File getViewFile() {
        return viewFile;
    }

    public void setViewFile(File viewFile) {
        this.viewFile = viewFile;
    }

    public void render(Map<String,?> model, HttpServletRequest request , HttpServletResponse response) throws Exception{
        StringBuffer sb = new StringBuffer();
        RandomAccessFile ra = new RandomAccessFile(this.viewFile,"r");

        try{
            String line = null;
            while(null != (line = ra.readLine())){
                line = new String(line.getBytes("ISO-8859-1"),"UTF-8");
                Pattern pattern = Pattern.compile("￥\\{[^\\}]+\\}",Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(line);

                while(matcher.find()){
                    String paramName = matcher.group();
                    paramName = paramName.replaceAll("￥\\{ | \\}" ,"");
                    Object paramValue = model.get(paramName);

                    if(paramValue == null){
                        continue;
                    }

                    line = matcher.replaceFirst(makeStringForRegExp(paramValue.toString()));
                    matcher = pattern.matcher(line);
                }

                sb.append(line);
            }
        }finally {
            ra.close();
        }
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(sb.toString());
    }

    private String makeStringForRegExp(String str) {
        return str;
    }
}
