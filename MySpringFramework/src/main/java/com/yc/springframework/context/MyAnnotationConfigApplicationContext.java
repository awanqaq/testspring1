package com.yc.springframework.context;

import com.yc.springframework.stereotype.*;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * @program: testspring
 * @description:
 * @author: ZYF
 * @create: 2021-04-05 11:41
 */
public class MyAnnotationConfigApplicationContext implements MyApplicationContext {
    private Map<String,Object> beanMap=new HashMap<String,Object>();
    //private Map<String,Class> classMap=new HashMap<String,Class>();


    public MyAnnotationConfigApplicationContext(Class<?>...componentClasses) throws InstantiationException, IllegalAccessException, InvocationTargetException, IOException, ClassNotFoundException {
        //由于他有父类，故而先调用父类的构造方法。然后调用自己的构造方法
        //在自己的构造方法中初始化一个读取器和一个扫描器
        register(componentClasses);
    }

    private void register(Class<?>[] componentClasses) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, ClassNotFoundException {
        //只实现IOC MyPostConstruct MyPreDestroy
        //实现DI
       if(componentClasses==null||componentClasses.length<=0){
           throw new RuntimeException("没有指定配置类");
        }
       for(Class cl:componentClasses){
           if(!cl.isAnnotationPresent(MyConfiguration.class)){
               continue;
           }
           String[] basePackages=getAppConfigBasePackages(cl);
           if(cl.isAnnotationPresent(MyComponentScan.class)){
               MyComponentScan mcs= (MyComponentScan) cl.getAnnotation(MyComponentScan.class);
               if(mcs.basePackages()!=null && mcs.basePackages().length>0){
                   basePackages=mcs.basePackages();
               }
           }
           //处理@MyBean的情况
           Object obj=cl.newInstance(); //obj就是当前解析的MyAppConfig对象
           handleAtMyBean(cl,obj);
           //处理 basePackages 基础包下的所有托管Bean
           for(String basePackage:basePackages){
               System.out.println("扫描包路径:"+basePackage);
               scanPackageAndSubPackageClasses(basePackage);
           }
           //继续托管其他bean
           handleManagedBean();
           //版本2：实现DI  循环beanMap中的每个bean 找到他们每个类中每个由@Autowired @Resource注解的方法以实现di
           // 只有托管完了之后才会实现依赖注入
           handleDi(beanMap);


       }

    }

    private void handleDi(Map<String, Object> beanMap) throws InvocationTargetException, IllegalAccessException {
        Collection<Object> objectCollection = beanMap.values();
        for (Object obj : objectCollection) {
            Class cls = obj.getClass();
            Method[] ms = cls.getDeclaredMethods();
            for (Method m : ms) {
                if (m.isAnnotationPresent(MyAutowired.class)&& m.getName().startsWith("set")) {

                    invokeAutoWiredMethod(m,obj);
                } else if (m.isAnnotationPresent(MyResource.class)) {

                    invokeResourceMethod(m,obj);
                }
            }
            Field[] fs=cls.getDeclaredFields();
            for(Field field:fs){
                if(field.isAnnotationPresent(MyAutowired.class)){

                }else if(field.isAnnotationPresent(MyResource.class)){

                }
            }
        }
    }

    private void invokeResourceMethod(Method m, Object obj) throws InvocationTargetException, IllegalAccessException {
        //1.取出MyResource中name属性值 当成beanid
        MyResource mr=m.getAnnotation(MyResource.class);
        String beanId=mr.name();
        //2.如果没有 则取出m方法中参数的类型名 改成首字小写 当成beanId
        if(beanId==null || beanId.equalsIgnoreCase("")){
            String pname=m.getParameterTypes()[0].getSimpleName();
            beanId=pname.substring(0,1).toLowerCase()+pname.substring(1);
        }
        //3.从beanMap取出
        Object o=beanMap.get(beanId);
        //4.invoke
        m.invoke(obj,o);
    }


    private void invokeAutoWiredMethod(Method m, Object obj) throws InvocationTargetException, IllegalAccessException {
        //1. 取出m的参数类型
        Class typeClass=m.getParameterTypes()[0];
        //2.从beanMap中循环所有的object
        Set<String> keys=beanMap.keySet();
        for(String key:keys){
            Object o=beanMap.get(key);
            //3.判断这些object是否为 参数类型的实例 instanceof
            if(o.getClass().getName().equalsIgnoreCase(typeClass.getName())){
                //5.invoke
                m.invoke(obj,o);
            }
        }

        //4.如果是，则从beanMap取出

        //m.invoke(obj,);
    }

    /*
    扫描包和子包
     */
    private void scanPackageAndSubPackageClasses(String basePackage) throws IOException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String packagePath=basePackage.replaceAll("\\.","/");
        System.out.println("扫描包路径:"+basePackage+",替换后："+packagePath);  //com.yc.bean  -> com/yc/bean
        Enumeration<URL> files=Thread.currentThread().getContextClassLoader().getResources(packagePath);
        while (files.hasMoreElements()){
            URL url=files.nextElement();
            System.out.println("配置的扫描路径为："+url.getFile());//D:/IdeaProjects/testspring/.....
            //TODO:递归这些目录 查找.class文件
            findClassesInPackages(url.getFile(),basePackage);
        }
    }

    private Set<Class> managedBeanClasses=new HashSet<Class>();
    /**查找 file下面及子包所有的要托管的class，存到一个Set(manageBeanClasses)中 */
    private void findClassesInPackages(String file, String basePackage) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        File f=new File(file);
        //File[]ff=f.listFiles();
        //文件过滤 得到需要的文件
        File[] classFiles=f.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".class")|| file.isDirectory();
            }
        });
        //System.out.println(classFiles);
        for(File cf:classFiles){
            if(cf.isDirectory()){
                //如果目录 则递归
                //拼接子目录
                basePackage+="."+cf.getName().substring(cf.getName().lastIndexOf("/")+1);
                findClassesInPackages(cf.getAbsolutePath(),basePackage);
            }else{
                //加载cf 作为class文件
                URL[] urls=new URL[]{};
                URLClassLoader ucl=new URLClassLoader(urls);
                //com.yc.bean.Hello.class  -> com.yc.bean.Hello
                Class c=ucl.loadClass(basePackage+"."+cf.getName().replace(".class",""));
                managedBeanClasses.add(c);
            }
            handleManagedBean();
        }


    }

    /**
     * 处理manageBeanClasses 所有的Class类  筛选出所有的 @Component  @Service @Respository的类 并实例化 存到beanMap中
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void handleManagedBean() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        for(Class c:managedBeanClasses){
            if(c.isAnnotationPresent(MyComponent.class)){
               saveManagedBean(c);
            }else if(c.isAnnotationPresent(MyService.class)){
                saveManagedBean(c);
            }else if(c.isAnnotationPresent(MyRepository.class)){
                saveManagedBean(c);
            }else if(c.isAnnotationPresent(MyController.class)){
                saveManagedBean(c);
            }else{
                continue;
            }
        }
    }

    private void saveManagedBean(Class c) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Object o=c.newInstance();
        handlePostConstruct(o,c);
        String beanId=c.getSimpleName().substring(0,1).toLowerCase()+c.getSimpleName().substring(1);
        beanMap.put(beanId,o);
    }


    /**
     * 处理MyAppConfig配置类中的@Bean注解 完成IOC操作
     * @param cls
     * @param obj
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void handleAtMyBean(Class cls,Object obj) throws InvocationTargetException, IllegalAccessException {
        ///1.获取cls中所有的method
       Method[] ms= cls.getDeclaredMethods();
        //2.循环判断每个method是否有@MyBean的注解
        for(Method m: ms){
            //3.有则invoke它   它有返回值 将返回值存到beanMap 键是方法名 值是返回值对象
            if(m.isAnnotationPresent(MyBean.class)){
                Object o=m.invoke(obj);
                //TODO: 加入处理 @MyBean注解对应的方法所实例化的类中的 @MyPostConstruct 对应的方法
                handlePostConstruct(o,o.getClass());//o在这指 HelloWorld对象 o.getClass() 反射对象
                beanMap.put(m.getName(),o);
            }
        }

    }

    /**
     * 处理一个Bean中的 @MyPostConstruct对应的方法
     * @param o
     * @param cls
     */
    private void handlePostConstruct(Object o, Class<?> cls) throws InvocationTargetException, IllegalAccessException {
        Method[] ms=cls.getDeclaredMethods();
        for(Method m:ms){
            if(m.isAnnotationPresent(MyPostConstruct.class)){
                m.invoke(o);
            }
        }
    }

    /*
    获取当前 AppConfig类所在的包路径
     */
    private String[] getAppConfigBasePackages(Class cl) {
        String []paths=new String[1];
        paths[0]=cl.getPackage().getName();
        return paths;
    }

    @Override
    public Object getBean(String id) {
        return beanMap.get(id);
    }
}
