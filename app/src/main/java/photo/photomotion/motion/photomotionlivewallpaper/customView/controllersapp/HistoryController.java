package photo.photomotion.motion.photomotionlivewallpaper.customView.controllersapp;

import android.graphics.Bitmap;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;

import photo.photomotion.motion.photomotionlivewallpaper.customView.beans.Point;

public class HistoryController {
    public static final int SIZE_MAX_BATTERY_INIT = 30;
    public static HistoryController instance;
    public int idGroupCurrent = 0;
    public int idGroupMax = 300;
    public Stack<ObjectHistory> stackUndo = new Stack<>();
    public Stack<ObjectHistory> stackRedo = new Stack<>();

    // Inner class to represent a history object
    public static class ObjectHistory {
        public boolean isChanged;
        public boolean hasMask = false;
        public int idGroup = -1;
        public Bitmap mask;
        public Point point;

        // Constructor for Point-based history
        public ObjectHistory(int idGroup, Point point, boolean isChanged) {
            this.idGroup = idGroup;
            this.point = point;
            this.isChanged = isChanged;
        }

        // Constructor for Bitmap-based history
        public ObjectHistory(int idGroup, Bitmap mask, boolean isChanged) {
            this.mask = mask;
            this.isChanged = isChanged;
            this.idGroup = idGroup;
            this.hasMask = true;
        }

        public Bitmap getMask() {
            return this.mask;
        }

        public Point getPoint() {
            return this.point;
        }

        public boolean hasMask() {
            return this.mask != null;
        }

        public boolean isChanged() {
            return this.isChanged;
        }
    }

    // Constructor
    public HistoryController() {
        clear();
    }

    // Singleton instance getter
    public static HistoryController getInstance() {
        if (instance == null) {
            instance = new HistoryController();
        }
        return instance;
    }

    // Verifies if the undo stack has reached the limit
    private void checkLimit() {
        if (idGroupCurrent >= idGroupMax) {
            idGroupMax++;
            if (stackUndo.size() <= 1) {
                do {
                    stackUndo.remove(0);
                } while (!stackUndo.isEmpty() && stackUndo.get(0).idGroup == stackUndo.get(0).idGroup);
            }
        }
    }

    // Add a new history entry with a point
    public void addHistory(Point point, boolean isChanged) {
        stackUndo.push(new ObjectHistory(idGroupCurrent, point, isChanged));
        stackRedo.clear();
        checkLimit();
        idGroupCurrent++;
    }

    // Add a new history entry with a list of points
    public void addHistory(List<Point> points, boolean isChanged) {
        for (Point point : points) {
            stackUndo.push(new ObjectHistory(idGroupCurrent, point, isChanged));
        }
        stackRedo.clear();
        checkLimit();
        idGroupCurrent++;
    }

    // Add a new history entry with a bitmap
    public void addHistory(Bitmap bitmap, boolean isChanged) {
        stackUndo.push(new ObjectHistory(idGroupCurrent, bitmap, isChanged));
        stackRedo.clear();
        checkLimit();
        idGroupCurrent++;
    }

    // Clear the history stacks and reset the counters
    public void clear() {
        idGroupCurrent = 0;
        stackUndo.clear();
        stackRedo.clear();
        idGroupMax = 300;
    }

    // Undo the last history actions
    public List<ObjectHistory> undo() {
        List<ObjectHistory> undoList = new CopyOnWriteArrayList<>();
        if (!stackUndo.isEmpty()) {
            do {
                ObjectHistory lastAction = stackUndo.pop();
                undoList.add(lastAction);
                idGroupCurrent = lastAction.idGroup;
                stackRedo.push(lastAction);
            } while (!stackUndo.isEmpty() && stackUndo.peek().idGroup == undoList.get(undoList.size() - 1).idGroup);
        }
        return undoList;
    }

    // Redo the undone actions
    public List<ObjectHistory> redo() {
        List<ObjectHistory> redoList = new CopyOnWriteArrayList<>();
        if (!stackRedo.isEmpty()) {
            do {
                ObjectHistory lastUndo = stackRedo.pop();
                redoList.add(lastUndo);
                idGroupCurrent = lastUndo.idGroup + 1;
                stackUndo.push(lastUndo);
            } while (!stackRedo.isEmpty() && stackRedo.peek().idGroup == redoList.get(redoList.size() - 1).idGroup);
        }
        return redoList;
    }

    // Check if there are actions to undo
    public boolean andUndo() {
        return !stackUndo.isEmpty();
    }

    // Check if there are actions to redo
    public boolean andRedo() {
        return !stackRedo.isEmpty();
    }
}