package services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.List;

public class JsonConfigurer {

    private final Double L;
    private final Double M;
    private final Double mass; // Changed from MASS to mass
    private final Double Gamma;
    private final Double Mu;
    private final Double Kn;
    private final Double Rmin;
    private final Double Rmax;
    private final Double iterationPerFrame;
    private final Double A;
    private final Double G;


    private final List<Double> dts;
    private final List<Double> Ws;
    private final List<Double> Ns;
    private final List<Double> Ds;

    public JsonConfigurer(String path) {
        JSONParser parser = new JSONParser();
        try (FileReader fileReader = new FileReader(path)) {

            JSONObject json = (JSONObject) parser.parse(fileReader);

            this.L = (Double) json.get("L");
            this.M = (Double) json.get("M");
            this.mass = (Double) json.get("Mass");
            this.Gamma = (Double) json.get("Gamma");
            this.G = (Double) json.get("G");
            this.Mu = (Double) json.get("Mu");
            this.Kn = (Double) json.get("Kn");
            this.Rmin = (Double) json.get("Rmin");
            this.Rmax = (Double) json.get("Rmax");
            this.iterationPerFrame = (Double) json.get("iterationPerFrame");
            this.A = (Double) json.get("A");

            this.Ds = (List<Double>) json.get("Ds");
            this.dts = (List<Double>) json.get("dts");
            this.Ws = (List<Double>) json.get("Ws");
            this.Ns = (List<Double>) json.get("Ns");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error in config file. Read the README.md file for more information.");
        }
    }

    public Double getL() {
        return L;
    }

    public Double getM() {
        return M;
    }

    public Double getMass() {
        return mass;
    }

    public List<Double> getDs() {
        return Ds;
    }

    public Double getGamma() {
        return Gamma;
    }

    public Double getMu() {
        return Mu;
    }

    public Double getKn() {
        return Kn;
    }

    public Double getRmin() {
        return Rmin;
    }

    public Double getRmax() {
        return Rmax;
    }

    public List<Double> getDts() {
        return dts;
    }

    public Double getA() {
        return A;
    }

    public List<Double> getWs() {
        return Ws;
    }

    public List<Double> getNs() {
        return Ns;
    }

    public Double getIterationPerFrame() {
        return iterationPerFrame;
    }

    public Double getG() {
        return G;
    }
}
