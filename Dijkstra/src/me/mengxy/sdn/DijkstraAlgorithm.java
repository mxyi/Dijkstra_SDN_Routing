package me.mengxy.sdn;

public class DijkstraAlgorithm {

    public static int MaxValue = 1000000000; //不能设置为Integer.MAX_VALUE，否则两个Integer.MAX_VALUE相加会溢出导致出现负权

    public static String[] doDijkstra(int[][] matrix, int src) {
        //最短路径长度
        int[] shortest = new int[matrix.length];
        //判断该点的最短路径是否求出
        int[] visited = new int[matrix.length];
        //存储输出路径
        String[] path = new String[matrix.length];

        //初始化输出路径
        for (int i = 0; i < matrix.length; i++) {
            path[i] = new String(src + ">" + i);
        }

        //初始化源节点
        shortest[src] = 0;
        visited[src] = 1;

        for (int i = 1; i < matrix.length; i++) {
            int minpath = Integer.MAX_VALUE;
            int index = -1;

            for (int j = 0; j < matrix.length; j++) {
                //已经求出最短路径的节点不需要再加入计算并判断加入节点后是否存在更短路径
                if (visited[j] == 0 && matrix[src][j] < minpath) {
                    minpath = matrix[src][j];
                    index = j;
                }
            }

            //更新最短路径
            shortest[index] = minpath;
            visited[index] = 1;

            //更新从index跳到其它节点的较短路径
            for (int m = 0; m < matrix.length; m++) {
                if (visited[m] == 0 && matrix[src][index] + matrix[index][m] < matrix[src][m]) {
                    matrix[src][m] = matrix[src][index] + matrix[index][m];
                    path[m] = path[index] + ">" + m;
                }
            }
        }
        String[] returnpath = new String[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            returnpath[i] = (path[i]);
        }
        return returnpath;
    }

}