package bearmaps.proj2c;

import bearmaps.KDTree;
import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.proj2ab.MyTrieSet;
import bearmaps.proj2ab.Point;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {

    private KDTree kdt;             //kdtree to find closest points on map
    private ArrayList<Point> points;      //a list of points (associated with a node) to give to kdtree
    private HashMap<Point, Node> nodePoint;  //a map connecting points and nodes
    private MyTrieSet<List<Node>> nodeNameMap;  //a map that connects Node object to the name
    private MyTrieSet<String> trie;            //a trie with cleaned string keys and a value for the original string

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        List<Node> nodes = this.getNodes();
        points = new ArrayList<>();
        nodePoint = new HashMap<>();
        trie = new MyTrieSet<>();
        nodeNameMap = new MyTrieSet<>();

        for (Node n : nodes){
            if (!neighbors(n.id()).isEmpty()){   //only use street points, not named (they have no neighbors)
                Point p = new Point(n.lon(), n.lat());
                points.add(p);
                nodePoint.put(p, n);   //connects Node data type to Point data type
            }

            if(n.name() != null) {    //for autocompleting prefix
                String clean = cleanString(n.name());
                trie.add(clean, n.name());   //for prefix matching
                //map of a list of nodes for repeat locations (more than 1 mcdonald's)
                if (nodeNameMap.containsKey(clean)){    //if there is already 1 entry in trie
                    nodeNameMap.get(clean).add(n);
                }else {                 //else, add a new list entry
                    ArrayList<Node> lst = new ArrayList<>();
                    lst.add(n);
                    nodeNameMap.add(clean, lst);
                }
            }
        }
        kdt = new KDTree(points);   //for finding closest points
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        Point closest = kdt.nearest(lon, lat);
        Node close = nodePoint.get(closest);
        return close.id();
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        return trie.valuesWithPrefix(prefix);
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        LinkedList<Map<String, Object>> locationInfo = new LinkedList<>();
        String clean = cleanString(locationName);
        ArrayList<Node> locations = (ArrayList) nodeNameMap.get(clean);
        for (Node n : locations){
            HashMap<String, Object> info = new HashMap<>();
            info.put("lat", n.lat());
            info.put("lon", n.lon());
            info.put("name", n.name());
            info.put("id", n.id());
            locationInfo.add(info);
        }
        return locationInfo;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
