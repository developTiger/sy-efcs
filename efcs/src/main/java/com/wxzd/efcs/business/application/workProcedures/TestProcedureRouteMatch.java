package com.wxzd.efcs.business.application.workProcedures;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouzh on 2017/4/18.
 */
public class TestProcedureRouteMatch {


    private final static int M = 100000;//(此路不通)

    private static int[][] getFomationRouteMatrix() {
    	   int[][] weight = {
                   // {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0},
                   {M, 1, M, M, M, M, M, M, 1, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M},//0
                   {M, M, M, M, 1, 1, 1, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M},//1
                   {M, M, M, M, M, M, 1, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M},//2
                   {M, M, M, M, M, 1, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M},//3
                   {M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M},//4
                   {M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, 5, M, M, M, M, M, M, M, M},//5
                   {M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, 5, M, M, M, M, M, M, M, M, M},//6
                   {M, 2, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M},//7
                   {1, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M},//8
                   {1, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M},//9
                   {M, M, M, M, M, M, M, M, M, M, M, 1, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M},//10
                   {M, M, M, M, M, M, M, M, M, M, M, M, 1, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M},//11
                   {M, M, M, M, M, M, M, M, M, M, M, M, M, 2, 2, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M},//12
                   {M, M, M, M, M, M, M, M, M, M, M, M, M, M, 2, M, M, M, M, M, 2, M, M, M, M, M, M, M, M, M, M},//13
                   {M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, 5, M, M, M, M, M, M, M, M, M},//14
                   {M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, 1, M, M, M, M, M, M, M, M, M, M, M, M, M, M},//15
                   {M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, 1, M, M, M, M, M, M, M, M, M, M, M, M, M},//16
                   {M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, 2, 2, M, M, M, M, M, M, M, M, M, M, M},//17
                   {M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, 2, 2, M, M, M, M, M, M, M, M, M, M},//18
                   {M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, 5, M, M, M, M, M, M, M, M},//19
                   {M, M, M, M, M, M, M, M, M, M, M, M, M, M, 2, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M},//20
                   {M, M, M, M, M, M, M, 5, M, M, 5, M, M, M, M, M, M, M, M, M, M, M, M, 5, 5, M, M, M, M, 3, M},//21
                   {M, M, M, M, M, M, M, M, M, 5, M, M, M, M, M, 5, M, M, M, M, M, M, M, M, M, 5, 5, M, M, M, 3},//22
                   {M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, 5, M, M, M, M, M, M, M, M, M},//23
                   {M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, 5, M, M, M, M, M, M, M, M, M},//24
                   {M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, 5, M, M, M, M, M, M, M, M},//25
                   {M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, 5, M, M, M, M, M, M, M, M},//26
                   {M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M},//27
                   {M, M, M, M, M, M, M, M, M, M, M, M, M, M, 3, M, M, M, M, 2, 2, M, M, M, M, M, M, M, M, M, M},//28
                   {M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, 3, M, M, M, M, M, M, M, M, 1},//29
                   {M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, 3, M, M, M, M, M, M, 1, M},//30


           };
           return weight;
    }

    private static Map<String, String> getEquipMap() {
        Map<String, String> map = new HashMap<>();
        map.put("0", "3071");//入口
        map.put("1", "3110");//提升机
        map.put("2", "3210");//上组盘位
        map.put("3", "3100");//下组盘位
        map.put("4", "cwzpjxs");//常温组盘机械手
        map.put("5", "3010");//下部分配库位点
        map.put("6", "3120");//上部分配库位点
        map.put("7", "3121");//下层下架放货位
        map.put("8", "3220");//异常口 无扫描
        map.put("9", "3011");//下层下架放货位
        map.put("10", "4120");//下架位
        map.put("11", "TTOCV0001");//ocv1 设备号
        map.put("12", "4170");//ocv1出条码阅读
        map.put("13", "4220");//上拆盘位
        map.put("14", "4121");//回库上架位
        map.put("15", "4010");//下架位
        map.put("16", "TTOCV0002");//ocv2
        map.put("17", "4060");//ocv2 出扫描
        map.put("18", "4100");//下部拆盘位
        map.put("19", "4011");//回库上架位
        map.put("20", "4190");//入口
        map.put("21", "常温1#机");//3号机
        map.put("22", "常温2#机");//4号机
        map.put("23", "5");// 第一排
        map.put("24", "6");//第二排
        map.put("25", "7");//第三排
        map.put("26", "8");//第四排
        map.put("27", "cwcpjxs");// 拆盘机械手
        map.put("28", "4110");//拆盘提升机
        map.put("29", "3260");//TODO 中间通道上
        map.put("30", "3240");//TODO 中间通道下
        return map;
    }


    public static Boolean isStocker(String deviceNO) {
        Map<String, String> map = new HashMap<>();
        map.put("常温1#机", "测试段堆垛机");// 堆垛机1
        map.put("常温2#机", "测试段堆垛机");//堆垛机2

        String s = map.get(deviceNO);
        if (s == null) {
            return false;
        } else {
            return true;
        }

    }


    public static String getCubicXPos(String cublic) {

        Integer s = Integer.parseInt(cublic.substring(2, cublic.length() - 4))+4;
        return s.toString();

    }

    private static Integer getEquipIndexByEquipNo(String equipNo) {

        for (Map.Entry<String, String> entry : getEquipMap().entrySet()) {
            if (entry.getValue().equals(equipNo))
                return Integer.parseInt(entry.getKey());
        }
        return null;
    }


    public static String getRouteByEquipNo(String fromEquip, String toEquip) {
        //特殊路线处理

        Integer start = getEquipIndexByEquipNo(fromEquip);
        Integer end = getEquipIndexByEquipNo(toEquip);
        String result = Dijsktra(getFomationRouteMatrix(), start, end);
        if (result.equals("error"))
            throw new RuntimeException("end point can not arrived!");

        StringBuilder route = new StringBuilder();

        String[] arrResult = result.split("-");
        for (String s : arrResult) {
            route.append(getEquipMap().get(s)).append("-");


        }
        String strRoute = route.substring(0, route.length() - 1);

        return route.substring(0, route.length() - 1);
    }

    public static Boolean isRouteStartPos(String route, String currentPos) {
        return true;
    }


    public static String getNextPosition(String route, String currentPos) {
        String[] arrRoute = route.split("-");
        int index = indexOf(arrRoute, currentPos);
        return arrRoute[index + 1];
    }


    public static Boolean isRouteEnd(String route, String currentPos) {
        return true;
    }

    public static String replaceEndPosition(String route, String loc_no) {
        String[] arrRoute = route.split("-");
        arrRoute[arrRoute.length - 1] = loc_no;
        StringBuilder builder = new StringBuilder();
        for (String s : arrRoute) {
            builder.append(s);
            builder.append("-");
        }
        return builder.substring(0, builder.length() - 1).toString();

    }

    public static String Dijsktra(int[][] weight, int start, int end) {
        if (start == end) return "error";
        //接受一个有向图的权重矩阵，和一个起点编号start（从0编号，顶点存在数组中）
        //返回一个int[] 数组，表示从start到它的最短路径长度
        int n = weight.length;        //顶点个数
        int[] shortPath = new int[n];    //存放从start到其他各点的最短路径
        String[] path = new String[n]; //存放从start到其他各点的最短路径的字符串表示
        for (int i = 0; i < n; i++)
            path[i] = new String(start + "-" + i);
        int[] visited = new int[n];   //标记当前该顶点的最短路径是否已经求出,1表示已求出
        //初始化，第一个顶点求出
        shortPath[start] = 0;
        visited[start] = 1;

        for (int count = 1; count <= n - 1; count++)  //要加入n-1个顶点
        {
            int k = -1;    //选出一个距离初始顶点start最近的未标记顶点
            int dmin = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (visited[i] == 0 && weight[start][i] < dmin) {
                    dmin = weight[start][i];
                    k = i;
                }
            }
            //将新选出的顶点标记为已求出最短路径，且到start的最短路径就是dmin
            shortPath[k] = dmin;
            visited[k] = 1;
            //以k为中间点，修正从start到未访问各点的距离
            for (int i = 0; i < n; i++) {
                if (visited[i] == 0 && weight[start][k] + weight[k][i] < weight[start][i]) {
                    weight[start][i] = weight[start][k] + weight[k][i];
                    path[i] = path[k] + "-" + i;
                }
            }
        }
        if (shortPath[end] == 100000)
            return "error";
        return path[end];

    }


    private static int indexOf(String[] arr, String s) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(s))
                return i;
        }

        return -1;
    }

    public static int getCurrentIndex(String route, String currentPos) {
        String[] arrRoute = route.split("-");
        int index = indexOf(arrRoute, currentPos);
        return index;

    }
}
