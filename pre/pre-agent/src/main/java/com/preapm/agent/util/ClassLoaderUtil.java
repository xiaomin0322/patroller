package com.preapm.agent.util;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.preapm.agent.bean.PluginConfigYaml.JarBean;
import com.preapm.agent.weave.impl.AroundInterceptorCollector;

public class ClassLoaderUtil {

	private static Logger log = LogManager.getLogger(ClassLoaderUtil.class);

	private static Set<String> loadPluginsJar = new HashSet<String>();

	private static volatile boolean initFlag = false;

	public static synchronized void init(ClassLoader classLoader) {
		if(initFlag) {
			return ;
		}
		File pluginDir = new File(PathUtil.getProjectPath(), "plugin");
		for (String name : com.preapm.agent.util.PreConfigUtil.getBasePlginName()) {
			if (loadPluginsJar.contains(name)) {
				continue;
			}
			File preZipkinSdkFile = new File(pluginDir, name + ".jar");
			loadJar(classLoader, preZipkinSdkFile.getAbsolutePath());
			loadPluginsJar.add(name);
		}
		initFlag = true;
	}

	public static void loadJarByClassName(ClassLoader classLoader, String className) {
		//classLoader = ClassLoader.getSystemClassLoader();
		/*if (!initFlag) {
			init(classLoader);
		}*/

		File pluginDir = new File(PathUtil.getProjectPath(), "plugin");
		Set<JarBean> plugins = PreConfigUtil.getPlugins(className);
		if (plugins != null) {
			for (JarBean p : plugins) {
				if (loadPluginsJar.contains(p.getJarName())) {
					continue;
				}
				File pFile = new File(pluginDir, p.getJarName() + ".jar");
				log.info(
						"className:" + className + "   加载插件包路径>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + pFile.getAbsolutePath());
				if (pFile.exists()) {
					/*if(p.getJarName().contains("jdk")) {
						classLoader = ClassLoader.getSystemClassLoader();
					}*/
					loadJar(classLoader, pFile.getAbsolutePath());
					loadPluginsJar.add(p.getJarName());
					//放此处再初始化zipkin-sdk.jar。为了解决延迟slf4j初始化，找不到日志实现。优先让应用程序初始化slf4j。以第一次插件加载器加载
					com.preapm.agent.util.ClassLoaderUtil.init(classLoader);
				}
			}
		}

	}

	public static String getJARPath() {
		String decode = AroundInterceptorCollector.class.getProtectionDomain().getCodeSource().getLocation().getFile();

		return decode.substring(0, decode.lastIndexOf("/"));
	}

	public static void loadJar(ClassLoader classLoader, String path) {
		// 系统类库路径
		File libPath = new File(path);

		File[] jarFiles = null;
		if (!libPath.isFile()) {
			// 获取所有的.jar和.zip文件
			jarFiles = libPath.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(".jar") || name.endsWith(".zip");
				}
			});
		} else {
			jarFiles = new File[] { libPath };
		}

		if (jarFiles != null) {
			// 从URLClassLoader类中获取类所在文件夹的方法
			// 对于jar文件，可以理解为一个存放class文件的文件夹
			Method method = null;
			try {
				method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			} catch (NoSuchMethodException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			boolean accessible = method.isAccessible(); // 获取方法的访问权限
			try {
				if (accessible == false) {
					method.setAccessible(true); // 设置方法的访问权限
				}
				// 获取系统类加载器
				if (classLoader == null) {
					classLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
				}
				;
				for (File file : jarFiles) {
					try {
						URL url = file.toURI().toURL();
						method.invoke(classLoader, url);
						log.info("classLoaderName:" + classLoader.getClass().getName() + " 读取jar文件成功" + file.getName());
					} catch (Exception e) {
						System.out.println("读取jar文件失败");
					}
				}
			} finally {
				method.setAccessible(accessible);
			}
		}
	}

	public static void main(String[] args) {
		loadJar(null, "C:\\eclipse-workspace\\test\\target");
	}
}
