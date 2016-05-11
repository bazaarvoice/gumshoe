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
4. Run the application with ```VEHICLES=[some number] mvn exec:java```
5. Refresh the view in kibana to see the new events
