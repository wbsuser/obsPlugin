package utils;

import service.ProjectGenerateService;
import service.impl.ProjectGenerateServiceImpl;

import java.io.*;

public class RunUtil {
    /**
     * 编译指定目录下的代码
     *
     * @param projectPath
     * @return
     */
    public static void buildProject(String projectPath, String apiName) {
        // Java调用 dos命令
        //javac -encoding UTF-8 -d C:\Users\Chadiy\Desktop\AutoRun\target -sourcepath C:\Users\Chadiy\Desktop\AutoRun\src\main\java -cp C:\Users\Chadiy\Desktop\AutoRun\lib\obs-java.jar  C:\Users\Chadiy\Desktop\AutoRun\src\main\java\ListBuckets.java
        String javaCommand = "javac ";
        String encoding = "-encoding UTF-8 ";
        String target = "-d " + projectPath + "\\target ";
        String sourcePath = "-sourcepath " + projectPath + "\\src\\main\\java ";
        String maven = "-cp " + getLibString(projectPath);
        String javaFile = projectPath + "\\src\\main\\java\\" + apiName + ".java";
        String cmd = javaCommand + encoding + target + sourcePath + maven + javaFile;
       // System.out.println(cmd);
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String content = br.readLine();
            while (content != null) {
                System.out.println(content);
                content = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * @Author sunwb
     * @Description 运行生成的文件
     * @Date 8:33 2020/6/24
     * @Param [filePath, fileType, apiName]
     * @return void
     **/
    public static String runFile(String filePath, String fileType, String apiName) {

        apiName = apiName.substring(0, apiName.indexOf(".java"));
        ProjectGenerateService projectGenerateService = new ProjectGenerateServiceImpl();

        //==============================利用cmd命令编译项目，执行class文件，生成结果返回至resString==========
        String runResult = projectGenerateService.runProject(filePath,apiName);
        if (runResult == null || runResult.length() == 0) {
            runResult = "null";
        }
        return runResult;
    }
    public static String runFile(String projectPath, String apiName) {
        // Java调用 dos命令
        String javaCommand = "java ";
        String jarPath = "-cp " + getLibString(projectPath);
        String cmd = javaCommand + jarPath + apiName;
        //System.out.println(cmd);
        StringBuilder resString= new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(cmd,null,new File(projectPath+"\\target"));
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String content = br.readLine();
            while (content != null) {
                resString.append(content).append("\n");
                content = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resString.toString();
    }

    public static String getLibString(String projectPath) {
        File file = new File(projectPath + "\\lib");
        File[] fs = file.listFiles();
        StringBuilder res = new StringBuilder("\".;");
        assert fs != null;
        for (File f : fs) {
            res.append(f.toString()).append(";");
        }
        res.replace(res.length() - 1, res.length(), "\"  ");
        return res.toString();
    }
}
