package de.ngerber.util;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class BowyerWatson
{
    private final ArrayList<Triangle> triangulation;
    private final int width, height;
    private final Triangle root;

    public BowyerWatson(int width, int height)
    {
        triangulation = new ArrayList<>();
        this.width = width;
        this.height = height;
        root = new Triangle(new Point(0,0), new Point (2 * width, 0), new Point(0, 2 * height));
        triangulation.add(root);
    }

    public void addPoint(Point point)
    {
        List<Triangle> badTriangles = triangulation.stream().filter(triangle -> triangle.contains(point)).collect(Collectors.toList());
        triangulation.removeAll(badTriangles);
        triangulation.addAll(badTriangles.stream()
                .flatMap(triangle -> Arrays.stream(triangle.getEdges()))
                .distinct()
                .map(edge -> new Triangle(point, edge.getA(), edge.getB()))
                .collect(Collectors.toList()));
    }

    public void reset()
    {
        triangulation.clear();
        triangulation.add(root);
    }

    public Point getNextPointIfExists()
    {
        Point nextPoint;
        do
        {
            nextPoint = getLargestTriangle().getCentre();
            int v = triangulation.size();
            addPoint(nextPoint);
            if (triangulation.size() == v) //triangulation size = 39. todo: return values above 39
                return null;
        } while (nextPoint.x > width || nextPoint.y > height);
        return nextPoint;
    }

    public void drawAllPoints(Graphics2D g)
    {
        for (Triangle triangle : triangulation)
        {
            for (Edge edge : triangle.getEdges())
            {
                g.fillOval(edge.a.x - 4, edge.a.y - 4, 8, 8);
                g.fillOval(edge.b.x - 4, edge.b.y - 4, 8, 8);
            }
        }
    }

    public void drawDebug(Graphics2D g)
    {
        for (Triangle triangle : triangulation)
        {
            g.fillOval(triangle.getCentre().x - 4, triangle.getCentre().y - 4, 8, 8);
            for (Edge edge : triangle.getEdges())
            {
                g.drawLine(edge.a.x, edge.a.y, edge.b.x, edge.b.y);
            }
        }
        Triangle triangle = getLargestTriangle();
        g.fillOval(triangle.getCentre().x - 4, triangle.getCentre().y - 4, 8, 8);
        g.drawOval((int) (triangle.getCentre().x - triangle.getSize()), (int) (triangle.getCentre().y - triangle.getSize()),
                (int) (triangle.getSize() * 2), (int) (triangle.getSize() * 2));
    }

    private Triangle getLargestTriangle()
    {
        float maxArea = 0;
        Triangle maxTriangle = null;
        for (Triangle triangle : triangulation)
        {
            float area = triangle.getSize();
            if (maxArea > area)
                continue;
            maxArea = area;
            maxTriangle = triangle;
        }

        return maxTriangle;
    }

    private final class Triangle
    {
        private final Point a, b, c;

        public Triangle(Point a, Point b, Point c)
        {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public Point getCentre()
        {
            return new Point((a.x + b.x + c.x) / 3, (a.y + b.y + c.y) / 3);
        }

        public float getSize()
        {
            Edge r1 = new Edge(a, getCentre());
            Edge r2 = new Edge(b, getCentre());
            Edge r3 = new Edge(c, getCentre());
            return Math.min(Math.min(r1.getLength(), r2.getLength()), r3.getLength());
        }

        public boolean contains(Point p)
        {
            float dX = p.x-c.x;
            float dY = p.y-c.y;
            float dX21 = c.x-b.x; 
            float dY12 = b.y-c.y; 
            float D = dY12*(a.x-c.x) + dX21*(a.y-c.y); 
            float s = dY12*dX + dX21*dY; 
            float t = (c.y-a.y)*dX + (a.x-c.x)*dY; 
            if (D<0) 
                return s<0 && t<0 && s+t>D;
            return s>0 && t>0 && s+t<D;
        }

        public Edge[] getEdges()
        {
            Edge[] edges = new Edge[3];
            edges[0] = new Edge(a, b);
            edges[1] = new Edge(a, c);
            edges[2] = new Edge(b, c);

            return edges;
        }
    }

    private final class Edge
    {
        private final Point a, b;

        public Edge(Point a, Point b)
        {
            this.a = a;
            this.b = b;
        }

        public Point getA()
        {
            return a;
        }

        public Point getB()
        {
            return b;
        }

        public float getLength()
        {
            return (float) Math.sqrt(Math.pow(a.x - b.x,2) + Math.pow(a.y - b.y,2));
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Edge edge = (Edge) o;
            return a.equals(edge.a) && b.equals(edge.b) || a.equals(edge.b) && b.equals(edge.a);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(a, b);
        }
    }
}
