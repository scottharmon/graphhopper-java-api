package com.onyxtop.graphhopper.api;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopperAPI;

public class GraphHopperWeb implements GraphHopperAPI {

	private String serviceUrl;
    private String key = "";
	
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
		// TODO Auto-generated method stub
		return null;
	}

}
