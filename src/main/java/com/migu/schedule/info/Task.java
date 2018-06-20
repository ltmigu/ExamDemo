package com.migu.schedule.info;

public class Task {
    private int taskId;
    private ServiceNode node;
    private int consumption;
    public ServiceNode getNode()
    {
        return node;
    }
    public int getTaskId(){  return taskId; }
    public void setNode(ServiceNode node)
    {
        this.node = node;
    }
    public void setTaskId(int taskId)
    {
        this.taskId = taskId;
    }

    public int getConsumption() {
        return consumption;
    }

    public void setConsumption(int consumption) {
        this.consumption = consumption;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", node=" + node +
                ", consumption=" + consumption +
                '}';
    }

    // 补充些方法
    public Task(int taskId,int consumption) {
        this.taskId = taskId;
        this.consumption = consumption;
    }

    public Task(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Task) {
            if (obj == this){
                return true;
            }
            Task anotherTaskInfo = (Task) obj;
            return this.taskId == anotherTaskInfo.taskId;
        } else {
            return false;
        }
    }

}
