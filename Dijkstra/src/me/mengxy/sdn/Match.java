package me.mengxy.sdn;

public class Match {

    public static int[] match(String inpath) {
        String path[] = inpath.split(">");
        int outpath [] = new int[path.length];
        for (int i = 0; i < path.length; i++) {
            int a = Integer.parseInt(path[i]);
            outpath [i] =a;
        }
        return outpath;
    }
}

