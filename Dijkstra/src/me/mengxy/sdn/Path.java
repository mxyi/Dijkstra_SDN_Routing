package me.mengxy.sdn;

public class Path {

    public static void flow(int[][] logiclink, int[] path,String src_ip,String dst_ip) throws Exception {
        int switchid = 0;
        int inport = 0;
        int outport = 0;
        int[][] portlist = new int[logiclink.length - 1][3];
        int[] switchlist = new int[path.length - 2];
        for (int i = 0; i < path.length; i++) {
            if (i == 0) {
            } else if (i == path.length - 1) {
            } else switchlist[i-1] = path[i];
        }

        for (int i = 0; i < switchlist.length; i++) {
            switchid = switchlist[i];
            if (logiclink[i][0]==switchid){
                inport = logiclink[i][1];
            }else if(logiclink[i][2]==switchid){
                inport = logiclink[i][3];
            }

            if(logiclink[i+1][0]==switchid){
                outport = logiclink[i+1][1];
            }else if(logiclink[i+1][2]==switchid){
                outport = logiclink[i+1][3];
            }
            portlist [i][0] = inport;
            portlist [i][1] = switchid;
            portlist [i][2] = outport;
        }
        Flows.getFlowentery(portlist,path,src_ip,dst_ip);
    }


}
