package com.migu.schedule.info;

import java.util.LinkedList;
import java.util.List;

public class ServiceNode {
    private int nodeId;

    private List<Task> taskList = new LinkedList<Task>();

    private int consumption = 0;

    public ServiceNode(int nodeId){
        this.nodeId = nodeId;
    }

    public void addTask2Node(Task task){
        taskList.add(task);
        consumption+=task.getConsumption();
        task.setNode(this);
    }


    public int getConsumption() {
        return consumption;
    }

    public void setConsumption(int consumption) {
        this.consumption = consumption;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ServiceNode) {
            if (obj == this){
                return true;
            }
            ServiceNode anotherServiceNode = (ServiceNode) obj;
            return nodeId == anotherServiceNode.nodeId;
        } else {
            return false;
        }
    }
}
