package org.apache.sshd;

public class Logger {
        String classNme="";
        
    public boolean isDebugEnabled() {
        return false;
    }

    public boolean isTraceEnabled() {
        return false;
    }

    public boolean isErrorEnabled() {
        return false;
    }
    public boolean isWarnEnabled() {
        return false;
    }

    public boolean isInfoEnabled() {
        return false;
    }
    /**
     * @param classNme
     */
    public Logger(Class classname) {
        this.classNme = classname.getName();
    }

    public void print(Object... str) {
        try {
            Object[] fm = new Object[str.length - 1];
            for (int i = 1; i < fm.length; i++) {
                fm[i - 1] = str[i];
            }
            System.out.println(String.format(str[0].toString(), fm));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void error(Object ...str) {
        print("错误:",str);
    }

    public void warn(Object ...str) {
        print("warn:",str);
    }

    public void trace(Object ...str) {
        print(str);
    }
    
 
    public void info(Object ...str) {
        print("info:",str);
    }
 
    
public void debug(Object ...str) {
    print("调试:",str);
}

}
