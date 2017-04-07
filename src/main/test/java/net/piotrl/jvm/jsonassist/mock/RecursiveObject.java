package net.piotrl.jvm.jsonassist.mock;

public class RecursiveObject {
    private int id;
    private RecursiveObject child;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RecursiveObject getChild() {
        return child;
    }

    public void setChild(RecursiveObject child) {
        this.child = child;
    }
}
