# Gumshoe Demo Application
This application generates some random vehicle sales data, then aggregates it
by manufacturer, type and model and displays its output.  It has been sprinkled
with Gumshoe goodness to allow event outputting to the gumshoe.log file.  These
are then forwarded to elasticsearch using FileBeat.  You can search and
visualize the event data in elasticsearch using kibana.  To see this in action,
do the following:

1. [Install Docker](https://www.docker.com/products/docker) and [docker-machine](https://docs.docker.com/machine/install-machine/)
1. [Create and configure a machine](https://docs.docker.com/machine/get-started/).
1. Start a docker-machine: `docker-machine start`
1. Open a terminal and run ```docker-compose up```.  This will bring up
Elasticsearch, FileBeat and Kibana.
1. Open a web browser and navigate to kibana: ```localhost:5601```
1. Specify the index for kibana as ```gumshoe-*``` and use ```gs$emitted_at```
as the timestamp
1. Import the ```ops/kibana_export.json``` file from the settings > objects
tab in kibana.
1. Open the Demo Dashboard in kibana.
1. Run the application with ```VEHICLES=[some number] mvn exec:java```
1. Watch the kibana dashboard to see the events stream by

![kibana dashboard](kibana_dashboard.png)
