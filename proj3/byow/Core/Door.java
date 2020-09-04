package byow.Core;

import static byow.Core.Constants.*;

/** Door class extends Point class, because it is essentially just a point on the map
 * with a few extra instance variables to set it to 'opened' or 'closed'
 */
public class Door extends Point{
    private final String side;

    /** constructor for a Door, always initially set to "closed"
     *
     * @param x    x coordinate of door
     * @param y    y coordinate of door
     */
    public Door(int x, int y, String side){
        super(x, y);
        this.side = side;
    }

    public String getSide(){
        return side;
    }

    public String getOppositeSide(){
        switch(side){
            case RIGHT: return LEFT;
            case LEFT: return RIGHT;
            case TOP: return BOTTOM;
            case BOTTOM: return TOP;
            default: return null;
        }
    }

}
