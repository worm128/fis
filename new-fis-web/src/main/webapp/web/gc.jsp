<%@page import="java.lang.management.*"%>
<%@page import="java.util.List"%>

<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>查看jvm内存</title>
</head>
<body>
查看jvm内存:<br/><br/>

<%
	MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean();
	MemoryUsage usage = memorymbean.getHeapMemoryUsage();
	out.println("INIT HEAP: " + usage.getInit()/1024+"k<br/>");
	out.println("MAX HEAP: " + usage.getMax()/1024+"k<br/>");
	out.println("USE HEAP: " + usage.getUsed()/1024+"k<br/>");
	out.println("\nFull Information:"+"<br/>");
	out.println("Heap Memory Usage: "
			+ memorymbean.getHeapMemoryUsage()+"<br/>");
	out.println("Non-Heap Memory Usage: "
			+ memorymbean.getNonHeapMemoryUsage()+"<br/>");

	List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
	out.println("===================java options=============== "+"<br/>");
	out.println(inputArguments+"<br/>");



	out.println("=======================通过java来获取相关系统状态============================ "+"<br/>");
	int i = (int)Runtime.getRuntime().totalMemory()/1024;//Java 虚拟机中的内存总量,以字节为单位  
	out.println("总的内存量 i is "+i+"<br/>");
	int j = (int)Runtime.getRuntime().freeMemory()/1024;//Java 虚拟机中的空闲内存量  
	out.println("空闲内存量 j is "+j+"<br/>");
	out.println("最大内存量 is "+Runtime.getRuntime().maxMemory()/1024+"<br/>");

	out.println("=======================OperatingSystemMXBean============================ "+"<br/>");
	OperatingSystemMXBean osm = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
//    out.println(osm.getFreeSwapSpaceSize()/1024+"<br/>");  
//    out.println(osm.getFreePhysicalMemorySize()/1024+"<br/>");  
//    out.println(osm.getTotalPhysicalMemorySize()/1024+"<br/>");  

	//获取操作系统相关信息  
	out.println("osm.getArch() "+osm.getArch()+"<br/>");
	out.println("osm.getAvailableProcessors() "+osm.getAvailableProcessors()+"<br/>");
	//out.println("osm.getCommittedVirtualMemorySize() "+osm.getCommittedVirtualMemorySize()+"<br/>");  
	out.println("osm.getName() "+osm.getName()+"<br/>");
	//out.println("osm.getProcessCpuTime() "+osm.getProcessCpuTime()+"<br/>");  
	out.println("osm.getVersion() "+osm.getVersion()+"<br/>");
	//获取整个虚拟机内存使用情况  
	out.println("=======================MemoryMXBean============================ "+"<br/>");
	MemoryMXBean mm=(MemoryMXBean)ManagementFactory.getMemoryMXBean();
	out.println("getHeapMemoryUsage "+mm.getHeapMemoryUsage()+"<br/>");
	out.println("getNonHeapMemoryUsage "+mm.getNonHeapMemoryUsage()+"<br/>");
	//获取各个线程的各种状态，CPU 占用情况，以及整个系统中的线程状况  
	out.println("=======================ThreadMXBean============================ "+"<br/>");
	ThreadMXBean tm=(ThreadMXBean)ManagementFactory.getThreadMXBean();
	out.println("getThreadCount "+tm.getThreadCount()+"<br/>");
	out.println("getPeakThreadCount "+tm.getPeakThreadCount()+"<br/>");
	out.println("getCurrentThreadCpuTime "+tm.getCurrentThreadCpuTime()+"<br/>");
	out.println("getDaemonThreadCount "+tm.getDaemonThreadCount()+"<br/>");
	out.println("getCurrentThreadUserTime "+tm.getCurrentThreadUserTime()+"<br/>");

	//当前编译器情况  
	out.println("=======================CompilationMXBean============================ "+"<br/>");
	CompilationMXBean gm=(CompilationMXBean)ManagementFactory.getCompilationMXBean();
	out.println("getName "+gm.getName()+"<br/>");
	out.println("getTotalCompilationTime "+gm.getTotalCompilationTime()+"<br/>");

	//获取多个内存池的使用情况  
	out.println("=======================MemoryPoolMXBean============================ "+"<br/>");
	List<MemoryPoolMXBean> mpmList=ManagementFactory.getMemoryPoolMXBeans();
	for(MemoryPoolMXBean mpm:mpmList){
		out.println("getUsage "+mpm.getUsage()+"<br/>");
		out.println("getMemoryManagerNames "+mpm.getMemoryManagerNames().toString()+"<br/>");
	}
	//获取GC的次数以及花费时间之类的信息  
	out.println("=======================MemoryPoolMXBean============================ "+"<br/>");
	List<GarbageCollectorMXBean> gcmList= ManagementFactory.getGarbageCollectorMXBeans();
	for(GarbageCollectorMXBean gcm:gcmList){
		out.println("getName "+gcm.getName()+"<br/>");
		out.println("getMemoryPoolNames "+gcm.getMemoryPoolNames()+"<br/>");
	}
	//获取运行时信息  
	out.println("=======================RuntimeMXBean============================ "+"<br/>");
	RuntimeMXBean rmb=(RuntimeMXBean)ManagementFactory.getRuntimeMXBean();
	out.println("getClassPath "+rmb.getClassPath()+"<br/>");
	out.println("getLibraryPath "+rmb.getLibraryPath()+"<br/>");
	out.println("getVmVersion "+rmb.getVmVersion()+"<br/>");
%>

</body>
</html>