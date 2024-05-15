package com.spkiddai;

import java.lang.instrument.Instrumentation;


public class JavaAgent {

	/**
	 * 主要函数 agentmain
	 *
	 * @param inst Instrumentation对象
	 */
	public static void agentmain(String agentArgs, Instrumentation inst) {
		inst.addTransformer(new AgentTransformer(), true);

		if ("Test".equals(agentArgs)) {
			retransformSpecificClass(inst, "com.spkiddai.memoryserver.Servlet.HelloServlet");
		} else if ("Rasp".equals(agentArgs)) {
			retransformSpecificClass(inst, "org.apache.catalina.core.StandardContext");
			retransformSpecificClass(inst, "com.sun.tools.attach.VirtualMachine");
			retransformSpecificClass(inst, "sun.instrument.InstrumentationImpl");
		}
	}

	private static void retransformSpecificClass(Instrumentation inst, String className) {
		for (Class<?> clazz : inst.getAllLoadedClasses()) {
			if (clazz.getName().equals(className) && inst.isModifiableClass(clazz)) {
				try {
					System.out.println("Add JavaAgent Classes: " + className);
					inst.retransformClasses(clazz);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
