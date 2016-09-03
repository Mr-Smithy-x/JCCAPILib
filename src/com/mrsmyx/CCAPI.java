package com.mrsmyx;

import com.mrsmyx.core.CCAPIERROR;
import com.mrsmyx.core.CCAPIException;
import com.mrsmyx.models.ConsoleInfo;
import com.mrsmyx.models.Process;
import com.mrsmyx.models.Temperature;
import com.mrsmyx.network.CCNetwork;

import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr Smithy x on 1/3/16.
 */
public class CCAPI extends CCNetwork {

    private String process = "0";

    /**
     * Construct the CCAPI Class, Not Recommended
     @deprecated
     @param ip IP of the ps3 you want to connect to
     @param port Port of the ps3 you want to connect to
    */
    public CCAPI(String ip, int port) {
        super(ip, port);
    }

    /**
     * Construct the CCAPI class
     * @param ip Connect to the ps3 by ip
     */
    public CCAPI(String ip) {
        super(ip, 6333);
    }

    /**
     * Change the ip address you want to connect to.
     * @param ip Change the ip of the ps3 if changed.
     */
    public void changeIP(String ip) {
        super.ip = ip;
    }

    /**
     * Gets the ip address
     * @return IP Address of the ps3
     */
    public String getIP() {
        return super.ip;
    }

    /**
     * Getss the current process.
     * @return The Process the ps3 is currently attached to.
     */
    public String getAttachedProcess() {
        return this.process;
    }

    /**
     * Gets the current temperature
     * @see Temperature
     * @return The RSX & CELL Temperature of the PS3 in both Celsius and Fahrenheit
     * @throws IOException
     */
    public Temperature getTemperature() throws IOException {
        List<String> temp = super.getListRequest(super.compileUrl(CCFactory.getTemperature()));
        return Temperature.Builder(String.valueOf(Integer.decode("0x" + temp.get(1))), String.valueOf(Integer.decode("0x" + temp.get(2))));
    }

    /**
     * Gets the process running on the ps3
     * @see Process
     * @return All the process that the PS3 has running.
     * @throws IOException
     */
    public List<Process> getProcesses() throws IOException {
        List<Process> processList = new ArrayList<Process>();
        for (String s : getProcessList()) {
            if (!s.equals("0")) {
                for (String x : getProcessName(s)) {
                    if (!x.equals("0")) {
                        processList.add(Process.Builder().setPid(s).setPidName(x));
                    }
                }
            }
        }
        return processList;
    }

    /**
     * Set the mode for the ps3
     * @param boot SHUTDOWN, SOFTBOOT, HARDBOOT
     * @throws IOException
     */
    public void shutDown(ShutdownMode boot) throws IOException {
        super.doRequest(super.compileUrl(CCFactory.shutDown(boot)));
    }

    /**
     * Send a message to the ps3
     * @param notifyicons A Selection of notify icons you can choose from
     * @param message A Message you want to send to the ps3
     * @return 0
     * @throws IOException
     */
    public String notify(NotifyIcon notifyicons, String message) throws IOException {
        return super.getSimpleRequest(super.compileUrl(CCFactory.notify(notifyicons, URLEncoder.encode(message,"UTF-8").replace("+","%20"))));
    }

    /**
     * Set the led color and status (on or off)
     * @param color Colors of the ps3 LED
     * @param led OFF or ON
     * @return 0
     * @throws IOException
     */
    public String setConsoleLed(LedColor color, LedStatus led) throws IOException {
        return super.getSimpleRequest(super.compileUrl(CCFactory.setConsoleLed(color, led)));
    }

    /**
     * Set the psid or idps of the ps3
     * @param type IDPS or PSID
     * @param id The ID here
     * @return
     * @throws IOException
     */
    public String setConsoleIds(ConsoleIdType type, String id) throws IOException {
        return super.getSimpleRequest(super.compileUrl(CCFactory.setConsoleIds(type, id)));
    }

    /**
     * Set the psid of idps of the ps3 on boot or remove the onboot trigger
     * @param type IDPS or PSID
     * @param onBoot OnBoot or Turn off OnBoot.
     * @param id The ID Here
     * @return 0
     * @throws IOException
     */
    public String setBootConsoleIds(ConsoleIdType type, boolean onBoot, String id) throws IOException {
        return super.getSimpleRequest(super.compileUrl(CCFactory.setBootConsoleIds(type, onBoot, id)));
    }


    /**
     * Send a buzz to the ps3
     * @param buzzer Continuous, Single, Double, Triple
     * @return 0
     * @throws IOException
     */
    public String ringBuzzer(BuzzerType buzzer) throws IOException {
        return super.getSimpleRequest(super.compileUrl(CCFactory.ringBuzzer(buzzer)));
    }

    /**
     * Attaches to the game process
     * @return Whether the attachment was successful
     * @throws IOException
     */
    public boolean attach() throws IOException {
        for (Process process : getProcesses()) {
            if (process.getPidName().contains("EBOOT.BIN")) {
                attach(process.getPid());
                return true;
            }
        }
        detach();
        return false;
    }

    /**
     * Detaches from the ps3.
     */
    public void detach() {
        this.process = "0";
    }

    /**
     * @return If ps3 is currently attached to a process id.
     */
    public boolean isProcessAttached() {
        return !this.process.equals("0");
    }

    /**
     * @param pid Process ID
     * @param addr Address
     * @param value Value you want to set
     * @return 0
     * @throws IOException
     */
    private String setMemory(String pid, String addr, String value) throws IOException {
        return super.getSimpleRequest(super.compileUrl(CCFactory.setMemory(pid, addr, value)));
    }

    /**
     * <p>For those who rather set memory using the char[], byte is signed (annoying)</p>
     * @param addr 0x Format
     * @param value char array format
     * @return 0
     * @throws CCAPIException
     * @throws IOException
     */
    public String setMemory(int addr, char[] value) throws CCAPIException, IOException {
        if (!process.equals("0")) {
            return setMemory(process, Integer.toHexString(addr), Hex.encodeHexString(value, false));
        } else {
            throw new CCAPIException(CCAPIERROR.NOT_ATTACHED, "You are not attached to any processes");
        }
    }

    /**
     * <p>For those who prefer to use byte[] over char[], ie. (byte)0xFF</p>
     * @param addr 0x format
     * @param value byte array format, value you want to set
     * @return
     * @throws CCAPIException
     * @throws IOException
     */
    public String setMemory(int addr, byte[] value) throws CCAPIException, IOException {
        if (!process.equals("0")) {
            return setMemory(process, Integer.toHexString(addr), Hex.encodeHexString(value, false));
        } else {
            throw new CCAPIException(CCAPIERROR.NOT_ATTACHED, "You are not attached to any processes");
        }
    }

    /**
     * <p>Get the memory of the ps3</p>
     * @param addr 0x format
     * @param size size you want to get
     * @return memory byte[]
     * @throws Exception
     * @throws CCAPIException
     */
    public byte[] getMemory(int addr, int size) throws Exception, CCAPIException {
        String s = getMemoryString(Integer.toHexString(addr), size);
        byte[] bytes = new byte[s.length() / 2];
        for (int i = 0; i < s.length(); i += 2)
            bytes[i / 2] = (byte) ((int) Long.parseLong(String.valueOf(s.charAt(i)) + String.valueOf(s.charAt(i + 1)), 16));
        return bytes;
    }

    /**
     * <p>Get the memory of the ps3</p>
     * @param addr 0x format
     * @param size size you want to recieve
     * @return char[] memory
     * @throws Exception
     * @throws CCAPIException
     */
    public char[] getMemoryTC(int addr, int size) throws Exception, CCAPIException {
        String s = getMemoryString(Integer.toHexString(addr), size);
        char[] chars = new char[s.length() / 2];
        for (int i = 0; i < s.length(); i += 2)
            chars[i / 2] = (char) ((int) Long.parseLong(String.valueOf(s.charAt(i)) + String.valueOf(s.charAt(i + 1)), 16));
        return chars;
    }

    private String getMemoryString(String pid, String addr, int size) throws IOException {
        return super.getSimpleRequest(super.compileUrl(CCFactory.getMemory(pid, addr, size))).substring(1);
    }

    private String getMemoryString(String addr, int size) throws CCAPIException, IOException {
        if (!process.equals("0")) {
            return getMemoryString(this.process, addr, size);
        } else {
            throw new CCAPIException(CCAPIERROR.NOT_ATTACHED, "You are not attached to any processes");
        }
    }

    /**
     * Receives the console information
     * @return Console Information
     * @throws IOException
     * @throws IndexOutOfBoundsException
     * @see ConsoleInfo
     */
    public ConsoleInfo getConsoleInfo() throws IOException, IndexOutOfBoundsException {
        Temperature temperature = getTemperature();
        List<String> firmware = getFirmwareInfo();
        String firm = "";
        int occur = 0;
        for (char c : firmware.get(1).substring(0, 4).toCharArray()) {
            if (c == '0' && occur < 1) {
                firm += ".";
                occur++;
            } else firm += c;
        }
        return ConsoleInfo.Builder().setTemperature(temperature).setFirmware(firm).setType(ConsoleType.values()[Integer.valueOf(firmware.get(3))]);
    }

    //region Enumerations
    public enum NotifyIcon {
        INFO, CAUTION, FRIEND,
        SLIDER, WRONGWAY, DIALOG,
        DIALOGSHADOW, TEXT, POINTER,
        GRAB, HAND, PEN, FINGER, ARROW,
        ARROWRIGHT, PROGRESS, TROPHY1,
        TROPHY2, TROPHY3, TROPHY4
    }

    public enum ShutdownMode {
        SHUTDOWN(1),
        SOFTBOOT(2),
        HARDBOOT(3);

        int val;

        ShutdownMode(int val) {
            this.val = val;
        }

        public int getVal() {
            return val;
        }
    }

    public enum LedColor {
        RED, GREEN
    }

    public enum ConsoleIdType {
        IDPS, PSID
    }

    public enum LedStatus {
        OFF, ON, BLINK
    }

    public enum ConsoleType {
        UNK,
        CEX,
        DEX,
        TOOL
    }

    public enum BuzzerType {
        CONTINUOUS, SINGLE, DOUBLE, TRIPLE
    }
    //endregion

    //region Private Methods
    private List<String> getFirmwareInfo() throws IOException {
        return super.getListRequest(super.compileUrl(CCFactory.getFirmWareInfo()));
    }

    private List<String> getProcessList() throws IOException {
        return super.getListRequest(super.compileUrl(CCFactory.getProcessList()));
    }

    private List<String> getProcessName(String pid) throws IOException {
        return super.getListRequest(super.compileUrl(CCFactory.getProcessName(pid)));
    }

    private void attach(String pid) {
        this.process = pid;
    }
    //endregion

    private static class CCFactory {

        private enum DIR {
            GETFIRMWAREINFO,
            SETBOOTCONSOLEIDS,
            SETCONSOLEIDS,
            SHUTDOWN,
            GETTEMPERATURE,
            GETPROCESSLIST,
            SETMEMORY,
            GETMEMORY,
            GETPROCESSNAME,
            SETCONSOLELED,
            NOTIFY,
            RINGBUZZER,
        }

        public static String getFirmWareInfo() {
            return DIR.GETFIRMWAREINFO.name().toLowerCase();
        }

        public static String getTemperature() {
            return DIR.GETTEMPERATURE.name().toLowerCase();
        }

        public static String getProcessList() {
            return DIR.GETPROCESSLIST.name().toLowerCase();
        }

        public static String getProcessName(String pid) {
            return DIR.GETPROCESSNAME.name().toLowerCase() + "?pid=" + pid;
        }

        public static String shutDown(ShutdownMode boot) {
            return DIR.SHUTDOWN.name().toLowerCase() + "?mode=" + String.valueOf(boot.getVal());
        }

        public static String notify(NotifyIcon notifyicons, String message) {
            return DIR.NOTIFY.name().toLowerCase() + "?id=" + String.valueOf(notifyicons.ordinal()) + "&msg=" + message.replace(" ", "%20");
        }

        public static String setConsoleLed(LedColor color, LedStatus led) {
            return DIR.SETCONSOLELED.name().toLowerCase() + "?color=" + String.valueOf(color.ordinal()) + "&status=" + String.valueOf(led);
        }

        public static String setConsoleIds(ConsoleIdType type, String id) {
            return DIR.SETCONSOLEIDS.name().toLowerCase() + String.format("?type=%s&id=%s", type.ordinal(), id);
        }

        public static String setBootConsoleIds(ConsoleIdType type, boolean onBoot, String id) {
            return DIR.SETBOOTCONSOLEIDS.name().toLowerCase() + String.format("?type=%s&on=%s&id=%s", type.ordinal(), onBoot ? 1 : 0, id);
        }

        public static String ringBuzzer(BuzzerType buzzer) {
            return DIR.RINGBUZZER.name().toLowerCase() + "?type=" + String.valueOf(buzzer.ordinal());
        }

        public static String setMemory(String pid, String addr, String value) {
            return DIR.SETMEMORY.name().toLowerCase() + String.format("?pid=%s&addr=%s&value=%s", pid, addr, value);
        }


        public static String getMemory(String pid, String addr, int size) {
            return DIR.GETMEMORY.name().toLowerCase() + String.format("?pid=%s&addr=%s&size=%s", pid, addr, size);
        }

    }

}
