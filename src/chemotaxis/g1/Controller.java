package chemotaxis.g1; // TODO modify the package name to reflect your team

import java.awt.Point;
import java.util.*;

import chemotaxis.sim.ChemicalPlacement;
import chemotaxis.sim.ChemicalCell;
import chemotaxis.sim.ChemicalCell.ChemicalType;
import chemotaxis.sim.SimPrinter;

public class Controller extends chemotaxis.sim.Controller {

    private List<Point> path;
    private Integer currApplication;
    private Integer totalChemicals;

    /**
     * Controller constructor
     *
     * @param start       start cell coordinates
     * @param target      target cell coordinates
     * @param size     	 grid/map size
     * @param simTime     simulation time
     * @param budget      chemical budget
     * @param seed        random seed
     * @param simPrinter  simulation printer
     *
     */
    public Controller(Point start, Point target, Integer size, Integer simTime, Integer budget, Integer seed, SimPrinter simPrinter) {
        super(start, target, size, simTime, budget, seed, simPrinter);
        this.path = new ArrayList<Point>();
        this.currApplication = 0;
        this.totalChemicals = 0;
    }

    /**
     * Apply chemicals to the map
     *
     * @param currentTurn         current turn in the simulation
     * @param chemicalsRemaining  number of chemicals remaining
     * @param currentLocation     current location of the agent
     * @param grid                game grid/map
     * @return                    a cell location and list of chemicals to apply
     *
     */
    @Override
    public ChemicalPlacement applyChemicals(Integer currentTurn, Integer chemicalsRemaining, Point currentLocation, ChemicalCell[][] grid) {
        if (this.path.size() == 0) {
            this.path = getShortestPath(this.start, grid);
        }
        // System.out.println(this.path);

        if (currentTurn == 1)
            this.totalChemicals = chemicalsRemaining;

        ChemicalPlacement chemicalPlacement = new ChemicalPlacement();

        List<ChemicalType> chemicals = new ArrayList<>();
 		chemicals.add(ChemicalType.BLUE);

        if ((this.currApplication) < this.path.size()) {
            chemicalPlacement.chemicals = chemicals;
            if (chemicalsRemaining >= this.totalChemicals/2) {
                chemicalPlacement.location = path.get(this.currApplication++);
            }
            else if (chemicalsRemaining < this.totalChemicals/2 && currentTurn % 2 == 1) {
                chemicalPlacement.location = path.get(this.currApplication);
                this.currApplication += 2;
            }
        }
        else
            chemicalPlacement.location = this.target;

        return chemicalPlacement;
    }

    // BFS implementation
    public List<Point> getShortestPath(Point s, ChemicalCell[][] grid) {
        List<List<Point>> queue = new ArrayList<>();
        Set<Point> visited = new HashSet<Point>();
        List<Point> start = new ArrayList<>();
        start.add(s);
        visited.add(s);
        queue.add(start);

        for (int i = 0; i < queue.size(); i++) {
            List<Point> node = queue.get(i);
            node.add(node.get(0));                      // First element of "node": the current cell
            if (target.equals(node.get(0)))             // If target cell is reached, return path
                return node.subList(1,node.size());     // Second -> last elements of "node": path to current cell

            List<Point> neighbors = getNeighbors(node.get(0), grid);
            for (Point neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    List<Point> new_node = new ArrayList(node.subList(1,node.size()));
                    new_node.add(0, neighbor);
                    queue.add(new_node);
                }
            }
            visited.add(node.get(0));
        }
        return new ArrayList<Point>();
    }

    // Get available neighbors of a cell
    public List<Point> getNeighbors(Point node, ChemicalCell[][] grid) {
        List<Point> neighbors = new ArrayList<>();
        int x = (int)node.getX()-1;
        int y = (int)node.getY()-1;
        if (x > 0 && grid[x-1][y].isOpen())
            neighbors.add(new Point(x, y+1));
        if (y > 0 && grid[x][y-1].isOpen())
            neighbors.add(new Point(x+1, y));
        if (x < grid.length-1 && grid[x+1][y].isOpen())
            neighbors.add(new Point(x+2, y+1));
        if (y < grid.length-1 && grid[x][y+1].isOpen())
            neighbors.add(new Point(x+1, y+2));
        return neighbors;
    }

    // Testing the BFS (run with "java Controller" in chemotaxis/g1)
    public static void main(String[] args) {
        Point start = new Point(1,1);
        Point target = new Point(5,5);
        int size = 5;
        int simTime = 0;
        int budget = 50;
        int seed = 0;
        SimPrinter simPrinter = new SimPrinter(true);
		Controller test = new Controller(start, target, size, simTime, budget, seed, simPrinter);
		ChemicalCell closed = new ChemicalCell(false);
		ChemicalCell open = new ChemicalCell(true);
		ChemicalCell[][] grid = new ChemicalCell[size][size];
		grid[0][0]=open;
		grid[0][1]=open;
		grid[0][2]=open;
		grid[0][3]=closed;
		grid[0][4]=open;
		grid[1][0]=open;
		grid[1][1]=closed;
		grid[1][2]=closed;
		grid[1][3]=closed;
		grid[1][4]=open;
		grid[2][0]=open;
		grid[2][1]=open;
		grid[2][2]=open;
		grid[2][3]=open;
		grid[2][4]=open;
		grid[3][0]=open;
		grid[3][1]=closed;
		grid[3][2]=open;
		grid[3][3]=closed;
		grid[3][4]=closed;
		grid[4][0]=open;
		grid[4][1]=closed;
		grid[4][2]=open;
		grid[4][3]=open;
		grid[4][4]=open;

		List<Point> path = test.getShortestPath(start, grid);
		System.out.println(path);
	}
}