package yyl.mvc.common.oshi;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.ComputerSystem;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.hardware.Sensors;
import oshi.hardware.VirtualMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import yyl.mvc.common.oshi.model.CpuInfo;
import yyl.mvc.common.oshi.model.DiskInfo;
import yyl.mvc.common.oshi.model.MemoryInfo;
import yyl.mvc.common.oshi.model.NetIoInfo;
import yyl.mvc.common.oshi.model.OsInfo;
import yyl.mvc.common.oshi.model.SystemLoadInfo;
import yyl.mvc.common.thread.ThreadUtil;

/**
 * _Oshi库封装的工具类，通过此工具类，可获取系统、硬件相关信息
 */
public class OshiUtil {

    /** 保留两位小数 */
    private static final DecimalFormat PERCENTAGE_FORMAT = new DecimalFormat("#.00");
    /** 负载返回数组长度 */
    private static final int LOAD_NELEM = 3;

    /** 系统信息（OSHI的主要入口点） */
    private static final SystemInfo SYSTEM_INFO;
    /** 硬件信息 */
    private static final HardwareAbstractionLayer HARDWARE;
    /** 操作系统信息 */
    private static final OperatingSystem OS;

    static {
        SYSTEM_INFO = new SystemInfo();
        HARDWARE = SYSTEM_INFO.getHardware();
        OS = SYSTEM_INFO.getOperatingSystem();
    }

    /**
     * 获取操作系统相关信息，包括系统版本、文件系统、进程等
     * @return 操作系统相关信息
     */
    public static OperatingSystem getOperatingSystem() {
        return OS;
    }

    /**
     * 获取当前进程信息{@link OSProcess}
     * @return 进程信息 {@link OSProcess}
     */
    public static OSProcess getCurrentProcess() {
        return OS.getProcess(OS.getProcessId());
    }

    /**
     * 获取硬件相关信息，包括内存、硬盘、网络设备、显示器、USB、声卡等
     * @return 硬件相关信息
     */
    public static HardwareAbstractionLayer getHardware() {
        return HARDWARE;
    }

    /**
     * 获取BIOS中计算机相关信息，比如序列号、固件版本等
     * @return 获取BIOS中计算机相关信息
     */
    public static ComputerSystem getSystem() {
        return HARDWARE.getComputerSystem();
    }

    /**
     * 获取内存相关信息，比如总内存、可用内存等
     * @return 内存相关信息
     */
    public static GlobalMemory getMemory() {
        return HARDWARE.getMemory();
    }

    /**
     * 获取CPU（处理器）相关信息，比如CPU负载等
     * @return CPU（处理器）相关信息
     */
    public static CentralProcessor getProcessor() {
        return HARDWARE.getProcessor();
    }

    /**
     * 获取传感器相关信息，例如CPU温度、风扇转速等，传感器可能有多个
     * @return 传感器相关信息
     */
    public static Sensors getSensors() {
        return HARDWARE.getSensors();
    }

    /**
     * 获取磁盘相关信息，可能有多个磁盘（包括可移动磁盘等）
     * @return 磁盘相关信息
     */
    public static List<HWDiskStore> getDiskStores() {
        return HARDWARE.getDiskStores();
    }

    /**
     * 获取网络相关信息，可能多块网卡
     * @return 网络相关信息
     */
    public static List<NetworkIF> getNetworkIFs() {
        return HARDWARE.getNetworkIFs();
    }

    /**
     * 获取系统CPU 系统使用率、用户使用率、利用率等等 相关信息<br>
     * @return 系统 CPU 使用率 等信息
     */
    public static CpuInfo getCpuInfo() {
        return getCpuInfo(1000L);
    }

    /**
     * 获取系统CPU 系统使用率、用户使用率、利用率等等 相关信息
     * @param waitingTime 设置等待时间，单位毫秒
     * @return 系统 CPU 使用率 等信息
     */
    public static CpuInfo getCpuInfo(long waitingTime) {

        CentralProcessor processor = HARDWARE.getProcessor();
        // CPU负载滴答计数器信息
        long[] previousTicks = processor.getSystemCpuLoadTicks();
        // 等待一定时间
        ThreadUtil.sleepQuietly(waitingTime);
        long[] currentTicks = processor.getSystemCpuLoadTicks();

        long user = getTickDifference(previousTicks, currentTicks, TickType.USER);
        long nice = getTickDifference(previousTicks, currentTicks, TickType.NICE);
        long system = getTickDifference(previousTicks, currentTicks, TickType.SYSTEM);
        long idle = getTickDifference(previousTicks, currentTicks, TickType.IDLE);
        long iowait = getTickDifference(previousTicks, currentTicks, TickType.IOWAIT);
        long irq = getTickDifference(previousTicks, currentTicks, TickType.IRQ);
        long softIrq = getTickDifference(previousTicks, currentTicks, TickType.SOFTIRQ);
        long steal = getTickDifference(previousTicks, currentTicks, TickType.STEAL);
        long total = Math.max(user + nice + system + idle + iowait + irq + softIrq + steal, 0);

        int cores = processor.getLogicalProcessorCount();
        String model = processor.toString();

        CpuInfo info = new CpuInfo();
        info.setCores(cores);
        info.setModel(model);
        info.setSys(getPercentage(system, total));
        info.setUsed(getPercentage(user, total));
        info.setWait(getPercentage(iowait, total));
        info.setFree(getPercentage(idle, total));
        return info;
    }

    /**
     * 获取内存使用相关信息
     * @return 内存信息
     */
    public static MemoryInfo getMemoryInfo() {
        GlobalMemory memory = HARDWARE.getMemory();
        long total = memory.getTotal();
        long free = memory.getAvailable();
        long used = total - free;
        double usePercent = getPercentage(used, total);

        VirtualMemory virtual = memory.getVirtualMemory();
        long swapTotal = virtual.getSwapTotal();
        long swapUsed = virtual.getSwapUsed();
        long swapFree = swapTotal - swapUsed;
        double swapUsePercent = getPercentage(swapUsed, swapTotal);

        MemoryInfo info = new MemoryInfo();
        info.setTotal(total);
        info.setFree(free);
        info.setUsed(used);
        info.setUsePercent(usePercent);
        info.setSwapTotal(swapTotal);
        info.setSwapFree(swapFree);
        info.setSwapUsed(swapUsed);
        info.setSwapUsePercent(swapUsePercent);
        return info;
    }

    /**
     * 获取系统负载信息<br>
     * @return 系统负载信息
     */
    public static SystemLoadInfo getSystemLoadInfo() {
        CentralProcessor processor = HARDWARE.getProcessor();
        double[] loadAverage = processor.getSystemLoadAverage(LOAD_NELEM);
        SystemLoadInfo info = new SystemLoadInfo();
        info.setOneLoad(loadAverage[0]);
        info.setFiveLoad(loadAverage[1]);
        info.setFifteenLoad(loadAverage[2]);
        return info;
    }

    /**
     * 获取网络带宽信息<br>
     * @return 网络带宽信息
     */
    public static NetIoInfo getNetIoInfo() {
        return getNetIoInfo(3000L);
    }

    /**
     * 获取网络带宽信息<br>
     * @param waitingTime 设置等待时间，单位毫秒
     * @return 网络带宽信息
     */
    public static NetIoInfo getNetIoInfo(long waitingTime) {

        long previous = System.currentTimeMillis();
        long previousBytesRecv = 0;
        long previousBytesSent = 0;
        long previousPacketsRecv = 0;
        long previousPacketsSent = 0;
        for (NetworkIF net : HARDWARE.getNetworkIFs()) {
            previousBytesRecv += net.getBytesRecv();
            previousBytesSent += net.getBytesSent();
            previousPacketsRecv += net.getPacketsRecv();
            previousPacketsSent += net.getPacketsSent();
        }

        ThreadUtil.sleepQuietly(waitingTime);

        long current = System.currentTimeMillis();
        long currentBytesRecv = 0;
        long currentBytesSent = 0;
        long currentPacketsRecv = 0;
        long currentPacketsSent = 0;

        for (NetworkIF net : HARDWARE.getNetworkIFs()) {
            currentBytesRecv += net.getBytesRecv();
            currentBytesSent += net.getBytesSent();
            currentPacketsRecv += net.getPacketsRecv();
            currentPacketsSent += net.getPacketsSent();
        }

        double intervalSecond = (current - previous) / 1000D;

        long rxbyt = (long) ((previousBytesRecv - currentBytesRecv) / intervalSecond / 1024);
        long txbyt = (long) ((previousBytesSent - currentBytesSent) / intervalSecond / 1024);
        long rxpck = (long) ((previousPacketsRecv - currentPacketsRecv) / intervalSecond / 1024);
        long txpck = (long) ((previousPacketsSent - currentPacketsSent) / intervalSecond / 1024);

        NetIoInfo info = new NetIoInfo();
        info.setRxbyt(rxbyt);
        info.setTxbyt(txbyt);
        info.setRxpck(rxpck);
        info.setTxpck(txpck);
        return info;
    }

    /**
     * 磁盘存储信息<br>
     * @param waitingTime 设置等待时间，单位毫秒
     * @return 网络带宽信息
     */
    public static List<DiskInfo> getDiskStoreInfo(long waitingTime) {
        FileSystem fileSystem = OS.getFileSystem();
        List<DiskInfo> infos = new ArrayList<>();
        for (OSFileStore osfs : fileSystem.getFileStores()) {
            String name = osfs.getName();
            String mount = osfs.getMount();
            String type = osfs.getType();
            long total = osfs.getTotalSpace();
            long free = osfs.getUsableSpace();
            long used = total - free;
            double usePercent = getPercentage(used, total);
            DiskInfo info = new DiskInfo();
            info.setName(name);
            info.setMount(mount);
            info.setType(type);
            info.setTotal(total);
            info.setUsed(used);
            info.setFree(free);
            info.setUsePercent(usePercent);
        }
        return infos;
    }

    /**
     * 获取操作系统信息<br>
     * @return 操作系统信息
     */
    public static OsInfo getOsInfo() {
        OsInfo info = new OsInfo();
        info.setDetail(OS.toString());
        return info;
    }

    /**
     * 获取一段时间内的CPU负载标记差
     * @param previousTicks 开始的ticks
     * @param currentTicks 结束的ticks
     * @param tickType tick类型
     * @return 标记差
     */
    private static long getTickDifference(long[] previousTicks, long[] currentTicks, CentralProcessor.TickType tickType) {
        return currentTicks[tickType.getIndex()] - previousTicks[tickType.getIndex()];
    }

    /**
     * 返回百分比
     * @param used 使用量
     * @param total 总量
     * @return 百分比
     */
    private static double getPercentage(long value, long total) {
        if (total == 0 || value == 0) {
            return 0.00D;
        }
        return Double.parseDouble(PERCENTAGE_FORMAT.format(100D * value / total));
    }
}
