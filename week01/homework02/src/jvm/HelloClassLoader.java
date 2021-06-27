package jvm;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HelloClassLoader extends ClassLoader{
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> helloClass = new HelloClassLoader("/Hello.xlass").findClass("Hello");
        Object hello = helloClass.getConstructor().newInstance();
        Method helloMethod = helloClass.getMethod("hello");
        helloMethod.invoke(hello);
    }

    private String xlassPath="";
    public HelloClassLoader(String xlassPath){
        this.xlassPath = xlassPath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if(xlassPath==null || xlassPath.isBlank()){
            throw new RuntimeException("xlassPath cannot null");
        }
        InputStream input = this.getClass().getResourceAsStream(xlassPath);
        byte[] buff;
        try {
            buff = input.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("read file error");
        }finally{
            try {
                input.close();
            } catch (IOException e) {
                throw new RuntimeException("close inputStream error");
            }
        }

        for (int i = 0; i < buff.length; i++) {
            buff[i] = (byte)(255-buff[i]);
        }

        return defineClass(name,buff,0,buff.length);
    }
}
