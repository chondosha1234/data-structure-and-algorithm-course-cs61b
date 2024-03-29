package bearmaps.proj2c.server.handler.impl;

import bearmaps.proj2c.AugmentedStreetMapGraph;
import bearmaps.proj2c.server.handler.APIRouteHandler;
import spark.Request;
import spark.Response;
import bearmaps.proj2c.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bearmaps.proj2c.utils.Constants.*;

/**
 * Handles requests from the web browser for map images. These images
 * will be rastered into one large image to be displayed to the user.
 * @author rahul, Josh Hug, _________
 */
public class RasterAPIHandler extends APIRouteHandler<Map<String, Double>, Map<String, Object>> {

    /**
     * Each raster request to the server will have the following parameters
     * as keys in the params map accessible by,
     * i.e., params.get("ullat") inside RasterAPIHandler.processRequest(). <br>
     * ullat : upper left corner latitude, <br> ullon : upper left corner longitude, <br>
     * lrlat : lower right corner latitude,<br> lrlon : lower right corner longitude <br>
     * w : user viewport window width in pixels,<br> h : user viewport height in pixels.
     **/
    private static final String[] REQUIRED_RASTER_REQUEST_PARAMS = {"ullat", "ullon", "lrlat",
            "lrlon", "w", "h"};

    /**
     * The result of rastering must be a map containing all of the
     * fields listed in the comments for RasterAPIHandler.processRequest.
     **/
    private static final String[] REQUIRED_RASTER_RESULT_PARAMS = {"render_grid", "raster_ul_lon",
            "raster_ul_lat", "raster_lr_lon", "raster_lr_lat", "depth", "query_success"};


    @Override
    protected Map<String, Double> parseRequestParams(Request request) {
        return getRequestParams(request, REQUIRED_RASTER_REQUEST_PARAMS);
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param requestParams Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @param response : Not used by this function. You may ignore.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image;
     *                    can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    @Override
    public Map<String, Object> processRequest(Map<String, Double> requestParams, Response response) {

        Map<String, Object> results = new HashMap<>();

        //get information from request map
        double lrlon = requestParams.get("lrlon");
        double ullon = requestParams.get("ullon");
        double ullat = requestParams.get("ullat");
        double lrlat = requestParams.get("lrlat");
        double width = requestParams.get("w");
        double height = requestParams.get("h");

        //calculate various numbers to find pictures
        double reqLonDPP = (lrlon - ullon) / width;  //the longitude distance per pixel for request query box
        int D = findDepth(reqLonDPP);        //image depth or blurriness variable  0-7
        int K = (int) Math.pow(2, D) - 1;     //the number of possible pictures in X and Y direction  (minus 1)

        int upperLeftX = findCorner(ullon, K+1);  //upper right tile x coordinate
        int upperLeftY = findCorner(ullat, K+1);  //upper right tile y coordinate
        int lowerRightX = findCorner(lrlon, K+1);
        int lowerRightY = findCorner(lrlat, K+1);
        int xTiles = lowerRightX - upperLeftX + 1;
        int yTiles = lowerRightY - upperLeftY + 1;
        int x = upperLeftX;            //used to fill out png filenames
        int y = upperLeftY;

        //make 2D string array and use double loop to populate, use string format to fill in png file name using variables
        String[][] renderGrid = new String[yTiles][xTiles];
        for (int i = 0; i < yTiles; i++){
            for (int j = 0; j < xTiles; j++){
                String png = String.format("d%d_x%d_y%d.png", D, x, y);
                renderGrid[i][j] = png;
                x += 1;
            }
            x = upperLeftX;
            y += 1;
        }

        //calculate the coordinate for the rastered image, method has booleans for x/y axis and upper/lower position
        double rasterULLon = getImgCoordinates(upperLeftX, K+1, true, false);
        double rasterULLat = getImgCoordinates(upperLeftY, K+1, false, false);
        double rasterLRLon = getImgCoordinates(lowerRightX, K+1, true, true);
        double rasterLRLat = getImgCoordinates(lowerRightY, K+1, false, true);
        boolean querySuccess = checkSuccess(rasterULLon, rasterLRLon, rasterULLat, rasterLRLat);

        //put all calculated results into map to return
        results.put("raster_ul_lon", rasterULLon);
        results.put("raster_ul_lat", rasterULLat);
        results.put("raster_lr_lon", rasterLRLon);
        results.put("raster_lr_lat", rasterLRLat);
        results.put("render_grid", renderGrid);
        results.put("depth", D);
        results.put("query_success", true);

        /*for (int i = 0; i < yTiles; i++) {
            for (int j = 0; j < xTiles; j++) {
                System.out.print(renderGrid[i][j] + "  ");
            }
        }
        System.out.println();
        System.out.println(yTiles + "  " + xTiles);
        System.out.println(results);*/

        return results;
    }

    /** helper method written by me
     * finds the depth D (from 0 - 7) of the raster request
     * LonDPP of depth should be less than or equal to request LonDPP
     * @param requestLonDPP
     * @return
     */
    private int findDepth(double requestLonDPP){
        int depth = 0;
        double TopDPP = (ROOT_LRLON - ROOT_ULLON) / TILE_SIZE;  //LonDPP at depth 7 (lowest possible)
        while (depth < 7) {
            if (TopDPP <= requestLonDPP) {
                return depth;
            } else {
                TopDPP = TopDPP / 2;
                depth += 1;
            }
        }
        return depth;
    }

    /** method written by me
     * find the x or y coordinates of image for top left or bottom right
     * corner image
     * @param K
     * @param position
     * @return
     */
    private int findCorner(double position, int K){
        double distPerImage;
        double queryDist;
        if (position < 0){
            if (position < ROOT_ULLON){
                return 0;
            }else if(position > ROOT_LRLON){
                return K-1;
            }
            distPerImage = distPerImg(K, true);
            queryDist = position - ROOT_ULLON;
        }else {
            if (position > ROOT_ULLAT){
                return 0;
            }else if (position < ROOT_LRLAT){
                return K-1;
            }
            distPerImage = distPerImg(K, false);
            queryDist = ROOT_ULLAT - position;
        }
        double tile = queryDist / distPerImage;
        return (int) Math.floor(tile);
    }

    /** method written by me
     * takes an img (given by the X or Y coordinate in the image png
     * and returns the LAT or LON coordinate
     * @param dimension
     * @param K
     * @param isX
     * @return
     */
    private double getImgCoordinates(int dimension, int K, boolean isX, boolean lower){
        double distPerImg;
        double coordinate;
        if (isX){
            distPerImg = distPerImg(K, isX);
            coordinate = ROOT_ULLON + (distPerImg * dimension);
            if (lower){
                coordinate += distPerImg;
            }
        }else{
            distPerImg = distPerImg(K, isX);
            coordinate = ROOT_ULLAT - (distPerImg * dimension);
            if (lower){
                coordinate -= distPerImg;
            }
        }
        return coordinate;
    }

    /** small helper method to calculate the longitudinal or latitudinal distance per image based on Depth */
    private double distPerImg(int K, boolean isX){
        if (isX){
            return (ROOT_LRLON - ROOT_ULLON) / K;
        }else{
            return (ROOT_ULLAT - ROOT_LRLAT) / K;
        }
    }

    /** method written by me
     * checks if the query can be successful
     * if the query box is outside range it is false, or if the query just doesnt make sense
     * @return
     */
    private boolean checkSuccess(double rasterULLon, double rasterLRLon, double rasterULLat, double rasterLRLat){
        return !(rasterULLon > rasterLRLon) && !(rasterULLat < rasterLRLat);
    }

    @Override
    protected Object buildJsonResponse(Map<String, Object> result) {
        boolean rasterSuccess = validateRasteredImgParams(result);

        if (rasterSuccess) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writeImagesToOutputStream(result, os);
            String encodedImage = Base64.getEncoder().encodeToString(os.toByteArray());
            result.put("b64_encoded_image_data", encodedImage);
        }
        return super.buildJsonResponse(result);
    }

    private Map<String, Object> queryFail() {
        Map<String, Object> results = new HashMap<>();
        results.put("render_grid", null);
        results.put("raster_ul_lon", 0);
        results.put("raster_ul_lat", 0);
        results.put("raster_lr_lon", 0);
        results.put("raster_lr_lat", 0);
        results.put("depth", 0);
        results.put("query_success", false);
        return results;
    }

    /**
     * Validates that Rasterer has returned a result that can be rendered.
     * @param rip : Parameters provided by the rasterer
     */
    private boolean validateRasteredImgParams(Map<String, Object> rip) {
        for (String p : REQUIRED_RASTER_RESULT_PARAMS) {
            if (!rip.containsKey(p)) {
                System.out.println("Your rastering result is missing the " + p + " field.");
                return false;
            }
        }
        if (rip.containsKey("query_success")) {
            boolean success = (boolean) rip.get("query_success");
            if (!success) {
                System.out.println("query_success was reported as a failure");
                return false;
            }
        }
        return true;
    }

    /**
     * Writes the images corresponding to rasteredImgParams to the output stream.
     * In Spring 2016, students had to do this on their own, but in 2017,
     * we made this into provided code since it was just a bit too low level.
     */
    private  void writeImagesToOutputStream(Map<String, Object> rasteredImageParams,
                                                  ByteArrayOutputStream os) {
        String[][] renderGrid = (String[][]) rasteredImageParams.get("render_grid");
        int numVertTiles = renderGrid.length;
        int numHorizTiles = renderGrid[0].length;

        BufferedImage img = new BufferedImage(numHorizTiles * Constants.TILE_SIZE,
                numVertTiles * Constants.TILE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics graphic = img.getGraphics();
        int x = 0, y = 0;

        for (int r = 0; r < numVertTiles; r += 1) {
            for (int c = 0; c < numHorizTiles; c += 1) {
                graphic.drawImage(getImage(Constants.IMG_ROOT + renderGrid[r][c]), x, y, null);
                x += Constants.TILE_SIZE;
                if (x >= img.getWidth()) {
                    x = 0;
                    y += Constants.TILE_SIZE;
                }
            }
        }

        /* If there is a route, draw it. */
        double ullon = (double) rasteredImageParams.get("raster_ul_lon"); //tiles.get(0).ulp;
        double ullat = (double) rasteredImageParams.get("raster_ul_lat"); //tiles.get(0).ulp;
        double lrlon = (double) rasteredImageParams.get("raster_lr_lon"); //tiles.get(0).ulp;
        double lrlat = (double) rasteredImageParams.get("raster_lr_lat"); //tiles.get(0).ulp;

        final double wdpp = (lrlon - ullon) / img.getWidth();
        final double hdpp = (ullat - lrlat) / img.getHeight();
        AugmentedStreetMapGraph graph = SEMANTIC_STREET_GRAPH;
        List<Long> route = ROUTE_LIST;

        if (route != null && !route.isEmpty()) {
            Graphics2D g2d = (Graphics2D) graphic;
            g2d.setColor(Constants.ROUTE_STROKE_COLOR);
            g2d.setStroke(new BasicStroke(Constants.ROUTE_STROKE_WIDTH_PX,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            route.stream().reduce((v, w) -> {
                g2d.drawLine((int) ((graph.lon(v) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(v)) * (1 / hdpp)),
                        (int) ((graph.lon(w) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(w)) * (1 / hdpp)));
                return w;
            });
        }

        rasteredImageParams.put("raster_width", img.getWidth());
        rasteredImageParams.put("raster_height", img.getHeight());

        try {
            ImageIO.write(img, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BufferedImage getImage(String imgPath) {
        BufferedImage tileImg = null;
        if (tileImg == null) {
            try {
                File in = new File(imgPath);
                tileImg = ImageIO.read(in);
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return tileImg;
    }
}
