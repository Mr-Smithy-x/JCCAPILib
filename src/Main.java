import com.mrsmyx.CCAPI;
import com.mrsmyx.models.ConsoleInfo;
import com.mrsmyx.models.Process;
import java.io.IOException;
import java.util.Scanner;


public class Main {

    /* Attaching & Writing & Getting Memory
     *
            if (ccapi.attach()) {
                byte[] bytes = {0x38, 0x60, 0x7F, (byte) 0xFF, (byte) 0xB0, 0x7F, 0x00, (byte) 0xB4};
                try {
                    ccapi.setMemory(0x118A548, bytes);
                    byte[] b = ccapi.getMemory(0x118A548, 8);
                    System.out.println(Hex.encodeHex(b, false));
                } catch (CCAPIException | Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Process Not Attached");
            }
     *
     */


    public static void main(String[] args) {
        CCAPI c = null;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your PS3 IP: ");
        String ip = scanner.nextLine();
        CCAPI ccapi = new CCAPI(ip);
        try {
            ConsoleInfo consoleInfo = ccapi.getConsoleInfo();
            System.out.printf("Firmware: %s\nCell: %s (C)\tRSX: %s (C)\nCell: %s (F)\tRSX: %s (F)\nConsole Type: %s\n", consoleInfo.getFirmware(),
                    consoleInfo.getCellC(),
                    consoleInfo.getRsxC(),
                    consoleInfo.getCellF(),
                    consoleInfo.getRsxF(),
                    consoleInfo.getType().name());
            for (Process p : ccapi.getProcesses()) {
                System.out.printf("%s : %s\n", p.getPidName(), p.getPid());
            }
            System.out.println();
            System.out.println("Triple Buzz: " + ccapi.ringBuzzer(CCAPI.BuzzerType.TRIPLE));
            System.out.println("Notify Msg: " + ccapi.notify(CCAPI.NotifyIcon.INFO, "CCAPI JAVA!"));
            System.out.println("LED: " + ccapi.setConsoleLed(CCAPI.LedColor.GREEN, CCAPI.LedStatus.OFF));

            System.out.print("HB = Hardboot, SB = Softboot, SD = Shutdown, S = skip\nType a command: ");
            String command = scanner.nextLine().toLowerCase().trim();
            if (command.equals("HB")) {
                ccapi.shutDown(CCAPI.ShutdownMode.HARDBOOT);
                System.out.println("Hardboot...");
            } else if (command.equals("SD")) {
                ccapi.shutDown(CCAPI.ShutdownMode.SHUTDOWN);
                System.out.println("Shutdown...");
            } else if (command.equals("SB")) {
                ccapi.shutDown(CCAPI.ShutdownMode.SOFTBOOT);
                System.out.println("Softbooted...");
            } else {
                System.out.println("Skipped....");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
