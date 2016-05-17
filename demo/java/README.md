# Gumshoe Demo Application
This application generates some random vehicle sales data, then aggregates it
by manufacturer, type and model and displays its output.  It has been sprinkled
with Gumshoe goodness to allow event outputting to the gumshoe.log file.  These
are then forwarded to elasticsearch using FileBeat.  You can search and
visualize the event data in elasticsearch using kibana.  To see this in action,
do the following:

1. Open a terminal and run ```docker-compose up```.  This will bring up
Elasticsearch, FileBeat and Kibana.
2. Open a web browser and navigate to kibana: ```localhost:5601```
3. Specify the index for kibana as ```gumshoe-*``` and use ```gs$emitted_at```
as the timestamp
4. Import the ```ops/kibana_export.json``` file from the settings > objects
tab in kibana.
5. Open the Demo Dashboard in kibana.
6. Run the application with ```VEHICLES=[some number] mvn exec:java```
7. Watch the kibana dashboard to see the events stream by

![kibana dashboard](kibana_dashboard.png)
