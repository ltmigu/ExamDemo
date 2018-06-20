package com.migu.schedule;


import com.migu.schedule.constants.ReturnCodeKeys;
import com.migu.schedule.info.TaskInfo;
import com.migu.schedule.info.Task;
import com.migu.schedule.info.ServiceNode;

import java.util.*;

/*
*类名和方法不能修改
 */
public class Schedule {

    private List<ServiceNode> nodeList = new LinkedList<ServiceNode>();

    private LinkedList<Task> waitTaskList = new LinkedList<Task>();

    private LinkedList<Task> runTaskList = new LinkedList<Task>();

    public int init() {
        // TODO 方法未实现
        nodeList = new LinkedList<ServiceNode>();
        waitTaskList = new LinkedList<Task>();
        runTaskList =  new LinkedList<Task>();
        return ReturnCodeKeys.E001;
    }


    public int registerNode(int nodeId) {
        // TODO 方法未实现
        if(nodeId <=0){
            return ReturnCodeKeys.E004;
        }
        ServiceNode serviceNode = new ServiceNode(nodeId);
        if(nodeList.contains(serviceNode)){
            return ReturnCodeKeys.E005;
        }

        nodeList.add(serviceNode);

        return ReturnCodeKeys.E003;
    }

    public int unregisterNode(int nodeId) {
        // TODO 方法未实现
        // 参数校验
        if(nodeId <=0){
            return ReturnCodeKeys.E004;
        }
        ServiceNode serviceNode = new ServiceNode(nodeId);
        if(!nodeList.contains(serviceNode)){
            return ReturnCodeKeys.E007;
        }
        Task tmpTask = null;
        // 检测是否在运行中
        for (Task task:runTaskList ) {
            if(task.getNode().getNodeId() == nodeId){
                tmpTask = task;
                break;
            }
        }
        if(tmpTask != null){
            runTaskList.remove(tmpTask);
            waitTaskList.add(tmpTask);
        }
        nodeList.remove(serviceNode);
        return ReturnCodeKeys.E006;
    }


    public int addTask(int taskId, int consumption) {
        // TODO 方法未实现
        if(taskId <=0){
            return ReturnCodeKeys.E009;
        }

        Task task = new Task(taskId,consumption);
        if(waitTaskList.contains(task)){
            return ReturnCodeKeys.E010;
        }
        // 添加任务，consumption 需要处理
        waitTaskList.add(task);

        return ReturnCodeKeys.E008;
    }


    public int deleteTask(int taskId) {
        // TODO 方法未实现
        if(taskId <=0){
            return ReturnCodeKeys.E009;
        }
        Task task= new Task(taskId);
        if(waitTaskList.remove(task)){
            return ReturnCodeKeys.E011;
        }
        else if(runTaskList.contains(task)){
            runTaskList.remove();
            return ReturnCodeKeys.E011;
        }
        else{
            return  ReturnCodeKeys.E012;
        }
    }


    public int scheduleTask(int threshold) {
        // TODO 方法未实现
        if(threshold <=0){
            return ReturnCodeKeys.E002;
        }

        // 任务分配
        int avgConsumption = 0;
        int sumConsumption = 0;
        int sericeNodeNum = nodeList.size();
        int tasknum = waitTaskList.size();
        int avgTaskNum = tasknum/sericeNodeNum;
        if(tasknum%sericeNodeNum !=0){
            avgTaskNum++;
        }
        for (Task task:waitTaskList
             ) {
            sumConsumption+=task.getConsumption();
        }
        avgConsumption = sumConsumption/nodeList.size();

        // 分配前排序
        Collections.sort(waitTaskList,new Comparator<Task>(){
            public int compare(Task o1, Task o2) {
                if(o1.getTaskId() < o2.getTaskId()){return -1;}
                else if(o1.getTaskId() > o2.getTaskId()){return 1;}
                else {return 0;}
            }
        });

        Collections.sort(nodeList,new Comparator<ServiceNode>(){
            public int compare(ServiceNode o1, ServiceNode o2) {
                if(o1.getNodeId() < o2.getNodeId()){return -1;}
                else if(o1.getNodeId() > o2.getNodeId()){return 1;}
                else {return 0;}
            }
        });


        if(waitTaskList.size()>0){
            // 初步分配
            for(int i = 0;i<tasknum;i++){
                Task task = waitTaskList.poll();
                if(task !=null){
                    nodeList.get((i/avgTaskNum)).addTask2Node(task);
                    runTaskList.add(task);
                }
            }

            // 分配算法
            // 如果迁移后，有任意两台服务器的总消耗率相同，则应保证编号小的服务器的运行任务总数量少

            // 如果迁移后，所有的物理服务器的总消耗率不相同，保证编号大的服务器的总消耗大于编号小的服务器的总消耗

            // 如果迁移后，满足以上要求的方案有多个，则应选择编号小的服务器上的任务编号升序序列最小

            // 分配后排序
            Collections.sort(runTaskList,new Comparator<Task>(){
                public int compare(Task o1, Task o2) {
                    if(o1.getTaskId() < o2.getTaskId()){return -1;}
                    else if(o1.getTaskId() > o2.getTaskId()){return 1;}
                    else {return 0;}
                }
            });


            // 分配后计算是否在调度阈值范围内
            int minConsumption=0;
            int MaxConsumption=0;
            for (ServiceNode serviceNode:nodeList
                 ) {
                if(serviceNode.getConsumption() >MaxConsumption){MaxConsumption = serviceNode.getConsumption();}
                if(minConsumption ==0 ){minConsumption=serviceNode.getConsumption();}
                if(serviceNode.getConsumption() < minConsumption){minConsumption =serviceNode.getConsumption();}
            }
            if(MaxConsumption - minConsumption <= threshold){return ReturnCodeKeys.E013;}
            else{return ReturnCodeKeys.E014;}
        }
        else if(runTaskList.size()>0){



            // 分配后计算是否在调度阈值范围内
            int minConsumption=0;
            int MaxConsumption=0;
            for (ServiceNode serviceNode:nodeList
                    ) {
                if(serviceNode.getConsumption() >MaxConsumption){MaxConsumption = serviceNode.getConsumption();}
                if(minConsumption ==0 ){minConsumption=serviceNode.getConsumption();}
                if(serviceNode.getConsumption() < minConsumption){minConsumption =serviceNode.getConsumption();}
            }
            if(MaxConsumption - minConsumption <= threshold){return ReturnCodeKeys.E013;}
            else{return ReturnCodeKeys.E014;}
        }
        return ReturnCodeKeys.E014;
    }


    public int queryTaskStatus(List<TaskInfo> tasks) {
        // TODO 方法未实现
        if(tasks == null){
            return ReturnCodeKeys.E016;
        }
        // 任务被哪个节点执行
        for (Task task:runTaskList
             ) {
            TaskInfo tmp = new TaskInfo();
            tmp.setTaskId(task.getTaskId());
            tmp.setNodeId(task.getNode().getNodeId());
            tasks.add(tmp);
        }
        for (Task task:waitTaskList) {
            TaskInfo tmp = new TaskInfo();
            tmp.setTaskId(task.getTaskId());
            tmp.setNodeId(-1);
            tasks.add(tmp);
        }
        return ReturnCodeKeys.E015;
    }


    public List<ServiceNode> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<ServiceNode> nodeList) {
        this.nodeList = nodeList;
    }

    public List<Task> getWaitTaskList() {
        return waitTaskList;
    }

    public void setWaitTaskList(LinkedList<Task> waitTaskList) {
        this.waitTaskList = waitTaskList;
    }

    public List<Task> getRunTaskList() {
        return runTaskList;
    }

    public void setRunTaskList(LinkedList<Task> runTaskList) {
        this.runTaskList = runTaskList;
    }


}
