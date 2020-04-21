import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {
    public static void main(String[] args) {

        String path = "sh /Users/muyou/ide/webide/shell/test.sh xxx";
        try{
            Runtime runtime = Runtime.getRuntime();
            Process pro = runtime.exec(path);
            int status = pro.waitFor();
            if (status != 0)
            {
                System.out.println("Failed to call shell's command");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            StringBuffer strbr = new StringBuffer();
            String line;
            while ((line = br.readLine())!= null)
            {
                strbr.append(line).append("\n");
            }

            String result = strbr.toString();
            System.out.println(result);

        }
        catch (IOException ec)
        {
            ec.printStackTrace();
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }

    }
}
