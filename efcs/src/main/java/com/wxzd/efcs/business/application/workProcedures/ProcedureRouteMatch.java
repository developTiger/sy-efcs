package com.wxzd.efcs.business.application.workProcedures;

import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.gaia.common.base.core.array.ArrayUtl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouzh on 2017/4/18.
 */
public class ProcedureRouteMatch {


    private final static int M = 100000;//(此路不通)

    private static int[][] getFomationRouteMatrix() {
        int[][] weight = {
                // {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8},
                {M, M, 2, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M},//0
                {1, M, 3, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M},//1
                {M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, 5, M},//2
                {M, 2, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, 2, M, M},//3
                {M, M, M, 5, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M},//4
                {M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, 5, 5},//5
                {M, M, M, M, M, M, M, 3, M, M, M, M, M, M, M, M, M, M, M, M, M},//6M
                {M, M, M, M, M, M, M, M, 2, M, 1, 1, M, M, M, M, M, M, M, M, M},//7
                {M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, 5},//8
                {M, M, M, 3, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M},//9
                {M, M, M, M, M, M, M, M, M, M, M, M, 3, M, M, M, M, M, M, M, M},//10
                {M, M, M, M, M, M, M, M, 3, M, M, M, M, M, M, M, M, M, M, M, M},//11
                {M, M, M, M, M, M, M, M, 3, M, M, M, M, 1, M, M, M, M, M, M, M},//12
                {M, M, M, M, M, M, M, M, M, M, M, M, 1, M, M, M, M, M, M, M, M},//13
                {M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, 5, M},//14
                {M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, 5, 5},//15
                {M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, 5},//16
                {M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, 5},//17
                {M, 2, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M, M},//18
                {M, M, M, M, 5, 5, M, M, M, M, M, M, M, M, 5, 5, M, M, M, M, M},//19
                {M, M, M, M, 4, 5, 5, M, M, 5, M, M, M, M, M, 5, 5, 5, M, M, M}//20


        };
        return weight;
    }

    public static Map<String, String> getEquipMap() {
        Map<String, String> map = new HashMap<>();
        map.put("0", "1230");//入库口扫描
        map.put("1", "1220");//扫描  移栽
        map.put("2", "1300");//高温上架
        map.put("3", "1170");//组盘前置扫描
        map.put("4", "1060");//高温下架出库 （左侧位）
        map.put("5", "1310");//高温下架--》化成
        map.put("6", "2010");//化成下架位
        map.put("7", "2060");//拆盘前致扫描
        map.put("8", "2260");//rework上架位
        map.put("9", "1010");//组盘前置缓存位
        map.put("10", "2090");//1号拆盘位
        map.put("11", "2110");//2号拆盘位
        map.put("12", "2150");//rework前置扫描
        map.put("13", "2080");//rework组盘位
        map.put("14", "1");//排1
        map.put("15", "2");//排2
        map.put("16", "3");//排3
        map.put("17", "4");//排4
        map.put("18", "hczpjxs");//化成组盘机械手
        map.put("19", "高温库堆垛机");// 堆垛机1
        map.put("20", "化成堆垛机");//堆垛机2
        map.put("21", "1360");
        return map;
    }


    public static Boolean isStocker(String deviceNO) {
        Map<String, String> map = new HashMap<>();
        map.put("高温库堆垛机", "化成段高温堆垛机");// 堆垛机1
        map.put("化成堆垛机", "化成段化成堆垛机");//堆垛机2

        String s = map.get(deviceNO);
        if (s == null) {
            return false;
        } else {
            return true;
        }

    }


    public static String getCubicXPos(String cublic) {

        Integer s = Integer.parseInt(cublic.substring(2, cublic.length() - 4));
        return s.toString();

    }

    private static Integer getEquipIndexByEquipNo(String equipNo) {

        for (Map.Entry<String, String> entry : getEquipMap().entrySet()) {
            if (entry.getValue().equals(equipNo))
                return Integer.parseInt(entry.getKey());
        }
        throw  new RuntimeException("地址位未设置");
    }


    public static String replaceStartPosition(String route, String loc_no) {
        String[] arrRoute = route.split("-");
        arrRoute[0] = loc_no;
        StringBuilder builder = new StringBuilder();
        for (String s : arrRoute) {
            builder.append(s);
            builder.append("-");
        }
        return builder.substring(0, builder.length() - 1).toString();

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
        if (toEquip.equals("4")) {
            String[] routeTemp = strRoute.split("-");
            if (indexOf(routeTemp, "1310") > -1)
                return strRoute;
            else {
                strRoute = strRoute.substring(0, strRoute.length() - 1);
                strRoute = strRoute + "1310-" + getEquipMap().get("20") + "-4";
                return strRoute;
            }

        }
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
