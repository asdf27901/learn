package org.example.utils;


import org.example.MainClass;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private static BufferedReader bufferedReader;

    private static BufferedWriter bufferedWriter;

    private static InputStream inputStream;

    public static void findFile(String path){
        if (path == null || path.equals("")){
            System.out.println("输入的文件路径不规范");
            return;
        }

        File file = new File(path);
        if (file.exists()){
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static Reader readFile(String name){
        inputStream = MainClass.class.getResourceAsStream(name);

        return new InputStreamReader(inputStream);
    }

    public static List<String> readFile() throws IOException {
        List<String> list = new ArrayList<>();
        String str;

        while(((str = bufferedReader.readLine()) != null))
        {
            try {
                str = str.substring(str.indexOf("GNGGA"),str.lastIndexOf("*")+2);
            }catch (StringIndexOutOfBoundsException e){
                continue;
            }
            list.add(str);
        }
        return list;
    }

    public static void writeFile(List<String> list,String path){
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String s : list) {
            try {
                bufferedWriter.write(s);
                bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(){
        if (bufferedReader != null){
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (bufferedWriter != null){
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (inputStream != null){
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
