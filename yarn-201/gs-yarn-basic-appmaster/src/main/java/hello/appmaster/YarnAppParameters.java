package hello.appmaster;

public class YarnAppParameters {

    private int containersNum;
    private int priority;
    private int memory;
    private int coresNum;

    public int getContainersNum() {
        return containersNum;
    }

    public void setContainersNum(int containersNum) {
        this.containersNum = containersNum;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getCoresNum() {
        return coresNum;
    }

    public void setCoresNum(int coresNum) {
        this.coresNum = coresNum;
    }
}
