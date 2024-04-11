import java.util.HashMap;
import java.util.Map;

// Enumeration for directions within the maze
enum Direction {
    NORTH, EAST, SOUTH, WEST;
}

// Wall class to represent walls in the maze
class Wall {
    public void enter() {
        System.out.println("You hit a wall!");
    }
}

// Door class to represent doors in the maze
class Door extends Wall {
    private Room room1;
    private Room room2;
    private boolean isOpen;

    public Door(Room room1, Room room2) {
        this.room1 = room1;
        this.room2 = room2;
        this.isOpen = false; // Doors start closed by default
    }

    public void enter() {
        if (isOpen) {
            System.out.println("You pass through the door.");
        } else {
            System.out.println("The door is closed.");
        }
    }

    public void open() {
        isOpen = true;
        System.out.println("You open the door.");
    }
}

// Room class to represent rooms in the maze
class Room {
    private int roomNo;
    private Map<Direction, Wall> sides = new HashMap<>();

    public Room(int roomNo) {
        this.roomNo = roomNo;
    }

    public void setSide(Direction direction, Wall wall) {
        sides.put(direction, wall);
    }

    public Wall getSide(Direction direction) {
        return sides.get(direction);
    }

    public void enter() {
        System.out.println("You are in room " + roomNo);
    }

    public Integer getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(Integer roomNo) {
        this.roomNo = roomNo;
    }
}

// Maze class to represent the whole maze
class Maze {
    private Map<Integer, Room> rooms = new HashMap<>();

    public void addRoom(Room room) {
        rooms.put(room.getRoomNo(), room);
    }

    public Room getRoom(int roomNo) {
        return rooms.get(roomNo);
    }
}

// Builder interface
interface MazeBuilder {
    void buildMaze();

    void buildRoom(int roomNo);

    void buildDoor(int roomFrom, int roomTo);

    Maze getMaze();
}

// Concrete builder for standard maze
class StandardMazeBuilder implements MazeBuilder {
    private Maze currentMaze;

    public void buildMaze() {
        currentMaze = new Maze();
    }

    public void buildRoom(int roomNo) {
        if (currentMaze.getRoom(roomNo) == null) {
            Room room = new Room(roomNo);
            currentMaze.addRoom(room);
            // Initialize all sides of the room with walls
            for (Direction dir : Direction.values()) {
                room.setSide(dir, new Wall());
            }
        }
    }

    public void buildDoor(int roomFrom, int roomTo) {
        Room r1 = currentMaze.getRoom(roomFrom);
        Room r2 = currentMaze.getRoom(roomTo);
        Door door = new Door(r1, r2);

        // Place the door between two rooms
        r1.setSide(Direction.EAST, door);
        r2.setSide(Direction.WEST, door);
    }

    public Maze getMaze() {
        return currentMaze;
    }
}

// MazeGame class that uses the MazeBuilder to construct the maze
class MazeGame {
    public Maze createMaze(MazeBuilder builder) {
        builder.buildMaze();

        builder.buildRoom(1);
        builder.buildRoom(2);
        builder.buildDoor(1, 2);

        return builder.getMaze();
    }

    public static void main(String[] args) {
        MazeBuilder builder = new StandardMazeBuilder();
        MazeGame game = new MazeGame();

        Maze maze = game.createMaze(builder);

        Room room1 = maze.getRoom(1);
        Room room2 = maze.getRoom(2);

        room1.enter();
        Wall northWall = room1.getSide(Direction.NORTH);
        northWall.enter(); // Should print "You hit a wall!"

        Door doorBetweenRooms = (Door) room1.getSide(Direction.EAST);
        doorBetweenRooms.enter(); // Should print "The door is closed."
        doorBetweenRooms.open(); // Opens the door
        doorBetweenRooms.enter(); // Should now print "You pass through the door."

        room2.enter();
    }
}

// This code is just a starting point and can be further enhanced to handle
// more complex mazes, different kinds of rooms, and additional elements like
// keys, monsters, or treasures.
