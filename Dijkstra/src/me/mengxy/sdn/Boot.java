package me.mengxy.sdn;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

public class Boot {
    //主方法
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("input jsonfile path:");
        String jsonpath = input.next();
        File file = new File(jsonpath);
        if (file.exists()) {
            System.out.println("read jsonfile!");
//            doBoot(jsonpath);
            timeKeeping(jsonpath);
        } else {
            System.out.println("jsonfile not exist!");
        }
    }

    //记时，超时返回报错
    public static void timeKeeping(String jsonpath) {
        Callable<String> task = new Callable<String>() {
            @Override
            public String call() throws Exception {
                //设置执行响应时间的方法体
                String time_return = doBoot(jsonpath);
                return time_return;
            }
        };
        ExecutorService exeservices = Executors.newSingleThreadExecutor();
        Future<String> future = exeservices.submit(task);
        try {
            //设置方法
            String result = future.get(10, TimeUnit.SECONDS);
            System.out.println("complete!");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            //异常处理的方法
            System.out.println("connect controller worng!check controller status?");
            System.exit(0);
        }
    }

    //启动
    public static String doBoot(String jsonpath) throws Exception {
        Gettopo topo = new Gettopo();
        int nodesnum = topo.getNodesnum(jsonpath); //设备数
        int linksnum = topo.getLinksnum(jsonpath); //链路数
        int[][] matrix = new int[nodesnum][nodesnum];
        int[][] matrix_bw = topo.getTopoinfo_bw(jsonpath); //带宽矩阵
        int[][] matrix_dl = topo.getTopoinfo_dl(jsonpath); //延迟矩阵
        int[] hostlist = topo.getHostlist(jsonpath); //主机列表
        int[][] alllink = topo.getAllpath(jsonpath);    //获取全部带端口路径
        String method = topo.getMethod(jsonpath); //获取使用哪测量方法
        if (Objects.equals(method, "bw")) { //选择使用带宽或者延迟作为权重
            matrix = matrix_bw;
        } else if (Objects.equals(method, "dl")) {
            matrix = matrix_dl;
        }

        for (int i = 0; i < hostlist.length; i++) { //输出到获取交换机接口链路类
            String[] allpath = getAllpath(matrix, hostlist[i]);
//            System.out.println(Arrays.toString(allpath));
            for (int j = i + 1; j < hostlist.length; j++) {
                int[] path = getPath(allpath, hostlist[i], hostlist[j]);
//                System.out.println(Arrays.toString(path));
                String src_ip = topo.getHostip(jsonpath,path[0]);
                String dst_ip = topo.getHostip(jsonpath,path[path.length-1]);
                int[] logiclinks = pickLinks(alllink, path);
                int[][] logicpath = topo.getTopolinks(jsonpath, logiclinks);
                Path.flow(logicpath,path,src_ip,dst_ip);
            }
        }
        return null;
    }

    //输入一个源点的所有路径，源目，输出源目路径
    public static int[] getPath(String[] links, int src, int dst) {

        for (String link : links) {
            int[] a = Match.match(link);
            if (a[0] == src && a[a.length - 1] == dst) {
                return a;
            }
        }
        return null;
    }

    //获取一个源为起点的所有路径
    public static String[] getAllpath(int[][] matrix, int src) {
        String[] links = DijkstraAlgorithm.doDijkstra(matrix, src);
        return links;
    }

    //选择最优路径
    public static int[] pickLinks(int[][] allpath, int[] path) {
        int[] finallinks = new int[path.length - 1];

        for (int i = 0; i < path.length - 1; i++) {
            int src = path[i];
            int dst = path[i + 1];
            for (int j = 0; j < allpath.length; j++) {
                int temp_src = allpath[j][0];
                int temp_dst = allpath[j][2];
                if (src == temp_src && dst == temp_dst) {
                    finallinks[i] = allpath[j][4];
                } else if (src == temp_dst && dst == temp_src) {
                    finallinks[i] = allpath[j][4];
                }
            }
        }
        return finallinks;
    }

}
