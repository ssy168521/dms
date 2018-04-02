package com.web.thread;
import java.text.DecimalFormat;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class CoordinateConversion{

    /**
     * 平面坐标转经纬度
     */
    public String convert(double x, double y,CoordinateReferenceSystem crs) 
            throws FactoryException, MismatchedDimensionException, TransformException {
    Coordinate sourceCoord = new Coordinate(x, y);
    GeometryFactory geoFactory = new GeometryFactory();
    Point sourcePoint = geoFactory.createPoint(sourceCoord);

    MathTransform transform = CRS.findMathTransform(crs, DefaultGeographicCRS.WGS84, false);
    Point targetPoint = (Point) JTS.transform(sourcePoint, transform);
    double[] targetCoord = {targetPoint.getX(), targetPoint.getY()};
    DecimalFormat df = new DecimalFormat( "0.00000"); //限定小数位数为5位  
    return df.format(targetCoord[0])+" "+df.format(targetCoord[1]);
    }
    /**
     * 测试
     */
//    public static void main( String[] args ) throws Exception
//    {
//        double x = -132202.486;
//        double y = 1657014.274;
//        //第一种获取方式，imageFile为卫星影像的文件地址，目的是获取影像的投影
////        GeoTiffReader reader = new GeoTiffReader(imageFile);
////        GridCoverage2D coverage = reader.read(null);
////        //获取投影
////        CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem2D();
//        //这是第二种获取方式（参考博文），
//        final String wkt="PROJCS[\"unnamed\"," 
//                                +"GEOGCS[\"WGS 84\"," 
//                                      + "DATUM[\"World Geodetic System 1984\", "
//                                         +"SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]]," 
//                                        + "AUTHORITY[\"EPSG\",\"6326\"]], "
//                                     +  "PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]]," 
//                                     + " UNIT[\"degree\", 0.017453292519943295], "
//                                      + "AXIS[\"Geodetic longitude\", EAST], "
//                                      +" AXIS[\"Geodetic latitude\", NORTH], "
//                                      + "AUTHORITY[\"EPSG\",\"4326\"]], "
//                                   + " PROJECTION[\"Albers_Conic_Equal_Area\"], "
//                                   +  "PARAMETER[\"central_meridian\", 110.0], "
//                                   + " PARAMETER[\"latitude_of_origin\", 12.0], "
//                                    +" PARAMETER[\"standard_parallel_1\", 25.0], "
//                                   +  "PARAMETER[\"false_easting\", 0.0], "
//                                   +  "PARAMETER[\"false_northing\", 0.0]," 
//                                    + "PARAMETER[\"standard_parallel_2\", 47.0]," 
//                                    + "UNIT[\"m\", 1.0], "
//                                    + "AXIS[\"Easting\", EAST], "
//                                   +  "AXIS[\"Northing\", NORTH]]";
//        CoordinateReferenceSystem crs = CRS.parseWKT(wkt);
//        double[] coordinate = convert(x, y,crs);
//        System.out.println("X: " + coordinate[0] + ", Y: " + coordinate[1]);
//    }
}
