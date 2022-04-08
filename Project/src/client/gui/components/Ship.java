package client.gui.components;

public class Ship {

    private int id;
    private String name;
    private int size;
    private int[] position;
    private int startPosition;
    private String side = "right";
    private boolean placed = false;
    private int sideMultiplier = 1;

    public Ship(int id, String name, int size) {
        this.name = name;
        this.size = size;
        this.id = id;
        position = new int[size];
    }

    public void rotate(){
        if ("right".equals(side)) {
            side = "down";
            sideMultiplier = 10;
        } else {
            side = "right";
            sideMultiplier = 1;
        }
        calculatePosition();
    }

    private void calculatePosition() {
        int[] prognosesPosition = new int[size];
        boolean possible = true;
        for(int i = 0; i < size; i++) {
            prognosesPosition[i] = startPosition+(i*sideMultiplier);
        }
        for(int pos : prognosesPosition) {
            if(pos > 99) possible = false;
            if(Math.round(pos/10)*10 >= (Math.round(startPosition/10)*10)+10) possible = false;
        }
        if(prognosesPosition[0]+10 == prognosesPosition[1] && prognosesPosition[size-1] < 100) {
            possible = true;
        }
        if(possible) position = prognosesPosition;
    }

    public void setStartPosition(int startPosition) {
        if(getHoveredPositions(startPosition)[size-1] > 99) return;
        this.startPosition = startPosition;
        this.placed = true;
        calculatePosition();
    }

    public int[] getPosition() {
        if(!placed) return null;
        return position;
    }

    public int[] getHoveredPositions(int id) {
        int[] shipPosition = new int[size];
        for(int i = 0; i < size; i++) {
            shipPosition[i] = id+(i*sideMultiplier);
        }
        return shipPosition;
    }
}
