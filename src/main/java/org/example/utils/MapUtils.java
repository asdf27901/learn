package org.example.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.variable.*;

import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

public class MapUtils {
    //private static double EARTH_RADIUS = 6378.137;
    private static final double EARTH_RADIUS = 6371.393;
    private static final Scanner sc = new Scanner(System.in);
    private static final Map<Integer,Integer> map = new HashMap<>();
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }
    private FileOutputStream fileOutputStream;
    private XSSFWorkbook xssfWorkbook;
    private XSSFSheet xssfSheet;

    /**
     * 计算两个经纬度之间的距离
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double GetDistance(double lat1, double lng1, double lat2, double lng2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = s * 1000;
        return s;
    }

    /**
     * 转化GNSS数据中坐标为普通度分秒格式，并保留小数
     *
     * @param loc   GNSS格式中坐标如2346.12121121,11312.12112121
     * @param scale 保留小数位
     * @return
     */
    public static double convertGgaLocation(double loc, int scale) {
        if (scale < 0) {
            scale = 9;
        }
        double result = (int) (loc / 100.0) + (loc - ((int) (loc / 100)) * 100) / 60.0;
        return new BigDecimal(result).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    class ThreadClass implements Runnable{

        private String configPath;
        private String logPath;

        public ThreadClass(String configPath, String logPath) {
            this.configPath = configPath;
            this.logPath = logPath;
        }

        @Override
        public void run() {
            newThread();
        }

        public void newThread(){
            try {

                Place place = getParam(configPath);

                ParamClass paramClass = getParam();

                FileUtils.findFile(logPath);

                List<String> list = FileUtils.readFile();

                setSheet(list,paramClass,place,logPath);

                xssfWorkbook.write(fileOutputStream);
                fileOutputStream.close();
                FileUtils.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void init(){

        System.out.print("测试了几台设备:");
        int time = sc.nextInt();
        for (int i = 1; i <= time ; i++) {
            System.out.print("输入第"+i+"台要读取的配置文件路径:");
            String configPath = sc.next();
            System.out.print("输入第"+i+"要读取的日志文件路径:");
            String logPath = sc.next();
            new Thread(new ThreadClass(configPath,logPath),"第"+i+"台设备").start();
        }
    }

    public static int timeTransform(String time){
        try {
            return Integer.parseInt(time.substring(0,2))*3600+Integer.parseInt(time.substring(2,4))*60+Integer.parseInt(time.substring(4,6));
        }catch (NumberFormatException e){
            return (Integer.parseInt(time.split(":")[0])-8)*3600+Integer.parseInt(time.split(":")[1])*60+Integer.parseInt(time.split(":")[2]);
        }
    }

    public Integer findMissingData(int count,String startTime,String time,String preTime){
        if (count == 1){
            return ExcelUtils.getColumn(xssfSheet,count,timeTransform(time) - timeTransform(startTime));
        }else {
            return ExcelUtils.getColumn(xssfSheet,count,timeTransform(time) - timeTransform(preTime));
        }
    }

    public void setSheet(List<String> list,ParamClass paramClass,Place place,String logPath) throws IOException {

        Map<Integer,PointClass> pointClassMap = new HashMap<>();

        for (PointClass ex : paramClass.getEx()) {
            pointClassMap.put(ex.getPlaceNo(),ex);
        }

        for (PlaceInfo placeInfo : place.getPlaceInfoList()) {

            if (!placeInfo.getStartTime().equals("")) {
                int count = 0;

                File file = new File(logPath.substring(0, logPath.lastIndexOf("\\") + 1) + placeInfo.getDeviceId() + "-" + placeInfo.getAntenna() + "-" + placeInfo.getStartTime().split(" ")[0]+".xls");
                if (!file.exists()) file.createNewFile();

                if (ExcelUtils.xssfWorkbookMap.get(file) == null){
                    xssfWorkbook = new XSSFWorkbook();
                    ExcelUtils.xssfWorkbookMap.put(file,xssfWorkbook);
                } else xssfWorkbook = ExcelUtils.xssfWorkbookMap.get(file);

                fileOutputStream = new FileOutputStream(file);

                if (map.get(placeInfo.getPlaceNo()) != null){
                    count = map.get(placeInfo.getPlaceNo());
                }
                xssfSheet = xssfWorkbook.createSheet("场景"+placeInfo.getPlaceNo()+(count == 0 ? "" : "-" + count));
                count++;
                map.put(placeInfo.getPlaceNo(),count);

                getDistance(list,placeInfo,pointClassMap);
            }
        }
    }

    public void getDistance(List<String> list,PlaceInfo placeInfo,Map<Integer,PointClass> pointClassMap) throws IOException {
        boolean flag = false;
        String str,preTime = null,startTime = placeInfo.getStartTime().split(" ")[1];
        int count = 0;
        double weiDu = 0.0,jingDu = 0.0;

        for (String s : list) {

            String[] aa = s.split(",");

            try {
                if (timeTransform(aa[1]) >= timeTransform(startTime) ){
                    count++;
                    flag = true;
                }
            }catch (StringIndexOutOfBoundsException e){
                continue;
            }

            if (!flag) continue;

            if (count == 1) ExcelUtils.getColumn(xssfSheet, "时间戳,纬度,经度,解状态,搜星数,距离真值点距离 单位/米", 0);

            try {
                weiDu = Double.parseDouble(aa[2]);
                jingDu = Double.parseDouble(aa[4]);
            }catch (NumberFormatException e){
                ExcelUtils.getExceptionColumn(xssfSheet,aa[1],count);
                continue;
            }

            if (timeTransform(aa[1]) >= (timeTransform(startTime) + placeInfo.getTime()*60)) {
                xssfSheet.getRow(6).createCell(10).setCellValue("真值点经纬度" + pointClassMap.get(placeInfo.getPlaceNo()).getPointValue());
                ExcelUtils.createChart(xssfSheet,count);
                break;
            }

            str = aa[1] + "," + MapUtils.convertGgaLocation(weiDu, -1) + "," + MapUtils.convertGgaLocation(jingDu, -1) + "," + aa[6]
                    + "," + aa[7] + "," + MapUtils.GetDistance(MapUtils.convertGgaLocation(weiDu, -1), MapUtils.convertGgaLocation(jingDu, -1),
                    Double.parseDouble(pointClassMap.get(placeInfo.getPlaceNo()).getPointValue().split(",")[0]), Double.parseDouble(pointClassMap.get(placeInfo.getPlaceNo()).getPointValue().split(",")[1]));

            if (count == 1 || preTime != null) count = findMissingData(count,startTime,aa[1],preTime);


            ExcelUtils.getColumn(xssfSheet, str, count);
            if (preTime != null){
                if (timeTransform(aa[1]) - timeTransform(preTime) == 0) ExcelUtils.getSameGGATip(xssfSheet,count);
            }
            preTime = aa[1].equals("") ? null : aa[1];
        }
    }

    public ParamClass getParam() throws IOException {
        Gson gson = new Gson();

        Reader reader = FileUtils.readFile("/config.json");

        Type type = new TypeToken<ParamClass>(){}.getType();

        return gson.fromJson(reader,type);
    }

    public Place getParam(String configPath) throws FileNotFoundException {
        Gson gson = new Gson();

        File file1 = new File(configPath);
        FileReader fileReader = new FileReader(file1);

        Type type = new TypeToken<Place>(){}.getType();

        return gson.fromJson(fileReader,type);
    }

    public void countSearchStar(String str,String time,String preTime){

        int star = (!str.equals("")) ? Integer.parseInt(str) : 0;

    }
}



