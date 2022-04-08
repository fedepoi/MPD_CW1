package org.me.gcu.vivaldo_federico_s1828951;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ListController extends Observable implements Observer {

    private ArrayList<RoadWorkItem> plannedList;
    private ArrayList<RoadWorkItem> roadWorkItemsList;
    private ArrayList<RoadWorkItem> incidentsList;
    private ArrayList<RoadWorkItem> all;

    private String plannedRoadWorksURL = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String roadWorksURL = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String incidentsURL = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";

    private DataFeed plannedDatafeed;
    private DataFeed incidentDataFeed;
    private DataFeed roadWorksDataFeed;


    private boolean finished;


    public ListController(MapsActivity ma) {
        all = new ArrayList<RoadWorkItem>();


        plannedDatafeed = new DataFeed();
        plannedDatafeed.addObserver(this);
        plannedDatafeed.fetchData("plannedRoadWork", plannedRoadWorksURL);


        roadWorksDataFeed = new DataFeed();
        roadWorksDataFeed.addObserver(this);
        roadWorksDataFeed.fetchData("roadWork", roadWorksURL);

        incidentDataFeed = new DataFeed();
        incidentDataFeed.addObserver(this);
        incidentDataFeed.fetchData("incident", incidentsURL);


    }


    @Override
    public void update(Observable o, Object arg) {

        plannedList = plannedDatafeed.getList();

        roadWorkItemsList = roadWorksDataFeed.getList();

        incidentsList = incidentDataFeed.getList();

        if (plannedDatafeed.getFinished() && roadWorksDataFeed.getFinished() && incidentDataFeed.getFinished()) {


            setFinished(true);
            setChanged();
            notifyObservers();
        }


    }

    public ArrayList<RoadWorkItem> getAllList() {


        if (this.plannedList != null) {
            all.addAll(this.plannedList);
        }

        if (this.incidentsList != null) {
            all.addAll(this.incidentsList);
        }
        if (this.roadWorkItemsList != null) {
            all.addAll(this.roadWorkItemsList);
        }
        return this.all;
    }

    public boolean getFinished() {
        return this.finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public ArrayList<RoadWorkItem> getRoadWorkItemsList() {
        return this.roadWorkItemsList;
    }

    public ArrayList<RoadWorkItem> getPlannedList() {
        return this.plannedList;
    }

    public ArrayList<RoadWorkItem> getIncidentsList() {
        return this.incidentsList;
    }

}
