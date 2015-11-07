package com.onyxtop.graphhopper.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;
import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopperAPI;
import com.graphhopper.util.shapes.GHPoint;

public class GraphHopperWeb implements GraphHopperAPI {

	private String serviceUrl;
	private String key = "";
    private boolean instructions = true;
    private boolean calcPoints = true;
    private boolean elevation = false;
    private String optimize = "false";

	public GraphHopperWeb() {
		this("https://graphhopper.com/api/1/route");
	}

	public GraphHopperWeb(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public boolean load(String urlOrFile) {
		this.serviceUrl = urlOrFile;
		return true;
	}

	public GHResponse route(GHRequest request) {
		GHResponse response = null;
		try {
			URL url = new URL(createRequestURL(request));
			InputStream inputStream = url.openStream();
			Gson gson = new Gson();
			response = gson.fromJson(new InputStreamReader(inputStream), GHResponse.class);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public String createRequestURL(GHRequest request) {
        boolean tmpInstructions = request.getHints().getBool("instructions", instructions);
        boolean tmpCalcPoints = request.getHints().getBool("calcPoints", calcPoints);
        String tmpOptimize = request.getHints().get("optimize", optimize);

        if (tmpInstructions && !tmpCalcPoints) {
            throw new IllegalStateException("Cannot calculate instructions without points (only points without instructions). "
                    + "Use calcPoints=false and instructions=false to disable point and instruction calculation");
        }

        boolean tmpElevation = request.getHints().getBool("elevation", elevation);

        String tmpKey = request.getHints().get("key", key);

        String places = "";
        for (GHPoint p : request.getPoints()) {
            places += "point=" + p.lat + "," + p.lon + "&";
        }

        String url = serviceUrl
                + "?"
                + places
                + "&type=json"
                + "&instructions=" + tmpInstructions
                + "&points_encoded=true"
                + "&calc_points=" + tmpCalcPoints
                + "&algo=" + request.getAlgorithm()
                + "&locale=" + request.getLocale().toString()
                + "&elevation=" + tmpElevation
                + "&optimize=" + tmpOptimize;

        if (!request.getVehicle().isEmpty()) {
            url += "&vehicle=" + request.getVehicle();
        }

        if (!tmpKey.isEmpty()) {
            url += "&key=" + tmpKey;
        }

        return url;
    }
	
    public GraphHopperWeb setKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalStateException("Key cannot be empty");
        }

        this.key = key;
        return this;
    }

    /**
     * Enable or disable calculating points for the way. The default is true.
     */
    public GraphHopperWeb setCalcPoints(boolean calcPoints) {
        this.calcPoints = calcPoints;
        return this;
    }

    /**
     * Enable or disable calculating and returning turn instructions. The
     * default is true.
     */
    public GraphHopperWeb setInstructions(boolean b) {
        instructions = b;
        return this;
    }

    /**
     * Enable or disable elevation. The default is false.
     */
    public GraphHopperWeb setElevation(boolean withElevation) {
        this.elevation = withElevation;
        return this;
    }

}
